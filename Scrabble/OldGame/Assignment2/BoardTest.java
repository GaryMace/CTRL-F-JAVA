/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment2;

import OldGame.Assignment1.Frame;
import OldGame.Assignment1.Player;
import OldGame.Assignment1.Pool;
import OldGame.Assignment1.Tile;


public class BoardTest {
	static Tile[] myFrame;
	static Pool game;
	static Player p1;
	static Frame p1Frame;
	static Board myBoard;
	
	public static void main(String[] args) {
		game = new Pool();
		myFrame = new Tile[7];
		p1 = new Player();
		p1Frame = new Frame(game);
		myBoard = new Board();				//methods take in frame
		myBoard.grabPlayerFrame(p1Frame);
	
		p1Frame.displayFrame();
		/*myBoard.placeWord(4, 4, "Across", "car");					//to check first word at [8,8]
		myBoard.placeWord(8, 8, "Across", "car");
		myBoard.placeWord(9, 7, "Down", "patz");					//this isn't supposed to print but sometimes 'z' is randomly put into the frame so it is placed
		myBoard.placeWord(-1, -5, "Down", "pat");					//Check index's out of bounds
		myBoard.placeWord(8, 8, "Down", "cat");	
		myBoard.placeWord(8, 8, "Down", "cat");						//check overlaying words
		myBoard.placeWord(1, 1, "Across", "op");					//check surrounding tiles for valid word placement
		myBoard.placeWord(9, 10, "Down", "op");						//check bordering tiles operation*/
		
		System.out.println();
		System.out.println();
		myBoard.reset();
		System.out.println("***Reset the Board***");
		System.out.println();
		//myBoard.placeWord(8, 8, "Down","patac");
		System.out.println("Square value at [8, 8] :"+myBoard.getSquareValue(8,8));			//should return 4!!
		System.out.println("Square value at [9, 10] :"+myBoard.getSquareValue(9,10));		//should return 1!!
	}
}
