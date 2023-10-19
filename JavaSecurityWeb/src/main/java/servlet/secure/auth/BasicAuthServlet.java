package servlet.secure.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.WebKeyUtil;

/*
 * BasicAuthServlet 是一個實現了 HTTP Basic Authentication 的 Servlet。
 * 由於用戶名和密碼在 HTTP 請求中以 Base64 編碼的形式發送（而非加密形式），
 * 注意：在實務應用上必須使用 HTTPS 進行通信，以確保傳輸過程中的安全性。
 * 
 * 預設的用戶名和密碼，用於HTTP Basic和Digest認證
 * username = user
 * password = 1234
 * */
@WebServlet(value = "/secure/servlet/auth/basic_auth")
public class BasicAuthServlet extends HttpServlet {
	private static final String REALM = "Restricted Area"; // 定義領域

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String authHeader = req.getHeader("Authorization"); // 得到 client 端來的 Authorization 標頭
		
		if(authHeader != null && WebKeyUtil.isValidBasicAuth(authHeader)) {
			// 若認證成功就顯示受保護的資料
			resp.getWriter().println("Welcome to the protected page !");
			return;
		}
		
		// 如果認證失敗，則要求客戶端提供認證訊息
		resp.setHeader("WWW-Authenticate", WebKeyUtil.generateBasicChallenge(REALM));
		// 發送 401 未經授權的回應
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	
	}
	
}
