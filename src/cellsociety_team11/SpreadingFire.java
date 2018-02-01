package cellsociety_team11;

public class SpreadingFire extends Simulation{
	public SpreadingFire(CellOccupant[][] grid) {
		super(grid);
		init(grid);
	}

	/*
	 * Initiates the 2D array of CellOccupants 
	 */
	@Override
	public void init(CellOccupant[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				int[] pos = { i, j };
				double randNum = Math.random();
				if (randNum <= 0.1){
					CellOccupant toAdd = new FireOccupant(0, pos);
					this.setOccupant(i, j, toAdd);
				} 
				else if (randNum > 0.1 && randNum <= 0.55) {
					CellOccupant toAdd = new FireOccupant(2, pos);
					this.setOccupant(i, j, toAdd);
				} 
				else {
					CellOccupant toAdd = new FireOccupant(1, pos);
					this.setOccupant(i, j, toAdd);
				}
			}
		}
	}

	@Override
	public void setNextStates() {
		for (int x=0; x<this.myOccupants.length; x++) {
			
		}
		
	}

	@Override
	public void updateStates() {
		// TODO Auto-generated method stub
		
	}
}
