package models;

public class Car {
	int id;
	String name;
	int price;
	String type;
	String brand;
	
	public Car() {
		super();
	}
	public Car(int id, String name, int price, String type, String brand) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.type = type;
		this.brand = brand;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
