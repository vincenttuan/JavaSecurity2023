package servlet.secure.oauth2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/secure/oauth2/github_login")
public class GithubLogin extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 建立 Github OAuth 2.0 授權 URL 並重定向用戶
		String authURL = OAuth2Util.AUTH_URL;
		resp.sendRedirect(authURL);
		
	}
	
}
