package source;

public class Card {
	private boolean isWildCard;
	private String territory;
	private String unit;
	
	/**
	 * 
	 * @param unit	Unit corresponding to card
	 * @param territory	Territory corresponding to card
	 * @param isWildCard	Is it a wild card?
	 */
	public Card(String unit, String territory, boolean isWildCard) {
		this.unit=unit;
		this.territory=territory;
		this.isWildCard=isWildCard;
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
