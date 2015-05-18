package source;

import java.util.ArrayList;

public class Continent {
	private String name;
	private int numTerritories;
	private int controlVal;
	private ArrayList<Territory> territories;
	
	public Continent(String name, ArrayList<Territory> territories, int controlVal) {
		this.name =name;
		this.controlVal = controlVal;
		this.numTerritories = territories.size();
		setTerritories(territories);
	}
	
	public String getName() {
		return name;
	}

	public int getNumTerritories() {
		return numTerritories;
	}

	public int getControlVal() {
		return controlVal;
	}

	public ArrayList<Territory> getTerritories() {
		return territories;
	}
	
	private void setTerritories(ArrayList<Territory> territories){
		this.territories = new ArrayList<Territory>(territories);
	}
}
