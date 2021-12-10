package help.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import static help.helper.Config.DATABASE_URL;
import static help.helper.Config.DATABASE_USER;
import static help.helper.Config.DATABASE_PASSWORD_FILE;

public class Database {
	private static Connection con = null;
	private static File file = new File(DATABASE_PASSWORD_FILE);
	public static Connection getInstance() throws IOException, Exception {
		if (con == null) {
		Properties props = new Properties();
		props.put("user",DATABASE_USER);
		props.put("password", Main.decrypt(Files.readAllBytes(file.toPath())));
		con = DriverManager.getConnection(DATABASE_URL,props);
		}
		return con;
	}
	
//	Connection conection = null;
//	
//	try {
//		   String databaseUrl = "jdbc:postgresql://localhost:5432/pre-prod";
//		   Properties connectionProperties = new Properties();
//		   connectionProperties.put("user", "postgres");
//		   connectionProperties.put("password", "Joa@2000");
//		   conection = DriverManager.getConnection(databaseUrl,connectionProperties);
//		   String querySelect = "select * from public.user where public.user.id = ?";
//		   PreparedStatement statement = null;
//		try {
//			statement = conection.prepareStatement(querySelect);
//			int id = 4;
//			statement.setInt(1, id);
//		
//			ResultSet rs = statement.executeQuery();
//			
//			while(rs.next()) {
//				System.out.println(rs.getString("username"));
//			}
//		} catch (SQLException e ) {
//	          throw new Error("Problem", e);
//	      } finally {
//	          if (statement != null) { statement.close(); }
//	      }	
//	}  catch (SQLException e) {
//        throw new Error("Problem", e);
//    } finally {
//      try {
//        if (conection != null) {
//        	conection.close();
//        }
//      } catch (SQLException ex) {
//          System.out.println(ex.getMessage());
//      }
//}
	
}
