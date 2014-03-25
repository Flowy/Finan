package main;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import main.Shop.Status;

public class Main {
	
//	final private static int MAX_DISTANCE_FOR_SHOP_PRICE = 7;
	private float FORECAST = .9f;	//multiplier of prices
	final private static int ITERATION = 10000;
	final private Stats stats;
	
	Main(Stats stats) {
		this.stats = stats;
	}
	
	public static void main(String[] args) {
		
		Main main = new Main(new PlayfieldLoader().loadFile("playfield.txt"));
		
//		stats.setOwnPlayer(stats.findPlayerByID(Integer.parseInt(args[0])));
		main.stats.setOwnPlayer(main.stats.findPlayerByID(1));
//		main.createShopsByClusters();
		main.setBestPrices();
		System.out.println(main.stats.getExpectedProfit());
				
//		System.out.println(main.stats.toString());

	}
	
	

	public void createShopsByClusters() {
		Set<Shop> shopList = null;
		
		TreeMap<Double, Set<Shop>> distanceMap = new TreeMap<>();
		for (int i = 0; i<ITERATION; i++) {
				shopList = assignShopsToClusters(stats.getTowns(), 3); //set maxshops
				distanceMap.put(getAvgDistance(shopList), shopList);
		}
		
		shopList = distanceMap.get(distanceMap.lastKey());
		
		for (Shop shop: shopList) {
			if (shop.getStatus() == Shop.Status.VOID) {
				shop.setForBuild();
			}
			stats.getOwnPlayer().addShop(shop);
		}

	}

	double getAvgDistance(Collection<Shop> shops) {
		double distance = 0, population = 0;
		for (Shop shop: shops) {
			for (Town town: stats.getTowns()) {
				population += town.getPopulation();
				distance += (1/(shop.distanceFrom(town)*125)) * town.getPopulation();
			}
		}
		return distance / population / shops.size();
	}
	
	Set<Shop> assignShopsToClusters(Collection<Town> towns, int clusters) {

		Map<Shop, Set<Town>> centerShops = new TreeMap<>();
		Map<Shop, Set<Town>> centerShopsBackup = null;
		//add old shops
		for (Shop shop: stats.getOwnPlayer().getShops()) {
			centerShops.put(shop, new TreeSet<Town>());
		}

		//add new shops
		for (int i = 0; i<clusters; i++) {
			int x, y;
			x = (int) (Math.random() * 14.9999);
			y = (int) (Math.random() * 14.9999);
			centerShops.put(new Shop(x, y, Shop.Status.VOID), new TreeSet<Town>());
		}

		boolean changed = true;
		while (changed) {
			changed = false;
			for (Town town: towns) {
				Shop closestShop = town.chooseClosestShop(centerShops.keySet());
				Set<Town> actual = centerShops.get(closestShop);
				actual.add(town);
			}

			centerShopsBackup = centerShops;
			centerShops = new TreeMap<>();
			for (Shop shop: centerShopsBackup.keySet()) {
				if (centerShopsBackup.get(shop).size() != 0) {
					Shop newCenter = shop;
					if (shop.getStatus() != Shop.Status.OLD) {
						newCenter = getNewCenterShop(centerShopsBackup.get(shop));
					}
					if (!shop.equals(newCenter) && !changed) {
						changed = true;
					}
					centerShops.put(newCenter, new TreeSet<Town>());
				}
			}
		}
		return centerShops.keySet();
	}

	private Shop getNewCenterShop(Collection<Town> towns) {
		int nominatorXSum = 0, nominatorYSum = 0;
		float denominatorSum = 0;

		for (Town town: stats.getTowns()) {  //counting all towns
			int population = town.getPopulation();
			if (towns.contains(town)) {  //towns from parameter have bigger weight
				population = (int) Math.pow(population, 2);
			}
			nominatorXSum += town.getX() * population;
			nominatorYSum += town.getY() * population;
			denominatorSum += population;
		}

		int x = Math.round(nominatorXSum / denominatorSum);
		int y = Math.round(nominatorYSum / denominatorSum);

		Shop newShop = new Shop(x, y, Status.VOID);

		if (stats.shopExists(newShop)) {
			Map<Double, Queue<Shop>> distanceFromCenterMap = new TreeMap<>();
			for (int i = 0; i<Math.pow(stats.getFieldSize(),2); i++) {
				Shop tempShop = Shop.createShopOnPosition(i, stats.getFieldSize(), Shop.Status.VOID);
				if (!stats.shopExists(tempShop)) {
					double distance = getWeightedDistanceFromCenter(tempShop, newShop, towns);
					Queue<Shop> tempSet = distanceFromCenterMap.get(distance);
					if (tempSet == null) {
						tempSet = new ArrayDeque<Shop>();
						distanceFromCenterMap.put(distance, tempSet);
					}
					tempSet.add(tempShop);
				}
			}
			Queue<Queue<Shop>> distanceQueue = new ArrayDeque<>(distanceFromCenterMap.values());
			Queue<Shop> actual = distanceQueue.poll();
			while (stats.shopExists(newShop)) {
				if (actual.size() == 0) {
					actual = distanceQueue.poll();
				}
				newShop = actual.poll();
			}
		}

		return newShop;
	}

