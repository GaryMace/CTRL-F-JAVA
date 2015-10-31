package SliderPuzzle;

import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SliderPuzzle extends JFrame {
	private Tile[] tiles;
	private JLabel background;
	private Random rand;
	private boolean randomizing;
	private myMouseListener mouseyMouse;
	
////////////////////////////////////////////////////////////////////////////////////
	SliderPuzzle() {
		
		super("Slider Puzzle"); 
		
		tiles = new Tile[15];
		mouseyMouse = new myMouseListener(this);
		rand = new Random();
		randomizing =true;
		
		makeTiles();
		randomize();
		
		this.setSize(350, 400); 					
		this.setResizable(false); 			
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(background);
		this.setVisible(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
	private void makeTiles() {
		background = new JLabel();
		for(int i=0; i < tiles.length; i++) {
				tiles[i]= new Tile(i);
				tiles[i].addMouseListener(mouseyMouse);
				background.add(tiles[i]);
		}
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
	public void tileClicked(Object o) {
		Tile isClicked = (Tile) o;
		
		isClicked.move(randomizing);
		haveIWon();
	}
	
////////////////////////////////////////////////////////////////////////////////////
	private void randomize() {
		for(int i=0; i < 1000; i++)
		{
			tiles[rand.nextInt(15)].move(randomizing);
		}
		randomizing =false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	private void haveIWon() {
		String test = "";
		int[][] temp = Tile.getLocations();
		
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				test += ("" + temp[i][j]);
			}
		}
		
		if(test.equals("12345678910111213141516")) {
			JOptionPane.showMessageDialog(null, "YOU WIN!");
			System.exit(0);
		}
	}
	

}
