/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment1;

public class PlayerTest {

	public static void main(String[] args) {
		int j,i=0;
		Pool game = new Pool();
		Tile[] myFrame = new Tile[7];
		Player p1 = new Player();
		Frame p1Frame = new Frame(game);
		
////////////////////////////////////////////////////////////////////////////////////
//				      	Test on Gary's code									      //
////////////////////////////////////////////////////////////////////////////////////
		for(j=0; j< 7; j++) {
			myFrame[j] = game.pullTile();
		}
		
		System.out.println("==================================");
		System.out.println("==        Test Pool Class       ==");
		System.out.println("==================================");
		
		for(i=0; i < myFrame.length; i++) {
			System.out.println("==    Tile "+(i+1)+": "+myFrame[i].getTileChar()+ "      Value: "+ game.tileValue(myFrame[i])+"   ==");
		}
		
		System.out.println("==    Pool Empty:    "+ game.isPoolEmpty()+"      ==");
		System.out.println("==================================");
		System.out.println("== Tiles Remaining in Pool: "+ game.poolSize()+"  ==");
		System.out.println("==================================");
		game.resetPool();
		System.out.println("Tiles Remaining in Pool after reset: " + game.poolSize());    
		
////////////////////////////////////////////////////////////////////////////////////
//							Test on Shammy's code								  //
////////////////////////////////////////////////////////////////////////////////////
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("=====================");
		System.out.println("==Test Frame Class ==");
		System.out.println("=====================");
		System.out.println();
		System.out.println("==================================");
		System.out.println("==    Frame pulled from Pool    ==");
		System.out.println("==================================");
		p1Frame.grabTiles();
		p1Frame.displayFrame();
		System.out.println();
		System.out.println("Removal of tile at index 1");
		p1Frame.remove(1);
		System.out.println();
		
		p1Frame.displayFrame();
		
		System.out.println();
		System.out.println();
		//p1Frame.areLettersInFrame();
		
////////////////////////////////////////////////////////////////////////////////////
//							Test on Ben's code									  //
////////////////////////////////////////////////////////////////////////////////////
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("======================");
		System.out.println("==Test Player Class ==");
		System.out.println("======================");
		p1.setName(null);
		System.out.println();
		System.out.println();
		System.out.println("=====================");
		System.out.println("==Test Player Class==");
		System.out.println("=====================");
		System.out.println("==      " +p1.displayName()+ "       ==");	
		System.out.println("=====================");

		p1.scoreIncrease(150);
		p1.scoreView();
		p1.accessPlayerFrame(p1Frame);
		System.out.print("Frame now has: ");
		p1Frame.displayFrame();
		p1.dataReset();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		

	}
}
