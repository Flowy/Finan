package main;

import java.util.Collection;

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

	void setForBuild() {
		if (status != Status.OLD){ 
			status = Status.BUILD;
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

	double getWeightForTown(Town town) {
		return getWeightForTown(town, getPrice());
	}

	double getWeightForTown(Town town, double price) {
		return getWeight(this.distanceFrom(town), price);
	}

	static double getWeight(int distance, double price) {
		if (price <= 0 || price >= 10) return 0;
		return 1 / ( (1 + distance) * (1 + Math.pow(price, 3)) );
	}

	double avgDistToTowns(Collection<Town> towns) throws NullPointerException {
		int nomSum = 0;
		int denomSum = 0;
		if (towns == null || towns.size() == 0) {
			throw new NullPointerException();
		}
		for (Town town: towns) {
			int dist = this.distanceFrom(town);
			nomSum += dist*town.getPopulation();
			denomSum += town.getPopulation();
		}
		return nomSum / (double) denomSum;

	}

	static double profitPerUnit(double price) {
		double profitPerUnit;
		profitPerUnit = price - 1 - (Math.pow(price,2) / 10);
		return profitPerUnit;
	}

	/**
	 * Calculate price for profit
	 * 
	 * @param profit
	 * @return price (returns NaN for x>5)
	 */
	static double wantedProfitPerUnit(double profit) {
		double wantedProfit = 0;
		if (profit > 1.5) {
			wantedProfit = 1.5;
		}
		wantedProfit = -5 * Math.sqrt( (3- 2 * profit) / 5) + 5;
		return wantedProfit;
	}

	@Override
	public String toString() {
		return super.toString() + ", Price: " + price + ", Profit " + profit + ", Status: " + status;
	}

}
