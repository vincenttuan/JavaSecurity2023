package security.mac;

import javax.crypto.SecretKey;

import security.KeyUtil;

/*
 * 情境描述：
 * 在一家大型企業，HR部門每月都會發送電子薪資明細給員工。
 * 為了確保薪資明細的安全性，HR部門決定對薪資明細檔案進行雜湊和 MAC 保護。
 * 雜湊保護確保薪資明細的完整性，而MAC則確保薪資明細確實來自HR部門，並未被其他部門或外部攻擊者更改。
 * 
 * */

public class SalaryProtectionCreator {
	public static void main(String[] args) throws Exception {
		// HR:
		String filepath = "src/security/mac/my_salary.txt";
		String keypath = "src/security/mac/macKey.key";
		System.out.println("薪資檔位置：" + filepath);
		System.out.println("密鑰檔位置：" + keypath);
		
		// 對 filepath 的檔案內容生成雜湊
		String fileHash = KeyUtil.generateFileHash(filepath);
		System.out.println("File Hash：" + fileHash); // 確保資料完整性
		
		// 生成 MAC value
		SecretKey macKey = KeyUtil.generateKeyForHmac();
		String fileMac = KeyUtil.generateMac("HmacSHA256", macKey, filepath);
		System.out.println("File MAC：" + fileMac); // 確保資料來源正確性
		
		// 儲存 MAC 密鑰供驗證使用
		KeyUtil.saveSecretKeyToFile(macKey, keypath);
		
	}
}
