package main;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import main.Shop.Status;

public class CreateShopsByClusters implements Strategy {

	final Stats stats;

	public CreateShopsByClusters(Stats stats) {
		this.stats = stats;
	}

	@Override
	public void execute() {
		Set<Shop> shopList = assignShopsToClusters(stats.getTowns(), 5);

		for (Shop shop: shopList) {
			if (shop.getStatus() == Shop.Status.VOID) {
				shop.setForBuild();
			}
			stats.getOwnPlayer().addShop(shop);
		}

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
						newCenter = getCenterShop(centerShopsBackup.get(shop));
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

	private Shop getCenterShop(Collection<Town> towns) {
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
			Queue<Shop> actual = null;
			while (stats.shopExists(newShop)) {
				if (actual == null) {
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

}
