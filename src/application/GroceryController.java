package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.models.Item;
import application.models.PurchasedItem;
import application.database.CartDatabase;
import application.models.Cart;
import application.models.CartListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
public class GroceryController {

    @FXML
    private Button addButton;
    
    @FXML
    private Button removeButton;

    @FXML
    private ComboBox<String> itemsCBox;
    
    @FXML
    private Slider unitSlider;

    @FXML
    private Text priceUnitStr;
    
    @FXML
    private Text purchasedUnitStr;

    @FXML
    private Text totalAmountStr;

    @FXML
    private TextArea itemDetails_textArea;

    @FXML
    private Text unitStr;

    @FXML
    private TableView<PurchasedItem> purchasedTableView;
    
    @FXML
    private TableColumn<PurchasedItem, String> itemName_tableColumn;
    
    @FXML
    private TableColumn<PurchasedItem, Double> purchasedUnits_tableColumn;
    
    @FXML
    private TableColumn<PurchasedItem, Double> purchasePrice_tableColumn;
    
    @FXML
    private TableView<Item> itemsTableView;
    
    @FXML
    private TableColumn<Item, String> name_itemTableColumn;
    
    @FXML
    private TableColumn<Item, Double> qty_itemTableColumn;
    
    @FXML
    private TableColumn<Item, Double> price_itemTableColumn;
    
    @FXML
    private TableColumn<Item, String> unit_itemTableColumn;
    
    // Three new buttons -- ws5
    @FXML
    private Button saveCartBtn;
    
    @FXML
    private Button showCartsBtn;
    
    @FXML
    private Button checkOutBtn;
    
    @FXML
    private Button loadCartFromDbBtn;
    
    private ObservableList<Item> itemsObservableList;
    
    // Six ew variables -- ws5
    private List<Cart> cartsData= new ArrayList<>();
    private File file;
    private int cartCount=0;
    private double totalPrice=0.0;
    private Cart loadCart;
    private CartListController cController;
  
 
	public GroceryController() {}
	
	private Item getSelectedItem(ObservableList<Item> itemsObservableList) {
	    String selectedName = itemsCBox.getValue();
	    return itemsObservableList.stream()
	            .filter(item -> item.getName().equals(selectedName))
	            .findFirst()
	            .orElse(null);
	}
	
	public void getItemsFromModel(ObservableList<Item> items) {
		itemsObservableList=items;	
	}
	
	public void loadDataToItemsTableView() {
		// Clear ItemstableView to ensure that it is empty
	    itemsTableView.getItems().clear();
	    
	    itemsTableView.setItems(itemsObservableList);

        // Update the table column information
	    name_itemTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        qty_itemTableColumn.setCellValueFactory(cellData -> cellData.getValue().unitQuantityProperty().asObject());
        price_itemTableColumn.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        unit_itemTableColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
	}
	

	public void loadDataToItemsComboBox() {	
		ObservableList<String> stringList=FXCollections.observableArrayList();
		
		for(Item item:itemsObservableList) {		
			stringList.add(item.getName());
		}
		
 		itemsCBox.setItems(stringList);		
	}
	
	public void bindPurchasedUnitsWithSliderValue() {
		unitSlider.valueProperty().addListener((observable, oldValue, newValue) ->{
			purchasedUnitStr.setText(String.valueOf(newValue.intValue()));
		});		
	}

	public void bindUnitAndPriceWithItem() {
		// Creates an ObjectBinding, automatically binds the Label text based on the properties of the selected item
		ObjectBinding<Item> selectedItemBinding=Bindings.createObjectBinding(()->
			getSelectedItem(itemsObservableList), itemsCBox.valueProperty());
		
		unitStr.textProperty().bind(Bindings.createStringBinding(() -> {
			Item selectedItem=selectedItemBinding.get();
			if (selectedItem != null) {
		        return  Double.toString(selectedItem.getUnitQuantity())+selectedItem.getUnit();
		    } else {
		        return "N/A";
		    }
		},selectedItemBinding));
		
		priceUnitStr.textProperty().bind(Bindings.createStringBinding(() -> {
		    Item selectedItem = selectedItemBinding.get();
		    if (selectedItem != null) {
		        return "$ "+Double.toString(selectedItem.getUnitPrice());
		    } else {
		        return "N/A";
		    }
		}, selectedItemBinding));
	}
	
