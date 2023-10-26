package servlet.secure.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.jwt.JWTClaimsSet;

import servlet.secure.oidc.OIDCUtil;

// 報告服務, 需驗證 idToken 無誤之後才能觀看的報告

@WebServlet("/secure/service/report")
public class ReportServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 取得 idToken
		String idToken = OIDCUtil.extractIdTokenFromHeader(req, resp);
		
		try {
			// 驗證 idToken 並取得使用者資料
			JWTClaimsSet claimsSet = OIDCUtil.getClaimsSetAndVerifyIdToken(idToken);
			// 根據 emial 來得到使用者名字
			String email = claimsSet.getStringClaim("email");
			String userName = email.substring(0, email.indexOf('@'));
			resp.getWriter().println("Hello " + userName);
			resp.getWriter().println("Your report: ");
			resp.getWriter().println("Secure report !!");
		} catch (Exception e) {
			resp.getWriter().println("Exception: " + e);
		}
	}
	
}
