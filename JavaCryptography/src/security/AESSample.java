package security;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

public class AESSample {
	// 建立一個 AES Key (AES-128 bits, 16 bytes)
	private static final String KEY = "0123456789abcdef"; // 16個字
	// Random (透過時序為亂數種子，易被破解) 與 SecureRandom
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	public static void main(String[] args) throws Exception {
		System.out.println("AES 加密範例:");
		
		String originalTest = "Hello 我的 AES 加密文字範例"; // 明文
		System.out.println("原始訊息（明文）：" + originalTest);
		System.out.println("---------------------------------");
		
		// 建立 AES 密鑰規範
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		
		// ECB 模式
		System.out.println("ECB 模式");
		// 進行資料加密(將明文進行 AES-EBC 加密)
		byte[] encryptedECB = KeyUtil.encryptWithAESKey(aesKeySpec, originalTest);
		System.out.println("加密後的訊息:" + Arrays.toString(encryptedECB));
		System.out.println("加密後的訊息(Base64):" + Base64.getEncoder().encodeToString(encryptedECB));
		// CBC 模式
		
		// CTR 模式
		
	}

}
