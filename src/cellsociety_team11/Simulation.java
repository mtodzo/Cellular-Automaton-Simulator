package cellsociety_team11;

import java.util.ArrayList;

public abstract class Simulation {
	private int gridWidth;
	private int gridHeight;
	private boolean simRunning;
	private int simType;
	private CellOccupant[][] myGrid;
	
	public Simulation(int width, int height, int type) {
		gridWidth = width;
		gridHeight = height;
		simType = type;
		myGrid = new CellOccupant[gridHeight][gridWidth];
		simRunning = true;
	}
	
	public void doneRunning() {
		simRunning = false;
	}
	
	public void setPos(int x, int y, CellOccupant type) {
		myGrid[x][y] = type;
	}
	
	public int getWidth() {
		return gridWidth;
	}
	
	public int getHeight() {
		return gridHeight;
	}
	
	public boolean getStatus() {
		return simRunning;
	}
	
	public int getType() {
		return simType;
	}
	
	public CellOccupant[][] getGrid() {
		return myGrid;
	}
	
	public CellOccupant getPos(int x, int y) {
		return myGrid[x][y];
	}
	
	
	public abstract void initializeSim();
	
	public abstract void passOne();
	
	public abstract void passTwo();
	
	public ArrayList<int[]> getPositionsOfType(int type){
		ArrayList<int[]> positionsOfType = new ArrayList<int[]>();
		for(int i = 0; i < gridWidth;i++) {
			for(int j = 0; j < gridHeight;j++) {
				if (myGrid[i][j].getCurrentState() == type) {
					positionsOfType.add(myGrid[i][j].getCurrentLocation());
				}
			}
		}
		return positionsOfType;
	}
	
	public ArrayList<CellOccupant> getNeighbors(int xPos, int yPos) {
		ArrayList<CellOccupant> myNeighbors = new ArrayList<CellOccupant>();
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(!(i==0 && j==0)) {
					int xGet = myGrid[xPos][yPos].getCurrentLocation()[0] + i;
					int yGet = myGrid[xPos][yPos].getCurrentLocation()[1] + j;
					if(xGet >= 0 && xGet < this.getWidth() && yGet >= 0 && yGet < this.getHeight()) {
						myNeighbors.add(this.getPos(xGet,yGet));
					}	
				}
			}
		}
		return myNeighbors;
	}
	public void printGrid() {
		for(CellOccupant[] row : this.getGrid()) {
			String buff = "";
			for(CellOccupant occ : row) {
				buff += occ.getCurrentState();
				buff += "   ";
			}
			System.out.println(buff);
		}
		System.out.println("\n\n");
	}
	
		
}
