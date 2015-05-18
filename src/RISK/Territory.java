package source;

//define territories in board class later !
//TODO: add arraylist of strings of bordering territories
public class Territory {
	private String name;
	private String conqueredBy;
	private int numUnits;
	private int boardZone;

	public Territory(String name, int boardZone) {
		this.name = name;
		this.boardZone=boardZone;
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
}
