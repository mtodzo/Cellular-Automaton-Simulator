package cellsociety_team11;

import java.util.ArrayList;

public class SquareGrid extends Grid {

	public SquareGrid(int width, int height) {
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
			if(xGet >=0 && xGet <this.getMyGrid().length) {
				myNeighbors.add(this.getMyGrid()[xGet][cell.getCurrentLocation()[1]]);
			}
		}
		
		for(int i : toAdd) {
			int yGet = cell.getCurrentLocation()[1] + i;
			if(yGet >=0 && yGet <this.getMyGrid()[0].length) {
				myNeighbors.add(this.getMyGrid()[cell.getCurrentLocation()[0]][yGet]);
			}
		}
		
		return myNeighbors;
	}
}
