package main;

public class Main {
	
	public static void main(String[] args) {
		Stats stats = new PlayfieldLoader().loadFile("playfield.txt");
		stats.setOwnPlayer(stats.findPlayerByID(Integer.parseInt(args[0])));
		stats.getOwnPlayer().createShopsInClusters(stats.getFieldSize(), stats.getTowns(), 10);
//		System.out.println("\nTowns: " + Shop.getCenterOfTowns(stats.getTowns()));
////		System.out.println(stats.getReply());
		System.out.println(stats.toString());

		
//		for (double i = 1; i<=5; i += 0.5) {
//			System.out.format("%4.2f %6.4f\n", i, Shop.profitPerUnit(i));
//		}
//		
//		for (int dist = 0; dist<=30; dist++) {
//			System.out.format("\nDistance: %2d ", dist);
//			for (double price = 1; price<=5; price += 0.5) {
//				System.out.format("%4f ", Shop.getWeight(dist, price));
//			}
//		}
	}
	
}