	public void bindTotalPriceWithPurchasedTableView() {
		DoubleBinding totalBinding=Bindings.createDoubleBinding(()->{
			totalPrice=0.0;
			for(PurchasedItem each: purchasedTableView.getItems()) {
				totalPrice+=each.getTotalPrice();
			}
			return totalPrice;
		}, purchasedTableView.getItems());
		
		totalAmountStr.textProperty().bind(Bindings.format("$ %.2f", totalBinding));
	}

	public void updateComboBoxAndSliderFromTableViewSelection() {
	    // listen to purchasedTableView
	    purchasedTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            // get the corresponding item object from the selected PurchasedItem
	            PurchasedItem selectedCartItem = newValue;

	            // set the value of ComboBox as the selected Item name
	            itemsCBox.setValue(selectedCartItem.getName());

	            // set the value of Slider as the selected Item quantity
	            unitSlider.setValue(newValue.getPurchasedQuantity());
	        } 
	    });
	}

	public void showSelectedPurchasedItemDetails() {
		purchasedTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				String itemDetails=newValue.toString();
	            itemDetails_textArea.setText(itemDetails);
	            
			}else {
				itemDetails_textArea.clear();
			}
		});		
	}
	
	 @FXML
	 void handleAddToCartBtn(ActionEvent event) {
		 Item selectedItem=getSelectedItem(itemsObservableList);
		 
		 if (selectedItem != null) {
		        // get the number of unitSlider selection
		        double selectedQuantity = unitSlider.getValue();
		        
		        // create a new row，and add to purchasedTableView
		        purchasedTableView.getItems().add(new PurchasedItem(selectedItem,selectedQuantity));

		        // Update the table column information
		        itemName_tableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		        purchasedUnits_tableColumn.setCellValueFactory(cellData -> cellData.getValue().purchasedQuantityProperty().asObject());
		        purchasePrice_tableColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
		    }
	 }

	 @FXML
	 void handleRemoveFromCartBtn(ActionEvent event) {
		 int selectedIndex = purchasedTableView.getSelectionModel().getSelectedIndex();

		    if (selectedIndex >= 0) {		
		        purchasedTableView.getItems().remove(selectedIndex);
		    }
	 }
	 
