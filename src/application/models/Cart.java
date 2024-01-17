package application.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

@XmlRootElement(name = "Cart")
public class Cart {
	private final IntegerProperty cartId= new SimpleIntegerProperty();
    private final DoubleProperty totalPrice = new SimpleDoubleProperty();
	private List<PurchasedItem> purchasedItems;
	
	@XmlElement(name = "PurchasedItem")
    public List<PurchasedItem> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<PurchasedItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }
	
	public Cart() {}

    public Cart(int id,ObservableList<PurchasedItem> itemObList,double price) {
		super();
		this.setCartId(id);
		this.setTotalPrice(price);
		this.purchasedItems=new ArrayList<>(itemObList);
	}
    
	public int getCartId() {
		return cartId.get();
	}
	
	public IntegerProperty cartIdProperty() {
        return cartId;
    }

	public void setCartId(int cartId) {
		this.cartId.set(cartId);
	}

	public double getTotalPrice() {
		return totalPrice.get();
	}
	
	public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

	public void setTotalPrice(double totalPrice) {
		this.totalPrice.set(totalPrice);
	}




}
