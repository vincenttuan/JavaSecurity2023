package security.keys;

import java.security.KeyPair;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
		
		// Alice 要使用 AES 密鑰對（combineMessage「訊息與簽名」）進行加密
		// 1. Alice 要生成一個 AES 密鑰, 同時 Bob 也要有此密鑰
		SecretKey aseKey = KeyUtil.generateAESKey();
		byte[] encyptedMessage = KeyUtil.encryptWithAESKey(aseKey, combineMessage);
		
		// Alice 使用 Bob 的 RSA 公鑰加密 AES 密鑰
		byte[] encryptedAESKey = KeyUtil.encryptWithPublicKey(bobKeyPair.getPublic(), aseKey.getEncoded());
		
		// 這裡模擬 Alice 發送加密後的訊息到 Bob
        // (此時 Alice 會將 encryptedMessage 和 encryptedAESKey 一同發送給 Bob) 
		System.out.println("Bob 解密:");
		// Bob 使用他的 RSA 私鑰解密得到 AES 密鑰
		byte[] decryptedAESKey = KeyUtil.decryptWithPrivateKey(bobKeyPair.getPrivate(), encryptedAESKey);
		// originalAESKey 就會還原得到 p.42 行的 Alice 所建立的 aesKey
		SecretKey originalAESKey = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");
		
		// Bob 使用解密後的 AES 密鑰解密訊息
		String decrypted = KeyUtil.decryptWithAESKey(originalAESKey, encyptedMessage);
		System.out.println(decrypted);
		// 1. 正則表示式 "|" 若要跳脫需要寫成 "\|"  
        // 2. 但是因為 "\" 也是 Java 的關鍵字所以要再跳脫變成 "\\|"
		String[] parts = decrypted.split("\\|");
		// 得到 Alice 的訊息
		String reveivedMessage = parts[0];
		System.out.println(reveivedMessage);
		// 得到 Alice 的簽名
		byte[] receivedSignature = Base64.getDecoder().decode(parts[1]);
		
		// Bob 要使用 Alice 的公鑰來驗證是否是 Alice 的簽名
		// 2048 位元的 RSA 密鑰長度與 "SHA512withRSA" 簽名算法可以一起使用。
		// RSA 2048 是足夠強大的，可以安全地與 SHA-512 一起使用。
		boolean isVerified = KeyUtil.verifySignature(aliceKeyPair.getPublic(), reveivedMessage, receivedSignature, "SHA512withRSA");
		if(isVerified) {
			System.out.println("收到 Alice 簽名的訊息：" + reveivedMessage);
		} else {
			System.out.println("收到不是來自 Alice 簽名的訊息：" + reveivedMessage);
		}
	}

}
