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
		int arrayListIndex = 0;
		int offsetIntoArrayList = 0;
		
		//Three parallel arrays
		int[] continentSizes =       {9,4,6,11,7,4};
		int[] continentControlVals = {5,2,3,7, 5,2};
		String[] continentNames =    {"North America", "South America", "Africa", "Asia", "Europe", "Australia"};
		
		String[] terrNames = { 
				"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec", "Western United States",
				"Eastern United States", "Central America", "Venezuela", "Peru", "Brazil", "Argentina", "North Africa",
				"Egypt", "Congo", "East Africa", "South Africa", "Madagascar", "Middle East", "Afghanistan", "Ural", "India",
				"China", "Siam", "Mongolia", "Japan", "Irkutsk", "Siberia", "Yakutsk", "Kamchatka", "Great Britain", "Western Europe",
				"Northern Europe", "Southern Europe", "Ukraine", "Scandinavia", "Iceland", "Western Australia", "Eastern Australia",
				"Indonesia", "New Guinea"
		};
		
		for(int i=0; i < 6; i++) {
			for(int j=0; j < continentSizes[i]; j++) {
				territories.add(arrayListIndex, new Vertex(new Territory(terrNames[arrayListIndex], i)) );
				arrayListIndex++;
			}
			//Create continent with (a name, num terrs in continent, control value) 
			setContinent(territories, offsetIntoArrayList, continentSizes[i], continentNames[i], continentControlVals[i]);
			offsetIntoArrayList += continentSizes[i];
		}
		
		//Add the territories to the graph 
		graph = new Graph(territories);
		
		//2-D array storing territories adjacent to territory at index i
		int[][] edges = { 
				{1,3,30}, {0,3,4,2}, {1,4,5,37}, {0,1,4,6}, {1,3,6,7,5,2}, {4,7,2}, {3,4,7,8}, {6,4,5,8}, {6,7,9}, 
				{8,10,11}, {9,11,12}, {9,10,12,13}, {10,11}, {11,14,16,15,32,34}, {13,16,34,19}, {13,17,16}, {13,14,15,17}, 
				{15,16,18}, {16,17}, {14,16,34,35,20,22}, {19,22,21,23,35}, {35,20,28,23}, {23,19,20,24}, {22,20,24,21,28,25},
				{22,23,40}, {23,28,27,30,26}, {25,30}, {28,25,29,30}, {21,23,29,27,25}, {30,28,27}, {29,27,25,26,0}, {37,32,36,33}, 
				{31,33,34,13}, {31,32,34,35,36}, {33,32,35,19,13,14}, {36,33,34,20,21,19}, {37,31,33,35}, {31,36,2}, {39,40,41}, 
				{41,38}, {24,41,38}, {40,38,39}
		};
		for(int i=0; i < edges.length; i++) {
			for(int j=0; j < edges[i].length; j++){
				graph.addEdge(territories.get(i), territories.get(edges[i][j]));
			}
		}
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
	
	public boolean checkMove(Vertex attacker, Vertex toAttack, Player player, int numUnits) {
		ArrayList<Territory> borders = graph.adjacencyList(attacker);
		String plr = player.getName();
		Territory territory = attacker.getTerritory();
		
		if( territory.getNumUnits()-numUnits > 2) { //Must have more than 2 units in attacking territory at all times
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
	/**
	 * 
	 * @param errorCode Displays error message based on which error has been triggered
	 */
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
	/**
	 * 
	 * @param attacker Vertex attacking from
	 * @param toAttack Vertex being defended
	 * @param player Player who is attacking
	 * @param numUnits Number units player wants to invade with
	 * @return returns whether or not invasion was successful
	 */
	public int makeMove(Vertex attacker, Vertex toAttack, Player player, int numUnits) {
		boolean success =false;
		Die[] aDie;
		Die[] dDie;
		int commandCode= MOVE_ERROR;
		int defendingDie=0;
		int aUnits = numUnits;
		int dUnits = toAttack.getTerritory().getNumUnits();
		int aControl=0, dControl=0;
		
		success =checkMove(attacker, toAttack, player, numUnits);
		if(success==false) 
			return commandCode;
		
		else {
			if(dUnits >= 2 ) 
				defendingDie=2;
			else 
				defendingDie=1;
			
			dDie= new Die[defendingDie];
			
			if(numUnits > 3 ) 
				aDie = new Die[3];
			else
				aDie = new Die[2];
			
			do {
				int[] aMaxDie= {0,0};
				int[] dMaxDie= {0,0};
				int temp=0;
				
				if(aUnits == 3) 	//Controls how many dice can be rolled based on number units attacker has left
					aControl=1;
				else if(aUnits <= 2) 
					aControl=2;
				
				if(dUnits < 2)		//Controls how many dice can be rolled based on number units defender has left
					dControl=1;
			
				for(int i=0; i < aDie.length -aControl; i++) {		//Rolls dice and obtains largest values of each roll and puts them in arrays
					aDie[i] = new Die();
					aDie[i].rollDie();
					if(aDie[i].getVal()> aMaxDie[0]) {
						temp= aMaxDie[0];
						aMaxDie[0]=aDie[i].getVal();
						aMaxDie[1]=temp;
					}
				}
				temp=0;
				
				for(int j=0; j < dDie.length -dControl; j++) {
					dDie[j] = new Die();
					dDie[j].rollDie();
					if(dDie[j].getVal() > dMaxDie[0]) {
						temp= dMaxDie[0];
						dMaxDie[0]=dDie[j].getVal();
						dMaxDie[1]=temp;
					}
				}
				
				//Updates units left based on die values
				if(aMaxDie[0] > dMaxDie[0]) 
					dUnits--;
				else
					aUnits--;
				
				if(aMaxDie[1] > dMaxDie[1] && (aMaxDie[1] >0 && dMaxDie[1] >0))		//Did we use a second die?
					dUnits--;
				else if(aMaxDie[1] < dMaxDie[1] && (aMaxDie[1] >0 && dMaxDie[1] >0))
					aUnits--;
				
			}while(aUnits > 0  && dUnits > 0);	//Continue this process until either the territory is successfully invaded or the attack fails
		
		if(aUnits == 0)
			commandCode = MOVE_ERROR;
		
		else if(dUnits == 0) 
			commandCode = MOVE_SUCCESS;
		
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
