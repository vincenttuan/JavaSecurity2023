package security.keys;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import security.KeyUtil;

public class AESSample {
	// 建立一個 AES Key (AES-128 bits, 16 bytes)
	//private static final String KEY = "0123456789abcdef"; // 16個字
	// 建立一個 AES Key (AES-192 bits, 24 bytes)
	//private static final String KEY = "0123456789abcdef12345678"; // 24個字
	// 建立一個 AES Key (AES-256 bits, 32 bytes)
	private static final String KEY = "0123456789abcdef0123456789abcdef"; // 32個字
	// Random (透過時序為亂數種子，易被破解) 與 SecureRandom
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	public static void main(String[] args) throws Exception {
		System.out.println("AES 加密範例:");
		
		String originalText = "1234"; // 明文
		System.out.println("原始訊息（明文）：" + originalText);
		System.out.println("------------------------------------------------------------------------------------------");
		
		// 建立 AES 密鑰規範
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		
		// ECB 模式
		System.out.println("ECB 模式");
		// 進行資料加密(將明文進行 AES-EBC 加密)
		byte[] encryptedECB = KeyUtil.encryptWithAESKey(aesKeySpec, originalText);
		System.out.println("加密後的訊息:" + Arrays.toString(encryptedECB));
		// byte[] 轉 base64 字串
		System.out.println("加密後的訊息(Base64):" + Base64.getEncoder().encodeToString(encryptedECB));
		// 進行解密
		String base64 = "LyZu9u0Trqjupx09zgxYnA==";
		// 將 base64 字串轉回 byte[]
		byte[] encryptedECB_2 = Base64.getDecoder().decode(base64);
		// 進行解密
		String decryptedECB = KeyUtil.decryptWithAESKey(aesKeySpec, encryptedECB_2);
		System.out.println("解密後的訊息:" + decryptedECB);
		
		System.out.println("------------------------------------------------------------------------------------------");
		// CBC 模式
		System.out.println("CBC 模式");
		// 建立隨機 IV
		byte[] ivCBC = new byte[16];
		SECURE_RANDOM.nextBytes(ivCBC); // 產生一個隨機的 byte[] 資訊當作 IV
		System.out.println("隨機 CBC IV:" + Base64.getEncoder().encodeToString(ivCBC));
		
		// 進行資料加密(將明文進行 AES-CBC 加密)
		byte[] encryptedCBC = KeyUtil.encryptWithAESKeyAndIV(aesKeySpec, originalText, ivCBC);
		System.out.println("加密後的訊息(Base64):" + Base64.getEncoder().encodeToString(encryptedCBC));
		
		// 進行資料解密
		String decryptedCBC = KeyUtil.decryptWithAESKeyAndIV(aesKeySpec, encryptedCBC, ivCBC);
		System.out.println("解密後的訊息:" + decryptedCBC);
		
		System.out.println("------------------------------------------------------------------------------------------");
		
		// CTR 模式
		System.out.println("CTR 模式");
		// 建立隨機 IV
		byte[] ivCTR = new byte[16];
		SECURE_RANDOM.nextBytes(ivCTR);
		System.out.println("隨機 CTR IV:" + Base64.getEncoder().encodeToString(ivCTR));
		
		// 進行資料加密(將明文進行 AES-CTR 加密)
		byte[] encryptedCTR = KeyUtil.encryptWithAESKeyAndIVInCTRMode(aesKeySpec, originalText, ivCTR);
		System.out.println("加密後的訊息(Base64):" + Base64.getEncoder().encodeToString(encryptedCTR));
		
		// 進行資料解密
		String decryptedCTR = KeyUtil.decryptWithAESKeyAndIVInCTRMode(aesKeySpec, encryptedCTR, ivCTR);
		System.out.println("解密後的訊息:" + decryptedCTR);
				
	}

}
