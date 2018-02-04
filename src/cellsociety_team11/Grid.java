package cellsociety_team11;

import java.util.ArrayList;
import java.util.List;

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
	
	public ArrayList<int[]> getNextPositionsOfType(int type){
		ArrayList<int[]> positionsOfType = new ArrayList<int[]>();
		for(int i = 0; i < myGrid.length;i++) {
			for(int j = 0; j < myGrid[0].length;j++) {
				if (myGrid[i][j].getNextState() == type) {
					positionsOfType.add(myGrid[i][j].getCurrentLocation());
				}
			}
		}
		return positionsOfType;
	}
	
	public abstract List<CellOccupant> getNeighbors(CellOccupant cell);
	
	public void printGrid() {
		for(CellOccupant[] row : myGrid) {
			String buff = "";
			for(CellOccupant occ : row) {
				buff += occ.getCurrentState();
				buff += "\t";
			}
			System.out.println(buff);
		}
		System.out.println("\n");
	}
}
