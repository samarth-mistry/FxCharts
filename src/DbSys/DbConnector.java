package DbSys;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnector {
	private static Connection conn=null;
	public static Connection getConnection() {
		try {			
			String url= String.format("jdbc:mysql://localhost:3306/fxcharts");
			Class.forName("com.mysql.jdbc.Driver");			
			conn= DriverManager.getConnection(url,"root","");
			System.out.println("DB\n\tConnection Established");				       
		}catch(Exception e) {
			System.out.println("DB\n\tConnection Abort");
			e.printStackTrace();			
		}
		return conn;
	}
	public static void main(String[] args) {
		getConnection();	
	}
}
