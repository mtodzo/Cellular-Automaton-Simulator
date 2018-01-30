package cellsociety_team11;

import java.util.ArrayList;

public class SpreadingFire extends Simulation {

	public SpreadingFire(int width, int height, int type) {
		super(width, height, type);
	}

	/**
	 * For the purpose of checking everything create a grid with blank spaces on the
	 * edges, trees in the middle, and one fire in the center. Later will replace
	 * with a method that sets up the grid from the given XML file
	 */
	@Override
	public void initializeSim() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				int[] pos = { i, j };
				if (i == 0 || j == 0 || i == this.getHeight() - 1 || j == this.getWidth() - 1) {
					// empty squares around the edges
					CellOccupant toAdd = new FireOccupant(0, pos);
					this.setPos(i, j, toAdd);
				} else if (i == (this.getHeight() / 2) && j == (this.getWidth() / 2)) {
					// fire in the middle
					CellOccupant toAdd = new FireOccupant(2, pos);
					this.setPos(i, j, toAdd);
				} else {
					// trees everywhere else
					CellOccupant toAdd = new FireOccupant(1, pos);
					this.setPos(i, j, toAdd);
				}
			}
		}

	}

	/**
	 * If a tree has a neighbor that is burning, then the tree will burn in the next
	 * turn. later need to refine the rules
	 */
	@Override
	public void passOne() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				CellOccupant update = this.getPos(i, j);
				if(update.getCurrentState() != 0) {
					ArrayList<CellOccupant> neighbors = this.getNeighbors(i, j);
					for (CellOccupant neighbor : neighbors) {
						// if neighbor on fire
						if (neighbor.getCurrentState() == 2) {
							// 15% chance of catching fire
							if(Math.random() <= 0.15) {
								update.setNextState(2);
							}
						}
					}
					// if currently on fire
					if(update.getCurrentState() == 2) {
						if(update.getTurnsOnFire() == 5) {
							update.setNextState(0);
						} else {
							update.updateTurnsOnFire();
						}
					}
					
				}
			}
		}
	}

	/**
	 * set currentState = nextState for every position in the grid.
	 */
	@Override
	public void passTwo() {
		// TODO Auto-generated method stub
		int numOnFire = 0;
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				CellOccupant currCell = this.getPos(i, j);
				if(currCell.getCurrentState() != currCell.getNextState()) {
					currCell.setCurrentState(currCell.getNextState());
					//running = true;
				}
				if(currCell.getCurrentState() == 2) {
					numOnFire++;
				}
			}
		}
		if(numOnFire == 0){
			this.doneRunning();
		};
	}

}
