package main;

public class Main {
	
	public static void main(String[] args) {
		Stats stats = new PlayfieldLoader().loadFile("playfield.txt");
		stats.setOwnPlayer(stats.findPlayerByID(Integer.parseInt(args[0])));
		System.out.println(stats.toString());
	}

}
