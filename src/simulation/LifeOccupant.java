package simulation;

import java.util.ArrayList;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/*Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
*Any live cell with two or three live neighbors lives on to the next generation.
*Any live cell with more than three live neighbors dies, as if by overpopulation.
*Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
*/

public class LifeOccupant extends CellOccupant{
	private static final int DEAD = 0;
	private static final int ALIVE = 1;
	private static final int minLiveNeighbors = 2;
	private static final int numToReproduce = 3;
	private static final int maxLiveNeighbors  = 3;
	private static final Paint[] typeColors = {Color.BLUE, Color.RED};
	public LifeOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState,initLocation,initColor);
	}

	@Override
	public void calculateNextState(Grid grid) {
		int liveNeighbors = 0;
		for (CellOccupant neighbor: grid.getNeighbors(this)) {
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
