module GroceryShoppingCartApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires jakarta.xml.bind;
	requires javafx.graphics;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.models to jakarta.xml.bind;
}
