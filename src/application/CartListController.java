package application;
	

import java.util.List;

import application.models.Cart;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CartListController {

    @FXML
    private TableView<Cart> CartsTableView;
    
    @FXML
    private TableColumn<Cart, Integer> id_CartsTableColumn;
    
    @FXML
    private TableColumn<Cart, Double> price_CartsTableColumn;
    
    @FXML
    private Button loadCartBtn;
    
    private GroceryController gController;
 
    
	public void loadDataToCartsTableView(List<Cart> carts) {
		// Clear ItemstableView to ensure that it is empty
		CartsTableView.getItems().clear();
	    
		CartsTableView.setItems(FXCollections.observableList(carts));
				
        // Update the table column information
		id_CartsTableColumn.setCellValueFactory(cellData -> cellData.getValue().cartIdProperty().asObject());
		price_CartsTableColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());      
	}
    

    @FXML
    void handleLoadCartBtn(ActionEvent event) {
    	
    	Cart loadCart=CartsTableView.getSelectionModel().getSelectedItem();
    	if(loadCart!=null) {
    		gController.loadDataToPurchasedItemsTableView(loadCart);
    		
    		Stage currStage=(Stage) loadCartBtn.getScene().getWindow();
    		currStage.close();
    	}

    }


	public void setGroceryController(GroceryController groceryController) {
		this.gController=groceryController;
		loadDataToCartsTableView(gController.getCartsData());
	}
    
    

}

