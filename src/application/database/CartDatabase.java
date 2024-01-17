package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.models.Cart;
import application.models.PurchasedItem;

public class CartDatabase {
	private static final String DB_NAME="cart.db";
	private static final String DB_CONNECTION = "C:\\Users\\User\\Documents\\APD545\\ws6\\GroceryShoppingCartApp\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	
	private static final String CART_TABLE = "Carts";
	private static final String PURCHASED_ITEM_TABLE = "PurchasedItems";
	
	private static final String CREATE_CART_TBL_QRY = "create table if not exists "+CART_TABLE+
			" (cartId integer not null primary key autoincrement, totalPrice double)";	
	
	private static final String CREATE_PURCHASED_ITEM_TBL_QRY ="create table if not exists "+PURCHASED_ITEM_TABLE+
			"(purchasedItemId integer not null primary key autoincrement,cartId integer,"
			+ "name varchar(20) not null, unit varchar(20) not null, unitPrice double not null, "
			+ "unitQty double not null, purchasedQty double not null, totalPrice double not null,"
			+ "foreign key (cartId) references "+ CART_TABLE+"(cartId))";
	
	private static final String INSERT_CART_QRY="Insert into "+ CART_TABLE+" (totalPrice) values (?)";
	
	private static final String INSERT_PURCHASED_ITEM_QRY = "Insert into "+PURCHASED_ITEM_TABLE+		
			"(cartId,name,unit,unitPrice,unitQty,purchasedQty,totalPrice)"
			+"values (?,?,?,?,?,?,?)";
	
	private static final String SELECT_CART_QRY="SELECT * FROM " +CART_TABLE+" where cartId = ?";
	private static final String SELECT_PURCHASED_ITEM_QRY = "SELECT * FROM " + PURCHASED_ITEM_TABLE+" where cartId = ?";
	
	public static void creatTables() {
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement psCart=conn.prepareStatement(CREATE_CART_TBL_QRY);
				PreparedStatement psPurchasedItem = conn.prepareStatement(CREATE_PURCHASED_ITEM_TBL_QRY)){
			psCart.execute();
			psPurchasedItem.execute();
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public static void saveCartWithPurchasedItems(double cartTotalPrice, List<PurchasedItem> purchasedItems) {
		// Save the cart and get the inserted cart ID
		int cartId=insertCartRecord(cartTotalPrice);
		if(cartId!=-1) {
			for(PurchasedItem each : purchasedItems) {
				insertPurchasedItemRecord(cartId,each.getName(),each.getUnit(),each.getUnitPrice(),
						each.getUnitQuantity(),each.getPurchasedQuantity(),each.getTotalPrice());
			}
		}
	}
	
	private static int insertCartRecord(double totalPrice) {
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement ps = conn.prepareStatement(INSERT_CART_QRY)) {
            ps.setDouble(1, totalPrice);
            ps.executeUpdate();
            
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); 
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Insert failure to return -1
        return -1;
    }
	
	private static void insertPurchasedItemRecord(int cartId,String name, String unit, double unitPrice, 
						double unitQty,double purchasedQty, double totalPrice) {
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(INSERT_PURCHASED_ITEM_QRY)){
			ps.setInt(1, cartId);
            ps.setString(2, name);
            ps.setString(3, unit);
            ps.setDouble(4, unitPrice);
            ps.setDouble(5, unitQty);
            ps.setDouble(6, purchasedQty);
            ps.setDouble(7, totalPrice);
			ps.executeUpdate();
			
		}catch(SQLException ex){
			 ex.printStackTrace();
		}
	}
	
	public static Cart getCartRecord(int cartId) {
		Cart cart=new Cart();
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(SELECT_CART_QRY)){
			ps.setInt(1, cartId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				cart.setCartId(rs.getInt("cartId"));
				cart.setTotalPrice(rs.getDouble("totalPrice"));
				cart.setPurchasedItems(getPurchasedItemsRecords(cartId));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		return cart;
	}
	
	private static List<PurchasedItem> getPurchasedItemsRecords(int cartId) {
		List<PurchasedItem> purchasedItems = new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps=conn.prepareStatement(SELECT_PURCHASED_ITEM_QRY)){
			ps.setInt(1, cartId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				PurchasedItem res = new PurchasedItem();
				res.setName(rs.getString("name"));
				res.setPurchasedQuantity(rs.getDouble("purchasedQty"));
				res.setUnit(rs.getString("unit"));
				res.setUnitPrice(rs.getDouble("unitPrice"));
				res.setUnitQuantity(rs.getDouble("unitQty"));
				purchasedItems.add(res);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return purchasedItems;
	}

	
}
