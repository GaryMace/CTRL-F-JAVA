package source;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	String[] territories = {"Western United States", "Alaska", "Japan", "Afganistan", "Iceland", "Alberta", "India", 
							"Argentina", "Western Europe", "Egypt", "Madagascar", "North Africa", "Irkutsk", "Eastern Australia",
							"New Guinea", "Central America", "China", "Kamchatka", "Greenland", "Northern Europe", "Congo", "Indonesia",
							"Southern Europe", "Great Britain", "Yakutsk", "Ural", "Ontario", "Peru",
							"Siam", "Eastern United States", "Western Australia", "Quebec", "Scandinavia", "Venezuela", "South Africa", 
							"Mongolia", "Ukraine", "Siberia", "Brazil", "East Africa", "Northwest Territory", "Middle East" };
	public static final int NUM_CARDS_PER_UNIT=14;
	ArrayList<Card> deck = new ArrayList<Card>();
	ArrayList<String> units = new ArrayList<String>();
	
	public Deck() {
		units.add("Infantry");
		units.add("Cavalry");
		units.add("Artillery");
		makeDeck();
	}
	private void makeDeck() {
		ArrayList<Card> temp = new ArrayList<Card>();
		Random rand = new Random();
		int pos=0;
		int random;
		
		for(int i=0; i< units.size();i++) {
			for(int j=0; j < NUM_CARDS_PER_UNIT;j++) {
				Card unitCard = new Card(units.get(i),territories[j+pos], false);
				temp.add(unitCard);
			}
			pos+=14;
		}
		
		//add the two wild cards
		temp.add(new Card("WILD_CARD", "NONE", true));
		temp.add(new Card("WILD_CARD", "NONE", true));
		
		//shuffle deck
		for(int i=0;  !(temp.isEmpty()); i++) {
			random = rand.nextInt(temp.size());
			deck.add(i, temp.get(random));
			temp.remove(random);
		}
	}
	public Card drawCard() {
		Card temp = deck.remove(0);
		return temp;
		
	}
	public int size() {
		return deck.size();
	}
	public boolean isEmpty() {
		return deck.size()==0;
	}
	public ArrayList<Card> getDeck() {
		return deck;
	}
}
