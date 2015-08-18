package source;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

public class Board {
	public static final int DIE_ERROR =0;
	public static final int ATTACKING_ERROR =1;
	public static final int DEFENDING_ERROR =2;
	public static final int ATTACKING_UNITS_ERROR =3;
	public static final int DEFENDING_UNITS_ERROR =4;
	public static final int ADJACENCY_ERROR = 5;
	public static final int NUM_CONTINENTS=6;
	public static final int MOVE_ERROR=7;
	public static final int MOVE_SUCCESS=8;
	
	private ArrayList<Continent> continents;
	private ArrayList<Vertex> territories;
	
	private Graph graph;
	
	public Board() {
		continents = new ArrayList<Continent>();
		territories  = new ArrayList<Vertex>();
		//makes 6 continents containing their territories and each territories border territories
		setContinents();
	}
	
	/**
	 * Core Method to game creation involving continent set-up and territories within continents set-up.
	 * Also assigns each territory(all 42) all it's bordering territories(Lot's of chump code)
	 */
	private void setContinents() {
		
		//N.America
		territories.add(0, new Vertex(new Territory("Alaska", 0)));
		territories.add(1, new Vertex(new Territory("Northwest Territory", 0)) ); 
		territories.add(2, new Vertex(new Territory("Greenland", 0))); 
		territories.add(3, new Vertex(new Territory("Alberta", 0)));
		territories.add(4, new Vertex(new Territory("Ontario", 0)));
		territories.add(5, new Vertex(new Territory("Quebec", 0))); 
		territories.add(6, new Vertex(new Territory("Western United States", 0)));
		territories.add(7, new Vertex(new Territory("Eastern United States", 0)));
		territories.add(8, new Vertex(new Territory("Central America", 0)));
		setContinent(territories, 0, 9, "North America", 5);
		
		//S.America
		territories.add(9, new Vertex(new Territory("Venezuela", 1)));
		territories.add(10, new Vertex(new Territory("Peru", 1))); 
		territories.add(11, new Vertex(new Territory("Brazil", 1)));
		territories.add(12, new Vertex(new Territory("Argentina", 1)));
		setContinent(territories, 9, 4, "South America", 2);
		
		//Africa
		territories.add(13, new Vertex(new Territory("North Africa", 2)));
		territories.add(14, new Vertex(new Territory("Egypt", 2))); 
		territories.add(15, new Vertex(new Territory("Congo", 2)));
		territories.add(16, new Vertex(new Territory("East Africa", 2))); 
		territories.add(17, new Vertex(new Territory("South Africa", 2)));
		territories.add(18, new Vertex(new Territory("Madagascar", 2)));
		setContinent(territories, 13, 6, "Africa", 3);
		
		//Asia
		territories.add(19, new Vertex(new Territory("Middle East", 3))); 
		territories.add(20, new Vertex(new Territory("Afghanistan", 3)));
		territories.add(21, new Vertex(new Territory("Ural", 3))); 
		territories.add(22, new Vertex(new Territory("India", 3))); 
		territories.add(23, new Vertex(new Territory("China", 3)));
		territories.add(24, new Vertex(new Territory("Siam", 3)));
		territories.add(25, new Vertex(new Territory("Mongolia", 3)));
		territories.add(26, new Vertex(new Territory("Japan", 3)));
		territories.add(27, new Vertex(new Territory("Irkutsk", 3)));
		territories.add(28, new Vertex(new Territory("Siberia", 3))); 
		territories.add(29, new Vertex(new Territory("Yakutsk", 3)));
		territories.add(30, new Vertex(new Territory("Kamchatka", 3)));
		setContinent(territories, 19, 11, "Asia", 7);
		
		//Europe
		territories.add(31, new Vertex(new Territory("Great Britain", 4)));
		territories.add(32, new Vertex(new Territory("Western Europe", 4)));
		territories.add(33, new Vertex(new Territory("Northern Europe", 4)));
		territories.add(34, new Vertex(new Territory("Southern Europe", 4)));
		territories.add(35, new Vertex(new Territory("Ukriane", 4)));
		territories.add(36, new Vertex(new Territory("Scandinavia", 4)));
		territories.add(37, new Vertex(new Territory("Iceland", 4))); 
		setContinent(territories, 31, 7, "Europe", 5);
		
		//Australia
		territories.add(38, new Vertex(new Territory("Western Australia", 5)));
		territories.add(39, new Vertex(new Territory("Eastern Australia", 5)));
		territories.add(40, new Vertex(new Territory("Indonesia", 5)));
		territories.add(41, new Vertex(new Territory("New Guinea", 5))); 
		setContinent(territories, 38, 4, "Australia", 2);
		
		//Add the territories to the graph 
		graph = new Graph(territories);
		
		graph.addEdge(territories.get(0), territories.get(1));	//Alaska
		graph.addEdge(territories.get(0), territories.get(3));
		graph.addEdge(territories.get(0), territories.get(30));
		
		graph.addEdge(territories.get(1), territories.get(0));	//Northwest Territory
		graph.addEdge(territories.get(1), territories.get(3));
		graph.addEdge(territories.get(1), territories.get(4));
		graph.addEdge(territories.get(1), territories.get(2));
		
		graph.addEdge(territories.get(2), territories.get(1)); //Greenland
		graph.addEdge(territories.get(2), territories.get(4));
		graph.addEdge(territories.get(2), territories.get(5));
		graph.addEdge(territories.get(2), territories.get(37));
		
		graph.addEdge(territories.get(3), territories.get(0)); //Alberta
		graph.addEdge(territories.get(3), territories.get(1));
		graph.addEdge(territories.get(3), territories.get(4));
		graph.addEdge(territories.get(3), territories.get(6));
		
		graph.addEdge(territories.get(4), territories.get(1)); //Ontario
		graph.addEdge(territories.get(4), territories.get(3));
		graph.addEdge(territories.get(4), territories.get(6));
		graph.addEdge(territories.get(4), territories.get(7));
		graph.addEdge(territories.get(4), territories.get(5));
		graph.addEdge(territories.get(4), territories.get(2));
		
		graph.addEdge(territories.get(5), territories.get(4)); //Quebec
		graph.addEdge(territories.get(5), territories.get(7));
		graph.addEdge(territories.get(5), territories.get(2));
		
		graph.addEdge(territories.get(6), territories.get(3)); //Western United States
		graph.addEdge(territories.get(6), territories.get(4));
		graph.addEdge(territories.get(6), territories.get(7));
		graph.addEdge(territories.get(6), territories.get(8));
		
		graph.addEdge(territories.get(7), territories.get(6)); //Eastern United States
		graph.addEdge(territories.get(7), territories.get(4));
		graph.addEdge(territories.get(7), territories.get(5));
		graph.addEdge(territories.get(7), territories.get(8));
		
		graph.addEdge(territories.get(8), territories.get(6)); //Central America
		graph.addEdge(territories.get(8), territories.get(7));
		graph.addEdge(territories.get(8), territories.get(9));
		
		graph.addEdge(territories.get(9), territories.get(8)); //Venezuela
		graph.addEdge(territories.get(9), territories.get(10));
		graph.addEdge(territories.get(9), territories.get(11));
		
		graph.addEdge(territories.get(10), territories.get(9)); //Peru
		graph.addEdge(territories.get(10), territories.get(11));
		graph.addEdge(territories.get(10), territories.get(12));
		
		graph.addEdge(territories.get(11), territories.get(9)); //Brazil
		graph.addEdge(territories.get(11), territories.get(10));
		graph.addEdge(territories.get(11), territories.get(12));
		graph.addEdge(territories.get(11), territories.get(13));
		
		graph.addEdge(territories.get(12), territories.get(10)); //Argentina
		graph.addEdge(territories.get(12), territories.get(11));
		
		graph.addEdge(territories.get(13), territories.get(11)); //North Africa
		graph.addEdge(territories.get(13), territories.get(14));
		graph.addEdge(territories.get(13), territories.get(16));
		graph.addEdge(territories.get(13), territories.get(15));
		graph.addEdge(territories.get(13), territories.get(32));
		graph.addEdge(territories.get(13), territories.get(34));
		
		graph.addEdge(territories.get(14), territories.get(13)); //Egypt
		graph.addEdge(territories.get(14), territories.get(16)); 
		graph.addEdge(territories.get(14), territories.get(34)); 
		graph.addEdge(territories.get(14), territories.get(19)); 
		
		graph.addEdge(territories.get(15), territories.get(13)); //Congo
		graph.addEdge(territories.get(15), territories.get(17)); 
		graph.addEdge(territories.get(15), territories.get(16)); 
		
		graph.addEdge(territories.get(16), territories.get(13)); //East Africa
		graph.addEdge(territories.get(16), territories.get(14));
		graph.addEdge(territories.get(16), territories.get(15));
		graph.addEdge(territories.get(16), territories.get(17));
		
		graph.addEdge(territories.get(17), territories.get(15)); //South Africa
		graph.addEdge(territories.get(17), territories.get(16));
		graph.addEdge(territories.get(17), territories.get(18));
		
		graph.addEdge(territories.get(18), territories.get(16)); //Madagascar
		graph.addEdge(territories.get(18), territories.get(17));
		
		graph.addEdge(territories.get(19), territories.get(14)); //Middle East
		graph.addEdge(territories.get(19), territories.get(16));
		graph.addEdge(territories.get(19), territories.get(34));
		graph.addEdge(territories.get(19), territories.get(35));
		graph.addEdge(territories.get(19), territories.get(20));
		graph.addEdge(territories.get(19), territories.get(22));
		
		graph.addEdge(territories.get(20), territories.get(19)); //Afganistan
		graph.addEdge(territories.get(20), territories.get(22));
		graph.addEdge(territories.get(20), territories.get(21));
		graph.addEdge(territories.get(20), territories.get(23));
		graph.addEdge(territories.get(20), territories.get(35));
		
		graph.addEdge(territories.get(21), territories.get(35)); //Ural
		graph.addEdge(territories.get(21), territories.get(20));
		graph.addEdge(territories.get(21), territories.get(28));
		graph.addEdge(territories.get(21), territories.get(23));
		
		graph.addEdge(territories.get(22), territories.get(23)); //India
		graph.addEdge(territories.get(22), territories.get(19));
		graph.addEdge(territories.get(22), territories.get(20));
		graph.addEdge(territories.get(22), territories.get(24));
		
		graph.addEdge(territories.get(23), territories.get(22)); //China
		graph.addEdge(territories.get(23), territories.get(20));
		graph.addEdge(territories.get(23), territories.get(24));
		graph.addEdge(territories.get(23), territories.get(21));
		graph.addEdge(territories.get(23), territories.get(28));
		graph.addEdge(territories.get(23), territories.get(25));
		
		graph.addEdge(territories.get(24), territories.get(22)); //Siam
		graph.addEdge(territories.get(24), territories.get(23));
		graph.addEdge(territories.get(24), territories.get(40));
		
		graph.addEdge(territories.get(25), territories.get(23)); //Mongolia
		graph.addEdge(territories.get(25), territories.get(28));
		graph.addEdge(territories.get(25), territories.get(27));
		graph.addEdge(territories.get(25), territories.get(30));
		graph.addEdge(territories.get(25), territories.get(26));
		
		graph.addEdge(territories.get(26), territories.get(25)); //Japan
		graph.addEdge(territories.get(26), territories.get(30));
		
		graph.addEdge(territories.get(27), territories.get(28)); //Irkutsk
		graph.addEdge(territories.get(27), territories.get(25));
		graph.addEdge(territories.get(27), territories.get(29));
		graph.addEdge(territories.get(27), territories.get(30));
		
		graph.addEdge(territories.get(28), territories.get(21)); //Siberia
		graph.addEdge(territories.get(28), territories.get(23));
		graph.addEdge(territories.get(28), territories.get(29));
		graph.addEdge(territories.get(28), territories.get(27));
		graph.addEdge(territories.get(28), territories.get(25));
		
		graph.addEdge(territories.get(29), territories.get(30)); //Yakutsk
		graph.addEdge(territories.get(29), territories.get(28));
		graph.addEdge(territories.get(29), territories.get(27));
		
		graph.addEdge(territories.get(30), territories.get(29)); //Kamchatka
		graph.addEdge(territories.get(30), territories.get(27));
		graph.addEdge(territories.get(30), territories.get(25));
		graph.addEdge(territories.get(30), territories.get(26));
		graph.addEdge(territories.get(30), territories.get(0));
		
		graph.addEdge(territories.get(31), territories.get(37)); //Great Britain
		graph.addEdge(territories.get(31), territories.get(32));
		graph.addEdge(territories.get(31), territories.get(36));
		graph.addEdge(territories.get(31), territories.get(33));
		
		graph.addEdge(territories.get(32), territories.get(31)); //Western Europe
		graph.addEdge(territories.get(32), territories.get(33));
		graph.addEdge(territories.get(32), territories.get(34));
		graph.addEdge(territories.get(32), territories.get(13));
		
		graph.addEdge(territories.get(33), territories.get(31)); //Northern Europe
		graph.addEdge(territories.get(33), territories.get(32));
		graph.addEdge(territories.get(33), territories.get(34));
		graph.addEdge(territories.get(33), territories.get(35));
		graph.addEdge(territories.get(33), territories.get(36));
		
		graph.addEdge(territories.get(34), territories.get(33)); //Southern Europe
		graph.addEdge(territories.get(34), territories.get(32));
		graph.addEdge(territories.get(34), territories.get(35));
		graph.addEdge(territories.get(34), territories.get(19));
		graph.addEdge(territories.get(34), territories.get(13));
		graph.addEdge(territories.get(34), territories.get(14));
		
		graph.addEdge(territories.get(35), territories.get(36)); //Ukraine
		graph.addEdge(territories.get(35), territories.get(33));
		graph.addEdge(territories.get(35), territories.get(34));
		graph.addEdge(territories.get(35), territories.get(20));
		graph.addEdge(territories.get(35), territories.get(21));
		graph.addEdge(territories.get(35), territories.get(19));
		
		graph.addEdge(territories.get(36), territories.get(37)); //Scandinavia
		graph.addEdge(territories.get(36), territories.get(31));
		graph.addEdge(territories.get(36), territories.get(33));
		graph.addEdge(territories.get(36), territories.get(35));
		
		graph.addEdge(territories.get(37), territories.get(31)); //Iceland
		graph.addEdge(territories.get(37), territories.get(36));
		graph.addEdge(territories.get(37), territories.get(2));
		
		graph.addEdge(territories.get(38), territories.get(39)); //Western Australia
		graph.addEdge(territories.get(38), territories.get(40));
		graph.addEdge(territories.get(38), territories.get(41));
		
		graph.addEdge(territories.get(39), territories.get(41)); //East Australia
		graph.addEdge(territories.get(39), territories.get(38));
		
		graph.addEdge(territories.get(40), territories.get(24)); //Indonesia
		graph.addEdge(territories.get(40), territories.get(41));
		graph.addEdge(territories.get(40), territories.get(38));
		
		graph.addEdge(territories.get(41), territories.get(40)); //New Guinea
		graph.addEdge(territories.get(41), territories.get(38));
		graph.addEdge(territories.get(41), territories.get(39));
	}
	
