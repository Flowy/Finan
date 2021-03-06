package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Stats {

	private final int SHOP_MAINTENANCE = 30;
	
	
	private int round;
	private int fieldSize;
	private Player ownPlayer;
	
	Set<Town> towns;
	List<Player> players;
	
	Stats() {
		 players = new ArrayList<Player>();
		 towns = new TreeSet<Town>();
	}
	
	//////////////  SETTERS
	void setRound(int round) {
		this.round = round;
	}
	
	void setFieldSize(int size) {
		this.fieldSize = size;
	}
	
	void setOwnPlayer(Player player) {
		this.ownPlayer = player;
	}
	
	////////////// GETTERS
	
	int getRound() {
		return round;
	}
	
	Player getOwnPlayer() {
		return ownPlayer;
	}
	
	int getFieldSize() {
		return fieldSize;
	}
	
	Set<Town> getTowns() {
		return towns;
	}
	
	int getTotalPopulation() {
		int result = 0;
		for (Town town: towns) {
			result += town.getPopulation();
		}
		return result;
	}
	
	double getExpectedProfit() {
		double result = 0;
		for (Shop shop: ownPlayer.getShops()) {
			result += getTotalShopProfit(shop);
		}
		return result;
	}
	
	double getTotalShopProfit(Shop shop) {
		double result = 0;
		for (Town town: towns) {
			result += getShopProfitPerTown(shop, town);
		}
		result -= SHOP_MAINTENANCE;
		return result;
	}
	
	double getShopProfitPerTown(Shop shop, Town town) {
		double result = 0;
		result += (shop.getWeightForTown(town) / getTotalWeightForTown(town)) * shop.getPrice() * town.getPopulation();
		return result;
	}
	
	double getTotalWeightForTown(Town town) {
		double result = 0;
		for (Player player: players) {
			for (Shop shop: player.getShops()) {
				if (shop.getStatus() == Shop.Status.OLD) {
					result += shop.getWeightForTown(town);					
				}
			}
		}
		return result;
	}
	
	Player findPlayerByID(int id) {
		for (Player player: players) {
			if (player.getID() == id) {
				return player;
			}
		}
		return null;
	}
	
	////////////// GETTERS END
	
	void addTown(Town town) {
		towns.add(town);
	}
	
	void addPlayer(Player player) {
		players.add(player);
	}
	
	boolean shopExists(Shop shop) {
		boolean result = false;
		for (Player player:players) {
			if (player.shopExists(shop)) {
				result = true;
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Round: " + round + "\n");
		result.append("Field Size: " + fieldSize + "\n");
		result.append("Own Player ID: " + ownPlayer.getID() + "\n");
		result.append("Towns: " + towns.size() + "\n");
		for (Town town: towns) {
			result.append("\t" + town.toString() + "\n");
		}
		result.append("\n");
		for (Player player: players) {
			result.append(player.toString() + "\n");
		}
		return result.toString();
	}
	
	String getReply() {
		StringBuilder prices = new StringBuilder();
		StringBuilder build = new StringBuilder();
		StringBuilder remove = new StringBuilder();
		for (Shop shop: ownPlayer.getShops()) {
			switch(shop.getStatus()) {
			case BUILD:
				build.append("n " + shop.getX() + " " + shop.getY() + ";");
				break;
			case REMOVE:
				remove.append("z " + shop.getX() + " " + shop.getY() + ";");
			case OLD:
				prices.append(shop.getPrice() + ";");
				break;
			case VOID:
				break;
			}
		}
		return prices.toString() + remove.toString() + build.toString();
	}
}
