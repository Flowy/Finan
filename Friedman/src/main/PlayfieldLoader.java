package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayfieldLoader {

	public static final Pattern roundRegex = Pattern.compile("^Kolo: (?<round>\\d+)");
	public static final Pattern sizeRegex = Pattern.compile("^Rozmer: (?<size>\\d+)");
	public static final Pattern townsRegex = Pattern.compile("^Mesta: (?<towns>\\d+)");
	public static final Pattern townRegex = Pattern.compile("^(?<x>\\d+) (?<y>\\d+) (?<population>\\d+)");
	public static final Pattern playerStatsRegex = Pattern.compile("^Hrac(?<id>\\d+): (?<wallet>\\d+.?\\d*) euro (?<shops>\\d+) obchodu");
	public static final Pattern shopsRegex = Pattern.compile("^(?<x>\\d+) (?<y>\\d+) (?<price>\\d+.?\\d*) (?<profit>\\d+.?\\d*)");
	//XXX: 
	//	public static final Pattern playerResponseRegex = Pattern.compile("^Hrac(?<id>\\d+)_odpoved: \"(?:(?<price>\\d*);?)*(?:(?<close>z \\d+ \\d+);?)*(?:(?<open>n \\d+ \\d+);?)*\"");


	Stats stats;
	Player actualPlayer;


	public PlayfieldLoader() {
		stats = new Stats();
	}

	Stats loadFile(String filePath) {

		List<String> content = null;
		Charset charset = Charset.forName("windows-1250");
		Path path = Paths.get(filePath);

		try {
			content = Files.readAllLines(path, charset);
		} catch (IOException ex) {
			System.err.println("Cannot read file " + filePath);
		}

		if (content.size() != 0) {
			int lineIndex = 0;

			Matcher m = roundRegex.matcher(content.get(lineIndex++));
			if (m.matches()) {
				stats.setRound(Integer.parseInt(m.group("round")));
			}

			m = sizeRegex.matcher(content.get(lineIndex++));
			if (m.matches()) {
				stats.setFieldSize(Integer.parseInt(m.group("size")));
			}

			int towns = parseTowns(content.get(lineIndex++));
			while (towns > 0) {
				parseTown(content.get(lineIndex++));
				towns--;
			}

			//XXX: only for two players
			for (int i = 0; i<2; i++) {
				int shops = parsePlayer(content.get(lineIndex++));
				while (shops > 0) {
					parseShop(content.get(lineIndex++));
					shops--;
				}
			}			

		}
		return stats;
	}


	private int parseTowns(String line) {
		int towns = 0;
		Matcher m = townsRegex.matcher(line);
		if (m.matches()) {
			towns = Integer.parseInt(m.group("towns"));
		}
		return towns;
	}

	private void parseTown(String line) {
		Matcher m = townRegex.matcher(line);
		if (m.matches()) {
			int x = Integer.parseInt(m.group("x"));
			int y = Integer.parseInt(m.group("y"));
			int population = Integer.parseInt(m.group("population"));
			Town actual = new Town(x, y, population, Town.Status.old);
			stats.addTown(actual);
		}
	}

	private int parsePlayer(String line) {
		int shops = 0;
		Matcher m = playerStatsRegex.matcher(line);
		if (m.matches()) {
			int id = Integer.parseInt(m.group("id"));
			double wallet = Double.parseDouble(m.group("wallet"));
			shops = Integer.parseInt(m.group("shops"));
			actualPlayer = new Player(id, wallet);
			stats.addPlayer(actualPlayer);
		}	
		return shops;
	}

	private void parseShop(String line) {
		Matcher m = shopsRegex.matcher(line);
		if (m.matches()) {
			int x = Integer.parseInt(m.group("x"));
			int y = Integer.parseInt(m.group("y"));
			double price = Double.parseDouble(m.group("price"));
			double profit = Double.parseDouble(m.group("profit"));
			Shop actualShop = new Shop(x, y);
			actualShop.setPrice(price);
			actualShop.setProfit(profit);
			actualPlayer.addShop(actualShop);
		}	
	}
}
