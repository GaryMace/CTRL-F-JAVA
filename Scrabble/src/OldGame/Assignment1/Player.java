/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment1;

import java.io.Serializable;
import java.util.Scanner;

public class Player implements Serializable{
	
	private String name;						
	private int Score;
	private Frame pFrame;
	private int passCount;
	/***********************************************************/
	public Player(){
		name = new String();
		Score = 0;
		passCount = 0;
	}
	/***********************************************************/
	
	public void dataReset() {
		name = "";
		Score = 0;
	}
	
	
	public void setName(String name) {
		this.name =name;
	}
	
	public int scoreIncrease(int Points) {
		Score = Score + Points;
		return Score;
	}
	
	public int scoreView() {
		return Score;			//just return the score, when we call the method we can stick a JOptionPane in. 
	}										

	public Frame accessPlayerFrame(Frame pFrame) {
		this.pFrame = pFrame;
		return this.pFrame;
	}												
	
	public  String displayName() {
		return name;	
	}
	
	public void setPassCount(int increase) {
		passCount = passCount + increase;
	}	
	
	public int getpassCount() {
		return passCount;
	}

}
