package SliderPuzzle;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Tile extends JLabel {
	
	private int moves[][] = { {0 , 0}, {0 , +1}, {0 , -1}, {+1 , 0}, {-1 , 0} };
	private static int locations[][] = new int[4][4];
	private int x,y,number;
	
////////////////////////////////////////////////////////////////////////////////////
	Tile(int i) {
		Border myBorder = BorderFactory.createLineBorder(Color.GRAY,1);
		
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
		locations[y][x] = number;
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void moveTo() {
			
	}
	
////////////////////////////////////////////////////////////////////////////////////	
	private void located() {
		
		
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
	
}
