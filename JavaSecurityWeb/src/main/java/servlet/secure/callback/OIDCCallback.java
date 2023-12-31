package servlet.secure.callback;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.jwt.JWTClaimsSet;

import servlet.secure.oidc.OIDCUtil;

/**
 * 此 Servlet 處理從 Google OAuth 2.0 服務重定向回的 OAuth 2.0 回調。
 * 它將解析授權碼，獲取ID令牌，然後驗證令牌的有效性。
 * 如果令牌有效，它將從ID令牌中提取用戶信息，並將令牌發送到報告服務。
 */
@WebServlet("/secure/callback/oidc")
public class OIDCCallback extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		resp.getWriter().print("<a href='/JavaSecurityWeb/form/login.jsp'>重新登入</a>");
		
		resp.getWriter().println("<pre>");
		// 取得授權碼
		String code = req.getParameter("code");
		resp.getWriter().println("code: " + code);
		try {
			// 得到 idToken
			String idToken = OIDCUtil.getIDToken(code);
			resp.getWriter().println("idToken: " + idToken); // JWT 的格式
			
			// 取得 email 資料
			JWTClaimsSet claimsSet = OIDCUtil.getClaimsSetAndVerifyIdToken(idToken);
			String email = claimsSet.getStringClaim("email");
			resp.getWriter().println("email: " + email);
			Boolean emailVerified = claimsSet.getBooleanClaim("email_verified");
			resp.getWriter().println("emailVerified: " + emailVerified);
			
			// 發送 idToken 來獲取所需要的服務資訊
			// 查看機密的報告
			// 報告位置：https://localhost:8443/JavaSecurityWeb/secure/service/report
			String reportURL = "https://localhost:8443/JavaSecurityWeb/secure/service/report";
			Map<String, String> respReport = OIDCUtil.sendAuthenticatedRequestToService(idToken, reportURL);
			resp.getWriter().println(respReport.get("data"));
			
		} catch(Exception e) {
			resp.getWriter().println("Exception: " + e);
		}
		
		resp.getWriter().println("</pre>");
	}
	
}
