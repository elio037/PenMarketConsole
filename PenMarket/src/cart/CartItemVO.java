package cart;

public class CartItemVO {

	private int penNo;
	private int quantity;
	
	public CartItemVO(int penNo, int quantity) {
		this.penNo = penNo;
		this.quantity = quantity;
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

	@Override
	public String toString() {
		return "[" + penNo + "번 상품, " + quantity + "개]";
	}
	
	
}
