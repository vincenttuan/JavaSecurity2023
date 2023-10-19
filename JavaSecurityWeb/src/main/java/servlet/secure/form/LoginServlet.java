package servlet.secure.form;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.WebKeyUtil;

@WebServlet(value = "/secure/servlet/form/login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain;charset=UTF-8"); // "text/html;charset=UTF-8"
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		PrintWriter out = resp.getWriter();
		out.println("username：" + username);
		out.println("password：" + password);
		
		// 比較 username 與 password
		// 取得金鑰
		String secretKey = WebKeyUtil.getSecretKeyStringFromContext();
		try {
			// 將使用者所輸入的密碼加密
			String encryptedPassword = WebKeyUtil.encrypt(password, secretKey);
			out.println("encryptedPassword：" + encryptedPassword);
			// 密碼加密後進行比對
			if(WebKeyUtil.checkFormLogin(username, encryptedPassword)) {
				out.println("Login OK !");
			} else {
				out.println("Login Error !");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
