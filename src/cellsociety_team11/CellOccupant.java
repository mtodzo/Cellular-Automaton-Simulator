package cellsociety_team11;

public abstract class CellOccupant {
	private int currentState;
	private int nextState;
	private int[] currentLocation;
	private int[] nextLocation;

	public CellOccupant(int initState, int[] initLocation) {
		currentState = initState;
		currentLocation = initLocation;
		nextState = currentState;
		nextLocation = currentLocation;
	}

	// get current state
	public int getCurrentState() {
		return currentState;
	}

	// get next state
	public int getNextState() {
		return nextState;
	}

	// get current location
	public int[] getCurrentLocation() {
		return currentLocation;
	}

	// get next location
	public int[] getNextLocation() {
		return nextLocation;
	}

	// set current state
	public void setCurrentState(int state) {
		currentState = state;
	}

	// set next state
	public void setNextState(int state) {
		nextState = state;
	}

	// set current location
	public void setCurrentLocation(int[] location) {
		currentLocation = location;
	}

	// set next location
	public void setNextLocation(int[] location) {
		nextLocation = location;
	}

	public abstract int getTurnsOnFire();
	
	public abstract void updateTurnsOnFire();
	

	// TODO: add subclasses:
	// Shark
	// Fish
	// Cells
	// Community1
	// Community2
	// Tree
	// Fire
	// Blank
}
