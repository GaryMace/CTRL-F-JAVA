package SliderPuzzle;

import java.awt.Color;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Tile extends JLabel {
	
	private int moves[][] = { {0 , 0}, {0 , +1}, {0 , -1}, {+1 , 0}, {-1 , 0} };
	private static boolean isOccupied[][] = new boolean[4][4];
	private static int locations[][] = new int[4][4];
	private int x,y,number;
	
////////////////////////////////////////////////////////////////////////////////////
	Tile(int i) {
		Border myBorder = BorderFactory.createLineBorder(Color.GRAY,1);
		if(i+2==16)	{
			locations[locations.length-1][locations[0].length-1]=16;
		}
		x = i%4;					 //Rows   //
		y = i/4;					 //Columns//				
		number = i+1;
		
		this.setSize(75,75);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(myBorder);
		this.setText("" +(i+1));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setLocation(20+x*76, 20+y*76);
		this.setVisible(true);
		isOccupied[y][x]= true;
		locations[y][x] = number;
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void move(boolean randomizing) {
		int cloneX,cloneY;
		int i,count=0;
		boolean checker;
		cloneX=x;
		cloneY=y;
		
		for(i=0; i < moves.length; i++) {
			checker =checkMove(cloneY , cloneX, i);
		
			if( checker ==true) {
				count++;
				break;
			}
		}
		
		if(count ==1) {
			this.setLocation(20+(cloneX+=moves[i][1])*76, 20+(cloneY+=moves[i][0])*76);
		}
		
		if(!(randomizing) ==true && count !=1) {
			JOptionPane.showMessageDialog(null,"No move possible");
			
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////	
	private boolean checkMove(int cloneY, int cloneX, int index) {
		int emptyTileY=cloneY;
		int emptyTileX=cloneX;
		int temp=0;
		
		emptyTileY+= moves[index][0];														//Set index of move to try
		emptyTileX+= moves[index][1];
		
		if((emptyTileY >=0 && emptyTileY < 4) && (emptyTileX>=0 && emptyTileX < 4)) {				
		
			if( isOccupied[emptyTileY][emptyTileX]== true) {								//Move instance Impossible
				return false;
			}
			
			else if(isOccupied[emptyTileY][emptyTileX]== false) {							//Found a possible move
				
				isOccupied[emptyTileY][emptyTileX]=true;									
				isOccupied[cloneY][cloneX]=false;
				
				temp = locations[cloneY][cloneX];
				locations[cloneY][cloneX] = locations[emptyTileY][emptyTileX];
				locations[emptyTileY][emptyTileX] = temp;
				x= emptyTileX;
				y= emptyTileY;
				
				return true;
			}
		}
		
		return false;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
	public int getTileNum() {
		return number;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public static int[][] getLocations() {
		return locations;
	}

}