package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDatabase {
	private static final String DB_NAME="customer.db";
	private static final String DB_CONNECTION = "C:\\Users\\User\\Documents\\APD545\\ws6\\GroceryShoppingCartApp\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	
	private static final String CUSTOMER_TABLE = "Customers";

	private static final String CREATE_CUSTOMER_TBL_QRY="create table if not exists "+CUSTOMER_TABLE+
			" (customerId integer not null primary key autoincrement, email string,password string)";

	private static final String INSERT_CUSTOMER_QRY="Insert into "+ CUSTOMER_TABLE+" (email,password) values (?,?)";

	private static final String SELECT_CUSTOMER_QRY="select * from " +CUSTOMER_TABLE+" where email = ? and password = ?";
    
	private static final String SELECT_EMAIL_QRY="select email from " + CUSTOMER_TABLE + " where email = ?";

	public static void creatTable() {
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(CREATE_CUSTOMER_TBL_QRY)){
			ps.execute();
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	// If email exists, don't insert the record and return false;
	// otherwise, insert record and return true
	public static boolean insertCustomerRecord(String email,String pw) {
		if(isEmailExists(email)) {
			return false;
		}
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(INSERT_CUSTOMER_QRY)){
			ps.setString(1, email);
			ps.setString(2, pw);
			ps.executeUpdate();
			return true;
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean isEmailExists(String email) {
	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
	            PreparedStatement ps = conn.prepareStatement(SELECT_EMAIL_QRY)) {
	           ps.setString(1, email);
	           ResultSet rs = ps.executeQuery();
	           return rs.next(); // if exists, return true;otherwise, return false
	       } catch (SQLException ex) {
	           ex.printStackTrace();
	       }
	       return false;
	   }
	
	public static boolean validation(String email,String pw) {
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(SELECT_CUSTOMER_QRY)){
			ps.setString(1, email);
			ps.setString(2, pw);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return true;
			}
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return false;

	}



}
