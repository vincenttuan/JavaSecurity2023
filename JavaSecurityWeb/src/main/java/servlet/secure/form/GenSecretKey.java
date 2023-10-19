package servlet.secure.form;

import servlet.WebKeyUtil;

/*
產生一個密鑰
在 context.xml 加入
<Context>
  ... 略
  <!-- Secret Key -->
  <Environment name="secretKey" 
  				value="你的金鑰" 
  				type="java.lang.String" 
  				override="true" />
</Context>

當 override 屬性設為 true 且 web.xml 中具有相應的 <env-entry> 條目時，
Tomcat 會使用 web.xml 中指定的值覆蓋 context.xml 中的值。
如果 web.xml 中沒有對應的 <env-entry> 條目，則 Tomcat 會使用 context.xml 中的原始值。

當 override 屬性設為 false 表示不會被 web.xml 的 <env-entry> 所取代
 
* */
public class GenSecretKey {

	public static void main(String[] args) {
		
		String secretKey = WebKeyUtil.generateSecret(32); // 密鑰
		System.out.println("金鑰: " + secretKey);

	}

}
