package cellsociety_team11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	 */
	public void setOccupant(int x, int y, CellOccupant cell) {
		myGrid[x][y] = cell;
	}
	/*
	 * Returns the CellOccupant at the specified location
	 */
	public CellOccupant getOccupant(int x, int y) {
		return myGrid[x][y];
	}
	
	public List<CellOccupant> iterGrid() {
		List<CellOccupant> allOccupants = new ArrayList<CellOccupant>();
		for(CellOccupant[] row : myGrid) {
			for(CellOccupant elem : row) {
				allOccupants.add(elem);
			}
		}
		return allOccupants;
	}
	
	public ArrayList<int[]> getCurrentPositionsOfType(int type){
		ArrayList<int[]> positionsOfType = new ArrayList<int[]>();
		for(int i = 0; i < myGrid.length;i++) {
			for(int j = 0; j < myGrid[0].length;j++) {
				if (myGrid[i][j].getCurrentState() == type) {
					positionsOfType.add(myGrid[i][j].getCurrentLocation());
				}
			}
		}
		return positionsOfType;
	}
	
	public List<int[]> getNextPositionsOfType(int type){
		ArrayList<int[]> positionsOfType = new ArrayList<int[]>();
		for(int i = 0; i < myGrid.length;i++) {
			for(int j = 0; j < myGrid[0].length;j++) {
				if (myGrid[i][j].getNextState() == type) {
					positionsOfType.add(new int[] {i,j});
					//myGrid[i][j].getCurrentLocation());
				}
			}
		}
		return positionsOfType;
	}
	
	public abstract List<CellOccupant> getNeighbors(CellOccupant cell);
	
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
