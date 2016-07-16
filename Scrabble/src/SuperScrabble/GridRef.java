package SuperScrabble;

public class GridRef {
	private int row, column;
	
	public GridRef (int rowIndex, int columnIndex) {
		row = rowIndex;
		column = columnIndex;
	}
	
	public int getRow () {
		return row;
	}
	
	public int getColumn () {
		return column;
	}
}
