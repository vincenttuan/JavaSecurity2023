package security.random;

import java.security.SecureRandom;

/**
 * OTP (One-Time Password) 生成器範例
 *
 * 情境應用：
 * 假設一家銀行想要提供給其客戶一個安全的網上交易平台。
 * 為了確保交易的安全性，銀行決定在客戶進行交易時，發送一個只能使用一次的密碼 (OTP) 到客戶的手機。
 * 客戶在完成交易時需要輸入此OTP。這增加了交易的安全性，因為即使攻擊者得到了客戶的登錄資訊，也無法完成交易，除非他們同時具有該OTP。
 *
 * 這個範例展示了如何使用Java的SecureRandom類生成這樣的OTP。
 */
public class OTP {

	public static void main(String[] args) {
		System.out.println("您的 OTP 一次性密碼為：" + generateOTP());
	}
	
	/**
     * 生成一個六位數的OTP
     * 
     * @return 六位數的OTP
     */
	public static String generateOTP() {
		// 使用 SecureRandom 確保生成的數字是密碼學安全的
		SecureRandom secureRandom = new SecureRandom();
		
		// 生成一個 0~999999 之間的整數
		int number = secureRandom.nextInt(1000000);
		
		// "%06d" 表示不足六位時前面會補 0
		return String.format("%06d", number);
		
	}

}
