package security.hash;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import security.KeyUtil;

public class SimpleAddSaltHash {
	
	// 加鹽 Hash
	public static void main(String[] args) throws Exception {
		// Hot Code 一個密碼
		String password = "1234";
		
		// 隨機生成一個鹽(salt)
		byte[] salt = new byte[16];
		System.out.println("鹽（填充前）：" + Arrays.toString(salt));
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt); // 填充隨機值
		System.out.println("鹽（填充後）：" + Arrays.toString(salt));
		System.out.println("鹽（填充後 Hex）：" + KeyUtil.bytesToHex(salt));
		
		// 1. 獲取 SHA-256 消息摘要物件，幫助我們生成密碼的哈希
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		// 2. 加鹽
		messageDigest.update(salt);
		
		// 3. 將密碼轉換為 byte[] 然後生成哈希
		byte[] hashedBytes = messageDigest.digest(password.getBytes());
		
		// 將 byte[] 轉 Hex(16進位) 字串呈現
		String hashedString = KeyUtil.bytesToHex(hashedBytes);
		System.out.println("原始密碼：" + password);
		System.out.println("哈希原始密碼：" + hashedString);
	}

}
