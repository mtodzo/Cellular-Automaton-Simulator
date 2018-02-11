package grids;
import simulation.CellOccupant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * The Grid superclass is an abstract class that implements a number of methods for accessing or changing CellOccupants that are part
 * of the grid. The one abstract method is getNeighbors. This is implemented by each of the subclasses since it is the only method that
 * varies with grid type.
 */
public abstract class Grid {
	private CellOccupant[][] myGrid;
	
	public Grid(CellOccupant[][] grid) {
		myGrid = grid;
	}
	
	public CellOccupant[][] getGrid() {
		return myGrid;
	}
	public int getLength() {
		return myGrid.length;
	}
	
	public int getWidth() {
		return myGrid[0].length;
	}
	/*
	 * Sets the specified cell within myOccupants to hold the passed type
	 * @param x is the x-coordinate at which the occupant is set
	 * @param y is the y-coordinate at which the occupant is set
	 * @param cell is the CellOccupant which will be placed at the specified location
	 * @return Nothing.
	 */
	public void setOccupant(int x, int y, CellOccupant cell) {
		myGrid[x][y] = cell;
	}
	/*
	 * Returns the CellOccupant at the specified location
	 * @param x specifies the x coordinate of the desired occupant
	 * @param y specifies the y coordinate of the desired occupant
	 * @return CellOccupant at the specified location
	 */
	public CellOccupant getOccupant(int x, int y) {
		return myGrid[x][y];
	}

	/*
	 * Retrieves the positions of occupants who's current and next state is type
	 * @param type is the state of the occupants being searched for
	 * @return List<int[]> with each int[] holding the x and y locations of each cell of the specified type 
	 */
	public List<int[]> getCurrentAndNextPositionsOfType(int type){
		List<int[]> positionsOfType = new ArrayList<>();
		for(int i = 0; i < myGrid.length;i++) {
			for(int j = 0; j < myGrid[0].length;j++) {
				if (myGrid[i][j].getCurrentState() == type && myGrid[i][j].getNextState() == type) {
					positionsOfType.add(myGrid[i][j].getCurrentLocation());
				}
			}
		}
		return positionsOfType;
	}
	
	/*
	 * Retrieves the positions of occupants who's next state is type
	 * @param type is the state of the occupants being searched for
	 * @return List<int[]> with each int[] holding the x and y locations of each cell of the specified type 
	 */
	public List<int[]> getNextPositionsOfType(int type){
		List<int[]> positionsOfType = new ArrayList<int[]>();
		for(int i = 0; i < myGrid.length;i++) {
			for(int j = 0; j < myGrid[0].length;j++) {
				if (myGrid[i][j].getNextState() == type) {
					positionsOfType.add(new int[] {i,j});
				}
			}
		}
		return positionsOfType;
	}

	/*
	 * This method is implemented by each of the Grid subclasses to appropriately find the neighbors of the given cell.
	 * @param cell is the cell for which the neighbors are being found
	 * @return List<CellOccupant> containing all the cell's neighbors
	 */
	public abstract List<CellOccupant> getNeighbors(CellOccupant cell);
	
	/*
	 * Find's a cell's neighbor that is of the specified type
	 * @param type is the state that is being checked for
	 * @param neighbors is the list of the cell's neighbors
	 * @return Returns a random neighbor CellOccupant that is of specified type
	 */
	public CellOccupant getNeighborOfType(List<CellOccupant> neighbors, int type){
		List<CellOccupant> neighborsOfType = new ArrayList<>();
		for (CellOccupant current: neighbors) {
			if (current.getCurrentState() == type && current.getNextState() == type) {
				neighborsOfType.add(current);
			}
		}
		if(neighborsOfType.isEmpty()) {
			return null;
		}

		Random r = new Random();
		int randomIndex = r.nextInt(neighborsOfType.size());
		return neighborsOfType.get(randomIndex);
	}
}

