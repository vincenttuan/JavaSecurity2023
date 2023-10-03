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
		// byte[] 轉 base64 字串
		System.out.println("加密後的訊息(Base64):" + Base64.getEncoder().encodeToString(encryptedECB));
		// 進行解密
		String base64 = "jK0VRYQnjX3y3/ZzyDNxQyjQyv5he2d+03VDd37Fmkl49flzn6mdTCCUu4kwgMsp";
		// 將 base64 字串轉回 byte[]
		byte[] encryptedECB_2 = Base64.getDecoder().decode(base64);
		// 進行解密
		String decryptedECB = KeyUtil.decryptWithAESKey(aesKeySpec, encryptedECB_2);
		System.out.println("解密後的訊息:" + decryptedECB);
		
		// CBC 模式
		
		// CTR 模式
		
	}

}
