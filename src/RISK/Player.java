package source;

import java.util.ArrayList;

public class Player {
	private String name;
	private int numTerritories;
	private int totalNumUnits;
	private ArrayList<Card> pCards= new ArrayList<Card>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumTerritories() {
		return numTerritories;
	}
	
	public void setNumTerritories(int numTerritories) {
		this.numTerritories = numTerritories;
	}
	
	public int getTotalNumUnits() {
		return totalNumUnits;
	}
	
	public void setTotalNumUnits(int totalNumUnits) {
		this.totalNumUnits = totalNumUnits;
	}
	
	public void addCard(Card card) {
		pCards.add(card);
	}
	
	public ArrayList<Card> getCards() {
		return pCards;
	}
	
	public int numCards() {
		return pCards.size();
	}
}
