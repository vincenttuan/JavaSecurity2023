package security.hash;

import java.security.MessageDigest;

import security.KeyUtil;

public class SimpleHash {

	public static void main(String[] args) throws Exception {
		// Hot Code 一段密碼
		String password = "1234";
		
		// 1. 獲取 SHA-256 消息摘要物件，幫助我們生成密碼的哈希
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		// 2. 將密碼轉換為 byte[] 然後生成哈希
		byte[] hashedBytes = messageDigest.digest(password.getBytes());
		
		// 將 byte[] 轉 Hex(16進位) 字串呈現
		String hashedString = KeyUtil.bytesToHex(hashedBytes);
		System.out.println("原始密碼：" + password);
		System.out.println("哈希原始密碼：" + hashedString);
	}

}
