package simulation;

import java.util.List;
import java.util.Random;

import grids.Grid;
import javafx.scene.paint.Paint;

public class RPSOccupant extends CellOccupant {

	private static final int ROCK_STATE = 3;
	private static final int SCISSORS_STATE = 2;
	private static final int PAPER_STATE = 1;
	private static final int NULL_STATE = 0;
	private static final int MAX_GRADIENT = 9;
	private static final int NUMBER_PLAYERS = 3;
	
	private int gradientLevel;
		
	public RPSOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		// TODO Auto-generated constructor stub
		gradientLevel = 0;
	}

	@Override
	public void calculateNextState(Grid grid) {
		// TODO Auto-generated method stub
		List<CellOccupant> myNeighbors = grid.getNeighbors(this);
		Random r = new Random();
		RPSOccupant randNeighbor = (RPSOccupant) myNeighbors.get(r.nextInt(myNeighbors.size()));
		
		if (this.getCurrentState() == NULL_STATE && this.getNextState() == NULL_STATE && randNeighbor.getCurrentState() != NULL_STATE && randNeighbor.getGradientLevel() < MAX_GRADIENT) {
			this.replaceGradientLevel(randNeighbor.getGradientLevel());
			this.setNextState(randNeighbor.getCurrentState());
			this.setNextPaint(randNeighbor.getCurrentPaint());
		}
		else if (this.getCurrentState() != NULL_STATE && this.getCurrentState() == this.getNextState() && this.getCurrentState() != randNeighbor.getCurrentState()) {
			if (cellEats(this.getCurrentState(), randNeighbor.getCurrentState())) {
				// this eats neighbor
				randNeighbor.setNextState(this.getCurrentState());
				randNeighbor.setNextPaint(this.getCurrentPaint());
				randNeighbor.downgradeGradientLevel();
				this.upgradeGradientLevel();
			} else {
				// neighbor eats this
				this.setNextState(randNeighbor.getNextState());
				this.setNextPaint(randNeighbor.getNextPaint());
				this.downgradeGradientLevel();
				randNeighbor.upgradeGradientLevel();
			}
		}
	}
	
	private boolean cellEats(int typeOne, int typeTwo) {
		return typeOne%NUMBER_PLAYERS == ((typeTwo + 1)%NUMBER_PLAYERS);
	}
	
	private int getGradientLevel() {
		return this.gradientLevel;
	}
	
	private void replaceGradientLevel(int x) {
		this.gradientLevel = x + 1;
	}
	
	private void upgradeGradientLevel() {
		this.gradientLevel--;
	}
	
	private void downgradeGradientLevel() {
		this.gradientLevel++;
	}
}
