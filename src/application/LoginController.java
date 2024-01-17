package application;

import java.io.File;
import java.io.IOException;

import application.database.CartDatabase;
import application.database.CustomerDatabase;
import application.models.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController {
	private Model model;
	private GroceryController controller;
	// declare file variable -- ws5
	private File cartsDataFile=new File("shoppingCarts.csv");

    @FXML
    private Button loginBtn;
    
    @FXML
    private Button registerBtn;

    @FXML
    private PasswordField password_text;

    @FXML
    private TextField email_text;

    @FXML
    void handleLoginBtn(ActionEvent event) {
    	Window owner = loginBtn.getScene().getWindow();
        if (email_text.getText().isEmpty() || password_text.getText().isEmpty()) {
        	Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Login Error");
	        alert.setContentText("Please enter email and password to login!");
	        alert.setHeaderText(null);
	        alert.initOwner(owner);
	        alert.showAndWait();
            return; 
        }

        String email = email_text.getText();
        String pw = password_text.getText();
        
        CustomerDatabase.creatTable();
        
        if(CustomerDatabase.isEmailExists(email)) {
        	boolean flag = CustomerDatabase.validation(email, pw);
            if (flag) {
                try {
                	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/GroceryView.fxml"));
        			Parent root = loader.load();
        			Stage newStage=new Stage();
        			newStage.setTitle("Shopping Cart");
        			newStage.setScene(new Scene(root,1100,800));   			
        			newStage.show();
        			
        			// Initialize Controller and Model when starting the application			
        			controller=loader.getController();
        			model=new Model();
        			
        			// Create table of the database -- ws6
        			CartDatabase.creatTables();
        			
        			setupActions();                
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // error -- invalid password 
            	Alert alert = new Alert(AlertType.ERROR);
    	        alert.setTitle("Login Error");
    	        alert.setContentText("Your login password is invalid!");
    	        alert.setHeaderText(null);
    	        alert.initOwner(owner);
    	        alert.showAndWait();
            }
    	}else {
    		// error - email doesn't exists
    		Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Login Error");
	        alert.setContentText("Your login email is invalid!");
	        alert.setHeaderText(null);
	        alert.showAndWait();
    	}
        
    }

    @FXML
    void handleRegisterBtn(ActionEvent event) throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/Register.fxml"));
		Parent registerLoad = loader.load();
		
		Stage registerStage = new Stage();
		registerStage.setTitle("Registration");
		Scene sc = new Scene(registerLoad, 800, 500);
		registerStage.setScene(sc);
		registerStage.show();
		
		RegisterController registerController = loader.getController();
        registerController.setRegisterStage(registerStage);

    }
    
    private void setupActions() {
		controller.getItemsFromModel(model.getItemsObservableList());
		
		controller.loadDataToItemsTableView();
		
		controller.loadDataToItemsComboBox();
		
		controller.bindPurchasedUnitsWithSliderValue();
		
		controller.bindUnitAndPriceWithItem();
		
		controller.bindTotalPriceWithPurchasedTableView();
		
		controller.updateComboBoxAndSliderFromTableViewSelection();
		
		controller.showSelectedPurchasedItemDetails();
		
		// Load the file when starting the program -- ws5
		controller.getFile(cartsDataFile);
	}

}
