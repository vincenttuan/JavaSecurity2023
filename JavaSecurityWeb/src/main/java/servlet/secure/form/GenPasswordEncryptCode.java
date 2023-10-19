package servlet.secure.form;

import servlet.WebKeyUtil;

// 生成加密的密碼
public class GenPasswordEncryptCode {

	public static void main(String[] args) throws Exception {
		// 從 context.xml 中取得 Secret Key
		String contextXmlPath = "/Users/vincenttuan/eclipse-workspace-javasecurity2023/Servers/Tomcat v9.0 Server at localhost-config/context.xml";
		
		// 取得金鑰
		String secretKey = WebKeyUtil.getSecretKeyFromContextXml(contextXmlPath);
		
		if(secretKey == null || secretKey.trim().length() < 10) {
			System.out.println("context.xml 中找不到金鑰");
		} else {
			// 針對 1234 密碼進行加密 
			String originalPassword = "1234";
			// 加密
			String encryptedPassword = WebKeyUtil.encrypt(originalPassword, secretKey);
			
			System.out.println("金鑰: " + secretKey);
			System.out.println("原始密碼(未加密): " + originalPassword);
			System.out.println("加密後密碼: " + encryptedPassword);
			
		}

	}

}
