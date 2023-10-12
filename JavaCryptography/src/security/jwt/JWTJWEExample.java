package security.jwt;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import security.KeyUtil;

/**
依據（JWA、JWK）與（JWE、JWS）產生 Token（JWT） 

	+-----+   +-----+   +-----+
	| JWK | → | JWE |   | JWS |
	+-----+   +-----+   +-----+
	   ↑            ↓   ↓
	+-----+        +-----+
	| JWA |        | JWT |
	+-----+        +-----+
	
*/
public class JWTJWEExample {
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
				.claim("email", "John@myapp.com")
				.build();
		
		// 3. JWT：創建 JWT（尚未簽名）
		SignedJWT signedJWT = new SignedJWT(header, payload);
		
		// 4. JWS：將 JWT 簽名
		JWSSigner jwsSigner = new MACSigner(signingSecret); // JWS 簽章利用 JWK 生成的密鑰
		// 進行簽名
		signedJWT.sign(jwsSigner);
		
		// 4.1 JWE：對已簽名的 JWT 進行加密 -------------------------------------------------
		// 使用 A192GCM 演算法 192位元（24字節）的密鑰
		JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A192GCM)
											.contentType("JWT")
											.build();
		JWEObject jweObject = new JWEObject(
					jweHeader,
					new Payload(signedJWT)
		);
		
		// 4.2 加密
		// 應該要用 24, 但是會有 bug 所以用 16
		// 可能是 nimbusds 的問題，也許在後續版本會修正
		String encryptionSecret = KeyUtil.generateSecret(16); 
		jweObject.encrypt(new DirectEncrypter(encryptionSecret.getBytes())); // 直接加密
		
		//--------------------------------------------------------------------------------
		
		// 5. 透過序列化技術可以將 jwt 被安全的傳遞或儲存，而不曝露其內容
		//String token = signedJWT.serialize(); // 序列化
		//System.out.println("「有簽名」但內容「無加密」的 JWT（token）: \n" + token);
		
		String token = jweObject.serialize();
		System.out.println("「有簽名」且內容「有加密」的 JWT（token）: \n" + token);
		
		//**********************************************************************
		System.out.println();
		
		// 6. 解密：將加密的 JWT 先行解密
		JWEObject decryptdJweObject = JWEObject.parse(token);
		decryptdJweObject.decrypt(new DirectDecrypter(encryptionSecret.getBytes()));
		
		// 7. 驗證：驗證 JWT 的簽名
		// 從 decryptdJweObject 中取得 jwt
		SignedJWT verifiedJWT = decryptdJweObject.getPayload().toSignedJWT();
		
		// 取得密鑰
		JWSVerifier verifier = new MACVerifier(signingSecret);
		
		if(verifiedJWT.verify(verifier)) {
			System.out.println("簽名驗證成功 !");
			JWTClaimsSet claims = verifiedJWT.getJWTClaimsSet();
			System.out.println("主題 subject：" + claims.getSubject());
			System.out.println("發行者 issuer：" + claims.getIssuer());
			System.out.println("name：" + claims.getStringClaim("name"));
			System.out.println("email：" + claims.getStringClaim("email"));
		} else {
			System.out.println("簽名驗證失敗");
		}
	}
}
