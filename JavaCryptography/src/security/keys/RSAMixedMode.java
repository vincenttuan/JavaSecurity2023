package security.keys;

import java.security.KeyPair;
import java.util.Base64;

import security.KeyUtil;

/**
 * 混合使用 AES 和 RSA 加密。
 * 
 * 運作概念：
 * 1. Alice 生成隨機的 AES 密鑰。
 * 2. Alice 使用 AES 密鑰加密訊息。
 * 3. Alice 使用 Bob 的 RSA 公鑰加密 AES 密鑰。
 * 4. Alice 發送加密後的訊息與加密的 AES 密鑰給 Bob。
 * 5. Bob 使用他的 RSA 私鑰解密 AES 密鑰。
 * 6. Bob 使用 AES 密鑰解密訊息，得到原始訊息。
 */
public class RSAMixedMode {

	public static void main(String[] args) throws Exception {
		// 建立 Alice 與 Bob 的密鑰對
		KeyPair aliceKeyPair = KeyUtil.generateRSAKeyPair();
		KeyPair bobKeyPair = KeyUtil.generateRSAKeyPair();
		
		// Alice 想傳的訊息
		String message = "Hello, Bob !";
		System.out.println("原始訊息：" + message);
		
		// Alice 使用自己的私鑰對訊息進行數字簽名，確保訊息真的是 Alice 自己寫的
		byte[] signature = KeyUtil.signMessage(aliceKeyPair.getPrivate(), message);
		// Alice 將訊息與簽名(Base64編碼)合併
		String combineMessage = message + "|" + Base64.getEncoder().encodeToString(signature);
		System.out.println("原始訊息+簽名：" + combineMessage);
	}

}
