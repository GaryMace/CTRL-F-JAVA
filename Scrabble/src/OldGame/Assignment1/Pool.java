/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment1;

import java.io.Serializable;
import java.util.Random;

public class Pool implements Serializable{
										//a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q, r,s,t,u,v,w,x,y,z, blank//
	static private int[] scores =		 {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10,0};		//Parallel arrays for storing tile quantities and scores(alphabetically ordered)
	static private int[] quantityTiles = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1, 2};
	private int poolSize;
	private Tile[] scrabbleTiles;
	private int counter;
	
////////////////////////////////////////////////////////////////////////////////////
	public Pool() {
		scrabbleTiles = new Tile[27];			//Instead of having 100 tiles( waste of memory) I'll use 27 tiles with each having it's own quantity of that letter
		counter =0;
		poolSize=100;
		
		createTiles();
	}
	
////////////////////////////////////////////////////////////////////////////////////
	private void createTiles() {
		int holder;
		
		for(int i = 'a'; i <= 'z'+1; i++) {
			holder=i;									//Passing holder instead of i so that changing holder to ' ' doesn't put the i index OutOfBounds in the "for" condition check up
			
			if(i > 'z') {								//Blank tile will just be a space and a score given zero, See last elements of parallel arrays^
				holder= '*';
			}
			
			scrabbleTiles[counter] = new Tile(quantityTiles[counter], scores[counter], (char)holder );		//Pass quantity of Tile, score of tile and actual letter to Tile class
			counter++;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public boolean isPoolEmpty() {
		return poolSize ==0;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public int poolSize() {
		return poolSize;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	public int tileValue(Tile queriedTile) {				//gets a specified tiles letter value
		return queriedTile.getTileValue();
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void resetPool() {
		poolSize=100;
		counter=0;
		createTiles();
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public Tile pullTile() {
		Random rand = new Random();
		boolean tileExists =true;
		int index;
		
		if(poolSize ==0) {
			System.out.println("Error: Pool is empty!");
			return null;
		}
		
		while(tileExists == true) {					
			index = rand.nextInt(27);
			
			if(scrabbleTiles[index].getQuantity() > 0) {
				scrabbleTiles[index].setQuantity(scrabbleTiles[index].getQuantity()-1);	  //reduce quantity of this tile(randomised tile) by 1
				poolSize--;							
				
				return scrabbleTiles[index];
			}
			
			else {
					//Keep going till you find a Tile in the pool who's quantity isn't zero
			}
		}
		return null;
	}
	
	public Tile[] poolAccess() {
		return scrabbleTiles;
	}
	public void setPool(Tile[] oldPool) {
		scrabbleTiles =oldPool;
	}
	public void setPoolSize(int oldPoolSize) {
		poolSize = oldPoolSize;
	}
////////////////////////////////////////////////////////////////////////////////////
}
