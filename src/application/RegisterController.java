package application;

import application.database.CustomerDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class RegisterController {

    @FXML
    private PasswordField password_text;

    @FXML
    private TextField email_text;
    
    @FXML
    private Button registerBtn;
    
    private Stage registerStage;

    @FXML
    void handleRegisterBtn(ActionEvent event) {
    	String email = email_text.getText();
        String pw = password_text.getText();
        
        if (email.isEmpty() || pw.isEmpty()) {
        	Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Registration Error");
	        alert.setHeaderText(null);
	        alert.setContentText("Please enter email and password to register!");
	        alert.showAndWait();
	        
	        return;
        }
        
        if(CustomerDatabase.insertCustomerRecord(email, pw)) {
        	
        	Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Registration Success");
	        alert.setHeaderText(null);
	        alert.setContentText("Register successfully!");
	        
	        // close register stage after closing the alert dialog
	        alert.setOnCloseRequest(e->{
	        	registerStage.close();
	        });
	        alert.showAndWait();
        }else {
        	//error-- email exists
        	Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Registration Error");
	        alert.setHeaderText(null);
	        alert.setContentText("The entered email is exists!");
	        alert.showAndWait();
        }
    }
    
    public void setRegisterStage(Stage stage) {
        this.registerStage = stage;
    }


}

