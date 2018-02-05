package simulation;

import java.util.List;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FireOccupant extends CellOccupant {
	private int turnsOnFire;

	private static final double PROB_CATCH_FIRE = .15;

	private static final Paint[] typeColors = {Color.YELLOW, Color.GREEN, Color.RED};

	public FireOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState, initLocation, initColor);
		// 1 for initially fire 
		turnsOnFire = 0;
	}

	private void updateTurnsOnFire() {
		turnsOnFire++;
	}

	private int getTurnsOnFire() {
		return turnsOnFire;
	}

	/** 
	 * Calculate the next state.
	 * States:
	 * 		Nothing:		0
	 * 		Tree:		1
	 * 		Fire:		2
	 * Transitions:	
	 * 			    <>N
	 * 				  ^	(after burning for 5 times)
	 * 				   \
	 * 			<>T  ->	F<> (if burning for < 5 times)
	 * 			(p=0.15 if neighbor)
	 */		
	@Override
	public void calculateNextState(Grid grid) {
		if(this.getCurrentState() == 0) {
			// no changes, might not be necessary
			this.noChange();
		} else if (this.getCurrentState() == 1) {
			if(neighborOnFire(grid.getNeighbors(this))) {
				if(Math.random() < PROB_CATCH_FIRE) {
					this.setNextState(2);
					this.setNextPaint(typeColors[2]);
				} else {
					this.noChange();
				}
			} else {
				this.noChange();
			}
		} else {
			if (getTurnsOnFire() == 5) {
				this.setNextState(0);
				this.setNextPaint(typeColors[0]);
			} else {
				this.updateTurnsOnFire();
				this.noChange();
			}
		}
	}
	
	/**
	 * Helper method for calculateNextState method. 
	 * Determines if one of the cell's neighbors is on Fire.
	 * @param neighborsList
	 * @return
	 */
	private boolean neighborOnFire(List<CellOccupant> neighborsList) {
		for(CellOccupant neighbor : neighborsList) {
			if (neighbor.getCurrentState() == 2) {
				return true;
			}
		}
		return false;
	}
	
}
