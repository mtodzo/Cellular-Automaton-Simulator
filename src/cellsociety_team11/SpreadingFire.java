package cellsociety_team11;

import javafx.scene.paint.Color;

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
					FireOccupant toAdd = new FireOccupant(0, pos, Color.YELLOW);
					this.setPos(i, j, toAdd);
				} else if (i == (this.getHeight() / 2) && j == (this.getWidth() / 2)) {
					// fire in the middle
					FireOccupant toAdd = new FireOccupant(2, pos, Color.RED);
					this.setPos(i, j, toAdd);
				} else {
					// trees everywhere else
					FireOccupant toAdd = new FireOccupant(1, pos, Color.GREEN);
					this.setPos(i, j, toAdd);
				}
			}
		}

	}

	/**
	 * iterate through the grid and calculate the nextState for each cellOccupant.
	 */
	@Override
	public void passOne() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				FireOccupant update = (FireOccupant) this.getPos(i, j);
				update.calculateNextState(this.getNeighbors(i, j));
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
				FireOccupant currCell = (FireOccupant) this.getPos(i, j);
				if(currCell.getCurrentState() != currCell.getNextState()) {
					currCell.setCurrentState(currCell.getNextState());
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
