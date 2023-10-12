package security.jwt;

import com.nimbusds.jwt.*;

import security.KeyUtil;

public class SimpleJWT {

    public static void main(String[] args) throws Exception {
        // 1. 生成簽名密鑰
        // JWK: 使用 `generateSecret` 方法產生了一個簽名用的密鑰
        String signingSecret = KeyUtil.generateSecret(32);
        
        // 2. 創建 JWT 的聲明 (claims)
        // JWT: 這是我們要進行簽名的 JSON Web Token 的部分
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user")  // 主體，通常是指代一個用戶
                .issuer("https://myapp.com")  // 發行者，這裡是模擬的應用網址
                .claim("name", "John Doe")  // 額外的聲明，這裡用於保存名稱
                .build();
        
        // 3. JWT：對 JWT 進行簽名
        // JWS: 這裡是使用 JSON Web Signature 進行簽名的過程
        // JWA: 我們在 `signJWT` 方法內部使用了一種稱為 HMAC + SHA-256 的算法 (通常表示為 HS256)
        String token = KeyUtil.signJWT(claimsSet, signingSecret);
        
        // 輸出已簽名的 JWT
        System.out.println("簽名的（無加密）JWT: " + token);

        // 4. 驗證 JWT 的簽名
        // JWA: 我們在 `verifyJWTSignature` 方法內部使用了一種稱為 HMAC + SHA-256 的算法 (通常表示為 HS256)
        if (KeyUtil.verifyJWTSignature(token, signingSecret)) {
            // 若簽名驗證成功，取出 JWT 內的聲明 (claims) 並輸出
            JWTClaimsSet claims = KeyUtil.getClaimsFromToken(token);
            System.out.println("聲明中的名稱: " + claims.getStringClaim("name"));
            System.out.println("主題 (subject): " + claims.getSubject());
            System.out.println("發行者 (issuer): " + claims.getIssuer());
        } else {
            // 若簽名驗證失敗，輸出提示
            System.out.println("簽名驗證失敗!");
        }
    }
}
