package security.hash;

import security.KeyUtil;

// 驗證 my_salary.txt 是否有遭受修改
public class SalaryHashValidator {

	public static void main(String[] args) {
		// 先得到已知的 Hash (from SalaryHashGenerator.java)
		String knowHash = "b45ddb72b35c63c275799101ded69627922e7b96019b0010f64ff8cbae5a1c1e";
		
		// 設定 my_salary.txt 的文件路徑
		String filepath = "src/security/hash/my_salary.txt";
			
		// 取得 Hash
		String fileHash = KeyUtil.generateFileHash(filepath);
		
		// 將 knowHash 與 fileHash 進行 Hash 比對
		if(knowHash.equals(fileHash)) {
			System.out.println("my_salary.txt 是完整的沒有遭受到其他人修改。");
		} else {
			System.out.println("my_salary.txt 可能已遭受修改或損壞。");
		}
	}

}
