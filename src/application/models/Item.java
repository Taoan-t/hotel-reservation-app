package application.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	private final StringProperty name;
	private final StringProperty unit;
	private final DoubleProperty unitQuantity;
	private final DoubleProperty unitPrice;
	
	public Item() {
		this(null,null,0.0,0.0);	
	}

	public Item(String name, String unit, double unitQuantity, double unitPrice) {
		this.name = new SimpleStringProperty(name);
		this.unit = new SimpleStringProperty(unit);
		this.unitQuantity = new SimpleDoubleProperty(unitQuantity);
		this.unitPrice = new SimpleDoubleProperty(unitPrice);
	}
	
	public String toString() {
		String itemName = getName();
        double unitQuantity = getUnitQuantity();
        String unitName=getUnit();
        double unitPrice = getUnitPrice();
       
        String itemDetails ="Item details:\n" +
        		"Item name: " + itemName + "\n" +
                "Unit: " + unitQuantity + unitName+ "\n" +
                "Unit price: $" + unitPrice + "\n";
      
		return itemDetails;		
	}

	public String getName() {
		return name.get();
	}

	public String getUnit() {
		return unit.get();
	}
	
	public double getUnitQuantity() {
		return unitQuantity.get();
	}
	
	public double getUnitPrice() {
		return unitPrice.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setUnit(String unit) {
		this.unit.set(unit);
	}
	
	public void setUnitQuantity(double unitQty) {
		this.unitQuantity.set(unitQty);
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice.set(unitPrice);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public StringProperty unitProperty() {
		return unit;
	}
	
	public DoubleProperty unitPriceProperty() {
		return unitPrice;
	}

	public DoubleProperty unitQuantityProperty() {
		return unitQuantity;
	}
	
	
	
}
