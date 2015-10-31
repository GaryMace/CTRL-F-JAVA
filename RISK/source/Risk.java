package source;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Risk extends JFrame {		
	ImageIcon[] pics = new ImageIcon[129];
	GUI gameGUI;							//All prompting, displaying and other commands are done through the GUI
	int numPlayers;
    
	public Risk() {
		super("RISK - The Global Conquest Game");
		gameGUI = new GUI(this);
		
		loadPics();
		gameGUI.loadingScreen();
		//makePicArray();
		
	}
	/**
	 * Loads all pictures for the game
	 * 
	 */
	private void loadPics() {
		int i = 1;
		String[] pic_names= {
				"1_RISK.jpg", "Afghan.png", "Alaska.png", "Alberta.png", "Argentina.png", "Brazil.png", "Central USA.png",
				"China.png", "Congo.png", "E Africa.png", "E Australia.png", "Egypt.png", "EUSA.png", "GB.png", "Greenland.png",
				"Iceland.png", "India.png", "Indonesia.png", "Irkutsk.png", "Japan.png", "Kamchatka.png", "Madagascar.png", 
				"Middle East.png", "Mongolia.png", "N Africa.png", "N Europe.png", "New Guinea.png", "North West Terr.png",
				"Ontario.png", "Peru.png", "Quebec.png", "S Africa.png", "S Europe.png", "Scandinavia.png", "Siam.png", "Siberia.png",
				"Ukraine.png", "Ural.png", "Venezuela.png", "W Australia.png", "W Europe.png", "WUSA.png", "Yakutsk.png", "Afghan_G.png", 
				"Alaska_G.png", "Alberta_G.png", "Argentina_G.png", "Brazil_G.png", "Central USA_G.png", "China_G.png", "Congo_G.png", 
				"E Africa_G.png", "E Australia_G.png", "Egypt_G.png", "EUSA_G.png", "GB_G.png", "Greenland_G.png", "Iceland_G.png", 
				"India_G.png", "Indonesia_G.png", "Irkutsk_G.png", "Japan_G.png", "Kamchatka_G.png", "Madagascar_G.png", "Middle East_G.png", 
				"Mongolia_G.png", "N Africa_G.png", "N Europe_G.png", "New Guinea_G.png", "North West terr_R.png", "Ontario_G.png", "Peru_G.png", 
				"Quebec_G.png", "S Africa_G.png", "S Europe_G.png", "Scandinavia_G.png", "Siam_G.png", "Siberia_G.png", "Ukraine_G.png", "Ural_G.png", 
				"Venezuela_G.png", "W Australia_G.png", "W Europe_G.png", "WUSA_G.png", "Yakutsk_G.png", "Afghan_R.png", "Alaska_R.png", "Alberta_R.png", 
				"Argentina_R.png", "Brazil_R.png", "Central USA_R.png", "China_R.png", "Congo_R.png", "E Africa_R.png", "E Australia_R.png", "Egypt_R.png", 
				"EUSA_R.png", "GB_R.png", "Greenland_R.png", "Iceland_R.png", "India_R.png", "Indonesia_R.png", "Irkutsk_R.png", "Japan_R.png", "Kamchatka_R.png", 
				"Madagascar_R.png", "Middle East_R.png", "Mongolia_R.png", "N Africa_R.png", "N Europe_R.png", "New Guinea_R.png", "NW_G.png", "Ontario_R.png",
				"Peru_R.png", "Quebec_R.png", "S Africa_R.png", "S Europe_R.png", "Scandinavia_R.png", "Siam_R.png", "Siberia_R.png", "Ukraine_R.png", "Ural_R.png", 
				"Venezuela_R.png", "W Australia_R.png", "W Europe_R.png", "WUSA_R.png", "Yakutsk_R.png", "Header.jpg"
		};
		pics[0] = null;
		for(String img: pic_names){
			pics[i] = new ImageIcon(getClass().getResource("pictures/"+img));
			i++;
		}
	}
	
	public void rightButtonClicked(Object source)
	{
		JButton buttonChops = (JButton) source;
		if(buttonChops != gameGUI.exitButton)
		{
			if(buttonChops.getIcon() == null)
			{
				buttonChops.setIcon(pics[10]);
			}
		
			else
			{
				buttonChops.setIcon(null);
			}
		}
	}
	
	public void leftButtonClicked(Object o)
	{
		JButton button =null;
		JLabel btn =null;
		
		if(o instanceof JButton) {												//Took like 5 minutes to fix this, Ez game
			button =(JButton)o;
		}
		else {
			btn = (JLabel)o;
		}
		
		if(button == gameGUI.exitButton) {
			System.out.println("Game Over");
			System.exit(0);
		}
		
		else if(button == gameGUI.accept) {
			Integer currentValue = (Integer)gameGUI.spinner.getValue();			//Grab number from the spinner input box
			numPlayers = currentValue;
			//System.out.println("Num players is: " + numPlayers);
			
			gameGUI.drawMap();													//Launches main game
		}
		
		else if( containsJLabel(btn)==true ){
			System.out.println("Sucess");
		}
		
	}
	/**
	 * Allows the pictures to be accessed by the GUI
	 * @return Array of the games' pictures
	 */
	public ImageIcon[] getPics() {
		return pics;
	}
	
	private boolean containsJLabel(JLabel toFind) {
		boolean success=false;
		for(int i=0; i < gameGUI.getButtons().length;i++) {
			if(gameGUI.getButtons()[i] == toFind)
				success=true;
		}
		
		return success;
	}
	public static void main(String[] args) {
		Risk game = new Risk();
	}
}
