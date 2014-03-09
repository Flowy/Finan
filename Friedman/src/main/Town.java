package main;

import java.util.Collection;

public class Town extends Field {
	
	int citizens;

	Town(int x, int y, int citizens) {
		super(x, y);
		this.citizens = citizens;
	}
	
	int getPopulation() {
		return citizens;
	}
	
	Shop chooseClosestShop(Collection<Shop> shopList) {
		Shop closest = null;
		int actualDistance;
		int minDistance;
		for (Shop shop: shopList) {
			if (closest == null) {
				closest = shop;
			} else {
				actualDistance = this.distanceFrom(shop);
				minDistance = this.distanceFrom(closest);
				if (actualDistance < minDistance) {
					closest = shop;
				}
			}
		}
		return closest;		
	}
	
	@Override
	public String toString() {
		return super.toString() + ", Population: " + citizens;
	}

}
