package DbSys;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class insert_xampp_mysql {

	public static void main(String[] args) {
		try {
			String sql="Select * from users";
			Connection conn=null;
			conn= DbConnector.getConnection();
			Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
			
			int cont=1;
	        while(rs.next()) {	           
	           System.out.print(cont+"  ");
	           System.out.print(rs.getString("username")+"  ");			//[N O T E : the string inside the green quotes must be same as the column name in database ]
	           System.out.println(rs.getString("email"));	
	           cont++;
	        }
	        conn.close();
	        System.out.print("\tConnection Terminated");
		}catch(SQLException se) {
			System.out.print("Flag02");
			se.printStackTrace();
		}
	}//Main end
}//Main class end
//featching data from DB