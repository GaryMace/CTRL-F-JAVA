/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import OldGame.Assignment1.Frame;
import OldGame.Assignment1.Player;
import OldGame.Assignment1.Pool;
import OldGame.Assignment1.Tile;

public class Board implements Serializable{
	Tile[][] myBoard;
	ArrayList<Tile> storingList;
	int[][]moves={{0,-1}, {0,+1}, {-1,0}, {+1,0}};						//for checking surrounding tiles
	int multiplier;
	int scoreBoard[][];
	int skipSideChecks;
	int moveScore;
	String[][] board;
	boolean[][] isOccupied;
	boolean boardEmpty;
	boolean success;
	char starChar;
	String starString;
	Frame playerFrame;
	ArrayList<Integer> indexs;
	
	
	public Board() {
		starString="";
		multiplier=0;
		moveScore=0;
		storingList = new ArrayList<Tile>();
		myBoard = new Tile[15][15];
		scoreBoard= new int[15][15];
		for(int i=0; i< myBoard.length; i++){
			for(int j=0; j< myBoard[0].length; j++) {
				myBoard[i][j] = new Tile(1,0,'@');
			}
		}
		indexs = new ArrayList<Integer>();
		isOccupied = new boolean[15][15];
		success=false;
		boardEmpty=true;
		skipSideChecks=0;
		
		board = setBoard();
		squareValues();													//static so only needs to be defined once, no need to reset	
	}
	
	public void grabPlayerFrame(Frame player) {
		playerFrame = player;
	}
	
	public boolean placeWord(int x1, int y1, String direction, String userWord, Frame playersFrame, Player currPlayer, Pool gamePool) {
		String lowerCaseWord;
		int copyX=x1-1, copyY=y1-1;
		grabPlayerFrame(playersFrame);
		
		lowerCaseWord = userWord.toLowerCase();
		boolean validMove= validateMove(copyY, copyX,direction, lowerCaseWord, currPlayer);
		
		if(validMove==true) {
			storeTilePositions(x1, y1, copyX, copyY, direction, userWord, lowerCaseWord);
			
			displayBoard();
			System.out.println();
			System.out.println("== Tiles Remaining in Pool: "+ gamePool.poolSize()+" ==");
		}
		else {
			System.out.println("**Non-Valid Move**");
			System.out.println();
		}
		
		skipSideChecks=0;
		return validMove;
	}

	public boolean validateMove(int x1, int y1, String direction, String userWord, Player currPlayer) {
		int validateMove=0;
		validateMove += moveInBounds(x1, y1, direction,userWord);
		if(validateMove!=1) {
			success=false;
			return success;
		}
		
		validateMove += areLettersInFrameOrOnBoard(x1, y1, direction,userWord, currPlayer);
		if(success==true) 
			boardEmpty=false;
		
		if(boardEmpty==false && validateMove==2) {
			success=true;
		}
		else{
			success=false;
		}
		return success;
	}

	public boolean isBoardEmpty() {
		int numTiles=225;
		
		for(int i =0; i < isOccupied.length; i++) {
			for(int j =0; j < isOccupied[0].length; j++) {
				if(isOccupied[i][j]==false) {
					numTiles--;
				}
			}
		}
		return numTiles==0;
	}

