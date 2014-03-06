package main;

import java.util.Set;

public class Shop extends Field {

	enum Status {
		OLD, BUILD, REMOVE, VOID
	}
	
	double price = 0;
	double profit = 0;
	Status status;
	
	public Shop(int x, int y, Status status) {
		super(x, y);
		this.status = status;
	}
	
	static Shop createShopOnPosition(int position, int fieldSize, Status status) {
		return new Shop( position/fieldSize, position%fieldSize, status );
	}

	void setForRemove() {
		if (status == Status.BUILD) {
			status = Status.VOID;
		} else {
			status = Status.REMOVE;
		}
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
	
	static double avgDistToTowns(int x, int y, Set<Town> towns) throws ArithmeticException {
		int nomSum = 0;
		int denomSum = 0;
		if (towns == null || towns.size() == 0) {
			throw new ArithmeticException();
		}
		for (Town town: towns) {
			int dist = Field.distanceFrom(x, y, town);
			nomSum += dist*town.getPopulation();
			denomSum += town.getPopulation();
		}
		return nomSum / (double) denomSum;
	}
	
	static double profitPerUnit(double price) {
		double profitPerUnit;
		profitPerUnit = price - 1 - Math.pow(price,2);
		return profitPerUnit;
	}
	
	/**
	 * Calculate price for profit
	 * 
	 * @param profit
	 * @return price (returns NaN for x>5)
	 */
	static double wantedProfitPerUnit(double profit) {
		double wantedProfit;
		wantedProfit = -5 * Math.sqrt( (3- 2 * profit) / 5) + 5;
		return wantedProfit;
	}

	@Override
	public String toString() {
		return super.toString() + ", Price: " + price + ", Profit " + profit + ", Status: " + status;
	}
}
