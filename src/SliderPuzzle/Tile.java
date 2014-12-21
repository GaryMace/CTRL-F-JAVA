package SliderPuzzle;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Tile extends JLabel {
	
	private int moves[][] = { {0 , 0}, {0 , +1}, {0 , -1}, {+1 , 0}, {-1 , 0} };
	private static boolean isOccupied[] = new boolean[16];
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
		int i,count=0;
		boolean checker;
		tempx=x;
		tempy=y;
			
		for(i=0; i < moves.length; i++) {
			checker =checkMove(tempy , tempx, i);
			
			if( checker ==true) {
				count++;
				break;
			}
		}
		
		if(count ==1) {
			this.setLocation(20+(tempx+=moves[i][1])*76, 20+(tempy+=moves[i][0])*76);
		}
		
		else {
			JOptionPane.showMessageDialog(null,"No move possible");
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////	
	private boolean checkMove(int cloneY, int cloneX, int index) {
		int tempi=cloneY, tempj=cloneX,temp=0;
		tempi+= moves[index][0];
		tempj+= moves[index][1];
		System.out.println("Empty slot Y "+ tempi);
		System.out.println("Empty slot X "+ tempj);
		if(((tempi >=0 && tempi < 4) && (tempj>=0 && tempj < 4)) && isOccupied[locations[tempi][tempj]-1]==true ) {				//Move impossible
			return false;
		}
		
		else if(((tempi >=0 && tempi < 4) && (tempj>=0 && tempj < 4)) && isOccupied[locations[tempi][tempj]-1]==false ) {		//If a move is possible
			System.out.println("////////////////////////////////////////////////");
			System.out.println("truth val at clicked index: "+ isOccupied[locations[cloneY][cloneX]-1] );
			System.out.println("truth val at empty index: "+ isOccupied[locations[tempi][tempj]-1] );
			System.out.println("////////////////////////////////////////////////");
			System.out.println("Add "+ moves[index][0]+" to Y");
			System.out.println("Add "+ moves[index][1]+" to X");
			System.out.println("////////////////////////////////////////////////");
		
			isOccupied[locations[tempi][tempj]-1]=true;
			isOccupied[locations[cloneY][cloneX]-1]=false;
			
			temp = locations[cloneY][cloneX];
			locations[cloneY][cloneX] = locations[tempi][tempj];
			locations[tempi][tempj] = temp;
			
			System.out.println("curr y= "+ y);
			System.out.println("curr x= "+ x);
			x= tempj;
			y= tempi;
			System.out.println("new y="+y);
			System.out.println("new x= "+ x);
			
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