	private int areLettersInFrameOrOnBoard(int x1, int y1, String direction, String userWord, Player currPlayer) {
		String starString;
		boolean doesMissingCharExist=false;
		boolean isCharInFrame;
		boolean hasBlankTile=false;
		boolean starStringExists=false;
		int tileOccupied=0;
		int starIndex=0;
		int skipFrameCheck;
		int tilesEmpty=0;
		int validMove=0;
		int i,n=0;
		
		for(int k=0; k < userWord.length();k++) {																			//If board has a char used in word and that char also exists in frame
			if(direction.equalsIgnoreCase("Down")) {																		//don't remove it from the frame!!
				if(myBoard[x1+k][y1].getTileChar()==userWord.charAt(k)){
					indexs.add(n, k);
					n++;
				}
			}
			else if(direction.equalsIgnoreCase("Across")) {
				if(myBoard[x1][y1+k].getTileChar()==userWord.charAt(k)) {
					indexs.add(n, k);
					n++;
				}
			}
		}
		
		for(i=0; i < userWord.length();i++) {																		
			isCharInFrame=false;
			skipFrameCheck=0;
	
			for(int k=0; k < indexs.size();k++) {
				if(i==indexs.get(k)) {
					tilesEmpty++;
					skipFrameCheck=1;
				}
			}
			if(skipFrameCheck==0)
				isCharInFrame=playerFrame.areLettersInFrame(userWord.charAt(i));									//Passing individual chars and test them
		
			else if(skipFrameCheck==1)																				//remove char from userWord if char is both on board and in frame
				userWord=userWord.substring(0, i)+userWord.substring(i+1);
			
			if(isCharInFrame==true && skipFrameCheck==0) {															//Are tiles along word path already occupied?
				if(direction.equalsIgnoreCase("Down")) {															
					if( isOccupied[x1+i][y1]==false) {																
						tilesEmpty++;
					}
					else {
						if(myBoard[x1+i][y1].getTileChar()==userWord.charAt(i)) {							
							tilesEmpty++;
							tileOccupied--;
						}
						else
							tileOccupied++;
					}
				}
			
				else if(direction.equalsIgnoreCase("Across")) {
					if(isOccupied[x1][y1+i]==false ) {
						tilesEmpty++;
					}
					else {
						if(myBoard[x1][y1+i].getTileChar()==userWord.charAt(i)) {
							tilesEmpty++;
							tileOccupied--;
						}
						else
							tileOccupied++;
					}
				}
			}
			//we want to store the char that we're replacing with the * on line 197 because when we check it against the dictionary the word will always be successfully challenged.
			else if(isCharInFrame==false&& skipFrameCheck==0) {
				doesMissingCharExist =searchForMissingChar(x1,y1,i,direction,userWord.charAt(i));					//Search for a char that isn't in user Frame
				
				if(isCharInFrame==false && doesMissingCharExist==false) {
					
					hasBlankTile=playerFrame.areLettersInFrame('*');
					if(hasBlankTile==true) {
						starChar = userWord.charAt(i);																//save the char we're replacing in user string with a *
						starString =userWord.substring(0, i)+'*'+userWord.substring(i+1);							//if all else fails check that they have a blank tile
						starIndex=i;
						starStringExists=true;
						userWord =starString;																		//if they do then use that as the missing char
						
						if(direction.equalsIgnoreCase("Down")) {
							if( isOccupied[x1+i][y1]==false)										
								tilesEmpty++;
							else
								tileOccupied++;
						}
					
						else if(direction.equalsIgnoreCase("Across")) {
							if(isOccupied[x1][y1+i]==false )
								tilesEmpty++;
							else
								tileOccupied++;
						}
					}
					else if(hasBlankTile==false && isCharInFrame==false && doesMissingCharExist==false){			//if move turns out to be invalid reset test frame to original frame
						playerFrame.resetTestFrame();
						success=false;
						tilesEmpty--;
					}
				}
				if(doesMissingCharExist==true) {
					validMove++;
					skipSideChecks=1;
				}
			}
			
		}
		
		if(tilesEmpty==userWord.length()) {
			if(boardEmpty==false && checkSurroundingTiles(x1, y1, direction,userWord)==1) {			//check surrounding tiles for tile
				skipSideChecks=1;
				validMove++;
			}
			else if(boardEmpty == true) {
				skipSideChecks=1;
				success=true;
				validMove++;
			}
		}
		else if(tileOccupied >= userWord.length()) {
			System.out.println("Word already Exists at ["+(y1+1)+", "+(x1+1)+"] " +direction);
		}
		
		if(validMove ==1) {																			//remove used words and then get the new ones for the frame.
			storingList =playerFrame.removeFromRealFrame(userWord);
			if(starStringExists==true)
				setUserWord(userWord.substring(0, starIndex)+starChar+userWord.substring(starIndex+1));
			
			if(playerFrame.isFrameEmpty() ==true ) {												//if they use all 7 tiles, add 50 points to their score.
				currPlayer.scoreIncrease(50);
			}
			playerFrame.grabTiles();
			playerFrame.resetTestFrame();
		}
		
		indexs.clear();																				//clear out the index's in ArrayList for next move
		return validMove;
	}

