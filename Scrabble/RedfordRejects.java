import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RedfordRejects implements Bot {

	private Word word = new Word();
	private String letters;
	
	
	
	public void reset() throws FileNotFoundException {
		
		word.setWord(-1, -1, Word.HORIZONTAL, "HELLO");
		letters = "hello";
		
	}
	public int getCommand (Player player, Board board, Dictionary dictionary) {
		letters = convertFrame(player);
		word = new Word();
		final long startTime = System.currentTimeMillis();
		
		if(board.isFirstPlay())
		{
			ArrayList<String> str = makeWords(null, null, letters, dictionary);
			word = bestWord(arrayListStringToWord(null,null, null,str), board);
			if(word.getLetters() == "")
				return UI.COMMAND_EXCHANGE;
			return UI.COMMAND_PLAY;
		}
		

		
		else if(!chooseMove(player, board, dictionary) || word.getLetters() == "")
		{
			return UI.COMMAND_EXCHANGE;
		}
		
		final long endTime = System.currentTimeMillis();
		final long time = endTime - startTime;
		System.out.println("\n\nTIME OF MOVE: "+ time + "------\n\n");
		
		return UI.COMMAND_PLAY;
		
		
		
		// make a decision on the play here
		// use board.board.getSqContents to check what is on the board
		// use Board.SQ_VALUE to check the multipliers
		// use frame.getAllTiles to check what letters you have
		// return the corresponding commandCode from UI
		// if a play, put the start position and letters into word
		// if an exchange, put the characters into letters
		
		//return(UI.COMMAND_PASS);
	}
	
	public Word getWord () {
		// should not change
		return(word);
	}
		
	public String getLetters () {
		// should not change
		return(letters);
	}
	
	private ArrayList<String> permutation(String prefix, String str, ArrayList<String> words)
    {
        int n = str.length();
        
        
        if (n == 0)
        	words.add(prefix);
            
       
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1), words);
        }
        
        
        
		return words;
    }
    
    public ArrayList<String> subStrings (String string, ArrayList<String> boardLetter, int size)
    {
    	ArrayList<String> subStrings = new ArrayList<String>();
	    String sub;
	    int i, c, length;
	
	    length = string.length();   
	
	    for( c = 0 ; c < length ; c++ )
	    {
	       for( i = 1 ; i <= length - c ; i++ )
	       {
	          sub = string.substring(c, c+i);
	          if(boardLetter == null)
	        	  subStrings.add(sub);
	        	  
	          else if(sub.contains(boardLetter.get(0)) && sub.length() <= size)
	        	  subStrings.add(sub);
	       }
	    }
		return subStrings;
    }
    
    
    public ArrayList<String> makeWords(ArrayList<String> boardLetters, int[] distances, String frame, Dictionary dictionary) 
    {
    	int size = 0;
    	String str;
    	if(boardLetters != null)
    	{
    		str = frame + boardLetters.get(0);
    		size = distances[1] - distances[0];
    	}
    	
    	else
    		str = frame;
    	
    	ArrayList<String> words = new ArrayList<String>(),temp = new ArrayList<String>();//boardWords = new ArrayList<String>()
    	int index;
    	
		
    	if(str.length() <= size)
            permutation("", str, words);
    	
        for(index = 0; index < subStrings(str, boardLetters, size).size();index++)
        	permutation("", subStrings(str, boardLetters, size).get(index), words);

        

    	
        for(index = 0; index < words.size();index++)    
        {
        	temp.clear();
        	temp.add(words.get(index));
        	
        	if(!dictionary.areWords(temp))
        	{
        		words.remove(index);
        		index--;
        	}
        	
        }

    	return words;
    }
    
    public  Word bestWord(ArrayList<Word> words, Board board)
    {
    	int bestScore = 0;
    	int index = 0;
    	
    	if(words.isEmpty())
    		return word;
    	
    	for(int i = 0; i < words.size(); i++)
    	{
    		//System.out.println("\nword: " + words.get(i) + " score: " + board.getTotalWordScore(x) + " best: " + bestScore + "\n\n");
    		  
    		if(board.getTotalWordScore(words.get(i)) > bestScore)
    		{
    			bestScore = board.getTotalWordScore(words.get(i));
    			index = i;
    		}
    		
    	}
    	
    	
    	return words.get(index);
    }


	public String convertFrame(Player player)
	{
		String x = "";
		
		for(int index = 0; index < player.getFrame().size(); index++)
		{
			if(player.getFrame().getTile(index).getFace() == '*')
				x += "e";
			
				
			else
				x += player.getFrame().getTile(index).getFace();
		}
		
		return x;
	}
	
	public boolean chooseMove(Player player, Board board, Dictionary dictionary)
	{
		int row, col;
		boolean condition = false;
		Word temp;
		//next tile
		for(row = 0; row < 15 && condition == false; row++)
		{
			for(col = 0; col < 15 && condition == false; col++)
			{
				if(board.getSqContents(row, col) != Board.EMPTY)
				{
					 //temp = word;
					 word = unObstructedMoves(row, col, board, dictionary);
					 
					 //if (word != temp )
						// condition = true;
				}
			}
		}
		
		if (word.getStartColumn() != -1 )
				return true;
		
		
		return false;
	}
	
	public ArrayList<String> checkWord(int direction, int row, int col, int[] parameters, Board board, ArrayList<String> strs, int test){
		
		int length, length2;
		int row_or_col = col;
		
		if(parameters[0] == parameters[1])
			return null;
		
		if(direction == 1){
			row_or_col = row;
		}
		
		length = row_or_col - parameters[0];
		length2 = parameters[1] - row_or_col;
		
		

		for(int i = 0; i < strs.size(); i++){
			
			int index = strs.get(i).lastIndexOf(board.getSqContents(row, col));
			
			//System.out.print("\nSTUPID: " + (row_or_col - index) + strs.get(i).length() + " WORD: " + strs.get(i));
			/*
			if(((row_or_col - index) + strs.get(i).length() )>= 15)
			{
				strs.remove(i);
				i--;
			}
			*/
			if((index > length) || ((strs.get(i).length() - index) > length2) ){
				strs.remove(i);
				i--;
			}
			
			
		}
		
		return strs;
	}
	
	
	public Word unObstructedMoves(int row, int col, Board board, Dictionary dictionary)
	{
		char boardLetter = board.getSqContents(row, col);
		int direction =0;
		
		ArrayList<String> boardLetters = new ArrayList<String>();
		boardLetters.add(String.valueOf(boardLetter));
		
		
		
		
		int[] x = getBounds(row,col, 0,board);
		int[] y = getBounds(row,col,1,board);
		
			
		if((x[1] - x[0]) < y[1] - y[0])
		{
			direction = 1;
			x = y;
		}
		
		ArrayList<String> strs = makeWords(boardLetters, x, letters, dictionary);
		
		
		strs = checkWord(direction, row, col, x, board, strs, 0);
		
		if(strs == null)
			return word;
			
		int[] rows = new int[strs.size()], cols = new int[strs.size()], directions = new int[strs.size()];
		if(direction == 1)
		{
			for(int i = 0; i < strs.size(); i++)
			{
				int pos = strs.get(i).lastIndexOf(boardLetter);
				
				rows[i] = row - pos;  //column[i] = column -pos;
				cols[i] = col;			//rows[i] = rows;                                                          
				directions[i] = 1;
			}
		}
		
		else{
			for(int i = 0; i < strs.size(); i++)
			{
				int pos = strs.get(i).lastIndexOf(boardLetter);
				
				rows[i] = row ;  
				cols[i] = col - pos;			                                                        
				directions[i] = 0;
			}
		}
		
		
		ArrayList<Word> words = arrayListStringToWord(rows, cols, directions, strs);
		
		Word temp = bestWord(words, board);
		
		
		if(board.getTotalWordScore(temp) > board.getTotalWordScore(word))
			return temp;
		

		
		return word;
		
		
	}
	
	public ArrayList<Word> arrayListStringToWord(int[] row , int[] col, int direction[], ArrayList<String> str)
	{
		ArrayList<Word> words = new ArrayList<Word>();
		if(row == null)
		{
			for(int i = 0; i < str.size(); i++)
			{
				words.add(new Word(7, 7, 0,str.get(i)));
			}
		}
		
		else
		{
			for(int i = 0; i < str.size(); i++)
			{
				//System.out.print("\nfinal word: " + str.get(i));
				words.add(new Word(row[i], col[i], direction[i],str.get(i)));
			}
		}

		
		return words;
	}
	
	private int[] getBounds(int row, int col, int direction,Board board)
	{
		int  temp = col, temp2 = row, j;
		int invalid[] = {-1,-1};
		int coord[] = new int[2];
		boolean condition;
		
		if(direction == Word.HORIZONTAL){
			
			
			for(condition = false, col++; col < 15 && condition == false; col++){
				
				for(j = 0; j < 2 && condition == false; j += 2){
					
					if(row + j > -1 && row + j < 15)
					{		
						if(board.getSqContents(row + j, col) != Board.EMPTY){
							
							if(col == temp + 1 && j == 0)
								return invalid;
							
							
							else if(j == 0)
								coord[1] = col - 2;
								
							else
								coord[1] = col - 1;
							
							condition = true;
						}
					}
					if(j == 0)
						j -= 3;
				}
			}	
			
			if (condition == false)
				coord[1] = 14;
			
			for(condition = false, col = temp - 1; col > -1 && condition == false; col--){
						
				for(j = 0; j < 2 && condition == false; j += 2){
					
					if(row + j > -1 && row + j < 15)
					{		
						if(board.getSqContents(row + j, col) != Board.EMPTY){
							
							if(col == temp - 1 && j == 0)
								return invalid;
							
							else if(j == 0)
								coord[0] = col + 2;
							
							else
								coord[0] = col + 1;
							
							condition = true;
						}
					}
					if(j == 0)
						j -= 3;
				}
			}	
		}
		
		
		else if(direction == Word.VERTICAL){
			
			for(condition = false, row++; row < 15 && condition == false; row++){
				
				for(j = 0; j < 2 && condition == false; j += 2){
					
					if(col + j > -1 && col + j < 15)
					{		
						if(board.getSqContents(row, col + j) != Board.EMPTY){
							
							if(row == temp2 + 1 && j == 0)
								return invalid;
							
							else if(j == 0)
								coord[1] = row - 2;
							
							else
								coord[1] = row - 1;
							
							condition = true;
						}
					}
					if(j == 0)
						j -= 3;
				}
			}	
			if (condition == false)
				coord[1] = 14;
			
			for(condition = false, row = temp2 - 1; row > -1 && condition == false; row--){
						
				for(j = 0; j < 2 && condition == false; j += 2){
					
					if(col + j > -1 && col + j < 15)
					{		
						if(board.getSqContents(row, col + j) != Board.EMPTY){
							
							if(row == temp2 - 1 && j == 0)
								return invalid;
							
							else if(j == 0)
								coord[0] = row + 2;
							
							else
								coord[0] = row + 1;
							
							condition = true;
						}
						if(j == 0)
							j -= 3;
					}
				}
			}	
		}
		
		return (coord);
	}
}
