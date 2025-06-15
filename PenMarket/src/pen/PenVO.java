package pen;

import java.io.Serializable;
import java.util.Date;

public class PenVO implements Serializable {
	private int penNo;
	private String title;
	private String origin;
	private String brand;
	private int price;
	private int instock;
	private Date regdate;
	
	public PenVO(int penNo, String title, String origin, String brand, int price, int instock, Date regdate) {
		this.penNo = penNo;
		this.title = title;
		this.origin = origin;
		this.brand = brand;
		this.price = price;
		this.instock = instock;
		this.regdate = regdate;
	}
	
	public PenVO(String title, String origin, String brand, int price, int instock) {
		this(-1, title, origin, brand, price, instock, null);
	}
	
	public String toString() {
		return "[" + penNo + ", " + title + ", " + origin + ", " + brand + ", " + price + ", " + instock + "]";
	}

	public int getPenNo() {
		return penNo;
	}

	public void setPenNo(int penNo) {
		this.penNo = penNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getInstock() {
		return instock;
	}

	public void setInstock(int instock) {
		this.instock = instock;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	
	
}