	private boolean searchForMissingChar(int x1, int y1, int index, String direction, char missingChar) {
		boolean doesCharExist=false;
		
		if(direction.equalsIgnoreCase("Down")) {
			if(missingChar==board[x1+index+1][y1+1].charAt(0)) {
				doesCharExist=true;
			}
			else {
				doesCharExist=false;
				System.out.println("Missing following letter to make that move: *"+missingChar+"*");
			}
		}
		else if(direction.equalsIgnoreCase("Across")) {
			if(missingChar==board[x1+1][y1+index+1].charAt(0)) {
				doesCharExist=true;
			}
			else {
				doesCharExist=false;
			}
		}
		
		return doesCharExist;
	}

	public int checkSurroundingTiles(int x1, int y1, String direction, String userWord) {
		int validMove=0;
		int i;
		
		if(direction.equalsIgnoreCase("Down")) {
			if(x1+moves[0][1] >=0 && skipSideChecks==0) {								//Check tile above start point if above or equal 0
				if(isOccupied[x1+moves[0][1]][y1] == true) {
					validMove++;
					skipSideChecks=1;
				}
			}
			
			if(x1+userWord.length() <= 14 && skipSideChecks==0){						//check tile below length of input word for tile
				if(isOccupied[x1+userWord.length()][y1] == true) {
					validMove++;
					skipSideChecks=1;
				}
			}
			if(skipSideChecks==0) {														//for checking Down side tiles
				for(i=0; i < userWord.length(); i++) {
					if(y1+moves[2][0] >=0) {
						if(isOccupied[x1+i][y1+moves[2][0]]==true ) {
							validMove++;
							break;
						}
					}
					if(y1+moves[3][0] <= 14) {
						if(isOccupied[x1+i][y1+moves[3][0]]==true) {
							validMove++;
							break;
						}
					}
				}
			}
			if(validMove==0) {
				System.out.println("No surrounding tiles here: Non-valid move");
				playerFrame.resetTestFrame();
			}
		}
		else if(direction.equalsIgnoreCase("Across")) {
			if(y1+moves[2][0] >=0 && skipSideChecks==0) {								//check tile before first letter of input word		
				if(isOccupied[x1][y1+moves[2][0]] == true) {
					validMove++;
					skipSideChecks=1;
				}
			}
			if(y1+userWord.length()+moves[1][1] <= 14 && skipSideChecks==0){			//check tile after final letter of input word
				if(isOccupied[x1][y1+userWord.length()+moves[1][1]] == true) {
					skipSideChecks=1;
					validMove++;
				}
			}
			if(skipSideChecks==0) {
				for(i=0; i < userWord.length(); i++) {									//for checking across side tiles
					if(x1+moves[0][1] >=0) {
						if(isOccupied[x1+moves[0][1]][y1+i]==true ) {
							validMove++;
							break;
						}
					}
					if(x1+moves[1][1] <= 14) {
						if(isOccupied[x1+moves[1][1]][y1+i]==true) {
							validMove++;
							break;
						}
					}
				}
			}
			if(validMove==0) {
				System.out.println("No surrounding tiles here: Non-valid move");
				playerFrame.resetTestFrame();
			}
		}
		return validMove;
	}
	