	private double getWeightedDistanceFromCenter(Shop newShop, Shop center, Collection<Town> towns) {
		double xDistance = 0, yDistance = 0;
		int denominatorSum = 0;
		for (Town town: stats.getTowns()) {
			int population = town.getPopulation();
			if (towns.contains(town)) {
				population = (int) Math.pow(population, 2);
			}
			xDistance += population * Math.pow(newShop.distanceFrom(center), 2);
			yDistance += population * Math.pow(newShop.distanceFrom(center), 2);
			denominatorSum += population;
		}

		return (xDistance/denominatorSum) + (yDistance/denominatorSum);
	}
	
	public void setBestPrices() {
		boolean changed = true;
		final double TOLLERANCE = 0.0000001; //needed for shortening the precice calculation of price
		while (changed) {
			changed = false;
//			System.out.println(stats.getOwnPlayer());
			for (Shop shop: stats.getOwnPlayer().getShops()) {
				final double oldPrice = shop.getPrice();
				setBestPriceForShop(shop);
				if (oldPrice <= (shop.getPrice() + TOLLERANCE) && oldPrice >= (shop.getPrice() - TOLLERANCE)) {
					changed = true;
				}
			}
		}
	}
	
	private void setBestPriceForShop(Shop shop) {
		double priceSum = 0;
		double priceN = 0;
		for (Town town: stats.getTowns()) {
			double actualWeight = town.getPopulation();
			if (actualWeight > 0) {
				priceN += actualWeight;
				priceSum += getBestPriceForTown(shop, town) * actualWeight;
			}
		}
		
		shop.setPrice((priceSum/priceN) * FORECAST);
	}
	
	private double getBestPriceForTown(Shop shop, Town town) {
		//needs to reset shop weight before calculate
		shop.setPrice(0);
		double totalWeight = stats.getTotalWeightForTown(town);
		int distance = shop.distanceFrom(town);
		
		//x vyjadrene z derivacie zisku v meste pre jedneho obyvatela (vaha/celkovaVaha * zisk na jednom tovare) porovnanej k nule
		double temp1 = 1201 * Math.pow(distance, 3) * Math.pow(totalWeight, 3) +
				702 * Math.pow(distance, 2) * Math.pow(totalWeight, 2) +
				distance * totalWeight;
		temp1 += Math.sqrt(1192401 * Math.pow(distance, 6) * Math.pow(totalWeight, 6) + 
				1686204 * Math.pow(distance, 5) * Math.pow(totalWeight, 5) +
				495206 * Math.pow(distance, 4) * Math.pow(totalWeight, 4) +
				1404 * Math.pow(distance, 3) * Math.pow(totalWeight, 3) +
				Math.pow(distance, 2) * Math.pow(totalWeight, 2)
				);
		temp1 = Math.pow(temp1, 1f/3);
		
		double temp2 = 50 * Math.pow(2, 2f/3) * distance * totalWeight;

		double temp3 = (Math.pow(2,1f/3) * temp1) / (distance * totalWeight);
		
		double temp4 = 16*(distance * totalWeight + 1) / (distance * totalWeight) + 5600;
		
		double result = 1f/2 * Math.sqrt(temp2/temp1 + temp3 + 80);
		
		result += -1f/2 * Math.sqrt(-temp2/temp1 + temp4/(4 * Math.sqrt(temp2/temp1 + temp3 + 80)) - temp3 + 160) + 5;
		
		return result;
	}
	
}
