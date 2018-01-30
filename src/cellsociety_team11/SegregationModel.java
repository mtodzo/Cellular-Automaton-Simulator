package cellsociety_team11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class SegregationModel extends Simulation {
	
	public SegregationModel(int width, int height, int type) {
		super(width, height, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeSim() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				int[] pos = { i, j };
				double randNum = Math.random();
				if (randNum <= 0.1){
					// empty squares around the edges
					CellOccupant toAdd = new FireOccupant(0, pos);
					this.setPos(i, j, toAdd);
				} else if (randNum > 0.1 && randNum <= 0.55) {
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

	@Override
	public void passOne() {
		// TODO Auto-generated method stub
		HashSet<int[]> movedTo = new HashSet<int[]>();
		
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				CellOccupant update = this.getPos(i, j);
				if(update.getCurrentState() != 0) {
					ArrayList<CellOccupant> neighbors = this.getNeighbors(i, j);
					int diffNeighbors = 0;
					for (CellOccupant neighbor : neighbors) {
						if (neighbor.getCurrentState() != update.getCurrentState() && neighbor.getCurrentState() != 0) {
							diffNeighbors++;							
						}
					}
					float similarity = (float) (neighbors.size() - diffNeighbors) / neighbors.size();
					if(similarity <= 0.5) {
						// send to random open position
						ArrayList<int[]> openPositions = this.getPositionsOfType(0);
						
						Collections.shuffle(openPositions);
						for(int[] potentialPos : openPositions) {
							//System.out.println(potentialPos[0] +" , "+ potentialPos[1]);
							if(!(movedTo.contains(potentialPos))) {
								movedTo.add(potentialPos);
								update.setNextLocation(potentialPos);
								//System.out.println("MOVED " + Arrays.toString(update.getCurrentLocation()) + "to " + Arrays.toString(update.getNextLocation()));
								break;
							}
						}
					}					
				}
			}
		}
	}

	@Override
	public void passTwo() {
		// TODO Auto-generated method stub
		int numChanged = 0;
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				CellOccupant currCell = this.getPos(i, j);
				CellOccupant switchCell = this.getPos(currCell.getNextLocation()[0], currCell.getNextLocation()[1]);
				if(currCell.getCurrentLocation() != currCell.getNextLocation()) {
					this.setPos(currCell.getCurrentLocation()[0], currCell.getCurrentLocation()[1], switchCell);
					currCell.setCurrentLocation(currCell.getNextLocation());
					this.setPos(currCell.getCurrentLocation()[0], currCell.getCurrentLocation()[1], currCell);
					//System.out.println("CHANGED LOCATIONS");
					numChanged++;
				}
			}
		}
//		if(numChanged == 0){
//			this.doneRunning();
//		};
	}

}
