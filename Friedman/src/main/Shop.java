package main;

import java.util.Collection;
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

	static Shop getCenter(Collection<Town> towns) {
		int nominatorXSum = 0, nominatorYSum = 0;
		float denominatorSum = 0;

		for (Town town:towns) {
			nominatorXSum += town.getX() * town.getPopulation();
			nominatorYSum += town.getY() * town.getPopulation();
			denominatorSum += town.getPopulation();
		}

		int x = Math.round(nominatorXSum / denominatorSum);
		int y = Math.round(nominatorYSum / denominatorSum);

		return new Shop(x, y, Status.VOID);
	}

	double getWeightForTown(Town town) {
		return getWeightForTown(town, getPrice());
	}

	double getWeightForTown(Town town, double price) {
		return getWeightForTown(this.getX(), this.getY(), town, price);
	}

	static double getWeightForTown(int originX, int originY, Town town, double price) {
		return getWeight(Field.distanceFrom(originX, originY, town), price);
	}

	static double getWeight(int distance, double price) {
		return 1 / ( (1 + distance) * (1 + Math.pow(price, 3)) );
	}

	static double avgWeightToTowns(int x, int y, Set<Town> towns, double price) {
		double nomSum = 0;
		int denomSum = 0;

		for (Town town: towns) {
			double weight = getWeightForTown(x, y, town, price);
			nomSum += weight * town.getPopulation();
			denomSum += town.getPopulation();
		}

		return nomSum / (double) denomSum;
	}

	static double avgDistToTowns(int x, int y, Set<Town> towns) throws NullPointerException {
		int nomSum = 0;
		int denomSum = 0;
		if (towns == null || towns.size() == 0) {
			throw new NullPointerException();
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
		double wantedProfit;
		wantedProfit = -5 * Math.sqrt( (3- 2 * profit) / 5) + 5;
		return wantedProfit;
	}

	@Override
	public String toString() {
		return super.toString() + ", Price: " + price + ", Profit " + profit + ", Status: " + status;
	}
}
