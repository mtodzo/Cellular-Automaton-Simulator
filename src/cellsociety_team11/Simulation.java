package cellsociety_team11;

public abstract class Simulation {
	private boolean isRunning;
	private String simType;
	private CellOccupant[][] myOccupants;
	
	public Simulation(CellOccupant[][] grid) {
		myOccupants =grid;
		isRunning = true;
	}
	
	public void setOccupant(int x, int y, CellOccupant type) {
		myOccupants[x][y] = type;
	}
	
	public void setRunStatus(boolean status) {
		isRunning = status;
	}
	public boolean getStatus() {
		return isRunning;
	}
	
	public String getType() {
		return simType;
	}
	
	public CellOccupant[][] getOccupantGrid() {
		return myOccupants;
	}
	
	public CellOccupant getOccupant(int x, int y) {
		return myOccupants[x][y];
	}
	
	
	public abstract void initializeSim();
	
	public abstract void setNextStates();
	
	public abstract void updateStates();
		
}
