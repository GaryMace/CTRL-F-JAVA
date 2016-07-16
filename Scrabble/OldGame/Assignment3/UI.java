/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import OldGame.Assignment1.Frame;
import OldGame.Assignment1.Player;
import OldGame.Assignment1.Pool;
import OldGame.Assignment1.Tile;
import OldGame.Assignment2.Board;
import OldGame.Assignment4.ObjectCloner;

public class UI implements Serializable{
	List<String> token;
	boolean skipMove;
	boolean quitGame;
	boolean validInput;
	String prevWord;								//This is used so we can check if the previous word inputted is in the dictionary
	ArrayList<String> prevList;
	int challengeCount;								// if it's a turn where the user challenges first and then makes a move we want the previous board to be the challenge-reverted one
	
	public UI () {			
		prevWord="";
		token = new ArrayList<String>();
		skipMove=false;
		validInput=false;
		welcome();		
	}
	
	private void welcome() {
		System.out.println("===================================================");
		System.out.println("==              Welcome to Scrabble              ==");
		System.out.println("===================================================");
		System.out.println();
		System.out.println("====              User Commands                ====");
		System.out.println("==  Quit: Quit current scrabble game             ==");
		System.out.println("==  Help: Display move Format                    ==");
		System.out.println("==  Pass: Pass your current move                 ==");
		System.out.println("==  Exchange <a,b>: Exchange some tiles in frame ==");
		System.out.println("==  Move: <x, y>, <across/down>, <word>          ==");
		System.out.println("===================================================");
	}
	
