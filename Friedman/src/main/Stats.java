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
	
	public void setRound(int round) {
		this.round = round;
	}
	
	public void setFieldSize(int size) {
		this.fieldSize = size;
	}
	
	public void setOwnPlayer(Player player) {
		this.ownPlayer = player;
	}
	
	public void addTown(Town town) {
		towns.add(town);
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public Player findPlayerByID(int id) {
		for (Player player: players) {
			if (player.getID() == id) {
				return player;
			}
		}
		return null;
	}
	
}
