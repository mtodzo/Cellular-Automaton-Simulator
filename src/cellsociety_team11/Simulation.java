package cellsociety_team11;

public abstract class Simulation {
	private boolean isRunning;
	private String simType;
	protected CellOccupant[][] myOccupants;
	
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
	
	
	public abstract void init(CellOccupant[][] grid);
	
	public abstract void setNextStates();
	
	public abstract void updateStates();
		
}
