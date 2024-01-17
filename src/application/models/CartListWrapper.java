package application.models;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Carts")
public class CartListWrapper {
	private List<Cart> carts;
	
	@XmlElement(name = "Cart")
	public List<Cart> getCarts(){
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts=carts;
	}
}
 