	private int moveInBounds(int x1, int y1, String direction, String userWord) {
		int validMove=0;
		if((x1 >=0 && x1 <= 14) && (y1 >= 0 && y1 <=14)) {											//Is position contained in frame
			if(direction.equalsIgnoreCase("Down")) {												//in particular are the tiles along the sides in bounds for checking
				if(!(userWord.length()+x1 > 14)) 
					validMove++;
			}
			else if(direction.equalsIgnoreCase("Across")) {
				if(!(userWord.length()+y1 > 14)) 
					validMove++;
			}
		}
		else{
			System.out.println("Position ["+(y1+1)+", "+(x1+1)+"] is out of Bounds");
			validMove=0;
			return validMove;
		}
		
		if(boardEmpty ==true && x1==7 && y1==7) {												//Only want to check this once for performance reasons
			if(isBoardEmpty() ==true)
				success=true;
		}
		
		else if(boardEmpty==true && (x1!=7 ||y1!=7)) {
			System.out.println();
			System.out.println("First word must be at centre of Board i.e [8, 8]");
			validMove=0;
		}
		return validMove;
	}

	public void reset() {
		for(int i=0; i< myBoard.length; i++){
			for(int j=0; j< myBoard[0].length; j++) {
				myBoard[i][j].setValue(0);
				myBoard[i][j].setTileChar('@');
				myBoard[i][j].setQuantity(1);
			}
		}
		for(int i=0; i< isOccupied.length; i++){
			for(int j=0; j< isOccupied[0].length; j++) 
				isOccupied[i][j]=false;
		}
		success=false;
		boardEmpty=true;
		moveScore=0;
		skipSideChecks=0;
		indexs.clear();
		playerFrame.resetTestFrame();
		
		board = setBoard();
	}
	
