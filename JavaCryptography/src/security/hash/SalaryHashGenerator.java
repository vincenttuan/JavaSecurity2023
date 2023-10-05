package security.hash;

import security.KeyUtil;

// 針對 my_salary.txt 產生 Hash
public class SalaryHashGenerator {

	public static void main(String[] args) {
		// 設定 my_salary.txt 的文件路徑
		String filepath = "src/security/hash/my_salary.txt";
		
		// 取得 Hash
		String fileHash = KeyUtil.generateFileHash(filepath);
		
		if(fileHash != null) {
			System.out.println("my_salary.txt 的 SHA-256 Hash: " + fileHash);
		} else {
			System.out.println("Error");
		}

	}

}
