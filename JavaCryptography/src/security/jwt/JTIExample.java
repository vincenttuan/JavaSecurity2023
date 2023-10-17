package security.jwt;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Source;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import security.KeyUtil;

/**
 * JTIExample 示範如何使用 JWT 的 JTI (JWT ID) 聲明。
 * 
 * 1. 生成 JWT 時，給予它一個獨特的 JTI。
 * 2. 在驗證 JWT 時，檢查其 JTI 以確保它未被撤銷。
 * 
 * 這種方法特別有用於需要撤銷 JWT 或跟踪特定 JWT 的場景。
 * 
 */
public class JTIExample {
	// 儲存已被撤銷的 JWT 的 JTI 列表
	private static Map<String, Object> revokedJTIs = new LinkedHashMap<>();
	
	public static void main(String[] args) throws Exception {
		// 1. 生成簽名密鑰
		String masterKey = KeyUtil.generateSecret(32);
		
		// 2. 創建一個 JWT 並透過 UUID 來建立 JTI
		String jtiId = UUID.randomUUID().toString();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("user")
				.issuer("sample")
				.claim("name", "John")
				.jwtID(jtiId) // 設定 JTI
				.build();
		
		// 3. 對 JWT 進行簽名
		String signedToken = KeyUtil.signJWT(claimsSet, masterKey);
		System.out.printf("JWT token: %s%n", signedToken);
		System.out.printf("撤銷列表：%s%n", revokedJTIs);
		
		Thread.sleep(2000);
		
		// 4. 撤銷 JWT
		System.out.println("撤銷 JWT");
		revokedJTIs.put(claimsSet.getJWTID(), "撤銷");
		System.out.printf("撤銷列表：%s%n", revokedJTIs);
		
		// 5. 驗證 JWT
		if(KeyUtil.verifyJWTSignature(signedToken, masterKey)) {
			System.out.println("JWT 目前仍有效");
			// 5.1 還要再檢查該 JWT 的 JTI 是否已被撤銷（是否有在 revokedJTIs 列表集合中？）
			JWTClaimsSet claims = KeyUtil.getClaimsFromToken(signedToken);
			String jti = claims.getJWTID();
			System.out.printf("JTI: %s%n", jti);
			if(revokedJTIs.get(jti) == null) { // 是否有在 revokedJTIs 列表集合中？
				System.out.println("JWT 尚未被撤銷");
			} else {
				System.out.println("JWT 已被撤銷");
			}
		} else {
			System.out.println("JWT 失效");
		}
	}

}
