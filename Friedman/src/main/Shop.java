package main;

public class Shop extends Field {

	enum Status {
		OLD, BUILD, REMOVE
	}
	
	double price = 0;
	double profit = 0;
	Status status;
	
	public Shop(int x, int y, Status status) {
		super(x, y);
		this.status = status;
	}
	
	void setForRemove() {
		status = Status.REMOVE;
	}
	
	void setPrice(double price) {
		this.price = price;
	}
	
	void setProfit(double profit) {
		this.profit = profit;
	}
	
	double getPrice() {
		return price;
	}
	
	double getProfit() {
		return profit;
	}
	
	Status getStatus() {
		return status;
	}
	
	static double profitPerUnit(double price) {
		double profitPerUnit;
		profitPerUnit = price - 1 - Math.pow(price,2);
		return profitPerUnit;
	}
	
	/**
	 * Calculate price for profit
	 * @param profit
	 * @return price
	 */
	static double wantedProfitPerUnit(double profit) {
		double wantedProfit;
		wantedProfit = -5 * Math.sqrt( (3- 2 * profit) / 5) + 5;
		return wantedProfit;
	}

	@Override
	public String toString() {
		return super.toString() + ", Price: " + price + ", Profit " + profit;
	}
}
