package main;

import java.util.Set;
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
	
	void addShop(Shop shop) {
		shops.add(shop);
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
