package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.paint.Paint;

public abstract class CellOccupant {
	private int currentState;
	private int nextState;
	private int[] currentLocation;
	private int[] nextLocation;
	private Paint currentColor;
	private Paint nextColor;

	public CellOccupant(int initState, int[] initLocation, Paint initColor) {
		currentState = initState;
		currentLocation = initLocation;
		currentColor = initColor;
		nextState = currentState;
		nextLocation = currentLocation;
		nextColor = currentColor;
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
	public void setCurrentPaint(Paint color) {
		currentColor = color;
	}
	
	// set next paint
	public void setNextPaint(Paint color) {
		nextColor = color;
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
	
	public void noChange() {
		this.setNextState(this.currentState);
		this.setNextLocation(this.currentLocation);
		this.setNextPaint(this.currentColor);
	}
	
	public abstract void calculateNextState(ArrayList<CellOccupant> neighborsList);
	// public abstract int getTurnsOnFire();
	
	// public abstract void updateTurnsOnFire();
	

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
