package grids;

import java.util.ArrayList;
import java.util.List;

import simulation.CellOccupant;

/*
 * @author Miles Todzo
 * Subclass of the Grid class. Implements getNeighbors method for square neighbors and diagonal neighbors.
 */
public class DiagonalSquareGrid extends Grid {

	public DiagonalSquareGrid(CellOccupant[][] grid) {
		super(grid);
	}

	/*
	 * (non-Javadoc)
	 * @see grids.Grid#getNeighbors(simulation.CellOccupant)
	 */
	@Override
	public List<CellOccupant> getNeighbors(CellOccupant current) {
			int xLoc = current.getCurrentLocation()[0];
			int yLoc = current.getCurrentLocation()[1];
			List<CellOccupant> neighbors = new ArrayList<>();
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
					neighbors.add(this.getOccupant(xLoc-1, yLoc+1));
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
