package cellsociety_team11;

public class DisplayGrid 
{
	private CellOccupant[][] GRID;
	private int NUMBER_NEIGHBORS;
	
	public DisplayGrid(CellOccupant[][] grid, int neighbors)
	{
		setGrid(grid);
		setNumNeighbors(neighbors);
		
	}
	
	public CellOccupant[][] getGrid() 
	{
		return GRID;
	}

	public void setGrid(CellOccupant[][] grid) 
	{
		GRID = grid;
	}

	public int getNumNeighbors() 
	{
		return NUMBER_NEIGHBORS;
	}

	public void setNumNeighbors(int neighbors)
	{
		NUMBER_NEIGHBORS = neighbors;
	}

}
