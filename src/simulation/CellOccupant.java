package simulation;

import grids.Grid;
import javafx.scene.paint.Paint;

/**
 * CellOccupant super class to represent types of states in each simulation.
 * 
 * @author Kelley Scroggs
 *
 */
public abstract class CellOccupant {

	private int currentState;
	private int nextState;
	private int[] currentLocation;
	private int[] nextLocation;
	private Paint currentColor;
	private Paint nextColor;
	private Paint[] typeColors;

	/**
	 * creates cell occupant. called when gird is initialized.
	 * 
	 * @param initState
	 *            initial state
	 * @param initLocation
	 *            initial x,y location
	 * @param initColor
	 *            initial color
	 * @param colors
	 *            list of colors for all state types in simulation
	 */
	public CellOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		currentState = initState;
		currentLocation = initLocation;
		currentColor = initColor;
		nextState = currentState;
		nextLocation = currentLocation;
		nextColor = currentColor;
		typeColors = colors;
	}

	// get all the color array for simulation type
	public Paint[] getTypeColors() {
		return typeColors;
	}

	// get current paint
	public Paint getCurrentPaint() {
		return currentColor;
	}

	// get next paint
	public Paint getNextPaint() {
		return nextColor;
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

	// set current paint
	public void setCurrentPaint() {
		currentColor = this.getNextPaint();
	}

	// set next paint
	public void setNextPaint(Paint color) {
		nextColor = color;
	}

	// set current state
	public void setCurrentState() {
		currentState = this.getNextState();
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

	/**
	 * called when the cell occupant does not need to be updated.
	 */
	public void noChange() {
		this.setNextState(this.currentState);
		this.setNextLocation(this.currentLocation);
		this.setNextPaint(this.currentColor);
	}

	/**
	 * Updates the cell occupants next state, location, paint and other relevant
	 * information based on the update rules specified for each simulation.
	 * 
	 * @param grid
	 */
	public abstract void calculateNextState(Grid grid);

}
