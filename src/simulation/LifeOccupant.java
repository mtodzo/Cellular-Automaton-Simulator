package simulation;

import grids.Grid;
import javafx.scene.paint.Paint;


/*
 * Subclass of CellOccupant for GameOfLife simuation. Uses the following rules:
 * Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
 * Any live cell with two or three live neighbors lives on to the next generation.
 * Any live cell with more than three live neighbors dies, as if by overpopulation.
 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 */

public class LifeOccupant extends CellOccupant{
	private static int DEAD = 0;
	private static int ALIVE = 1;
	private static int minLiveNeighbors = 2;
	private static int numToReproduce = 3;
	private static int maxLiveNeighbors  = 3;

	public LifeOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState,initLocation,initColor, colors);
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.CellOccupant#calculateNextState(grids.Grid)
	 */
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
			this.setNextPaint(this.getTypeColors()[DEAD]);
		}
		else if (this.getCurrentState() == DEAD && liveNeighbors >= numToReproduce) {
			this.setNextState(ALIVE);
			this.setNextPaint(this.getTypeColors()[ALIVE]);
		}
		else if (this.getCurrentState() == ALIVE && liveNeighbors >= maxLiveNeighbors) {
			this.setNextState(DEAD);
			this.setNextPaint(this.getTypeColors()[DEAD]);
		}
		else {
			this.noChange();
		}
	}

}
