package cellsociety_team11;

public class FireOccupant extends CellOccupant {
	private int turnsOnFire;
	
	public FireOccupant(int initState, int[] initLocation) {
		super(initState, initLocation);
		turnsOnFire = 0;
	}
	
	public void updateTurnsOnFire() {
		turnsOnFire++;
	}
	
	public int getTurnsOnFire() {
		return turnsOnFire;
	}

}
