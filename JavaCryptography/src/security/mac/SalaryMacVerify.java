package security.mac;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.SecretKey;

import security.KeyUtil;

/*
 * 
 * 用於驗證薪資明細的 MAC
 * 
 * 證明成功實施了 MAC 驗證策略，並且可以正確地驗證您的薪資明細的完整性和來源。
 * 使用這樣的策略可以確保只有擁有正確 MAC 密鑰的人（在這個例子中是 HR 部門）
 * 才能生成有效的 MAC，而其他人則不能。
 * 
 * 這是一個非常重要的安全策略，特別是在涉及敏感資訊（如薪資明細）的場合。
 * 只要保護好您的密鑰，就可以確保消息的真實性和完整性。
*/
public class SalaryMacVerify {

	public static void main(String[] args) throws Exception {
		// 員工：
		// 取得薪資檔案位置
		String filepath = "src/security/mac/my_salary.txt";
		// 知道金鑰檔案位置
		String keypath = "src/security/mac/macKey.key";
		
		// 將密鑰檔 macKey.key 轉成密鑰(SecretKey)物件
		SecretKey macKey = KeyUtil.getSecretKeyFromFile("HmacSHA256", keypath);
		
		// 得到 HR 部門所生成的 mac value
		String hrMacValue = "3cfe2ad7ded695e5c518c3fbe0f5e61267290fd12e781bf6592d11556e5a3c90";
		
		// 員工自己生成 mac value 來與 HR 部門所生成的 mac value 進行比對
		// macKey + filepath 生成 mac value
		String computedMacValue = KeyUtil.generateMac("HmacSHA256", macKey, filepath);
		
		// 最後確認資料是否是來自於 HR 部門
		if(computedMacValue.equals(hrMacValue)) {
			System.out.println("MAC 驗證成功，此訊息是來自於 HR 部門");
			// 讀取檔案內容
			String data = Files.readString(Paths.get(filepath));
			System.out.println(data);
		} else {
			System.out.println("MAC 驗證失敗");
		}
				

	}

}
