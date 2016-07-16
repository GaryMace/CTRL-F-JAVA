/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment3;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import OldGame.Assignment1.Frame;
import OldGame.Assignment1.Player;
import OldGame.Assignment1.Pool;
import OldGame.Assignment2.Board;
import OldGame.Assignment4.Dictionary;
import OldGame.Assignment4.ObjectCloner;

public class Scrabble implements Serializable{

	private static final long serialVersionUID = 1L;
	Pool prevPool;
	Player newP1Player;
	Player newP2Player;
	Board prevBoard;
	Frame newP1Frame;
	Frame newP2Frame;
	Pool gamePool;
	UI gameInterface;
	Player player1;
	Player player2;
	Frame p1Frame;
	Frame p2Frame;
	Board gameBoard;
	ObjectCloner myCloner;
	Dictionary gameDictionary;
	ArrayList<String> dictionary;
	
	public Scrabble() throws Exception{
		gamePool = new Pool();
		gameInterface = new UI();
		player1 = new Player();
		player2 = new Player();
		p1Frame = new Frame(gamePool);
		p2Frame = new Frame(gamePool);
		gameBoard = new Board();
		myCloner = new ObjectCloner();
		gameDictionary = new Dictionary();
		dictionary = new ArrayList<String>(gameDictionary.openDictionary());
		
		gameInterface.getName(player1,1);
		gameInterface.getName(player2,2);
		boolean playFirst = firstMove(player1,player2);
		boolean keepPrompting=false;
		int move=0;
		
		//I've moved this here because once we've reset the board, player and frame etc, we need to repass them as parameters to the rest of the game
		//If we didn't the game would be referencing an old version of the board aka. not the reset board.
		while(gamePool.isPoolEmpty()==false ) {				
			
			if(playFirst == true) {
				while(keepPrompting==true) {
					keepPrompting = prompting(1, move, player1, player2, p1Frame, p2Frame, gameInterface, gameBoard, gamePool, dictionary, myCloner);
				}
				keepPrompting=true;
				move++;
				
				while(keepPrompting==true) {
					keepPrompting = prompting(2, move, player2, player1, p2Frame, p1Frame, gameInterface, gameBoard, gamePool, dictionary, myCloner);
				}
				keepPrompting=true;
				move++;
			}
			
			else if(playFirst==false) {
				while(keepPrompting==true) {
					keepPrompting = prompting(2, move, player2, player1, p2Frame, p1Frame, gameInterface, gameBoard, gamePool, dictionary, myCloner);
				}
				keepPrompting=true;
				move++;
				
				while(keepPrompting==true) {
					keepPrompting = prompting(1, move, player1, player2, p1Frame, p2Frame, gameInterface, gameBoard, gamePool, dictionary, myCloner);
				}
				keepPrompting=true;
				move++;
			} 
		}
	}
	
	public void setBoard() { gameBoard = prevBoard; }
	public void setP1Frame() { p1Frame = newP1Frame; }
	public void setP2Frame() { p2Frame = newP2Frame; }
	public void setP1(){ player1 = newP1Player; }
	public void setP2(){ player2 = newP2Player; }
	public void setPool(){ gamePool = prevPool; }
	public Board getBoard(){ return gameBoard; }
	public Frame getP1Frame() { return newP1Frame; }
	public Frame getP2Frame() { return newP2Frame; }
	public Player getP1Player(){ return newP1Player; }
	public Player getP2Player(){ return newP2Player; }
	public Pool getPool(){ return prevPool; }
	
	private boolean firstMove(Player player1, Player player2) {
		Random rand = new Random();
		int p1Val=0,p2Val=0;
		do{
		p1Val = rand.nextInt(26)+97;
		p2Val = rand.nextInt(26)+97;
		System.out.println("==============================");
		System.out.println("==\t"+player1.displayName()+" drew \'"+(char)p1Val+"\'\t    "+"==");
		System.out.println("==\t"+player2.displayName()+" drew \'"+(char)p2Val+"\'\t    "+"==");
		System.out.println("==============================");
		if(p1Val==p2Val){ 
			System.out.println("Draw, Players will choose another two tiles");
		}
		else if( p1Val < p2Val){
			System.out.println(player1.displayName() +" goes first");
		}
		else{
			System.out.println(player2.displayName()+" goes first");
		}
		
		}while(p1Val==p2Val);
		
		return p1Val<p2Val;
	}
	