	/**
	 * 
	 * @param territories All game territories
	 * @param index Index into territories to work from
	 * @param numTerrs Number of territories in this continent
	 * @param name Name of this continent
	 * @param contVal Control value of continent
	 */
	private void setContinent(ArrayList<Vertex> territories, int index, int numTerrs, String name, int contVal) {
		ArrayList<Territory> temp = new ArrayList<Territory>();
		
		for(int i=0; i< numTerrs; i++) {
			temp.add(territories.get(i+index).getTerritory());
		}
		continents.add(new Continent(name, temp , contVal));
	}
	
	public boolean checkMove(Vertex attacker, Vertex toAttack, Player player, int numDie) {
		ArrayList<Territory> borders = graph.adjacencyList(attacker);
		String plr = player.getName();
		Territory territory = attacker.getTerritory();
		
		if( territory.getNumUnits() <= numDie) {//Must have at least 1 more unit in territory than number die rolling
			displayError(DIE_ERROR);
			return false;
		}
		if(territory.getNumUnits() < 2) {//Attacking territory needs a minimum of two units 
			displayError(ATTACKING_UNITS_ERROR);
			return false;
		}
		if(toAttack.getTerritory().getNumUnits() < 2) {//Defending territory also needs a minimum of two units
			displayError(DEFENDING_UNITS_ERROR);
			return false;
		}
		if(!(plr.equalsIgnoreCase(attacker.getTerritory().getConquerer()))) {//Must attack from territory you own
			displayError(ATTACKING_ERROR);
			return false;
		}
		if(territory.getConquerer().equalsIgnoreCase(toAttack.getTerritory().getConquerer()) ||plr.equalsIgnoreCase(toAttack.getTerritory().getConquerer())) {//can't attack itself or other friendly territories
			displayError(DEFENDING_ERROR);
			return false;
		}
		if(!(borders.contains(toAttack.getTerritory()))) { //Territory attacking must be a bordering territory of territory attacking from	
			displayError(ADJACENCY_ERROR);
			return false;
		}
		return true;
	}
	
