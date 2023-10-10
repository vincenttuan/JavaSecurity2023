package security.mac;

import javax.crypto.SecretKey;

import security.KeyUtil;

// MAC 訊息驗證碼簡單範例
public class MACExample {

	public static void main(String[] args) throws Exception {
		// 1. 定義我們要加上MAC的訊息
		String message = "這是我的訊息";
		
		// 2. 產生一把專用於 HMAC 的密鑰
		SecretKey macKey = KeyUtil.generateKeyForHmac();
		
		// 3. 利用此密鑰(macKey)和訊息(message)生成 MAC 值
		byte[] macValue = KeyUtil.generateMac("HmacSHA256", macKey, message.getBytes());
		
		// 4. 將 MAC 值印出
		System.out.println("產生的 MAC: " + KeyUtil.bytesToHex(macValue));

	}

}
