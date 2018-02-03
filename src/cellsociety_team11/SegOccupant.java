package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.paint.Paint;

public class SegOccupant extends CellOccupant {
	private static double similarityNeeded = .4;
	public SegOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState, initLocation, initColor);
	}
	/*
	 * Calculates the percentage of similar neighbors
	 * If it is below the threshold, the cell moves (see move() method)
	 * Otherwise it does not change
	 */
	@Override
	public void calculateNextState(ArrayList<CellOccupant> neighbors) {
		int similarCount = 0;
		for (CellOccupant neighbor: neighbors) {
			if (neighbor.getCurrentState() == this.getCurrentState()) similarCount++;
		}
//		if ((double)similarCount/neighbors.size() < similarityNeeded) {
//			int random = (int) Math.random()*emptyLocs.size();
//			this.setNextLocation(emptyLocs.get(random));
//		}
		//else {
			this.noChange();
		//}
	}
}

