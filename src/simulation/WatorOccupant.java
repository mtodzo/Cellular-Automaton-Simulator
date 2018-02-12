package simulation;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * CellOccupant subclass for Wator predator prey simulation.
 * 
 * @author Kelley Scroggs
 *
 */
public class WatorOccupant extends CellOccupant {
	private int turnsAlive;
	private int energyUnits;
	private static final int FISH_TO_REPRODUCE = 4;
	private static final int SHARK_TO_REPRODUCE = 20;
	private static final int SHARK_TO_DIE = 5;

	private static final int EMPTY_STATE = 0;
	private static final int FISH_STATE = 1;
	private static final int SHARK_STATE = 2;

	/**
	 * creates watorOccupant subclass upon intialization of the grid.
	 * 
	 * @param initState
	 * @param initLocation
	 * @param initColor
	 * @param colors
	 */
	public WatorOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		turnsAlive = 0;
		if (initState == SHARK_STATE) {
			resetEnergyUnits();
		} else {
			energyUnits = 0;
		}
	}

	private void incTurnsAlive() {
		turnsAlive++;
	}

	private void resetTurnsAlive() {
		turnsAlive = 0;
	}

	private void decEnergyUnits() {
		energyUnits--;
	}

	private void resetEnergyUnits() {
		energyUnits = SHARK_TO_DIE;
	}

	private void setEnergy(int x) {
		energyUnits = x;
	}

	private void setAlive(int x) {
		turnsAlive = x;
	}

	/**
	 * Calls the correct method to update the state based on the current state of
	 * the cell occupant.
	 */
	@Override
	public void calculateNextState(Grid grid) {
		if (this.getCurrentState() == FISH_STATE && this.getNextState() == FISH_STATE) {
			changeFishState(grid);
		} else if (this.getCurrentState() == SHARK_STATE && this.getNextState() == SHARK_STATE) {
			changeSharkState(grid);
		}
	}

	/**
	 * Changes the next state for a WatorOccupant of type shark. If a shark has a
	 * fish for a neighbor it moves to a random fish neighbor and eats it. If not a
	 * shark moves to an empty neighbor if it has one. When a shark moves to a
	 * neighbor, it reproduces, leaving behind a new shark, if it has been alive
	 * long enough. If a shark has not eaten enough fish to sustain it, it dies,
	 * leaving behind an empty spot.
	 * 
	 * @param grid
	 */
	private void changeSharkState(Grid grid) {
		WatorOccupant fishNeighbor = (WatorOccupant) grid.getNeighborOfType(grid.getNeighbors(this), FISH_STATE);
		if (fishNeighbor != null) {
			switchCells(this, fishNeighbor);
			fishNeighbor.resetEnergyUnits();
			if (fishNeighbor.turnsAlive >= SHARK_TO_REPRODUCE) {
				this.setNextState(SHARK_STATE);
				this.setNextPaint(this.getTypeColors()[SHARK_STATE]);
				this.resetTurnsAlive();
				this.resetEnergyUnits();
				fishNeighbor.resetTurnsAlive();
			} else {
				this.setNextState(EMPTY_STATE);
				this.setNextPaint(this.getTypeColors()[EMPTY_STATE]);
				this.resetTurnsAlive();
				this.resetEnergyUnits();
				fishNeighbor.incTurnsAlive();
			}

		} else {
			// move to empty
			WatorOccupant emptyNeighbor = (WatorOccupant) grid.getNeighborOfType(grid.getNeighbors(this), EMPTY_STATE);
			if (emptyNeighbor != null) {
				switchCells(this, emptyNeighbor);
				if (emptyNeighbor.turnsAlive >= SHARK_TO_REPRODUCE) {
					this.setNextState(SHARK_STATE);
					this.setNextPaint(this.getTypeColors()[SHARK_STATE]);
					this.resetTurnsAlive();
					this.resetEnergyUnits();
					emptyNeighbor.resetTurnsAlive();
				} else {
					this.setNextState(EMPTY_STATE);
					this.setNextPaint(this.getTypeColors()[EMPTY_STATE]);
					this.resetTurnsAlive();
					this.resetEnergyUnits();
					emptyNeighbor.incTurnsAlive();
				}
				emptyNeighbor.decEnergyUnits();
				if (emptyNeighbor.energyUnits <= 0) {
					emptyNeighbor.setNextState(EMPTY_STATE);
					emptyNeighbor.setNextPaint(this.getTypeColors()[EMPTY_STATE]);
					emptyNeighbor.resetEnergyUnits();
					emptyNeighbor.resetTurnsAlive();
				}

			} else {
				this.decEnergyUnits();
				this.incTurnsAlive();
				if (this.energyUnits <= 0) {
					this.setNextState(EMPTY_STATE);
					this.setNextPaint(this.getTypeColors()[EMPTY_STATE]);
					this.resetEnergyUnits();
					this.resetTurnsAlive();
				}
			}
		}
	}

	/**
	 * Changes the next state for a WatorOccupant of type fish. If a fish has an
	 * empty neighbor it moves to it. If it has been alive long enough, it
	 * reproduces, in the same way that the shark does.
	 * 
	 * @param grid
	 */
	private void changeFishState(Grid grid) {
		WatorOccupant neighborCell = (WatorOccupant) grid.getNeighborOfType(grid.getNeighbors(this), EMPTY_STATE);
		if (neighborCell != null) {
			// MOVE TO EMPTY NEIGHBOR, either we leave a fish behind or we dont
			switchCells(this, neighborCell);
			if (neighborCell.turnsAlive >= FISH_TO_REPRODUCE) {
				this.setNextState(FISH_STATE);
				this.setNextPaint(this.getTypeColors()[FISH_STATE]);
				this.resetTurnsAlive();
				neighborCell.resetTurnsAlive();
			} else {
				neighborCell.incTurnsAlive();
			}
		} else {
			this.setNextState(this.getCurrentState());
			this.incTurnsAlive();
		}
	}

	/**
	 * Helper method to switch the information of two cells. Used when a fish/or a
	 * shark swims from one cell to another. A fish or shark can switch with an
	 * empty cell. A shark can also switch with a fish cell, but afterwards need to
	 * make fish cell empty. this method does not handle the logic of implementing
	 * the correct information after the switch. only changing the information.
	 * between the two.
	 * 
	 * @param cellOne
	 * @param cellTwo
	 */
	private void switchCells(WatorOccupant cellOne, WatorOccupant cellTwo) {
		int temp_state = cellOne.getCurrentState();
		Paint temp_paint = cellOne.getCurrentPaint();
		int temp_energy = cellOne.energyUnits;
		int temp_turns = cellOne.turnsAlive;

		cellOne.setNextState(cellTwo.getCurrentState());
		cellOne.setNextPaint(cellTwo.getCurrentPaint());
		cellOne.setEnergy(cellTwo.energyUnits);
		cellOne.setAlive(cellTwo.turnsAlive);

		cellTwo.setNextState(temp_state);
		cellTwo.setNextPaint(temp_paint);
		cellTwo.setEnergy(temp_energy);
		cellTwo.setAlive(temp_turns);
	}
}
