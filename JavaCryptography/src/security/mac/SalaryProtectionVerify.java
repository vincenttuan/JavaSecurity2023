package security.mac;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.SecretKey;

import security.KeyUtil;

public class SalaryProtectionVerify {
	public static void main(String[] args) throws Exception {
		/*
		 * 薪資檔位置：src/security/mac/my_salary.txt
		 * 密鑰檔位置：src/security/mac/macKey.key
		 * File Hash：1d68245c7c03d857985d3a80b846b61101f1196292492db1b707c1657ce3fe04
		 * File MAC：572923280e6bd88a72a725dade9c7a33597336ff2eaf2361ee28d37a1926b5a8
		 * */
		
		String filepath = "src/security/mac/my_salary.txt";
		String keypath = "src/security/mac/macKey.key";
		String hashFromHR = "1d68245c7c03d857985d3a80b846b61101f1196292492db1b707c1657ce3fe04";
		String macFromHR = "572923280e6bd88a72a725dade9c7a33597336ff2eaf2361ee28d37a1926b5a8";
		
		// 從薪資檔案中重新計算雜湊值
		String computedHash = KeyUtil.generateFileHash(filepath);
		
		// 從密鑰檔生成 SecretKey 物件
		SecretKey macKey = KeyUtil.getSecretKeyFromFile("HmacSHA256", keypath);
		// 從薪資檔案與 macKey 中重新計算 mac 值
		String computedMac = KeyUtil.generateMac("HmacSHA256", macKey, filepath);
		
		// 驗證雜湊（Hash）與 MAC
		if(hashFromHR.equals(computedHash) && macFromHR.equals(computedMac)) {
			System.out.println("驗證成功！薪資明細完整性與真實性（來自HR）都得到了確認");
			// 讀取檔案內容
			String data = Files.readString(Paths.get(filepath));
			System.out.println(data);
		} else {
			System.out.println("驗證失敗！");
		}
		
	}
}
