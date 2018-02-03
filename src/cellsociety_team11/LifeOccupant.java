package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/*Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
*Any live cell with two or three live neighbors lives on to the next generation.
*Any live cell with more than three live neighbors dies, as if by overpopulation.
*Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
*/

public class LifeOccupant extends CellOccupant{
	private static int DEAD = 0;
	private static int ALIVE = 0;
	private static int minLiveNeighbors = 2;
	private static int numToReproduce = 3;
	private static int maxLiveNeighbors  = 3;
	private static final Paint[] typeColors = {Color.BLUE, Color.RED};
	public LifeOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState,initLocation,initColor);
	}

	@Override
	public void calcNextState(ArrayList<CellOccupant> neighbors) {
		int liveNeighbors = 0;
		for (CellOccupant neighbor: neighbors) {
			if (neighbor.getCurrentState() == ALIVE) {
				liveNeighbors++;
			}
		}
		if (this.getCurrentState() == ALIVE && liveNeighbors < minLiveNeighbors) {
			this.setNextState(DEAD);
			this.setNextPaint(typeColors[DEAD]);
		}
		else if (this.getCurrentState() == DEAD && liveNeighbors >= numToReproduce) {
			this.setNextState(ALIVE);
			this.setNextPaint(typeColors[ALIVE]);
		}
		else if (this.getCurrentState() == ALIVE && liveNeighbors >= maxLiveNeighbors) {
			this.setNextState(DEAD);
			this.setNextPaint(typeColors[DEAD]);
		}
		else {
			this.noChange();
		}
	}

}
