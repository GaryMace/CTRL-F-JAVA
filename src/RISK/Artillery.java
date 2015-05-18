package source;

public class Artillery extends Unit{

	public Artillery() {
		//Artillery units worth 10 Infantry
		setUValue(10);
		setULives(10);
		setUClass("Artillery");
	}
}
