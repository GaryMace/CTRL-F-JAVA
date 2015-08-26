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
	
	private void makePicArray() {
		
	}
	/**
	 * Loads all pictures for the game
	 */
	private void loadPics() {
		pics[0] = null;
		pics[1] = new ImageIcon(getClass().getResource("1_RISK.jpg"));			//Regular Borders
		pics[2] = new ImageIcon(getClass().getResource("Afghan.png"));
		pics[3] = new ImageIcon(getClass().getResource("Alaska.png"));
		pics[4] = new ImageIcon(getClass().getResource("Alberta.png"));
		pics[5] = new ImageIcon(getClass().getResource("Argentina.png"));
		pics[6] = new ImageIcon(getClass().getResource("Brazil.png"));
		pics[7] = new ImageIcon(getClass().getResource("Central USA.png"));
		pics[8] = new ImageIcon(getClass().getResource("China.png"));
		pics[9] = new ImageIcon(getClass().getResource("Congo.png"));
		pics[10] = new ImageIcon(getClass().getResource("E Africa.png"));
		pics[11] = new ImageIcon(getClass().getResource("E Australia.png"));
		pics[12] = new ImageIcon(getClass().getResource("Egypt.png"));
		pics[13] = new ImageIcon(getClass().getResource("EUSA.png"));
		pics[14] = new ImageIcon(getClass().getResource("GB.png"));
		pics[15] = new ImageIcon(getClass().getResource("Greenland.png"));
		pics[16] = new ImageIcon(getClass().getResource("Iceland.png"));
		pics[17] = new ImageIcon(getClass().getResource("India.png"));
		pics[18] = new ImageIcon(getClass().getResource("Indonesia.png"));
		pics[19] = new ImageIcon(getClass().getResource("Irkutsk.png"));
		pics[20] = new ImageIcon(getClass().getResource("Japan.png"));
		pics[21] = new ImageIcon(getClass().getResource("Kamchatka.png"));
		pics[22] = new ImageIcon(getClass().getResource("Madagascar.png"));
		pics[23] = new ImageIcon(getClass().getResource("Middle East.png"));
		pics[24] = new ImageIcon(getClass().getResource("Mongolia.png"));
		pics[25] = new ImageIcon(getClass().getResource("N Africa.png"));
		pics[26] = new ImageIcon(getClass().getResource("N Europe.png"));
		pics[27] = new ImageIcon(getClass().getResource("New Guinea.png"));
		pics[28] = new ImageIcon(getClass().getResource("North West Terr.png"));
		pics[29] = new ImageIcon(getClass().getResource("Ontario.png"));
		pics[30] = new ImageIcon(getClass().getResource("Peru.png"));
		pics[31] = new ImageIcon(getClass().getResource("Quebec.png"));
		pics[32] = new ImageIcon(getClass().getResource("S Africa.png"));
		pics[33] = new ImageIcon(getClass().getResource("S Europe.png"));
		pics[34] = new ImageIcon(getClass().getResource("Scandinavia.png"));
		pics[35] = new ImageIcon(getClass().getResource("Siam.png"));
		pics[36] = new ImageIcon(getClass().getResource("Siberia.png"));
		pics[37] = new ImageIcon(getClass().getResource("Ukraine.png"));
		pics[38] = new ImageIcon(getClass().getResource("Ural.png"));
		pics[39] = new ImageIcon(getClass().getResource("Venezuela.png"));
		pics[40] = new ImageIcon(getClass().getResource("W Australia.png"));
		pics[41] = new ImageIcon(getClass().getResource("W Europe.png"));
		pics[42] = new ImageIcon(getClass().getResource("WUSA.png"));
		pics[43] = new ImageIcon(getClass().getResource("Yakutsk.png"));
		
		
		pics[44] = new ImageIcon(getClass().getResource("Afghan_G.png"));				//Green borders
		pics[45] = new ImageIcon(getClass().getResource("Alaska_G.png"));
		pics[46] = new ImageIcon(getClass().getResource("Alberta_G.png"));
		pics[47] = new ImageIcon(getClass().getResource("Argentina_G.png"));
		pics[48] = new ImageIcon(getClass().getResource("Brazil_G.png"));
		pics[49] = new ImageIcon(getClass().getResource("Central USA_G.png"));
		pics[50] = new ImageIcon(getClass().getResource("China_G.png"));
		pics[51] = new ImageIcon(getClass().getResource("Congo_G.png"));
		pics[52] = new ImageIcon(getClass().getResource("E Africa_G.png"));
		pics[53] = new ImageIcon(getClass().getResource("E Australia_G.png"));
		pics[54] = new ImageIcon(getClass().getResource("Egypt_G.png"));
		pics[55] = new ImageIcon(getClass().getResource("EUSA_G.png"));
		pics[56] = new ImageIcon(getClass().getResource("GB_G.png"));
		pics[57] = new ImageIcon(getClass().getResource("Greenland_G.png"));
		pics[58] = new ImageIcon(getClass().getResource("Iceland_G.png"));
		pics[59] = new ImageIcon(getClass().getResource("India_G.png"));
		pics[60] = new ImageIcon(getClass().getResource("Indonesia_G.png"));
		pics[61] = new ImageIcon(getClass().getResource("Irkutsk_G.png"));
		pics[62] = new ImageIcon(getClass().getResource("Japan_G.png"));
		pics[63] = new ImageIcon(getClass().getResource("Kamchatka_G.png"));
		pics[64] = new ImageIcon(getClass().getResource("Madagascar_G.png"));
		pics[65] = new ImageIcon(getClass().getResource("Middle East_G.png"));
		pics[66] = new ImageIcon(getClass().getResource("Mongolia_G.png"));
		pics[67] = new ImageIcon(getClass().getResource("N Africa_G.png"));
		pics[68] = new ImageIcon(getClass().getResource("N Europe_G.png"));
		pics[69] = new ImageIcon(getClass().getResource("New Guinea_G.png"));
		pics[70] = new ImageIcon(getClass().getResource("North West terr_R.png"));
		pics[71] = new ImageIcon(getClass().getResource("Ontario_G.png"));
		pics[72] = new ImageIcon(getClass().getResource("Peru_G.png"));
		pics[73] = new ImageIcon(getClass().getResource("Quebec_G.png"));
		pics[74] = new ImageIcon(getClass().getResource("S Africa_G.png"));
		pics[75] = new ImageIcon(getClass().getResource("S Europe_G.png"));
		pics[76] = new ImageIcon(getClass().getResource("Scandinavia_G.png"));
		pics[77] = new ImageIcon(getClass().getResource("Siam_G.png"));
		pics[78] = new ImageIcon(getClass().getResource("Siberia_G.png"));
		pics[79] = new ImageIcon(getClass().getResource("Ukraine_G.png"));
		pics[80] = new ImageIcon(getClass().getResource("Ural_G.png"));
		pics[81] = new ImageIcon(getClass().getResource("Venezuela_G.png"));
		pics[82] = new ImageIcon(getClass().getResource("W Australia_G.png"));
		pics[83] = new ImageIcon(getClass().getResource("W Europe_G.png"));
		pics[84] = new ImageIcon(getClass().getResource("WUSA_G.png"));
		pics[85] = new ImageIcon(getClass().getResource("Yakutsk_G.png"));
		
		pics[86] = new ImageIcon(getClass().getResource("Afghan_R.png"));			//Red borders
		pics[87] = new ImageIcon(getClass().getResource("Alaska_R.png"));
		pics[88] = new ImageIcon(getClass().getResource("Alberta_R.png"));
		pics[89] = new ImageIcon(getClass().getResource("Argentina_R.png"));
		pics[90] = new ImageIcon(getClass().getResource("Brazil_R.png"));
		pics[91] = new ImageIcon(getClass().getResource("Central USA_R.png"));
		pics[92] = new ImageIcon(getClass().getResource("China_R.png"));
		pics[93] = new ImageIcon(getClass().getResource("Congo_R.png"));
		pics[94] = new ImageIcon(getClass().getResource("E Africa_R.png"));
		pics[95] = new ImageIcon(getClass().getResource("E Australia_R.png"));
		pics[96] = new ImageIcon(getClass().getResource("Egypt_R.png"));
		pics[97] = new ImageIcon(getClass().getResource("EUSA_R.png"));
		pics[98] = new ImageIcon(getClass().getResource("GB_R.png"));
		pics[99] = new ImageIcon(getClass().getResource("Greenland_R.png"));
		pics[100] = new ImageIcon(getClass().getResource("Iceland_R.png"));
		pics[101] = new ImageIcon(getClass().getResource("India_R.png"));
		pics[102] = new ImageIcon(getClass().getResource("Indonesia_R.png"));
		pics[103] = new ImageIcon(getClass().getResource("Irkutsk_R.png"));
		pics[104] = new ImageIcon(getClass().getResource("Japan_R.png"));
		pics[105] = new ImageIcon(getClass().getResource("Kamchatka_R.png"));
		pics[106] = new ImageIcon(getClass().getResource("Madagascar_R.png"));
		pics[107] = new ImageIcon(getClass().getResource("Middle East_R.png"));
		pics[108] = new ImageIcon(getClass().getResource("Mongolia_R.png"));
		pics[109] = new ImageIcon(getClass().getResource("N Africa_R.png"));
		pics[110] = new ImageIcon(getClass().getResource("N Europe_R.png"));
		pics[111] = new ImageIcon(getClass().getResource("New Guinea_R.png"));
		pics[112] = new ImageIcon(getClass().getResource("NW_G.png"));
		pics[113] = new ImageIcon(getClass().getResource("Ontario_R.png"));
		pics[114] = new ImageIcon(getClass().getResource("Peru_R.png"));
		pics[115] = new ImageIcon(getClass().getResource("Quebec_R.png"));
		pics[116] = new ImageIcon(getClass().getResource("S Africa_R.png"));
		pics[117] = new ImageIcon(getClass().getResource("S Europe_R.png"));
		pics[118] = new ImageIcon(getClass().getResource("Scandinavia_R.png"));
		pics[119] = new ImageIcon(getClass().getResource("Siam_R.png"));
		pics[120] = new ImageIcon(getClass().getResource("Siberia_R.png"));
		pics[121] = new ImageIcon(getClass().getResource("Ukraine_R.png"));
		pics[122] = new ImageIcon(getClass().getResource("Ural_R.png"));
		pics[123] = new ImageIcon(getClass().getResource("Venezuela_R.png"));
		pics[124] = new ImageIcon(getClass().getResource("W Australia_R.png"));
		pics[125] = new ImageIcon(getClass().getResource("W Europe_R.png"));
		pics[126] = new ImageIcon(getClass().getResource("WUSA_R.png"));
		pics[127] = new ImageIcon(getClass().getResource("Yakutsk_R.png"));
		pics[128] = new ImageIcon(getClass().getResource("Header.jpg"));				//Launch screen background
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
