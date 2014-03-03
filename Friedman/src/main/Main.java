package main;

public class Main {
	
	public static void main(String[] args) {
		Stats stats = new PlayfieldLoader().load("playfield.txt");
		stats.setOwnPlayer(stats.findPlayerByID(Integer.parseInt(args[0])));
	}

}
