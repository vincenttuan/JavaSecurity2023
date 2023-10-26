package servlet.secure.callback;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import servlet.secure.oauth2.OAuth2Util;

@WebServlet("/secure/callback/oauth2")
public class Oauth2Callback extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain;charset=UTF-8");
		
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
			// 取得使用者名稱
			JSONObject userInfoObject = new JSONObject(userInfo);
			resp.getWriter().println("login: " + userInfoObject.getString("login"));
			resp.getWriter().println("id: " + userInfoObject.getInt("id"));
			resp.getWriter().println("email: " + userInfoObject.getString("email"));
			resp.getWriter().println("name: " + userInfoObject.getString("name"));
			resp.getWriter().println("bio: " + userInfoObject.getString("bio"));
			
			// 1. 將登入資料放到 session 中 ...
			
			// 2. 重導到登入成功頁面（或指定網頁）
			
			
		} else {
			resp.getWriter().println("accessToken is null");
		}
	}
	
}
