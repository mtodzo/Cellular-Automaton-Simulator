package cellsociety_team11;

import java.util.ArrayList;
import java.util.List;

public class WrapAroundGrid extends Grid {

	public WrapAroundGrid(CellOccupant [][] grid) {
		super(grid);
	}
	
	@Override
	public List<CellOccupant> getNeighbors(CellOccupant cell) {
		List<CellOccupant> neighbors = new ArrayList<CellOccupant>();
		int[] toAdd = {-1,1};
		for(int i : toAdd) {
			int xGet = cell.getCurrentLocation()[0] + i;
			
			if(xGet < 0) {
				xGet = this.getLength() -1;
			} else if(xGet > this.getLength()-1) {
				xGet = 0;
			}
			neighbors.add(this.getOccupant(xGet, cell.getCurrentLocation()[1]));
		}
		for(int i : toAdd) {
			int yGet = cell.getCurrentLocation()[1] + i;
			
			if(yGet < 0) {
				yGet = this.getWidth() -1;
			} else if(yGet > this.getWidth() -1) {
				yGet = 0;
			}
			neighbors.add(this.getOccupant(0,yGet));
		}
		return neighbors;
	}
	
	
}
