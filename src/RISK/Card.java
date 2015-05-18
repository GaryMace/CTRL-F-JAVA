package source;

public class Card {
	private boolean isWildCard;
	private String territory;
	private String unit;
	
	public void setCard(boolean isWildCard) {
		this.isWildCard= isWildCard;
	}
	
	public void setTerritory(String territory) {
		this.territory=territory;
	}
	
	public void setUnit(String unit) {
		this.unit= unit;
	}
	
	public boolean isWildCard() {
		return isWildCard;
	}
	public String getTerritory() {
		return territory;
	}
	
	public String getUnit() {
		return unit;
	}
}
