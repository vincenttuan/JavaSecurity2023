package security;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyUtil {

    private static final int CHUNK_SIZE = 200; // 定義每段訊息的加密大小

    /**
     * 生成一個2048位的RSA鑰匙對。
     * 
     * @return 返回生成的RSA的公鑰和私鑰對。
     * @throws NoSuchAlgorithmException 若當前環境不支援RSA加密算法時拋出。
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * 生成一個256位的AES密鑰。
     * 
     * @return 返回生成的AES的密鑰。
     * @throws NoSuchAlgorithmException 若當前環境不支援AES加密算法時拋出。
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    /**
     * 使用私鑰對訊息生成一個數字簽名。
     * 
     * @param privateKey 用於簽名的私鑰。
     * @param message    待簽名的訊息。
     * @return 返回該訊息的數字簽名。
     * @throws Exception 若簽名過程中發生錯誤時拋出。
     */
    public static byte[] signMessage(PrivateKey privateKey, String message) throws Exception {
        // SHA512withRSA : 提供了更高的安全性，在64位的系統和現代的CPU，使用SHA-512可能實際上比使用SHA-256更快
    	// SHA256withRSA : 是安全的組合，它使用 SHA-256 作為散列演算法，並使用 RSA 作為簽名演算法。
    	// SHA384withRSA : 介於SHA256與SHA512之間
    	Signature sign = Signature.getInstance("SHA512withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes());
        return sign.sign();
    }

    /**
     * 使用公鑰驗證訊息的數字簽名。
     * 
     * @param publicKey 公鑰，用於驗證簽名。
     * @param message   原始的未加密訊息。
     * @param signature 要驗證的數字簽名。
     * @return 若簽名有效，返回true，否則返回false。
     * @throws Exception 若簽名驗證過程中發生錯誤時拋出。
     */
    public static boolean verifySignature(PublicKey publicKey, String message, byte[] signature) throws Exception {
        Signature sign = Signature.getInstance("SHA512withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes());
        return sign.verify(signature);
    }

    /**
     * 使用AES密鑰加密訊息。
     * 
     * @param aesKey  AES密鑰，用於加密。
     * @param message 待加密的訊息。
     * @return 返回加密後的字節數據。
     * @throws Exception 若加密過程中發生錯誤時拋出。
     */
    public static byte[] encryptWithAESKey(SecretKey aesKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(message.getBytes());
    }

    /**
     * 使用AES密鑰解密訊息。
     * 
     * @param aesKey        AES密鑰，用於解密。
     * @param encryptedData 已加密的訊息。
     * @return 返回解密後的字符串。
     * @throws Exception 若解密過程中發生錯誤時拋出。
     */
    public static String decryptWithAESKey(SecretKey aesKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(encryptedData));
    }

    /**
     * 使用公鑰進行RSA分段加密。
     * 
     * @param publicKey 公鑰，用於加密。
     * @param data      待加密的訊息。
     * @return 返回加密後的字節數據。
     * @throws Exception 若加密過程中發生錯誤時拋出。
     */
    public static byte[] encryptWithPublicKey(PublicKey publicKey, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = new byte[0];
        for (int i = 0; i < data.length; i += CHUNK_SIZE) {
            byte[] chunk = new byte[Math.min(CHUNK_SIZE, data.length - i)];
            System.arraycopy(data, i, chunk, 0, chunk.length);
            byte[] encryptedChunk = cipher.doFinal(chunk);
            encryptedData = concat(encryptedData, encryptedChunk);
        }
        return encryptedData;
    }

    /**
     * 使用私鑰進行RSA分段解密。
     * 
     * @param privateKey    私鑰，用於解密。
     * @param encryptedData 已加密的訊息。
     * @return 返回解密後的字節數據。
     * @throws Exception 若解密過程中發生錯誤時拋出。
     */
    public static byte[] decryptWithPrivateKey(PrivateKey privateKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        // 除以8是為了將位元數(bit count)轉換成字節數(byte count)。
        int chunkLength = (privateKey instanceof RSAPrivateKey) ? 
            ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 : CHUNK_SIZE;

        byte[] decryptedData = new byte[0];
        for (int i = 0; i < encryptedData.length; i += chunkLength) {
            byte[] encryptedChunk = new byte[chunkLength];
            System.arraycopy(encryptedData, i, encryptedChunk, 0, chunkLength);
            byte[] decryptedChunk = cipher.doFinal(encryptedChunk);
            decryptedData = concat(decryptedData, decryptedChunk);
        }
        return decryptedData;
    }

    /**
     * 合併兩個字節數組。
     * 
     * @param a 第一個字節數組。
     * @param b 第二個字節數組。
     * @return 返回合併後的字節數組。
     */
    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    /**
     * 使用AES密鑰和初始化向量 (IV) 進行加密。
     * 
     * @param key  用於加密的AES密鑰。
     * @param data 需要加密的訊息。
     * @param iv   用於CBC模式的初始化向量。
     * @return 返回加密後的字節數據。
     * @throws Exception 若在加密過程中出現問題時拋出。
     */
    public static byte[] encryptWithAESKeyAndIV(SecretKeySpec key, String data, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * 使用AES密鑰和初始化向量 (IV) 進行解密。
     * 
     * @param key           用於解密的AES密鑰。
     * @param encryptedData 需要解密的字節數據。
     * @param iv            用於CBC模式的初始化向量。
     * @return 返回解密後的訊息。
     * @throws Exception 若在解密過程中出現問題時拋出。
     */
    public static String decryptWithAESKeyAndIV(SecretKeySpec key, byte[] encryptedData, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    /**
     * 使用AES密鑰和初始化向量 (IV) 在CTR模式下進行加密。
     * 
     * @param key  用於加密的AES密鑰。
     * @param data 需要加密的訊息。
     * @param iv   用於CTR模式的初始化向量。
     * @return 返回加密後的字節數據。
     * @throws Exception 若在加密過程中出現問題時拋出。
     */
    public static byte[] encryptWithAESKeyAndIVInCTRMode(SecretKeySpec key, String data, byte[] iv) throws Exception {
    	// 當使用AES的CTR模式時，通常使用"NoPadding"作為填充方案，而不是"PKCS5Padding"。
    	// 這是因為CTR模式將密鑰轉化為一個字節流，該字節流與原始數據進行XOR操作，因此不需要任何填充。
    	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * 使用AES密鑰和初始化向量 (IV) 在CTR模式下進行解密。
     * 
     * @param key           用於解密的AES密鑰。
     * @param encryptedData 需要解密的字節數據。
     * @param iv            用於CTR模式的初始化向量。
     * @return 返回解密後的訊息。
     * @throws Exception 若在解密過程中出現問題時拋出。
     */
    public static String decryptWithAESKeyAndIVInCTRMode(SecretKeySpec key, byte[] encryptedData, byte[] iv) throws Exception {
    	// 當使用AES的CTR模式時，通常使用"NoPadding"作為填充方案，而不是"PKCS5Padding"。
    	// 這是因為CTR模式將密鑰轉化為一個字節流，該字節流與原始數據進行XOR操作，因此不需要任何填充。
    	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }
    
    // 輔助方法，將字節轉換為十六進制格式的字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * 要從十六進制格式的雜湊字串轉回 byte[]
     * 
     * @return 返回 byte[]。
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}