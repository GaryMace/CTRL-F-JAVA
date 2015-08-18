package source;

import java.util.Random;

public class Die {
	private int faceVal;
	
	public Die () {
		
	}
	public void rollDie() {
		Random rand = new Random();
		faceVal = rand.nextInt(6)+1;
	}
	public int getVal() {
		return faceVal;
	}
}
