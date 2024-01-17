package application.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PurchasedItem extends Item{
	private final DoubleProperty purchasedQuantity;
	
	public PurchasedItem() {
		super(); 
        this.purchasedQuantity = new SimpleDoubleProperty(0.0);
	}

	public PurchasedItem(Item item,double selectedQuantity) {
		 super(item.getName(),item.getUnit(),item.getUnitQuantity(), item.getUnitPrice());
	     this.purchasedQuantity = new SimpleDoubleProperty(selectedQuantity);
	}
	
	public double getPurchasedQuantity() {
		return purchasedQuantity.get();
	}
	
	public void setPurchasedQuantity(double purchasedQty) {
		this.purchasedQuantity.set(purchasedQty);
	}

	public double getTotalPrice() {
		return this.getUnitPrice()*purchasedQuantity.get();
	}	

	public PurchasedItem getItem() {
		return this;
	}
	
	public DoubleProperty purchasedQuantityProperty() {
		return purchasedQuantity;
	}

	public DoubleProperty totalPriceProperty() {
		DoubleProperty res=new SimpleDoubleProperty(this.getTotalPrice());
		return res;
	}
	
	public String toString() {
		String itemName = getName();
        double unitQuantity = getUnitQuantity();
        String unitName=getUnit();
        double unitPrice = getUnitPrice();
        double purchasedQuantity = getPurchasedQuantity();
        
        String itemDetails = "Purchased item details:\n" +
                "Item name: " + itemName + "\n" +
                "Unit: " + unitQuantity + unitName+ "\n" +
                "Unit price: $" + unitPrice + "\n" +
                "Purchased quantity: " + purchasedQuantity;
        
        return itemDetails;	
	}
	
	public boolean isEmpty() {
		if (purchasedQuantity.get()==0){
			return true;
		}else {
			return false;
		}
	}

}
