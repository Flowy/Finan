package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Stats {

	int round;
	int fieldSize;
	Player ownPlayer;
	
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
		for (Shop shop: ownPlayer.shops) {
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
