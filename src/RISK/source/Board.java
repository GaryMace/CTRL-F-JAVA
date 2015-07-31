package source;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

public class Board {
	private ArrayList<Continent> continents;
	public static final int NUM_CONTINENTS=6;
	
	public Board() {
		continents = new ArrayList<Continent>();
		
		//makes 6 continents containing their territories and each territories border territories
		setContinents();
	}
	
	/**
	 * Core Method to game creation involving continent set-up and territories within continents set-up.
	 * Also assigns each territory(all 42) all it's bordering territories(Lot's of chump code)
	 */
	private void setContinents() {
		Graph graph = new Graph();	
		Vertex[] territories  = new Vertex[42];
		ArrayList<Continent> continents = new ArrayList<Continent>();
			
		//N.America
		territories[0] = new Vertex(new Territory("Alaska", 0));
		territories[1] = new Vertex(new Territory("Northwest Territory", 0));
		territories[2] = new Vertex(new Territory("Greenland", 0));
		territories[3] = new Vertex(new Territory("Alberta", 0));
		territories[4] = new Vertex(new Territory("Ontario", 0));
		territories[5] = new Vertex(new Territory("Quebec", 0));
		territories[6] = new Vertex(new Territory("Western United States", 0));
		territories[7] = new Vertex(new Territory("Eastern United States", 0));
		territories[8] = new Vertex(new Territory("Central America", 0));
		setContinent(territories, 0, 9, "North America", 0);
		
		//S.America
		territories[9] = new Vertex(new Territory("Venezuela", 1));
		territories[10] = new Vertex(new Territory("Peru", 1));
		territories[11] = new Vertex(new Territory("Brazil", 1));
		territories[12] = new Vertex(new Territory("Argentina", 1));
		setContinent(territories, 9, 4, "South America", 0);
		
		//Africa
		territories[13] = new Vertex(new Territory("North Africa", 2));
		territories[14] = new Vertex(new Territory("Egypt", 2));
		territories[15] = new Vertex(new Territory("Congo", 2));
		territories[16] = new Vertex(new Territory("East Africa", 2));
		territories[17] = new Vertex(new Territory("South Africa", 2));
		territories[18] = new Vertex(new Territory("Madagascar", 2));
		setContinent(territories, 13, 6, "Africa", 0);
		
		//Asia
		territories[19] = new Vertex(new Territory("Middle East", 3));
		territories[20] = new Vertex(new Territory("Afghanistan", 3));
		territories[21] = new Vertex(new Territory("Ural", 3));
		territories[22] = new Vertex(new Territory("India", 3));
		territories[23] = new Vertex(new Territory("China", 3));
		territories[24] = new Vertex(new Territory("Siam", 3));
		territories[25] = new Vertex(new Territory("Mongolia", 3));
		territories[26] = new Vertex(new Territory("Japan", 3));
		territories[27] = new Vertex(new Territory("Irkutsk", 3));
		territories[28] = new Vertex(new Territory("Siberia", 3));
		territories[29] = new Vertex(new Territory("Yakutsk", 3));
		territories[30] = new Vertex(new Territory("Kamchatka", 3));
		setContinent(territories, 19, 11, "Asia", 0);
		
		//Europe
		territories[31] = new Vertex(new Territory("Great Britain", 4));
		territories[32] = new Vertex(new Territory("Western Europe", 4));
		territories[33] = new Vertex(new Territory("Northern Europe", 4));
		territories[34] = new Vertex(new Territory("Southern Europe", 4));
		territories[35] = new Vertex(new Territory("Ukriane", 4));
		territories[36] = new Vertex(new Territory("Scandinavia", 4));
		territories[37] = new Vertex(new Territory("Iceland", 4));
		setContinent(territories, 31, 7, "Europe", 0);
		
		//Australia
		territories[38] = new Vertex(new Territory("Western Australia", 5));
		territories[39] = new Vertex(new Territory("Eastern Australia", 5));
		territories[40] = new Vertex(new Territory("Indonesia", 5));
		territories[41] = new Vertex(new Territory("New Guinea", 5));
		setContinent(territories, 38, 4, "Australia", 0);
		
		//Add the territories to the graph 
		for(int i=0; i < 42; i++) {
			graph.addVertex(territories[i], true);
		}
		
		graph.addEdge(territories[0], territories[1]);	//Alaska
		graph.addEdge(territories[0], territories[3]);
		graph.addEdge(territories[0], territories[30]);
		
		graph.addEdge(territories[1], territories[0]);	//Northwest Territory
		graph.addEdge(territories[1], territories[3]);
		graph.addEdge(territories[1], territories[4]);
		graph.addEdge(territories[1], territories[2]);
		
		graph.addEdge(territories[2], territories[1]); //Greenland
		graph.addEdge(territories[2], territories[4]);
		graph.addEdge(territories[2], territories[5]);
		graph.addEdge(territories[2], territories[37]);
		
		graph.addEdge(territories[3], territories[0]); //Alberta
		graph.addEdge(territories[3], territories[1]);
		graph.addEdge(territories[3], territories[4]);
		graph.addEdge(territories[3], territories[6]);
		
		graph.addEdge(territories[4], territories[1]); //Ontario
		graph.addEdge(territories[4], territories[3]);
		graph.addEdge(territories[4], territories[6]);
		graph.addEdge(territories[4], territories[7]);
		graph.addEdge(territories[4], territories[5]);
		graph.addEdge(territories[4], territories[2]);
		
		graph.addEdge(territories[5], territories[4]); //Quebec
		graph.addEdge(territories[5], territories[7]);
		graph.addEdge(territories[5], territories[2]);
		
		graph.addEdge(territories[6], territories[3]); //Western United States
		graph.addEdge(territories[6], territories[4]);
		graph.addEdge(territories[6], territories[7]);
		graph.addEdge(territories[6], territories[8]);
		
		graph.addEdge(territories[7], territories[6]); //Eastern United States
		graph.addEdge(territories[7], territories[4]);
		graph.addEdge(territories[7], territories[5]);
		graph.addEdge(territories[7], territories[8]);
		
		graph.addEdge(territories[8], territories[6]); //Central America
		graph.addEdge(territories[8], territories[7]);
		graph.addEdge(territories[8], territories[9]);
		
		graph.addEdge(territories[9], territories[8]); //Venezuela
		graph.addEdge(territories[9], territories[10]);
		graph.addEdge(territories[9], territories[11]);
		
		graph.addEdge(territories[10], territories[9]); //Peru
		graph.addEdge(territories[10], territories[11]);
		graph.addEdge(territories[10], territories[12]);
		
		graph.addEdge(territories[11], territories[9]); //Brazil
		graph.addEdge(territories[11], territories[10]);
		graph.addEdge(territories[11], territories[12]);
		graph.addEdge(territories[11], territories[13]);
		
		graph.addEdge(territories[12], territories[10]); //Argentina
		graph.addEdge(territories[12], territories[11]);
		
		graph.addEdge(territories[13], territories[11]); //North Africa
		graph.addEdge(territories[13], territories[14]);
		graph.addEdge(territories[13], territories[16]);
		graph.addEdge(territories[13], territories[15]);
		graph.addEdge(territories[13], territories[32]);
		graph.addEdge(territories[13], territories[34]);
		
		graph.addEdge(territories[14], territories[13]); //Egypt
		graph.addEdge(territories[14], territories[16]); 
		graph.addEdge(territories[14], territories[34]); 
		graph.addEdge(territories[14], territories[19]); 
		
		graph.addEdge(territories[15], territories[13]); //Congo
		graph.addEdge(territories[15], territories[17]); 
		graph.addEdge(territories[15], territories[16]); 
		
		graph.addEdge(territories[16], territories[13]); //East Africa
		graph.addEdge(territories[16], territories[14]);
		graph.addEdge(territories[16], territories[15]);
		graph.addEdge(territories[16], territories[17]);
		
		graph.addEdge(territories[17], territories[15]); //South Africa
		graph.addEdge(territories[17], territories[16]);
		graph.addEdge(territories[17], territories[18]);
		
		graph.addEdge(territories[18], territories[16]); //Madagascar
		graph.addEdge(territories[18], territories[17]);
		
		graph.addEdge(territories[19], territories[14]); //Middle East
		graph.addEdge(territories[19], territories[16]);
		graph.addEdge(territories[19], territories[34]);
		graph.addEdge(territories[19], territories[35]);
		graph.addEdge(territories[19], territories[20]);
		graph.addEdge(territories[19], territories[22]);
		
		graph.addEdge(territories[20], territories[19]); //Afganistan
		graph.addEdge(territories[20], territories[22]);
		graph.addEdge(territories[20], territories[21]);
		graph.addEdge(territories[20], territories[23]);
		graph.addEdge(territories[20], territories[35]);
		
		graph.addEdge(territories[21], territories[35]); //Ural
		graph.addEdge(territories[21], territories[20]);
		graph.addEdge(territories[21], territories[28]);
		graph.addEdge(territories[21], territories[23]);
		
		graph.addEdge(territories[22], territories[23]); //India
		graph.addEdge(territories[22], territories[19]);
		graph.addEdge(territories[22], territories[20]);
		graph.addEdge(territories[22], territories[24]);
		
		graph.addEdge(territories[23], territories[22]); //China
		graph.addEdge(territories[23], territories[20]);
		graph.addEdge(territories[23], territories[24]);
		graph.addEdge(territories[23], territories[21]);
		graph.addEdge(territories[23], territories[28]);
		graph.addEdge(territories[23], territories[25]);
		
		graph.addEdge(territories[24], territories[22]); //Siam
		graph.addEdge(territories[24], territories[23]);
		graph.addEdge(territories[24], territories[40]);
		
		graph.addEdge(territories[25], territories[23]); //Mongolia
		graph.addEdge(territories[25], territories[28]);
		graph.addEdge(territories[25], territories[27]);
		graph.addEdge(territories[25], territories[30]);
		graph.addEdge(territories[25], territories[26]);
		
		graph.addEdge(territories[26], territories[25]); //Japan
		graph.addEdge(territories[26], territories[30]);
		
		graph.addEdge(territories[27], territories[28]); //Irkutsk
		graph.addEdge(territories[27], territories[25]);
		graph.addEdge(territories[27], territories[29]);
		graph.addEdge(territories[27], territories[30]);
		
		graph.addEdge(territories[28], territories[21]); //Siberia
		graph.addEdge(territories[28], territories[23]);
		graph.addEdge(territories[28], territories[29]);
		graph.addEdge(territories[28], territories[27]);
		graph.addEdge(territories[28], territories[25]);
		
		graph.addEdge(territories[29], territories[30]); //Yakutsk
		graph.addEdge(territories[29], territories[28]);
		graph.addEdge(territories[29], territories[27]);
		
		graph.addEdge(territories[30], territories[29]); //Kamchatka
		graph.addEdge(territories[30], territories[27]);
		graph.addEdge(territories[30], territories[25]);
		graph.addEdge(territories[30], territories[26]);
		graph.addEdge(territories[30], territories[0]);
		
		graph.addEdge(territories[31], territories[37]); //Great Britain
		graph.addEdge(territories[31], territories[32]);
		graph.addEdge(territories[31], territories[36]);
		graph.addEdge(territories[31], territories[33]);
		
		graph.addEdge(territories[32], territories[31]); //Western Europe
		graph.addEdge(territories[32], territories[33]);
		graph.addEdge(territories[32], territories[34]);
		graph.addEdge(territories[32], territories[13]);
		
		graph.addEdge(territories[33], territories[31]); //Northern Europe
		graph.addEdge(territories[33], territories[32]);
		graph.addEdge(territories[33], territories[34]);
		graph.addEdge(territories[33], territories[35]);
		graph.addEdge(territories[33], territories[36]);
		
		graph.addEdge(territories[34], territories[33]); //Southern Europe
		graph.addEdge(territories[34], territories[32]);
		graph.addEdge(territories[34], territories[35]);
		graph.addEdge(territories[34], territories[19]);
		graph.addEdge(territories[34], territories[13]);
		graph.addEdge(territories[34], territories[14]);
		
		graph.addEdge(territories[35], territories[36]); //Ukraine
		graph.addEdge(territories[35], territories[33]);
		graph.addEdge(territories[35], territories[34]);
		graph.addEdge(territories[35], territories[20]);
		graph.addEdge(territories[35], territories[21]);
		graph.addEdge(territories[35], territories[19]);
		
		graph.addEdge(territories[36], territories[37]); //Scandinavia
		graph.addEdge(territories[36], territories[31]);
		graph.addEdge(territories[36], territories[33]);
		graph.addEdge(territories[36], territories[35]);
		
		graph.addEdge(territories[37], territories[31]); //Iceland
		graph.addEdge(territories[37], territories[36]);
		graph.addEdge(territories[37], territories[2]);
		
		graph.addEdge(territories[38], territories[39]); //Western Australia
		graph.addEdge(territories[38], territories[40]);
		graph.addEdge(territories[38], territories[41]);
		
		graph.addEdge(territories[39], territories[41]); //East Australia
		graph.addEdge(territories[39], territories[38]);
		
		graph.addEdge(territories[40], territories[24]); //Indonesia
		graph.addEdge(territories[40], territories[41]);
		graph.addEdge(territories[40], territories[38]);
		
		graph.addEdge(territories[41], territories[40]); //New Guinea
		graph.addEdge(territories[41], territories[38]);
		graph.addEdge(territories[41], territories[39]);
	}
	
	/**
	 * 
	 * @param territories All game territories
	 * @param index Index into territories to work from
	 * @param numTerrs Num territories in this continent
	 * @param name Name of this continent
	 * @param contVal Control value of continent
	 */
	private void setContinent(Vertex[] territories, int index, int numTerrs, String name, int contVal) {
		ArrayList<Territory> temp = new ArrayList<Territory>();
		
		for(int i=0; i< numTerrs; i++) {
			temp.add(territories[i+index].getTerritory());
		}
		continents.add(new Continent(name, temp , contVal));
	}
	
	public boolean checkMove(Territory attacker, Territory toAttack, Player player) {
		boolean result =false;
		String plr = player.getName();
		
		if(attacker.isBorder(toAttack)) 
			return true;
		
		
		return result;
	}
}
