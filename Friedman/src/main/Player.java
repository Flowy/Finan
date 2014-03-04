package main;

import java.util.ArrayList;
import java.util.List;

public class Player {

	int id;
	List<Shop> shops;
	double wallet;
	
	Player(int id, double wallet) {
		this.id = id;
		this.wallet = wallet;
		shops = new ArrayList<Shop>();
	}
	
	void addShop(Shop shop) {
		shops.add(shop);
	}
	
	int getID() {
		return id;
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
