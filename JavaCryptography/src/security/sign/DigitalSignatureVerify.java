package security.sign;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PublicKey;

import security.KeyUtil;

/*
 * 情境說明：
 * 延續「DigitalSignatureCreator」的情境，小王的合作夥伴，小李，收到了小王用數位簽章簽署的「my_contract.txt」文件。
 * 小李想確認這份文件的完整性，以及確定這份合同確實是由小王簽署的。因此，他決定使用數位簽章驗證工具來進行檢查。
 *
 * 本程式「DigitalSignatureVerifier」的目的是驗證「my_contract.txt」文件的數位簽章。
 * 小李透過使用小王的公鑰（publicKey.key）和數位簽章（signature.sig），這個程式可以確認文件的完整性和簽署者的身份。
 * 如果驗證成功，這意味著文件在傳輸過程中沒有被修改，且確實是由小王簽署的。
 */
public class DigitalSignatureVerify {

	public static void main(String[] args) throws Exception {
		// 小李：
		// 合約檔位置
		String contractPath = "src/security/sign/my_contract.txt";
		// 小王的公鑰檔位置
		String publicKeyPath = "src/security/sign/publicKey.key";
		// 小王的數位簽章檔位置
		String signaturePath = "src/security/sign/signature.sig";
		
		PublicKey publicKey = KeyUtil.getPublicKeyFromFile("RSA", publicKeyPath);
		byte[] savedSignature = KeyUtil.getSignatureFromFile(signaturePath);
		
		// 驗證合約是否被小王的數位簽章簽證過：小王的publicKey + 合約 + 小王的數位簽章
		boolean isValid = KeyUtil.verifySignatureFromFile(publicKey, contractPath, savedSignature);
		
		if(isValid) {
			System.out.println("數位簽章驗證成功，文件未被更改且確認由小王簽署！");
			// 讀取合約檔案內容
			String data = Files.readString(Paths.get(contractPath));
			System.out.println(data);
		} else {
			System.out.println("數位簽章驗證失敗");
		}
	}

}
