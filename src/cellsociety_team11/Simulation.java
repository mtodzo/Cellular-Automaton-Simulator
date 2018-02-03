package cellsociety_team11;

import java.util.ArrayList;

public class Simulation {
	private boolean isRunning;
	private String simType;
	protected CellOccupant[][] myOccupants;
	private ArrayList<int[]> myEmptyLocs;
	private static final int EMPTY = 0;
	
	public Simulation(CellOccupant[][] grid) {
		myOccupants =grid;
		isRunning = true;
	}
	
	/*
	 * Sets the specified cell within myOccupants to hold the passed type
	 */
	public void setOccupant(int x, int y, CellOccupant type) {
		myOccupants[x][y] = type;
	}
	
	/*
	 * Change the run status to start, stop, or pause the simulation.
	 */
	public void setRunStatus(boolean status) {
		isRunning = status;
	}
	/*
	 * Returns the run status of the simulation
	 */
	public boolean getStatus() {
		return isRunning;
	}
	
	/*
	 * Returns the type of the simulation
	 */
	public String getType() {
		return simType;
	}
	/*
	 * Returns the myOccupants grid
	 */
	public CellOccupant[][] getOccupantGrid() {
		return myOccupants;
	}
	
	/*
	 * Returns the CellOccupant at the specified location
	 */
	public CellOccupant getOccupant(int x, int y) {
		return myOccupants[x][y];
	}
	
	
	
	public void setNextStates() {
		for (int y=0; y< myOccupants.length; y++) {
			for (int x=0; x<myOccupants[0].length; x++) {
				ArrayList<CellOccupant> neighbors = getNeighbors(myOccupants[x][y]);
				myOccupants[x][y].calcNextState(neighbors);
			}
		}
	}
	
	public  void updateStates() {
		for (int y=0; y< myOccupants.length; y++) {
			for (int x=0; x<myOccupants[0].length; x++) {
				myOccupants[x][y].setCurrentState();
				myOccupants[x][y].setCurrentPaint();
			}
		}
	}
	
	private ArrayList<CellOccupant> getNeighbors(CellOccupant current) {
		int xLoc = current.getCurrentLocation()[0];
		int yLoc = current.getCurrentLocation()[1];
		ArrayList<CellOccupant> neighbors = new ArrayList<>();
		if (xLoc < myOccupants[0].length-1) {
			neighbors.add(myOccupants[xLoc+1][yLoc]);
		}
		if (xLoc !=0) {
			neighbors.add(myOccupants[xLoc-1][yLoc]);
		}
		if (yLoc != 0) {
			neighbors.add(myOccupants[xLoc][yLoc-1]);
		}
		if (yLoc < myOccupants.length-1) {
			neighbors.add(myOccupants[xLoc][yLoc+1]);
		}
		return neighbors;
	}
	
	private ArrayList<int[]> getEmptyLocations(){
		for (int y=0; y<myOccupants.length; y++) {
			for (int x=0; x<myOccupants[0].length; x++) {
				if (myOccupants[x][y].getCurrentState() == EMPTY) {
					myEmptyLocs.add(myOccupants[x][y].getCurrentLocation());
				}
			}
		}
		return myEmptyLocs;
		
	}		
}