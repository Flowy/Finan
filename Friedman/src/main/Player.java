package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Player {

	
	
	int id;
	Set<Shop> shops;
	double wallet;

	Player(int id, double wallet) {
		this.id = id;
		this.wallet = wallet;
		shops = new TreeSet<Shop>();
	}

	boolean addShop(Shop shop) {
		return shops.add(shop);
	}

	int getID() {
		return id;
	}

	double dayProfit() {
		double sum = 0;
		for (Shop shop: shops) {
			sum += shop.getProfit();
		}
		return sum;
	}

	void createShopsInClusters(int fieldSize, Collection<Town> towns, int newShops) {
		Map<Shop, Set<Town>> centerShops = new TreeMap<>();
		for (int i = 0; i<newShops; i++) {
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
			
			Map<Shop, Set<Town>> oldShops = centerShops;
			centerShops = new TreeMap<>();
			for (Shop shop: oldShops.keySet()) {
				Shop newCenter = Shop.getCenter(oldShops.get(shop));
				if (!shop.equals(newCenter) && !changed) {
					changed = true;
				}
				centerShops.put(newCenter, new TreeSet<Town>());
			}
		}
		for (Shop shop: centerShops.keySet()) {
			shop.setForBuild();
			addShop(shop);
		}
	}

	//TODO: needs to count with near shops
	void createShopsByAverage(int fieldSize, Set<Town> towns, int newShops) {
		//sort shop positions by avg distance to towns
		Map<Double, Queue<Integer>> positions = new TreeMap<>();

		for (int i = 0; i<Math.pow(fieldSize, 2); i++) {
			double dist = Shop.avgWeightToTowns(i/fieldSize, i%fieldSize, towns, 1.5);

			//debug
				if (i%fieldSize == 0) {
					System.out.format("\n%2d ", i/fieldSize);
				}
				System.out.format("%8.5f ", dist);

			Queue<Integer> tempList = positions.get(dist);
			if (tempList == null) {
				tempList = new ArrayDeque<Integer>();
				positions.put(dist, tempList);
			}
			tempList.add(i);
		}
		
		//create shops
		List<Queue<Integer>> shopLists = new ArrayList<>(positions.values());
		int i = 0;
		for (Queue<Integer> shopList: shopLists) {
			while (i<newShops && shopList.size() > 0) {
				Shop actual = Shop.createShopOnPosition(shopList.poll(), fieldSize, Shop.Status.BUILD);
				if (addShop(actual)) {
					i++;
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Player ID: " + id + "\n");
		result.append("Wallet: " + wallet + "\n");
		result.append("Shops: " + shops.size() + "\n");
		for (Shop shop: shops) {
			result.append(shop.toString() + "\n");
		}
		return result.toString();
	}
}
