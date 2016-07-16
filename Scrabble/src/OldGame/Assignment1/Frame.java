/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment1;

import java.io.Serializable;
import java.util.ArrayList;

public class Frame implements Serializable{
	private ArrayList<Tile> myFrame;
	private ArrayList<Tile> testFrame;
	private Pool myPool;
	
	public Frame(Pool P) {
		myFrame = new ArrayList<Tile>();
		testFrame = new ArrayList<Tile>(myFrame);
		myPool = P;
		
		grabTiles();																	//only commented out for testing purposes, so we can give the frame exact tiles
		//setTestFrame();
		resetTestFrame();
	}	
	
	public Tile removeTile(char letter) {											 	//function which removes a tile at the desired position
		boolean validInput = false;
		Tile charFound=null;
		
		validInput= areLettersInFrame(letter);
		
		if(validInput==true){
			
			for(int i=0; i < myFrame.size();i++) {
				if(letter==myFrame.get(i).getTileChar()) {
					charFound=myFrame.remove(i);
					break;
				}
			}
		}
		else {
			charFound=null;
		}
		
		return charFound;	
	}
	
	
	
	public void grabTiles() { 														//refills the frame 
		int i;
		
		for(i=0; i < 7&& myFrame.size() <7; i++) {
			myFrame.add(myPool.pullTile());
		}
	}
	
	public boolean isFrameEmpty() { 												//checks if the frame is empty
			return myFrame.size() == 0;
		}
		
	
	public boolean areLettersInFrame(char userChar){ 								//searches for specific tiles in the frame
		boolean success = false;

		for(int n=0; n < testFrame.size();n++) {									//Does user have required words
			if(userChar == testFrame.get(n).getTileChar() ) {						//** Dont remove from main frame yet**
				testFrame.remove(n);
				success=true;
				break;
			}	
		}
		
		return success;
	}
	
	public void displayFrame(){														//displays the tiles in the frame one at a time
		int LetterCount;
		
		System.out.print("[");
		for(LetterCount = 0; LetterCount < myFrame.size(); LetterCount++){
			if(LetterCount == myFrame.size()-1) {
				System.out.print(myFrame.get(LetterCount).getTileChar());
				break;
			}
			System.out.print(myFrame.get(LetterCount).getTileChar()+" ,");
		}
		System.out.println("]");
	}
	
	public Tile remove(int i) { //removes desired tile
		myFrame.remove(i);
		
		return null;
	}
	
	
	public Frame getFrame(){ //passes the frame 
		return this;
	}
	
	public ArrayList<Tile> getList(){ 
		return myFrame;
	}
	
	public ArrayList<Tile> removeFromRealFrame(String userWord) {						//remove letters from frame ONLY if the input is valid
		ArrayList<Tile> myTiles =new ArrayList<Tile>();
		for(int k=0; k < userWord.length(); k++) {
			char charAtIndex = userWord.charAt(k);
			
			for(int n=0; n < myFrame.size();n++) {								
				if(charAtIndex == myFrame.get(n).getTileChar() ) {						
					myTiles.add(myFrame.get(n));
					myFrame.remove(n);
					break;
				}	
			}
		}
		return myTiles;
	}
	public void resetTestFrame() {
		testFrame = new ArrayList<Tile>(myFrame);
	}
}
