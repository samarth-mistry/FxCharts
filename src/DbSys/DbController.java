package DbSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbController {
	private static Connection conn=null;
	public static boolean insertSeries(String title, int chart_id, String data) throws SQLException {		
		try {
			String sql="INSERT INTO series(title,chart_id,data) VALUES('"+title+"','"+chart_id+"','"+data+"')";			
			conn = DbConnector.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
	     	if(preparedStatement.executeUpdate() > -1) {
	     		conn.close();
	     		System.out.println("\tInsert success");
	     		return true;
	     	}else {
	     		conn.close();
	     		System.out.println("\tInsert false");
	     		return false;
	     	}	        
		}catch(SQLException se) {			
			se.printStackTrace();
		}
		return false;
	}
	public static String getSeries() throws SQLException {
		conn = DbConnector.getConnection();
		String sql = "SELECT data FROM series WHERE chart_id = 1";
		PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        String data = "";boolean first=true;
        
        while(rs.next()) {        	
        	if(first) {
        		data+=rs.getString("data"); 
        		first=false;
        	}
        	else
        		data+="\n"+rs.getString("data");
        }        
        conn.close();
        System.out.println("\t"+data);
        return data;
	}
	public static void clearSeries() {
		String sql="TRUNCATE TABLE series";			
		conn = DbConnector.getConnection();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			if(preparedStatement.executeUpdate()>-1) {
				System.out.println("DB\n\tSeries Table cleared");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