	private boolean prompting(int id, int moves, Player player1, Player player2, Frame playerFrame, Frame otherFrame, UI gameInterface, Board gameBoard, Pool gamePool, ArrayList<String> gameDictionary, ObjectCloner myCloner) throws Exception{
		List<String> userInput = new ArrayList<String>();
		boolean keepPrompting=false;
		
		keepPrompting = gameInterface.promptForMove(id, moves, playerFrame, otherFrame, player1, player2, this, gameBoard, gameInterface, gameDictionary, gamePool, myCloner);
		userInput = gameInterface.accessInput();
		
		/*if(userInput.size()==4)
			gameBoard.calculateScore(userInput, userInput.get(3).length(), player1, player2, gamePool);*/		//score is broken
		return keepPrompting;
	}
	
	public boolean makeMove(int id, UI gameInterface, Board playOnBoard, int moveIndex, Frame currFrame, Frame otherFrame, Player currentPlayer, Player otherPlayer, Pool gamePool, ObjectCloner myCloner) throws Exception{
		List<String> userInput = new ArrayList<String>(gameInterface.accessInput());
		boolean success;
		
		//creates deepcopys of the previous moves so we can have a save point to go back to after a challenge
		//id's prevent deepcopy's being overridden every turn. they identify the user who's turn it currently is
		prevBoard = (Board)myCloner.deepCopy(gameBoard);
		if(id==1) {
			newP1Player = (Player)myCloner.deepCopy(currentPlayer);
			newP1Frame = (Frame)myCloner.deepCopy(currFrame);
		}
		if(id==2) {
			newP2Player = (Player)myCloner.deepCopy(currentPlayer);
			newP2Frame = (Frame)myCloner.deepCopy(currFrame);
		}
		
		prevPool = (Pool)myCloner.deepCopy(gamePool);
		//deepcopy everything before the move is made
		success = playOnBoard.placeWord(Integer.parseInt(userInput.get(0)), Integer.parseInt(userInput.get(1)),userInput.get(2), userInput.get(3),currFrame, currentPlayer, gamePool);

		return success;
	}
	
	//this will just display the board remaining tiles in the pool etc.
	public Board displayUI(Player currentPlayer, Player otherPlayer, Frame tempFrame){
		gameBoard.displayBoard();
		System.out.println(currentPlayer.displayName()+"'s score is " + currentPlayer.scoreView()+"\t\t" +otherPlayer.displayName()+"'s score is " + otherPlayer.scoreView());
		System.out.println();
		System.out.println("Tiles remaining in Pool: "+gamePool.poolSize());
		System.out.println();
		System.out.println("*** Successful Challenge :) ***");
		return gameBoard;
	}
	
	public void quitGame(Player player1, Player player2) {
		if(player1.scoreView() > player2.scoreView()) {
			System.out.println("== "+player1.displayName()+": had "+player1.scoreView()+" points.\t\t    ==");
			System.out.println("== "+player2.displayName()+": had "+player2.scoreView()+" points.\t\t    ==");
			System.out.println("== "+player1.displayName()+" wins\t\t\t    ==");
		}
		else if(player1.scoreView() < player2.scoreView()) {
			System.out.println("== "+player1.displayName()+": had "+player1.scoreView()+" points.\t\t    ==");
			System.out.println("== "+player2.displayName()+": had "+player2.scoreView()+" points.\t\t    ==");
			System.out.println("== "+player2.displayName()+" wins\t\t\t    ==");
		}
	}
	
	public void revertMoveOnChallenge(int id, Board gameBoard, Player prevPlayer, Pool gamePool, Frame currPlayer, Frame prevPlayerFrame) {
		setPool();
		//revert to the previous version of the other players move
		if(id==2) {
			setP1Frame();
			setP1();
		}
		if(id==1) {
			setP2Frame();
			setP2();
		}
		setBoard();
	}
	
	public boolean binarySearch(String userWord, ArrayList<String> dictionary) {
		int low=0;
		int high =dictionary.size();
		boolean found=false;
		
		//binary search algorithm
		while(high>=low) {
			int middle = (low+high)/2;
			if(userWord.compareToIgnoreCase(dictionary.get(middle)) ==0) {
				found=true;
				break;
			}
			else if(userWord.compareToIgnoreCase(dictionary.get(middle)) > 0) {
				low= middle+1;
			}
			else if(userWord.compareToIgnoreCase(dictionary.get(middle)) < 0) {
				high=middle-1;
			}
		}
		
		return !(found);
	}
	
	public static void main(String[] args) throws Exception{ //Main runner for entire game
		@SuppressWarnings("unused")
		Scrabble game = new Scrabble();
	}
	
}
