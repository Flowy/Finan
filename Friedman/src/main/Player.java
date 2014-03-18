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
	
	Set<Shop> getShops() {
		return shops;
	}
	
	boolean shopExists(Shop origin){
		boolean result = false;
		for (Shop shop: shops) {
			if (shop.equals(origin)) {
				result = true;
			}
		}
		return result;
	}

	double dayProfit() {
		double sum = 0;
		for (Shop shop: shops) {
			sum += shop.getProfit();
		}
		return sum;
	}

	double getTotalShopWeigths(Collection<Town> towns, double price) {
		double sum = 0;
		for (Shop shop: shops) {
			for (Town town: towns) {
				sum += shop.getWeightForTown(town, price);
			}
		}
		return sum;
	}

//	//TODO: needs to count with near shops
//	void createShopsByAverage(int fieldSize, Set<Town> towns, int newShops) {
//		//sort shop positions by avg distance to towns
//		Map<Double, Queue<Integer>> positions = new TreeMap<>();
//
//		for (int i = 0; i<Math.pow(fieldSize, 2); i++) {
//			double dist = Shop.avgWeightToTowns(i/fieldSize, i%fieldSize, towns, 1.5);
//
//			//debug
////			if (i%fieldSize == 0) {
////				System.out.format("\n%2d ", i/fieldSize);
////			}
////			System.out.format("%8.5f ", dist);
//
//			Queue<Integer> tempList = positions.get(dist);
//			if (tempList == null) {
//				tempList = new ArrayDeque<Integer>();
//				positions.put(dist, tempList);
//			}
//			tempList.add(i);
//		}
//
//		//create shops
//		List<Queue<Integer>> shopLists = new ArrayList<>(positions.values());
//		int i = 0;
//		for (Queue<Integer> shopList: shopLists) {
//			while (i<newShops && shopList.size() > 0) {
//				Shop actual = Shop.createShopOnPosition(shopList.poll(), fieldSize, Shop.Status.BUILD);
//				if (addShop(actual)) {
//					i++;
//				}
//			}
//		}
//	}

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
	
	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object instanceof Player) {
			Player player = (Player) object;
			if (player.getID() == this.getID()) {
				result = true;
			}
		}
		return result;
	}
}
