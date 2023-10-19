package servlet.secure.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.WebKeyUtil;

/*
 * 
 * 為了實現HTTP摘要驗證，我們需要進一步的邏輯和某些額外的步驟。摘要驗證的主要優勢是它避免明文傳送密碼，而是發送一個摘要。
 * 這是一種摘要算法的結果，通常是MD5，該算法使用密碼、隨機的nonce值、HTTP方法和請求的URI作為輸入。
 * 注意：在實務應用上必須使用 HTTPS 進行通信，以確保傳輸過程中的安全性。
 *
 * 預設的用戶名和密碼，用於HTTP Basic和Digest認證
 * username = user
 * password = 1234
 * */
@WebServlet(value = "/secure/servlet/auth/digest_auth")
public class DigestAuthServlet extends HttpServlet {
	
	private static final String REALM = "Digest Restricted Area";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 得到 client 端的 Authorization 標頭資訊
		String authHeader = req.getHeader("Authorization");
		
		// 檢查 'Authorization' 標頭內容是否是以 'Digest' 開頭
		if(authHeader != null && authHeader.startsWith("Digest ")) {
			// 驗證 digest 的正確性
			if(WebKeyUtil.isValidDigest(authHeader, req.getMethod(), req.getRequestURI(), REALM)) {
				// 若驗證成功就顯示受保護的資料
				resp.getWriter().print("Welcome to the Digest protected page !");
				return;
			}
		}
		
		// 如果認證失敗，則要求客戶端提供「摘要 Digest」認證訊息
		resp.setHeader("WWW-Authenticate", WebKeyUtil.generateDigestChallenge(REALM));
		// 發送 401 未經授權的回應
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	
}
