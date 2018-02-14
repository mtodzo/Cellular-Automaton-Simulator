package simulation;
import grids.Grid;

import javafx.scene.paint.Paint;

/*
 * @author Miles Todzo
 * Subclass of CellOccupant for the Segregation simulation.
 */
public class SegOccupant extends CellOccupant {
	private static double similarityNeeded = .24;
	private static int EMPTY = 0;

	public SegOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
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
			int random = (int) Math.floor(Math.random()*grid.getNextPositionsOfType(EMPTY).size());
			int [] emptyPos = grid.getNextPositionsOfType(EMPTY).get(random);

			SegOccupant nextCell = (SegOccupant) grid.getOccupant(emptyPos[0], emptyPos[1]);
			
			nextCell.setNextState(this.getCurrentState());
			nextCell.setNextPaint(this.getCurrentPaint());
			
			this.setNextState(EMPTY);
			this.setNextPaint(this.getTypeColors()[EMPTY]);
		}
	}
}
