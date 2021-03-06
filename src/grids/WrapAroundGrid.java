package grids;

import java.util.ArrayList;
import java.util.List;

import simulation.CellOccupant;

/*
 * @author Miles Todzo
 * Subclass of the Grid class. Implements getNeighbors method for square neighbors with wrap-around capabilities.
 * Wrap-around means that that right-most cell is neighbors with the left-most cell at the same y-position. This applies for all edges and corners.
 */
public class WrapAroundGrid extends Grid {

	public WrapAroundGrid(CellOccupant [][] grid) {
		super(grid);
	}
	
	@Override
	public List<CellOccupant> getNeighbors(CellOccupant cell) {
		List<CellOccupant> neighbors = new ArrayList<>();
		int[] toAdd = {-1,1};
		for(int i : toAdd) {
			int xGet = cell.getCurrentLocation()[0] + i;
			
			if(xGet < 0) {
				xGet = this.getLength() -1;
			} else if(xGet > this.getLength()-1) {
				xGet = 0;
			}
			neighbors.add(this.getOccupant(xGet, cell.getCurrentLocation()[1]));
			int yGet = cell.getCurrentLocation()[1] + i;
			
			if(yGet < 0) {
				yGet = this.getWidth() -1;
			} else if(yGet > this.getWidth() -1) {
				yGet = 0;
			}
			neighbors.add(this.getOccupant(cell.getCurrentLocation()[0],yGet));
		}
		return neighbors;
	}
	
	
}
