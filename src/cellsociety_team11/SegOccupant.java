package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SegOccupant extends CellOccupant {
	private static double similarityNeeded = .24;
	private static int EMPTY = 0;
	private static final Paint[] typeColors = { Color.WHITE, Color.RED, Color.BLUE, Color.ORANGE};
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
		if (this.getCurrentState() != EMPTY && (double)similarCount/grid.getNeighbors(this).size() < similarityNeeded) {
			//this.setNextPaint(typeColors[3]);
			int random = (int) Math.floor(Math.random()*grid.getNextPositionsOfType(EMPTY).size());
			int [] emptyPos = grid.getNextPositionsOfType(EMPTY).get(random);
			SegOccupant nextCell = (SegOccupant) grid.getOccupant(emptyPos[0], emptyPos[1]);
			
			nextCell.setNextState(this.getCurrentState());
			nextCell.setNextPaint(this.getCurrentPaint());
			
			this.setNextState(EMPTY);
			this.setNextPaint(typeColors[this.getNextState()]);
		}
		else {
			this.noChange();
		}
	}
}

