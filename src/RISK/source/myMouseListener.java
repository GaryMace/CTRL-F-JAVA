package source;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;


	public class myMouseListener implements MouseListener
	{
		Risk myGame;
		
		public myMouseListener(Risk g)
		{
			myGame = g;
		}
		
		public void mouseClicked(MouseEvent e) 
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				myGame.leftButtonClicked(e.getSource());		//source here is a button being clicked
			}
			else if(SwingUtilities.isRightMouseButton(e))
			{
				myGame.rightButtonClicked(e.getSource());
			}
		}

		public void mouseEntered(MouseEvent e)
		{
			
		}

		public void mouseExited(MouseEvent e) 
		{
			
		}

		public void mousePressed(MouseEvent e) 
		{
			
		}

		public void mouseReleased(MouseEvent e)
		{
			
		}
	}