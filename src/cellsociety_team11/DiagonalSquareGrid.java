package cellsociety_team11;

import java.util.ArrayList;
import java.util.List;

public class DiagonalSquareGrid extends Grid {

	public DiagonalSquareGrid(CellOccupant[][] grid) {
		super(grid);
	}

	@Override
	public ArrayList<CellOccupant> getNeighbors(CellOccupant current) {
			int xLoc = current.getCurrentLocation()[0];
			int yLoc = current.getCurrentLocation()[1];
			ArrayList<CellOccupant> neighbors = new ArrayList<>();
			if (xLoc < this.getWidth()-1) {
				neighbors.add(this.getOccupant(xLoc+1, yLoc));
				if (yLoc != 0) {
					neighbors.add(this.getOccupant(xLoc+1, yLoc-1));
				}
				if (yLoc != this.getLength()-1) {
					neighbors.add(this.getOccupant(xLoc+1, yLoc+1));
				}
			}
			if (xLoc !=0) {
				neighbors.add(this.getOccupant(xLoc-1, yLoc));
				if (yLoc != 0) {
					neighbors.add(this.getOccupant(xLoc-1, yLoc-1));
				}
				if (yLoc != this.getLength()-1) {
					neighbors.add(this.getOccupant(xLoc+1, yLoc+1));
				}
			}
			if (yLoc != 0) {
				neighbors.add(this.getOccupant(xLoc, yLoc-1));
			}
			if (yLoc < this.getLength()-1) {
				neighbors.add(this.getOccupant(xLoc, yLoc+1));
			}
			return neighbors;
	}

}