/**********************************************	 
 * Workshop 5 - new functions
**********************************************/
	 private void saveCartDataToFile(ObservableList<PurchasedItem> itemObList,File file) {
		try {
			if(!file.exists()) {
				file.createNewFile();
			}				
			JAXBContext context=JAXBContext.newInstance(CartListWrapper.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
			CartListWrapper wrapper = new CartListWrapper();
				
			// If itemObList is not null, add a cart to file; otherwise, only update the cartsData in file
			if(itemObList!=null) {			
				cartCount++;
				Cart currCart=new Cart(cartCount,itemObList,totalPrice);
				cartsData.add(currCart);
			}else {
				// update the CartId
				int currIndex=1;
				for(Cart each:cartsData) {
					each.setCartId(currIndex);
					currIndex++;
				}
			}
				
			wrapper.setCarts(cartsData);
			marshaller.marshal(wrapper, file);
			
		}catch(IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Could not create or write to a file!");
			alert.showAndWait();
		}catch(JAXBException e) {
			e.printStackTrace();
		       Alert alert = new Alert(AlertType.ERROR);
		       alert.setTitle("Error");
		       alert.setContentText("Error occurred during XML serialization!");
		       alert.showAndWait();
		}
			
	}

	 @FXML
	 void handleSaveCartBtn(ActionEvent event) {
		 // get all purchasedItems
		 ObservableList<PurchasedItem> pcdItemObList=purchasedTableView.getItems();
		 		 
		 if(pcdItemObList.isEmpty()) {
			 Alert alert = new Alert(AlertType.ERROR);
			 alert.setTitle("Error");
			 alert.setContentText("Could not save an empty shopping cart!");
			 alert.showAndWait();				
		  }else {
			 // load all purchasedItems into the file
			 saveCartDataToFile(pcdItemObList,file);
			 
			 // save cart into the database -- ws6
			 CartDatabase.saveCartWithPurchasedItems(totalPrice, pcdItemObList);
			 
			 
			 // clear purchased table
			 purchasedTableView.getItems().clear();
			 
			 // prompting message
			 Alert alert=new Alert(AlertType.INFORMATION);
			 alert.setTitle("Save Successfully");
			 alert.setHeaderText(null);
			 alert.setContentText("The shopping cart saved successfully!");
			 alert.showAndWait();
		  }	 	
	 }
	 
	@FXML
	void handleShowCartsBtn(ActionEvent event) {
		if(hasCart()) {
			loadCartDataFromFile(file);			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CartListView.fxml"));

				Parent root = loader.load();
				Stage newStage = new Stage();
				newStage.setTitle("List of carts");
				newStage.setScene(new Scene(root));
				newStage.show();
				
				cController=loader.getController();
				cController.setGroceryController(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
	        alert.setTitle("Error");
	        alert.setContentText("The shopping carts is empty!");
	        alert.showAndWait();
		}
	 }

	private boolean hasCart() {
		boolean res=false;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(file);

	        Element rootElement = doc.getDocumentElement();
	        if (rootElement != null && rootElement.getElementsByTagName("Cart").getLength() > 0) {
	        	res=true;
	        }
		}catch (Exception e) {
            e.printStackTrace();
        }
		return res;
	}

	private void loadCartDataFromFile(File file) {
		try {
			if(!file.exists()) {
				Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setContentText("The file is no exists!");
		        alert.showAndWait();
			}
			if(file.length() != 0) {
				JAXBContext context=JAXBContext.newInstance(CartListWrapper.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				
				CartListWrapper wrapper = (CartListWrapper) unmarshaller.unmarshal(file);
				cartsData.clear();
				cartsData.addAll(wrapper.getCarts());
			}
			
		}catch(JAXBException e) {
			e.printStackTrace();
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setContentText("Error occurred during XML serialization!");
	        alert.showAndWait();
		}
	}


	public void getFile(File cartsDataFile) {
		file=cartsDataFile;		
		try {
			FileWriter fileWriter = new FileWriter(file, false); // The second parameter is set to false to truncate the file (empty the contents of the file)
            fileWriter.write(""); // The empty string was written to the file, causing the file to be emptied.
            fileWriter.close();
		}catch(IOException e) {
			e.printStackTrace();
		}		
	}

	public List<Cart> getCartsData() {
		return cartsData;
	}

	public void loadDataToPurchasedItemsTableView(Cart selectedCart) {	   
		loadCart=selectedCart;
		purchasedTableView.setItems(FXCollections.observableList(loadCart.getPurchasedItems()));

        // Update the table column information
        itemName_tableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        purchasedUnits_tableColumn.setCellValueFactory(cellData -> cellData.getValue().purchasedQuantityProperty().asObject());
        purchasePrice_tableColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
        
        // update total price text
        bindTotalPriceWithPurchasedTableView();
	}
	
	@FXML
	void handleCheckOutBtn(ActionEvent event) {
		 Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Check out confirmation");
	        alert.setHeaderText(null);
	        alert.setContentText("Are you done with the grocery and don't want to add more items?");
	        
	        alert.getButtonTypes().setAll(ButtonType.YES,ButtonType.NO);
	        
	        alert.showAndWait().ifPresent(res->{
	        	if(res==ButtonType.YES) {
	        		//remove that cart 
	        		cartsData.remove(loadCart); // remove it from cartsData
	        		loadCart=null;        		
	        		saveCartDataToFile(null,file); // save the change to file		        		
	        		
	        		cController.setGroceryController(this);// update the table in CartListController

	        		//clear out its contents in shopping cart table
	        		purchasedTableView.getItems().clear();
	        	}
	        });;

	 }
	
	@FXML
	void handleLoadCartFromDbBtn(ActionEvent event) {
		TextInputDialog cartIdText = new TextInputDialog();
		cartIdText.setTitle("Enter Cart ID");
		cartIdText.setHeaderText(null);
		cartIdText.setContentText("Please enter the Cart ID you want to load:");
	    
		Optional<String> result = cartIdText.showAndWait();
		result.ifPresent(cartId->{
			int id=Integer.parseInt(cartId);
			loadDataToPurchasedItemsTableView(CartDatabase.getCartRecord(id));
		});
		
	}

}
