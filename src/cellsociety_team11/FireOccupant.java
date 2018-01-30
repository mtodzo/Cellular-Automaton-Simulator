package cellsociety_team11;

import javafx.scene.paint.Paint;

public class FireOccupant extends CellOccupant {
	private int turnsOnFire;

	public FireOccupant(int initState, int[] initLocation, Paint initColor) {
		super(initState, initLocation, initColor);
		turnsOnFire = 0;
	}

	public void updateTurnsOnFire() {
		turnsOnFire++;
	}

	public int getTurnsOnFire() {
		return turnsOnFire;
	}

}
