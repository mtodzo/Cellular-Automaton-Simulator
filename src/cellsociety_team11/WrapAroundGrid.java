package cellsociety_team11;

import java.util.ArrayList;

public class WrapAroundGrid extends Grid {

	public WrapAroundGrid(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<CellOccupant> getNeighbors(CellOccupant cell) {
		// TODO Auto-generated method stub

		ArrayList<CellOccupant> myNeighbors = new ArrayList<CellOccupant>();
		int[] toAdd = {-1,1};
		
		for(int i : toAdd) {
			int xGet = cell.getCurrentLocation()[0] + i;
			
			if(xGet < 0) {
				xGet = this.getGridHeight() -1;
			} else if(xGet > this.getGridHeight() -1) {
				xGet = 0;
			}
			
			myNeighbors.add(this.getMyGrid()[xGet][cell.getCurrentLocation()[1]]);
		}
		
		for(int i : toAdd) {
			int yGet = cell.getCurrentLocation()[1] + i;
			
			if(yGet < 0) {
				yGet = this.getGridWidth() -1;
			} else if(yGet > this.getGridWidth() -1) {
				yGet = 0;
			}
			
			myNeighbors.add(this.getMyGrid()[cell.getCurrentLocation()[0]][yGet]);
		
		}
		return myNeighbors;
	}
	
	
	
}
