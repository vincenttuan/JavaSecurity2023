package security.keys;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import security.KeyUtil;

public class RSASample {

	public static void main(String[] args) throws Exception {
		// 1. 生成 RSA 密鑰對(公鑰/私鑰)
		System.out.println("1. 生成 RSA 密鑰對(公鑰/私鑰)");
		KeyPair keyPair = KeyUtil.generateRSAKeyPair(); // RSA-2048
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("PublicKey 公鑰:" + publicKey);
		System.out.println("PrivateKey 私鑰:" + privateKey);
		
		

	}

}
