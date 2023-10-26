package servlet.secure.callback;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.secure.oauth2.OAuth2Util;

@WebServlet("/secure/callback/oauth2")
public class Oauth2Callback extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		resp.getWriter().println("code: " + code);
		
		// 已有授權碼(code)之後，可以跟 Github 來得到 token (訪問令牌)
		// 有了 token 就可以得到客戶的公開資訊例如: userInfo
		// 1. 根據 code 的得到 token
		String token = OAuth2Util.getGitHubAccessToken(code);
		resp.getWriter().println("token: " + token);
		// 2. 透過 token 裡面的 access_token 來取得用戶資訊
		String accessToken = OAuth2Util.parseAccessToken(token);
		resp.getWriter().println("accessToken: " + accessToken);
		
		if(accessToken != null) {
			// 取得該用戶在 Github 上的公開資料
			String userInfo = OAuth2Util.getUserInfoFromGitHub(accessToken);
			resp.getWriter().println("userInfo: " + userInfo);
		} else {
			resp.getWriter().println("accessToken is null");
		}
	}
	
}
