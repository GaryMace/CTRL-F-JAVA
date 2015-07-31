package source;

import java.util.ArrayList;

//define territories in board class later !
public class Territory {
	private String name;
	private String conqueredBy;
	private int numUnits;
	private int boardZone;
	//private ArrayList<String> borders;
	
	/**
	 * 	
	 * @param name	Territory Name
	 * @param boardZone	Continent territory is in
	 */
	public Territory(String name, int boardZone) {
		this.name = name;
		this.boardZone=boardZone;
		//this.borders = new ArrayList<String>(bordering);
	}
	//N.America = 0, S.America = 1, Africa = 2, Asia = 3, Europe = 4, Australia = 5.
	public int getBoardZone() {
		return boardZone;
	}

	public void setConquerer(String conqueredBy) {
		this.conqueredBy = conqueredBy;
	}
	
	public void setNumUnits(int numUnits) {
		this.numUnits = numUnits;
	}
	
	public String getConquerer() {
		return conqueredBy;
	}
	
	public int getNumUnits() {
		return numUnits;
	}
	
	public String getName() {
		return name;
	}
	
	/*public ArrayList<String> getBorders(){
		return borders;
	} */
	
	//Re-design
	
	/*public boolean isBorder(Territory toCheck) {
		boolean result=false;
		for(int i=0; i < borders.size(); i++) {
			if(toCheck.getName().equalsIgnoreCase(borders.get(i))) {
				result=true;
				break;
			}
		}
		
		
		return result;
	} */
}
