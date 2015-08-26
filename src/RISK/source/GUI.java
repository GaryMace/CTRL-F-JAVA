package source;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GUI extends JFrame{	//This class handles displays, prompts, inputs and the graphic interface
	JButton exitButton;
	JButton accept;
	JLabel[] regButtons = new JLabel[42];
	JLabel[][] picArray = new JLabel[10][20];
	JLabel backGround;
	JLabel loadingScreen;
	JSpinner spinner;
	ImageIcon[] pics;
	myMouseListener mouseListener;
	
	public GUI(Risk game){
		super("Risk - The Global Conquest Game");
		
		mouseListener = new myMouseListener(game);
		pics = game.getPics();
		
	}

	/**
	 * Opens the game launcher which prompts for the amount of players for the current game.
	 */
	public void loadingScreen() {
		accept = new JButton("Accept");
		int min = 2;
		int max = 6;
		int step = 1;
		int initValue = 2;
		
		loadingScreen = new JLabel();				//Loading screen GUI with background and JSpinner numeric input field
		loadingScreen.setLocation(0, 0);
		loadingScreen.setSize(372, 591);
		loadingScreen.setIcon(pics[128]);
		
		accept.setSize(75,20);						//once hit it will get how many players user wants
		accept.setLocation(280, 200);
		accept.addMouseListener(mouseListener);
		loadingScreen.add(accept);

		SpinnerModel model = new SpinnerNumberModel(initValue, min, max, step);			//Sets limits on number players you can have in the game
		spinner = new JSpinner(model);
	    
	    spinner.setSize(55,20);
	    spinner.setLocation(215, 200);
		loadingScreen.add(spinner); 
		
		setSize(372, 591);
		this.setResizable(false); 
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(loadingScreen);
		setVisible(true);
	}
	/**
	 * Removes previous JFrame, i.e. frees up the memory,  and then creates the main JFrame for the game
	 */
	public void drawMap() {
		this.getContentPane().removeAll();
		backGround = new JLabel();
		backGround.setLocation(0, 0);
		backGround.setSize(1200, 700);
		backGround.setIcon(pics[1]);
		
		exitButton = new JButton("Exit");
		exitButton.setLocation(1110,650);
		exitButton.setSize(75, 20);
		exitButton.addMouseListener(mouseListener);
		backGround.add(exitButton);
						//TODO: territories as buttons
		regButtons[0] = new JLabel();
		regButtons[0].setLocation(599,174);
		regButtons[0].setBorder(BorderFactory.createEmptyBorder());
		regButtons[0].setIcon(pics[2]);
		regButtons[0].addMouseListener(mouseListener);
		backGround.add(regButtons[0]);
		
		regButtons[1] = new JLabel();
		regButtons[1].setSize(125,125);
		regButtons[1].setLocation(-4,49);
		regButtons[1].setBorder(BorderFactory.createEmptyBorder());
		regButtons[1].setIcon(pics[3]);
		regButtons[1].addMouseListener(mouseListener);
		backGround.add(regButtons[1]);
		
		setSize(1200, 700);
		this.setResizable(false); 
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(backGround);
		setVisible(true);
	}
	
	public JLabel[] getButtons() {
		return regButtons;
	}
}

