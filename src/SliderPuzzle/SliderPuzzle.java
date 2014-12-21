package SliderPuzzle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Container;

public class SliderPuzzle extends JFrame {
	private Tile[] tiles;
	private JLabel background;	
	private myMouseListener mouseyMouse;
	
////////////////////////////////////////////////////////////////////////////////////
	SliderPuzzle() {
		
		super("Slider Puzzle"); 
		
		tiles = new Tile[15];
		mouseyMouse = new myMouseListener(this);
		
		makeTiles();
		
		this.setSize(350, 400); 					
		this.setResizable(false); 			
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(background);
		this.setVisible(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void makeTiles() {
		
		background = new JLabel();
		for(int i=0; i < tiles.length; i++) {
				tiles[i]=new Tile(i);
				tiles[i].addMouseListener(mouseyMouse);
				background.add(tiles[i]);
		}
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void tileClicked(Object o) {
		Tile isClicked = (Tile) o;
		int temp = isClicked.getTileNum();
		
		isClicked.printBoard();
		//isClicked.move();
	}
	
}
