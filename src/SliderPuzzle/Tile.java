package SliderPuzzle;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Tile extends JLabel {
	
	private int moves[][] = { {0 , 0}, {0 , +1}, {0 , -1}, {+1 , 0}, {-1 , 0} };
	private boolean isOccupied[] = new boolean[16];
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
		isOccupied[number-1]= true;
		locations[y][x] = number;
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void move() {
		int tempx,tempy;
		int i;
		boolean checker;
		tempx=x;
		tempy=y;
			
		for(i=0; i < moves.length; i++) {
			checker =checkMove(tempy , tempx, i);
			
			if( checker ==true) {
				break;
			}
		}
		
		this.setLocation(20+(tempx+=moves[i][0])*76, 20+(tempy+=moves[i][1])*76);
	}
	
////////////////////////////////////////////////////////////////////////////////////	
	private boolean checkMove(int cloneY, int cloneX, int index) {
		int tempi=cloneY, tempj=cloneX;
		tempi+= moves[index][0];
		tempj+= moves[index][1];
		
		if(isOccupied[locations[tempi][tempj]-1]==true) {
			return false;
		}
		
		else if(isOccupied[locations[tempi][tempj]-1]==false) {
			isOccupied[locations[tempi][tempj]-1]=true;
			isOccupied[locations[cloneY][cloneX]-1]=false;
			return true;
		}
		
		else
			return false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	static void printBoard()
	{
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(locations[i][j] + " ");
			}
			
			System.out.println();
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public int getTileNum(){
		return number;
	}
	
////////////////////////////////////////////////////////////////////////////////////	
	public boolean isOccupied(){
		return isOccupied[number-1];
	}
}
