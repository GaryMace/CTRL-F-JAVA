package source;

import java.util.ArrayList;

public class Board {
	private ArrayList<Continent> continents;
	public static final int NUM_CONTINENTS=6;
	
	public Board() {
		continents = new ArrayList<Continent>();
		
		setContinents();
		
	}
	
	private void setContinents() {
		ArrayList<Territory> temp = new ArrayList<Territory>();
		
		//N.America
		temp.add(new Territory("Alaska", 0));
		temp.add(new Territory("Northwest Territory", 0));
		temp.add(new Territory("Greenland", 0));
		temp.add(new Territory("Alberta", 0));
		temp.add(new Territory("Ontario", 0));
		temp.add(new Territory("Quebec", 0));
		temp.add(new Territory("Western United States", 0));
		temp.add(new Territory("Eastern United States", 0));
		temp.add(new Territory("Central America", 0));
		continents.add(new Continent("N.America", temp, 5));
		temp.clear();
		
		//S.America
		temp.add(new Territory("Venezuela", 1));
		temp.add(new Territory("Brazil", 1));
		temp.add(new Territory("Peru", 1));
		temp.add(new Territory("Argentina", 1));
		continents.add(new Continent("S.America", temp, 2));
		temp.clear();
		
		//Africa
		temp.add(new Territory("North Africa", 2));
		temp.add(new Territory("Egypt", 2));
		temp.add(new Territory("Congo", 2));
		temp.add(new Territory("East Africa", 2));
		temp.add(new Territory("Madagascar", 2));
		continents.add(new Continent("Africa", temp, 3));
		temp.clear();
		
		//Asia
		temp.add(new Territory("Middle East", 3));
		temp.add(new Territory("Afghanistan", 3));
		temp.add(new Territory("Ural", 3));
		temp.add(new Territory("India", 3));
		temp.add(new Territory("China", 3));
		temp.add(new Territory("Siam", 3));
		temp.add(new Territory("Mongolia", 3));
		temp.add(new Territory("Japan", 3));
		temp.add(new Territory("Irkutsk", 3));
		temp.add(new Territory("Siberia", 3));
		temp.add(new Territory("Yakutsk", 3));
		temp.add(new Territory("Kamchatka", 3));
		continents.add(new Continent("Asia", temp, 7));
		temp.clear();
		
		//Europe
		temp.add(new Territory("Great Britain", 4));
		temp.add(new Territory("Western Europe", 4));
		temp.add(new Territory("Southern Europe", 4));
		temp.add(new Territory("Northen Europe", 4));
		temp.add(new Territory("Ukraine", 4));
		temp.add(new Territory("Scandinavia", 4));
		temp.add(new Territory("Iceland", 4));
		continents.add(new Continent("Europe", temp, 5));
		temp.clear();
		
		//Australia
		temp.add(new Territory("Western Australia", 5));
		temp.add(new Territory("Eastern Australia", 5));
		temp.add(new Territory("New Guinea", 5));
		temp.add(new Territory("Indonesia", 5));
		continents.add(new Continent("Australia", temp, 2));
		temp.clear();
	}
}
