package security.keys;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import security.KeyUtil;

public class RSASample {

	public static void main(String[] args) throws Exception {
		// 1. 生成 RSA 密鑰對(公鑰/私鑰)
		System.out.println("1. 生成 RSA 密鑰對(公鑰/私鑰)");
		KeyPair keyPair = KeyUtil.generateRSAKeyPair(); // RSA-2048
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		//System.out.println("PublicKey 公鑰:" + publicKey);
		//System.out.println("PrivateKey 私鑰:" + privateKey);
		
		// 2. 原始訊息（要加密的訊息）
		String originalMessage = "明天正常上班上課";
		System.out.println("2. 原始訊息: " + originalMessage);
		
		// 3. 利用公鑰進行加密
		System.out.println("3. 利用公鑰進行加密");
		// 將 originalMessage 加密
		byte[] encryptedBytes = KeyUtil.encryptWithPublicKey(publicKey, originalMessage.getBytes());
		// 以 Base64 編碼來呈現加密後的資訊
		System.out.println("   加密後的訊息(Base64): " + Base64.getEncoder().encodeToString(encryptedBytes));
		
		// 4. 利用私鑰進行解密
		System.out.println("4. 利用私鑰進行解密");
		System.out.println("   解密中...");
		Thread.sleep(3000);
		// 將 encryptedBytes(已被加密) 的資料透過私鑰解密
		byte[] decryptedBytes = KeyUtil.decryptWithPrivateKey(privateKey, encryptedBytes);
		String decryptedMessage = new String(decryptedBytes); // 將解密後的資料轉字串
		System.out.println("   解密後的訊息: " + decryptedMessage);
	}

}
