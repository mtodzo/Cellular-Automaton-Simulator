package cellsociety_team11;

import java.util.ArrayList;

public abstract class Grid {
	private CellOccupant[][] myGrid;
	
	public Grid(int width, int height) {
		myGrid = new CellOccupant[height][width];
	}
	
	public CellOccupant[][] getMyGrid() {
		return myGrid;
	}
	public int getGridHeight() {
		return myGrid.length;
	}
	
	public int getGridWidth() {
		return myGrid[0].length;
	}
	
	public void putInGrid(int x, int y, CellOccupant cell) {
		myGrid[x][y] = cell;
	}
	
	public CellOccupant getFromGrid(int x, int y) {
		return myGrid[x][y];
	}
	
	public ArrayList<CellOccupant> iterGrid() {
		ArrayList<CellOccupant> allOccupants = new ArrayList<CellOccupant>();
		for(CellOccupant[] row : myGrid) {
			for(CellOccupant elem : row) {
				allOccupants.add(elem);
			}
		}
		return allOccupants;
	}
	
	public ArrayList<int[]> getPositionsOfType(int type){
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
	
	public abstract ArrayList<CellOccupant> getNeighbors(CellOccupant cell);
	
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
