package help.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Main  {
	static int count = 0;
	public static void main(String[] args) throws Exception  {
		File exel = new File("C:\\Users\\jjanku\\Desktop");
		List<String> queries = new ArrayList<>();
		
		List<Certification> certs = ExcelFile.getCertificationList();
		
		System.out.println("SQL LOGS");
		certs.forEach(cert -> {
			try {
				queries.add(update(cert));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
		FileWriter fw = new FileWriter("C:\\Users\\jjanku\\Desktop\\data.sql");
		queries.forEach(query -> {
			try {
				fw.write(query + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		});
		fw.close();
		System.out.println("\nTotal queries executed "+queries.size());
		
	}
	
	public static String decrypt(byte[] aes) throws Exception {
		String key = "Bar12345Bar12345";
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		return new String(cipher.doFinal(aes));
	}
	
	public static String update(Certification certification) throws IOException, Exception {
		Connection con = Database.getInstance();
		Double weight= certification.getHours() != null ? (certification.getHours()/300.0)*100 : null;
		String updateQuery = " UPDATE PUBLIC.CERTIFICATION  "
				+ " SET hours = ? , description = ? , weight = ?  WHERE NAME = ?";
		PreparedStatement update = con.prepareStatement(updateQuery);
		update.setInt(1, certification.getHours() != null ? certification.getHours() : -1);
		update.setString(2, certification.getDescription());
		update.setInt(3,weight != null ? weight.intValue() : -1);
		update.setString(4, certification.getName());
		
		System.out.print("\n" + update.toString());
		if (!update.toString().isEmpty()) {
			count++;
		}
		
		return update.toString();

	}
}