package main;

public class Shop extends Field {

	double price = 0;
	double profit = 0;
	
	public Shop(int x, int y) {
		super(x, y);
		
	}
	
	void setPrice(double price) {
		this.price = price;
	}
	
	void setProfit(double profit) {
		this.profit = profit;
	}

}
