package simulation;

import java.util.List;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Subclass for cell occupants for the spreading fire simulation.
 * 
 * @author Kelley Scroggs
 *
 */
public class FireOccupant extends CellOccupant {

	private static final double PROB_CATCH_FIRE = .15;
	private static final int MAX_TURNS_ON_FIRE = 5;
	private static final int EMPTY = 0;
	private static final int TREE = 1;
	private static final int FIRE = 2;

	private int turnsOnFire;

	/**
	 * creates a cell occupant for the spreading fire simulation.
	 * 
	 * @param initState
	 * @param initLocation
	 * @param initColor
	 * @param colors
	 */
	public FireOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		turnsOnFire = 0;
	}

	/**
	 * updates the number of turns a cell occupant has been on fire for. Called when
	 * the state is currently on fire.
	 */
	private void updateTurnsOnFire() {
		turnsOnFire++;
	}

	/**
	 * returns the number of turns a state has been on fire for.
	 * 
	 * @return
	 */
	private int getTurnsOnFire() {
		return turnsOnFire;
	}

	/**
	 * Calculate the next state of a fire occupant. States: Nothing, Tree, or Fire.
	 * Transitions: N -> N; F -> N; T -> F
	 */
	@Override
	public void calculateNextState(Grid grid) {
		if (this.getCurrentState() == EMPTY) {
			// no changes, might not be necessary
			this.noChange();
		} else if (this.getCurrentState() == TREE) {
			if (neighborOnFire(grid.getNeighbors(this))) {
				if (Math.random() < PROB_CATCH_FIRE) {
					this.setNextState(FIRE);
					this.setNextPaint(this.getTypeColors()[FIRE]);
				} else {
					this.noChange();
				}
			} else {
				this.noChange();
			}
		} else {
			if (getTurnsOnFire() == MAX_TURNS_ON_FIRE) {
				this.setNextState(EMPTY);
				this.setNextPaint(this.getTypeColors()[EMPTY]);
			} else {
				this.updateTurnsOnFire();
				this.noChange();
			}
		}
	}

	/**
	 * Helper method for calculateNextState method. Determines if one of the cell's
	 * neighbors is on Fire.
	 * 
	 * @param neighborsList
	 * @return
	 */
	private boolean neighborOnFire(List<CellOccupant> neighborsList) {
		for (CellOccupant neighbor : neighborsList) {
			if (neighbor.getCurrentState() == FIRE) {
				return true;
			}
		}
		return false;
	}

}
