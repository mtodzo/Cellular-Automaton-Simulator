package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.paint.Paint;

public class SegOccupant extends CellOccupant {
	private static double similarityNeeded = .4;
	private static int EMPTY = 0;
	public SegOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState, initLocation, initColor);
	}
	/*
	 * Calculates the percentage of similar neighbors
	 * If it is below the threshold, the cell moves (see move() method)
	 * Otherwise it does not change
	 */
	@Override
	public void calculateNextState(Grid grid) {
		int similarCount = 0;
		for (CellOccupant neighbor: grid.getNeighbors(this)) {
			if (neighbor.getCurrentState() == this.getCurrentState()) { 
				similarCount++;
			}
		}
		if ((double)similarCount/grid.getNeighbors(this).size() < similarityNeeded) {
			int random = (int) Math.random()*grid.getNextPositionsOfType(EMPTY).size();
			this.setNextState(EMPTY);
			this.setNextLocation(grid.getNextPositionsOfType(EMPTY).get(random));
			grid.setOccupant(grid.getNextPositionsOfType(EMPTY).get(random)[0], grid.getNextPositionsOfType(EMPTY).get(random)[1], this);
		}
		else {
			this.noChange();
		}
	}
}

