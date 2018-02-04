package cellsociety_team11;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class WatorOccupant extends CellOccupant {
	private int turnsAlive;
	private int energyUnits;
	private static final int FISH_TO_REPRODUCE = 2;
	private static final int SHARK_TO_REPRODUCE = 4;
	private static final int SHARK_TO_DIE = 2;
	private static final Paint[] typeColors = { Color.WHITE, Color.GREEN, Color.BLUE };
	private static final int EMPTY_STATE = 0;
	private static final int FISH_STATE = 1;
	private static final int SHARK_STATE = 2;

	public WatorOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState, initLocation, initColor);
		// 1 for initially fire
		turnsAlive = 0;
		// nextAlive = turnsAlive;
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
	 * Calculates the next state. Does so by switching the information in each cell.
	 */
	@Override
	public void calcNextState(ArrayList<CellOccupant> neighborsList) {

		if (this.getCurrentState() == FISH_STATE && this.getNextState() == FISH_STATE) {
			WatorOccupant neighborCell = neighborIsType(EMPTY_STATE, neighborsList);

			if (neighborCell != null) {
				// MOVE TO EMPTY NEIGHBOR, either we leave a fish behind or we dont
				switchCells(this, neighborCell);
				// System.out.println(turnsAlive);
				if (neighborCell.turnsAlive >= FISH_TO_REPRODUCE) {
					this.setNextState(FISH_STATE);
					this.resetTurnsAlive();
					neighborCell.resetTurnsAlive();
				} else {
					neighborCell.incTurnsAlive();
				}
			}

			else {
				this.setNextState(this.getCurrentState());
				this.incTurnsAlive();
			}
		} else if (this.getCurrentState() == SHARK_STATE && this.getNextState() == SHARK_STATE) {

			WatorOccupant fishNeighbor = neighborIsType(FISH_STATE, neighborsList);

			if (fishNeighbor != null) {
				switchCells(this, fishNeighbor);
				fishNeighbor.resetEnergyUnits();
				if (fishNeighbor.turnsAlive >= SHARK_TO_REPRODUCE) {
					this.setNextState(SHARK_STATE);
					this.resetTurnsAlive();
					this.resetEnergyUnits();
					fishNeighbor.resetTurnsAlive();
					// System.out.println("Shark eats fish and reproduces");

				} else {
					this.setNextState(EMPTY_STATE);
					this.resetTurnsAlive();
					this.resetEnergyUnits();
					fishNeighbor.incTurnsAlive();
					// System.out.println("Shark eats fish, no babies");
				}

			} else {
				// move to empty
				WatorOccupant emptyNeighbor = neighborIsType(EMPTY_STATE, neighborsList);

				if (emptyNeighbor != null) {
					switchCells(this, emptyNeighbor);
					if (emptyNeighbor.turnsAlive >= SHARK_TO_REPRODUCE) {
						this.setNextState(SHARK_STATE);
						this.resetTurnsAlive();
						this.resetEnergyUnits();
						emptyNeighbor.resetTurnsAlive();
						// System.out.println("SHARK MOVING and REPRODUCING");

					} else {
						this.setNextState(EMPTY_STATE);
						this.resetTurnsAlive();
						this.resetEnergyUnits();
						emptyNeighbor.incTurnsAlive();
						// System.out.println("SHARK JUST MOVING");
					}
					emptyNeighbor.decEnergyUnits();
					if (emptyNeighbor.energyUnits <= 0) {
						// System.out.println("SHARK DIES\n");
						emptyNeighbor.setNextState(EMPTY_STATE);
						emptyNeighbor.resetEnergyUnits();
						emptyNeighbor.resetTurnsAlive();
						emptyNeighbor.setNextPaint(typeColors[EMPTY_STATE]);
					}

				} else {
					// System.out.println("SHARK STUCK");
					this.decEnergyUnits();
					this.incTurnsAlive();
					if (this.energyUnits <= 0) {
						// System.out.println("SHARK DIES");
						this.setNextState(EMPTY_STATE);
						this.resetEnergyUnits();
						this.resetTurnsAlive();
						this.setNextPaint(typeColors[EMPTY_STATE]);
					}
				}
			}
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

	/**
	 * Helper method for calculateNextState method. Determines which if any of the
	 * cell's neighbors are of the type specified. If there are more than one.
	 * returns a random one. If there are no neighbors of the type specified,
	 * returns null.
	 * 
	 * @param neighborsList
	 * @param type
	 * @return
	 */
	private WatorOccupant neighborIsType(int type, ArrayList<CellOccupant> neighborsList) {
		// System.out.println("CALL TO neighborIsType(" + type +")");
		Collections.shuffle(neighborsList);
		for (CellOccupant neighbor : neighborsList) {
			if (neighbor.getCurrentState() == type && neighbor.getNextState() == type) {
				// System.out.println("found type: " + neighbor.getCurrentState());
				return (WatorOccupant) neighbor;
			}
		}
		return null;
	}
}
