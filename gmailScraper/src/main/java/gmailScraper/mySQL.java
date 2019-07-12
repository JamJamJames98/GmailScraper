package gmailScraper;
import java.sql.*;  

public class mySQL {
	/*
	 * Deletes all records of a given email from the database
	 */
	public static void deleteFromDatabase(String email) {
		try {  
			Class.forName("com.mysql.cj.jdbc.Driver");
			String database_name = "ScrapedEmails";
			String root_username = "root";
			String root_password = "password";
			String server_timezone = "Australia/Sydney";
			Connection con=DriverManager.getConnection(  "jdbc:mysql://localhost:3306/"+database_name+"?serverTimezone="+server_timezone,root_username,root_password);  
		    
			String query = "delete from emailDump where email = '" + email + "';";
			PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.execute();
			con.close();  
		} catch(Exception e) { 
			System.out.println(e);
		}
	}
	
	/*
	 * Writes a record to the database
	 */
	public static void writeToDatabase(String sender, String subject, String email) {
		try {  
			Class.forName("com.mysql.cj.jdbc.Driver");
			String database_name = "ScrapedEmails";
			String root_username = "root";
			String root_password = "password";
			String server_timezone = "Australia/Sydney";
			Connection con=DriverManager.getConnection(  "jdbc:mysql://localhost:3306/"+database_name+"?serverTimezone="+server_timezone,root_username,root_password);  
			
			String query = "insert into emailDump (sender,subject,email) values ('"+sender+"','"+subject+"','"+email+"');";
			PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.execute();
			con.close();
			System.out.println("[OK] Sender: " + sender + " Subject: " + subject + " User: " + email + " [OK]");
		} catch(Exception e) { 
			System.out.println(e);
			System.out.println("[ERROR] Sender: " + sender + " Subject: " + subject + " User: " + email + " [ERROR]");		}  
	}
	
	/*
	 * Prints all records of a given email from the database
	 */
	public static String getDatabaseValues(String email) {
		String return_result = "";
		try {  
			Class.forName("com.mysql.cj.jdbc.Driver");
			String database_name = "ScrapedEmails";
			String root_username = "root";
			String root_password = "password";
			String server_timezone = "Australia/Sydney";
			Connection con=DriverManager.getConnection(  "jdbc:mysql://localhost:3306/"+database_name+"?serverTimezone="+server_timezone,root_username,root_password);  
		    
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from emailDump where email = '" + email + "';");
			while(rs.next()) {
				return_result = return_result + "ID: " + rs.getInt(1) + " Sender: " + rs.getString(2) + " Subject: " + rs.getString(3) + System.lineSeparator();
			}
			con.close();  
		} catch(Exception e) { 
			System.out.println(e);
		}
		return return_result;
	}
}
