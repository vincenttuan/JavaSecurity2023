package security.jwt;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.*;

import security.KeyUtil;

public class SimpleJWTJWE {
	
    public static void main(String[] args) throws Exception {
        // 1. JWK: 生成簽名和加密的密鑰
        String signingSecret = KeyUtil.generateSecret(32);  // JWK: 這裡我們產生了一個密鑰 (Key) 用於簽名
        String encryptionSecret = KeyUtil.generateSecret(16);  // JWK: 這裡我們產生了另一個密鑰用於加密

        // 2. 創建 JWT 的內容 (claims)
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user")
                .issuer("https://myapp.com")
                .claim("name", "John Doe")
                .build();

        // 3. JWT：使用 JWS (JSON Web Signature) 來對 JWT 進行簽名
        SignedJWT signedJWT = KeyUtil.signWithSecret(claimsSet, signingSecret);  // 這裡使用了 JWS 進行簽名

        // 4. 使用 JWE (JSON Web Encryption) 來加密 JWT
        String encryptedJWT = KeyUtil.encryptJWT(signedJWT, encryptionSecret);  // 這裡使用了 JWE 進行加密
        System.out.println("簽名的（加密）JWT: " + encryptedJWT);

        // 5. 解密 JWT
        SignedJWT decryptedSignedJWT = KeyUtil.decryptJWT(encryptedJWT, encryptionSecret);

        // 6. 驗證 JWT 的簽名
        // JWA (JSON Web Algorithms) 定義了在 JWT 中使用的加密算法
        // 在這裡我們使用 HMAC + SHA-256 算法 (通常表示為 HS256)
        if (decryptedSignedJWT.verify(new MACVerifier(signingSecret))) {
            System.out.println("簽名驗證成功!");
            JWTClaimsSet claims = decryptedSignedJWT.getJWTClaimsSet();
            System.out.println("聲明中的名稱: " + claims.getStringClaim("name"));
            System.out.println("主題 (subject): " + claims.getSubject());
            System.out.println("發行者 (issuer): " + claims.getIssuer());
        } else {
            System.out.println("簽名驗證失敗!");
        }
    }
}
