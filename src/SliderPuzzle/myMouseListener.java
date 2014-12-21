package SliderPuzzle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

public class myMouseListener implements MouseListener
{
	SliderPuzzle myGame;

	public myMouseListener(SliderPuzzle game)
	{
		myGame = game;
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			myGame.tileClicked(e.getSource());
		}
	}

	public void mouseEntered(MouseEvent arg0) 
	{	
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
	}

	public void mouseReleased(MouseEvent arg0)
	{
	}

}
