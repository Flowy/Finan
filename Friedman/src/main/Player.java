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
}
