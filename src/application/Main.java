/**********************************************
Workshop 6
Course:APD545 - Semester Fall
Last Name: Tang
First Name: Qian
ID: 124746207
Section: NAA
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature
Date: 2023-11-25
**********************************************/

package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/Login.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root,800,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Grocery Shopping Login");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
