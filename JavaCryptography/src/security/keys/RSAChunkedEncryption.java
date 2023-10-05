package security.keys;

import java.security.KeyPair;
import java.util.Base64;

import security.KeyUtil;

import static security.KeyUtil.*;

/**
 * 混合使用 RSA 分段加密技術。
 * 
 * 運作概念：
 * Alice 使用自己的私鑰對訊息生成數字簽名。
 * Alice 將訊息和簽名合併。
 * Alice 使用 Bob 的公鑰對合併後的訊息進行分段RSA加密。
 * Alice 將加密後的數據發送給 Bob。
 * Bob 使用自己的私鑰對加密數據進行分段解密。
 * Bob 分離訊息和簽名。
 * Bob 使用 Alice 的公鑰驗證簽名的有效性。
 * */

public class RSAChunkedEncryption {

    public static void main(String[] args) throws Exception {
        // 初始化 Alice 和 Bob 的鑰匙對
        KeyPair aliceKeyPair = generateRSAKeyPair();
        KeyPair bobKeyPair = generateRSAKeyPair();

        // Alice 的訊息
        String message = "Hello, Bob! 這是一個長訊息並使用 RSA 分段加密技術";

        // Alice 使用自己的私鑰對訊息進行數字簽名
        byte[] signature = signMessage(aliceKeyPair.getPrivate(), message);
        String combinedMessage = message + "|" + Base64.getEncoder().encodeToString(signature);

        // Alice 使用 Bob 的公鑰對合併後的訊息進行分段RSA加密
        byte[] encryptedMessage = encryptWithPublicKey(bobKeyPair.getPublic(), combinedMessage.getBytes());

        // 這裡模擬 Alice 發送加密後的訊息到 Bob

        // Bob 使用他的私鑰對加密的訊息進行分段RSA解密
        byte[] decryptedMessageBytes = decryptWithPrivateKey(bobKeyPair.getPrivate(), encryptedMessage);
        String decryptedCombinedMessage = new String(decryptedMessageBytes);
        // 1. 正則表示式 "|" 若要跳脫需要寫成 "\|"  
        // 2. 但是因為 "\" 也是 Java 的關鍵字所以要再跳脫變成 "\\|"
        String[] parts = decryptedCombinedMessage.split("\\|");
        String receivedMessage = parts[0];
        byte[] receivedSignature = Base64.getDecoder().decode(parts[1]);

        // Bob 使用 Alice 的公鑰驗證簽名的有效性
        boolean isVerified = KeyUtil.verifySignature(aliceKeyPair.getPublic(), receivedMessage, receivedSignature, "SHA512withRSA");

        if (isVerified) {
            System.out.println("驗證成功：Message from Alice: " + receivedMessage);
        } else {
            System.out.println("驗證失敗：Message signature verification failed!");
        }
    }
}

