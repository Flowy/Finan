package main;

import java.util.ArrayList;
import java.util.List;

public class Stats {

	int round;
	int fieldSize;
	Player ownPlayer;
	
	List<Town> towns;
	List<Player> players;
	
	public Stats() {
		 players = new ArrayList<Player>();
		 towns = new ArrayList<Town>();
	}
	
	//////////////  SETTERS
	public void setRound(int round) {
		this.round = round;
	}
	
	public void setFieldSize(int size) {
		this.fieldSize = size;
	}
	
	public void setOwnPlayer(Player player) {
		this.ownPlayer = player;
	}
	
	////////////// GETTERS
	
	public Player findPlayerByID(int id) {
		for (Player player: players) {
			if (player.getID() == id) {
				return player;
			}
		}
		return null;
	}
	
	////////////// GETTERS END
	
	public void addTown(Town town) {
		towns.add(town);
	}
	
	public void addPlayer(Player player) {
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
}
