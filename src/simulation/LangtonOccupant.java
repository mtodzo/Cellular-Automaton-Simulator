package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Cell occupant subclass for the Langton's Loop simulation.
 * 
 * @author Kelley Scroggs
 *
 */
public class LangtonOccupant extends CellOccupant {

	private static final int NUM_STATES = 8;
	private LangtonRules[] myStateRules = new LangtonRules[NUM_STATES];

	/**
	 * Helper method called when a new occupant is created. creates an array of
	 * langton rules objects. the position of the langtonRules object in the array
	 * corresponds to what initial state it remembers the rules for.
	 */
	private void setRules() {
		for (int i = 0; i < NUM_STATES; i++) {
			myStateRules[i] = new LangtonRules(i);
		}
	}

	/**
	 * Langton occupant constructor, called during initialization of the langton's
	 * loop grid.
	 * 
	 * @param initState
	 * @param initLocation
	 * @param initColor
	 * @param colors
	 */
	public LangtonOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		setRules();
	}

	/**
	 * calculates the next state and paint for the cell occupant objects in the
	 * langtons loop simulation.
	 */
	@Override
	public void calculateNextState(Grid grid) {
		List<Integer> neighborsStates = new ArrayList<>();
		List<CellOccupant> neighbors = grid.getNeighbors(this);
		for (CellOccupant neighbor : neighbors) {
			neighborsStates.add(neighbor.getCurrentState());
		}
		if (sumList(neighborsStates) > 0) {
			int nextState = myStateRules[this.getCurrentState()].getNextState(neighborsStates);
			if (nextState != -1) {
				this.setNextState(nextState);
				this.setNextPaint(this.getTypeColors()[nextState]);
			} else {
				System.out.println("NO RULE FOR STATE: " + this.getCurrentState() + " with neighbors: "
						+ neighborsStates.toString());
				this.setNextState(this.getCurrentState());
				this.setNextPaint(this.getCurrentPaint());
			}
		}
	}

	/**
	 * Helper method called from calculateNextState(). Used to reduce time by
	 * eliminating meaningless calculations. Objects with only null state neighbors
	 * do not need to be updated.
	 * 
	 * @param myList
	 * @return
	 */
	private int sumList(List<Integer> myList) {
		int sum = 0;
		for (Integer i : myList) {
			sum += i;
		}
		return sum;
	}
}
