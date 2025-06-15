package order;

import java.io.Serializable;

public class OrderItemVO implements Serializable {
	private int penNo;
	private int quantity;
	private int price;
	
	public OrderItemVO(int penNo, int quantity, int price) {
		this.penNo = penNo;
		this.quantity = quantity;
		this.price = price;
	}

	public int getPenNo() {
		return penNo;
	}

	public void setPenNo(int penNo) {
		this.penNo = penNo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "\t***" + penNo + ", " + quantity + "(개), " + price + "(원)";
	}
	
	
}
