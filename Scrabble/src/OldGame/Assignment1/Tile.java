/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment1;

import java.io.Serializable;

public class Tile implements Serializable {
	private int tileValue, quantity;
	private char tileChar;

	/**
	 * Create new tile with the following values:
	 * @param quan - quantity of letter
	 * @param i - value for letter
	 * @param letter - letter on tile
	 */
	public Tile(int quan, int i, char letter) {				//Just for data hiding, also allows some memory saving with the quantity of each tile
		tileChar = letter;
		tileValue = i;
		quantity = quan;
	}

	public int getTileValue() {
		return tileValue;
	}

	/**
	 * 
	 * @return - returnds bob
	 */
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {				//Needed for resetting tile quantity when picked out of pool
		this.quantity = quantity;
	}

	public char getTileChar() {
		return tileChar;
	}
	
	public void setValue(int tileValue){
		this.tileValue = tileValue;
	}

	public void setTileChar(char tileChar) {
		this.tileChar =tileChar;
	}

}
