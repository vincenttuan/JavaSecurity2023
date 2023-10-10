package security.mac;

import java.util.Arrays;

import javax.crypto.SecretKey;

import security.KeyUtil;

// MAC 訊息驗證碼簡單範例
public class MACExample {

	public static void main(String[] args) throws Exception {
		// 1. 定義我們要加上MAC的訊息
		String message = "以色列被攻擊...";
		
		// 2. 產生一把專用於 HMAC 的密鑰
		SecretKey macKey = KeyUtil.generateKeyForHmac();
		
		// 3. 利用此密鑰(macKey)和訊息(message)生成 MAC 值
		byte[] macValue = KeyUtil.generateMac("HmacSHA256", macKey, message.getBytes());
		
		// 4. 將 MAC 值印出
		System.out.println("產生的 MAC: " + KeyUtil.bytesToHex(macValue));
		
		// 5. 在實際應用中，接收方會收到訊息(message)與 macValue
		//    此時，message 要與 macKey(雙方統一都有的密鑰) 所產生的 computedMacValue 值進行與 macValue 的比對
		byte[] computedMacValue = KeyUtil.generateMac("HmacSHA256", macKey, message.getBytes());
		
		// 6. 比較 macValue 與 computedMac 是否相同
		if(Arrays.equals(macValue, computedMacValue)) {
			System.out.println("MAC 驗證成功，來源正確");
		} else {
			System.out.println("MAC 驗證失敗");
		}
	}

}
