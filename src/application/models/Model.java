package application.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private ObservableList<Item> itemsObservableList;
	
	public Model() {
        itemsObservableList = FXCollections.observableArrayList();
        loadData(); 
    }
	
	private void loadData(){
		try (BufferedReader reader = new BufferedReader(new FileReader("ItemsMaster.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    String name = values[0];
                    String unit = values[1];
                    Double unitQuantity = Double.parseDouble(values[2]);
                    Double unitPrice = Double.parseDouble(values[3]);

                    Item item = new Item(name, unit, unitQuantity, unitPrice);
                    itemsObservableList.add(item);
                }
            }
		}catch (IOException e) {
            e.printStackTrace();
        }		 
	}

	public ObservableList<Item> getItemsObservableList() {
		return itemsObservableList;
	}
	
	
}