	private void displayError(int errorCode) {
		switch(errorCode) {
			case DIE_ERROR:
				System.out.println("You don't have enough units in this territory to roll this many die");
				break;
			case ATTACKING_ERROR:
				System.out.println("You can't attack from a territory you don't control");
				break;
			case DEFENDING_ERROR:
				System.out.println("You can't attack a territory you already control");
				break;
			case ATTACKING_UNITS_ERROR:
				System.out.println("You don't have enough units in this territory to attack from it(Min: 2)");
				break;
			case DEFENDING_UNITS_ERROR:
				System.out.println("Defender doesn't have enough units in their territory for you to attack it(Min: 2)");
				break;
			case ADJACENCY_ERROR:
				System.out.println("You can't attack a territory that's not adjacent to your invading territory");
				break;
		}
	}
	
	private int makeMove(Vertex attacker, Vertex toAttack, Player player, int numDie) {
		boolean success =false;
		Die[] aDie = new Die[numDie];
		Die[] dDie;
		int commandCode= MOVE_ERROR;
		int defendingDie=0;
		int aUnits = attacker.getTerritory().getNumUnits();
		int dUnits = toAttack.getTerritory().getNumUnits();
		
		success =checkMove(attacker, toAttack, player, numDie);
		if(success==false) 
			return commandCode;
		
		else {
			if(dUnits > 2 ) 
				defendingDie=2;
			else 
				defendingDie=1;
			
			dDie= new Die[defendingDie];
			
			do {
				int aMaxDie=0;
				int dMaxDie=0;
				for(int i=0; i < aDie.length; i++) {
					aDie[i] = new Die();
					aDie[i].rollDie();
					if(aDie[i].getVal()> aMaxDie)
						aMaxDie=aDie[i].getVal();
					
				}
				
			}while(aUnits > 2 && dUnits > 0);
		}
		
		return commandCode;
	}
	
	public ArrayList<Vertex> getTerritories() {
		return territories;
	}
	
	public ArrayList<Continent> getContinents() {
		return continents;
	}
	
	public static void main(String[] args) {
		Board gameBoard = new Board();
		Player p1 =new Player();
		p1.setName("Gary");
		gameBoard.getTerritories().get(3).getTerritory().setConquerer("Gary");
		gameBoard.getTerritories().get(3).getTerritory().setNumUnits(3);
		gameBoard.getTerritories().get(4).getTerritory().setNumUnits(3);
		gameBoard.getTerritories().get(4).getTerritory().setConquerer("Brad");
		
		gameBoard.checkMove(gameBoard.getTerritories().get(3), gameBoard.getTerritories().get(4), p1 , 2);
	}
}
