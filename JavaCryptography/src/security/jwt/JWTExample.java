package security.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import security.KeyUtil;

/**
依據（JWA、JWK）與（JWS）產生 Token（JWT） 

	+-----+   +-----+
	| JWK | → | JWS |
	+-----+   +-----+
	   ↑         ↓
	+-----+   +-----+
	| JWA |   | JWT |
	+-----+   +-----+
		
 */
public class JWTExample {
	public static void main(String[] args) throws Exception {
		// 1. JWA：使用 HS256 算法
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
		
		// 2. JWK：產生一個簽名用的密鑰 給 JWS 使用
		String signingSecret = KeyUtil.generateSecret(32); // 256位元（32）字結的密鑰
		System.out.println("signingSecret: \n" + signingSecret);
		// 定義 Payload
		JWTClaimsSet payload = new JWTClaimsSet.Builder()
				.subject("user") // 設定主題
				.issuer("http://myapp.com") // 設定發行者
				.claim("name", "John") // 添加自定義資訊
				.claim("email", "jhon@myapp.com")
				.build();
		
		// 3. JWT：創建 JWT（尚未簽名）
		SignedJWT signedJWT = new SignedJWT(header, payload);
		
		// 4. JWS：將 JWT 簽名
		JWSSigner jwsSigner = new MACSigner(signingSecret); // JWS 簽章利用 JWK 生成的密鑰
		// 進行簽名
		signedJWT.sign(jwsSigner);
		
		// 5. 透過序列化技術可以將 jwt 被安全的傳遞或儲存，而不曝露其內容
		String token = signedJWT.serialize(); // 序列化
		
		System.out.println("「有簽名」但內容「無加密」的 JWT（token）: \n" + token);
		
		
	}
}