	private String[][] setBoard(){  	// = Triple word score
		// DW  = Double word score
		// TL  = Triple letter score
		// DL  = Double letter score
		// _ = Standard board tile
		String[][] board = new String[][] { 
				{ "    ", "1  ", "2  ", "3  ", "4  ", "5  ", "6  ", "7  ", "8  ", "9  ", "10 ", "11 ", "12 ", "13 ", "14 ", "15 ", " "},
				{ "1 | ", "TW ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "TW ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  ", "TW " ,"|"},
				{ "2 | ", "_  ", "DW ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "DW ", "_  " ,"|"},
				{ "3 | ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "DL ", "_  ", "DL ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  " ,"|"},
				{ "4 | ", "DL ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "DL " ,"|"},
				{ "5 | ", "_  ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "TL ", "_  " ,"|"},
				{ "6 | ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "_  ", "_  " ,"|"},
				{ "7 | ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "DL ", "_  ", "DL ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  " ,"|"},
				{ "8 | ", "TW ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  ", "TW " ,"|"},
				{ "9 | ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "DL ", "_  ", "DL ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  " ,"|"},
				{ "10| ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  " ,"|"},
				{ "11| ", "_  ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "_  " ,"|"},
				{ "12| ", "DL ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  ", "DL " ,"|"},
				{ "13| ", "_  ", "_  ", "DW ", "_  ", "_  ", "_  ", "DL ", "_  ", "DL ", "_  ", "_  ", "_  ", "DW ", "_  ", "_  " ,"|"},
				{ "14| ", "_  ", "DW ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "TL ", "_  ", "_  ", "_  ", "DW ", "_  " ,"|"},
				{ "15| ", "TW ", "_  ", "_  ", "DL ", "_  ", "_  ", "_  ", "TW ", "_  ", "_  ", "_  ", "DL ", "_  ", "_  ", "TW " ,"|"},
		};
		return board;
	}
	
	public void displayBoard() {
		for(int i=0; i < board.length;i++) {
			for(int j=0; j < board[0].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
	private void squareValues(){
		
		for(int i=1; i < 14; i++){
			for(int j=1; j < 14; j++){
			
				if(board[i][j].equalsIgnoreCase("TW "))	 		// x3 word score
					scoreBoard[i][j] = 6; 
				
				else if(board[i][j].equalsIgnoreCase("DW ")) 	// x2 word score
					scoreBoard[i-1][j-1] = 4; 
				
				else if(board[i][j].equalsIgnoreCase("TL "))	// x3 letter score
					scoreBoard[i-1][j-1] = 3; 
				
				else if(board[i][j].equalsIgnoreCase("DL "))	 // x2 letter score
					scoreBoard[i-1][j-1] = 2; 
				
				else if(board[i][j].charAt(0)== '_')	 // ordinary square
					scoreBoard[i-1][j-1] = 1; 
			}
		}
		
	} 
	
	public int getSquareValue(int x, int y) {
		int squareVal=scoreBoard[y][x];
		return squareVal;
	}
	
	public void storeTilePositions(int x1, int y1, int copyX, int copyY, String direction, String userWord, String lowerCaseWord) {
		if(getUserWord().length()==lowerCaseWord.length()) {
			lowerCaseWord = getUserWord();
		}
		
		if(direction.equalsIgnoreCase("down")) {
			int k=0;
			for(int i=0; i< lowerCaseWord.length(); i++) {
				board[y1+i][x1]=lowerCaseWord.charAt(i)+"  ";								//update observable board
				if(isOccupied[copyY+i][copyX]==false){
					myBoard[copyY+i][copyX] = storingList.get(k);								//store current tile position  
					k++;
				}
				isOccupied[copyY+i][copyX]=true;											//update boolean board
			}
		}
		else if(direction.equalsIgnoreCase("Across")) {
			int k=0;
			for(int i=0; i< lowerCaseWord.length(); i++) {
				board[y1][x1+i]=lowerCaseWord.charAt(i)+"  ";
				if(isOccupied[copyY][copyX+i]==false){
					myBoard[copyY][copyX+i] = storingList.get(k);								//store current tile position  
					k++;
				}	
				isOccupied[copyY][copyX+i]=true;
			}
		}
	}
	public int calculateScore(List<String> input, int wordLength, Player currentPlayer, Player otherPlayer, Pool gamePool){
		int x =Integer.parseInt(input.get(0));
		int y =Integer.parseInt(input.get(1));
		int userWordLength = input.get(3).length();
		String direction = input.get(2);
		int copyX=x-1;
		int copyY=y-1;
		int m=0,n=0;

		for(int i=0; i < wordLength;i++) {		//along word path
			if(i==0){
				m=0;
				n=0;
			}
			else if(i==1){
				if(direction.equalsIgnoreCase("down")) {
					m=1;
					n=0;
				}
				else if(direction.equalsIgnoreCase("across")) {
					m=0;
					n=1;
				}
			}
			moveScore+=checkMultipliers(copyY+m,copyX+n, direction);
			if(m >n)
				m++;
			else
				n++;
		}
		moveScore = scoreMultiplier(moveScore);
		
		moveScore+=frontTiles(copyY,copyX, direction);
		moveScore+=rearTiles(copyY,copyX, direction, userWordLength);
		moveScore+=sideTiles(copyY,copyX, direction, userWordLength);
		
		currentPlayer.scoreIncrease(moveScore);
		System.out.println(currentPlayer.displayName()+"'s score is " + currentPlayer.scoreView()+"\t\t\t" +otherPlayer.displayName()+"'s score is " + otherPlayer.scoreView());
		System.out.println();
		System.out.println("Tiles remaining in Pool: "+gamePool.poolSize());
		System.out.println();
		multiplier=0;
		return 1;
	}
	
	private int checkMultipliers(int x1, int y1, String direction){
		int[][] multipliers = squareValuesAccess();
		int score=0;
		
		if(multipliers[x1][y1] == 6){		//score multipliers
			score =myBoard[x1][y1].getTileValue();
			setMultiplier(3);			//multiply everything at the end
			multipliers[x1][y1] =1;
		}
		else if(multipliers[x1][y1] == 4){
			score =myBoard[x1][y1].getTileValue();
			setMultiplier(2);
			multipliers[x1][y1] =1;
		}
		else if(multipliers[x1][y1] == 3){
			score =(3 * myBoard[x1][y1].getTileValue() );
			multipliers[x1][y1] =1;
		}	
		else if(multipliers[x1][y1] == 2){
			score =(2 * myBoard[x1][y1].getTileValue() );
			multipliers[x1][y1] =1;
		}	
		else if(multipliers[x1][y1] == 1){
			score= myBoard[x1][y1].getTileValue();
		}
		return score;
	}
	//get score for the simple case of score, that is, along the actual path of the placed word
	private int frontTiles(int x1, int y1, String direction) {
		int score=0,m=0,n=0;
		
		if(direction.equalsIgnoreCase("down")){
			m=-1;
			n=0;
		}
		else if(direction.equalsIgnoreCase("across")) {
			m=0;
			n=-1;
		}
		while(isOccupied[x1+m][y1+n]==true) {
			score+=checkMultipliers(x1+m,y1+n,direction);
			if(m <n)
				m--;
			else
				n--;
		}
		score =scoreMultiplier(score);
		return score;
	}
	
	//get the score for the tile before and after the placed word, if it's an extension word
	private int rearTiles(int x1, int y1, String direction, int wordLength) {
		int score=0,m=0,n=0;
		
		if(direction.equalsIgnoreCase("down")){
			m= wordLength;
			n=0;
		}
		else if(direction.equalsIgnoreCase("across")) {
			m=0;
			n=wordLength;
		}
		while(isOccupied[x1+m][y1+n]==true) {
			score+=checkMultipliers(x1+m,y1+n,direction);
			if(m >n)
				m++;
			else
				n++;
		}
		
		score = scoreMultiplier(score);
		return score;
	}
	
	//get the score for the sideTiles
	//EDIT: this isn't implemented into the game because some cases don't work and it causes it to crash, the person in charge of fixing them
	//didn't get it done in time so we discluded it
	private int sideTiles(int x1, int y1, String direction, int wordLength) {
		int score=0,m,n;
		int lock=0;
		boolean caseExists=false;
		
		for(int index=0; index<wordLength; index++) {
			m=0;
			n=0;
			
			if(direction.equalsIgnoreCase("down")){
				m= -index;
				n= 1;
			}
			else if(direction.equalsIgnoreCase("across")) {
				m= 1;
				n= index;
			}
			
			//lock stops it going back the direction its going and recounting the score
			if(isOccupied[x1-m][y1+n]==true && isOccupied[x1-m][y1-n]==false && lock!=1) {
				
				while(isOccupied[x1-m][y1+n]==true) {
					score+=checkMultipliers(x1-m,y1+n,direction);
					if(m >n)
						m--;
					else
						n++;
				}
				caseExists=true;
			}
			else if(isOccupied[x1-m][y1+n]==false && isOccupied[x1-m][y1-n]==true && lock!=-1) {					//PICK UP HERE!
				while(isOccupied[x1-m][y1-n]==true) {
					score+=checkMultipliers(x1-m,y1-n,direction);
					if(m >n)
						m--;
					else if(m<n)
						n++;
				}
				lock=1;
				caseExists=true;
			}
			
			if(index==wordLength-1 && caseExists==true){
				score+=checkMultipliers(x1,y1,direction);
			}
			/*for(int i=0; i < myBoard.length;i++) {
				for(int j=0; j < myBoard[0].length; j++) {
					System.out.print("/ i= "+i+ " J= "+j+"**"+myBoard[i][j].getTileValue()+"** / ");
				}
				System.out.println();
			}*/
		}
		
		score = scoreMultiplier(score);
		return score;
	}
	private int scoreMultiplier(int moveScore) {
		if(getMultiplier()>0){
			if(getMultiplier()==2) {
				moveScore *= 2;
			}
			else if(getMultiplier()==3) {
				moveScore *= 3;
			}
		}
		return moveScore;
	}

	private void setMultiplier(int num) { multiplier=num; }
	private int getMultiplier() { return multiplier; }
	public Tile[][] boardAccess(){ return myBoard; }
	public boolean[][] occupiedAccess(){ return isOccupied; }
	public String[][] stringboardAccess(){ return board; }
	public int[][] squareValuesAccess(){ return scoreBoard; }
	public int getMoveScore() { return moveScore; }
	public void setUserWord(String userWord){ starString =userWord; }
	public String getUserWord(){ return starString; }
	
	//We need to do this if the challenged word is the only word on the board, otherwise and error saying "no surrounding tiles" is thrown
	public void setBoardEmpty(boolean newVal){ boardEmpty = newVal; }
}