	private boolean inputOptions(int id, int moveIndex, List<String> userInput, Frame tempFrame, Frame otherFrame, Player currentPlayer, Player otherPlayer, Scrabble runner, Board board, UI gameInterface, ArrayList<String> gameDictionary, Pool gamePool, ObjectCloner myCloner) throws Exception{
		ArrayList<String> token = new ArrayList<String>(userInput);
		boolean keepPrompting = true;
		validInput=false;
		skipMove=false;
		
		//check if first,second position in user input is an integer and that the move is of the right size
		try{ 
			if(token.size() >1 && (Integer)Integer.parseInt(token.get(0)) instanceof Integer && (Integer)Integer.parseInt(token.get(1)) instanceof Integer ){
				setPrevWordInput(userInput.get(3));
				validInput=true;
				challengeCount=0;
				if(userInput.size()<4) return keepPrompting=true;
				
				//check to see if the input is a valid move, if it's not then keepPrompting them for an input
				keepPrompting = runner.makeMove(id, gameInterface, board, moveIndex, tempFrame, otherFrame, currentPlayer, otherPlayer, gamePool, myCloner);
				if(currentPlayer.getpassCount() >0 ) {
					currentPlayer.setPassCount(-(currentPlayer.getpassCount() ));
				}
				return !(keepPrompting);
			}
			
		}catch(NumberFormatException e){ }
		
		if(token.get(0).equalsIgnoreCase("QUIT") || token.get(0).equalsIgnoreCase("q")){
			int endScore = 0;
			
			//Subtract current frame tilescore from the user who quit
			for(int i=0; i< tempFrame.getList().size(); i++) {
            	endScore = endScore + tempFrame.getList().get(i).getTileValue();
            }
            currentPlayer.scoreIncrease(-endScore);
            System.out.println("======================================");
			System.out.println("== Exiting Game: Thanks for Playing ==");		//display results
			runner.quitGame(currentPlayer, otherPlayer);
			System.out.println("======================================");
			System.exit(0);
		}
			
		else if(token.get(0).equalsIgnoreCase("HELP") || token.get(0).equalsIgnoreCase("h")){
			System.out.println();
			System.out.println("======================================================================== ");
			System.out.println("== The input format is as follows: <x , y> , <across/down> , <word>   == ");
			System.out.println("======================================================================== ");
			System.out.println();
			keepPrompting = true;
		}
			
		else if(token.get(0).equalsIgnoreCase("PASS") || token.get(0).equalsIgnoreCase("p")){
			keepPrompting=false;
			currentPlayer.setPassCount(1);
			
			//if both users have passed 3 times in a row
			if(currentPlayer.getpassCount() ==3 && otherPlayer.getpassCount() ==3 ) {
				System.out.println("Exiting Game: Thanks for Playing!");		//display results
				runner.quitGame(currentPlayer, otherPlayer);
				System.exit(0);
			}
			
		}
			
		else if(token.get(0).equalsIgnoreCase("EXCHANGE") || token.get(0).equalsIgnoreCase("e")){	
			 ArrayList<String> exchangeTiles = new ArrayList<String>();
			 keepPrompting=true;
			 int areExchangeLettersInFrame=0;
			 String tempString="";
			 ArrayList<Tile> reAddTiles = new ArrayList<Tile>();
		
			 for(int i=1; i< token.size(); i++)
				 exchangeTiles.add(token.get(i) );
			 
			 //check that all the tiles user wants to exchange actually exist in frame
		     for(int i=0; i< tempFrame.getList().size();i++) {
		    	 
		    	 for(int j=0; j< exchangeTiles.size(); j++ ) {
		    		 if(exchangeTiles.get(j).equalsIgnoreCase(tempFrame.getList().get(i).getTileChar()+"" ) ) {
		    			 areExchangeLettersInFrame++;
			             tempString += exchangeTiles.get(j);
			             reAddTiles.add(tempFrame.getList().get(i));
			         }      
			      }
			  }      
		     //if all tiles user wants to exchange exists then remove them from user frame and readd them to the pool
			  if(areExchangeLettersInFrame == exchangeTiles.size() ) {
				  tempFrame.removeFromRealFrame(tempString);
			      tempFrame.grabTiles();
			      for(int i=0; i < gamePool.poolAccess().length; i++ ) {
			    	  
			    	  for(int j=0; j < reAddTiles.size(); j++) {
			    		  if(reAddTiles.get(j).getTileChar() == gamePool.poolAccess()[i].getTileChar() ) {
			                  gamePool.poolAccess()[i].setQuantity( gamePool.poolAccess()[i].getQuantity()+1) ;
			              }
			          }
			       }
			  }
		}
		
		else if(token.get(0).equalsIgnoreCase("CHALLENGE") || token.get(0).equalsIgnoreCase("c")){
			//challenge count is essential for the reverting process. If the user(curr) challenges first and then makes a move, and then the next user challenges that successfully
			//then revert to the board that was gotten when user(curr) challenged originally		
			boolean successfulChallenge=false;
			boolean falseChallenge = false;
			
			//if the user tries to make a challenge at the very start of the game
			if(board.isBoardEmpty()== true){
				System.out.println("*** Cannot challenge on first move: Nothing to challenge ***");
				keepPrompting = true;
				falseChallenge = true;
			}
			
			//if it's the first or the second move change the board to empty if they challenge it
			if(moveIndex==0 || moveIndex==1){
				if(moveIndex==0){
					keepPrompting = true;
					falseChallenge = true;
				}
				board.setBoardEmpty(true);
			}
			if(challengeCount==1){
				System.out.println("*** Cannot challenge more than once per turn ***");
				keepPrompting=true;
			}
			
			//If user tries to challenge on the first go this wont be executed, also wont let the user challenge twice in a row
			if(falseChallenge ==false) {
				System.out.println();
				
				//search dictionary for the previous word
				successfulChallenge =runner.binarySearch(getPrevWordInput(), gameDictionary);
				if(successfulChallenge==true && challengeCount==0) {
					
					//revert to a previous version of the game
					runner.revertMoveOnChallenge(id, board, otherPlayer, gamePool, tempFrame, otherFrame);
					board = runner.displayUI(currentPlayer, otherPlayer, tempFrame);
				}
				else {
					System.out.println("*** Unsuccessful Challenge :( ***");
					keepPrompting=false;
				}	
			}
			
			else if(falseChallenge==true){
				System.out.println("Not a recognised command");
				System.out.println();
				welcome();
			}
		}
		challengeCount=1;
		return keepPrompting;
	}

	public boolean promptForMove(int id, int moveIndex, Frame playerFrame, Frame otherFrame, Player currentPlayer, Player otherPlayer, Scrabble runner, Board board, UI gameInterface, ArrayList<String> dictionary, Pool gamePool, ObjectCloner myCloner)throws Exception{
		Scanner scanner = new Scanner(System.in);
		boolean keepPrompting=false;

		System.out.println();
		System.out.println("== "+currentPlayer.displayName()+"'s Turn ==");
		playerFrame.displayFrame();
		System.out.println("Enter A Command: ");
		String input= scanner.nextLine();
		
		//parse input when anything between ascii for , and - is used to separate the input
		token= Arrays.asList(input.split("\\s*[,--]\\s*"));
		keepPrompting = inputOptions(id, moveIndex, token, playerFrame, otherFrame, currentPlayer, otherPlayer, runner, board, gameInterface, dictionary, gamePool, myCloner);	

		return keepPrompting;
	}
	
	public List<String> accessInput() {
		return token;
	}
	
	//reserve the previous word inputted for checking in the dictionary
	public void setPrevWordInput(String word) {
		prevWord = word;
	}
	public String getPrevWordInput(){
		return prevWord;
	}
	public void getName(Player player, int index) {
		System.out.println("Enter player"+ index+"'s name:");
		Scanner getName = new Scanner(System.in);
		String name= getName.nextLine();
		player.setName(name);
	}
}
