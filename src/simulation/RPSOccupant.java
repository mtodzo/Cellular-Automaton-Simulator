package simulation;

import java.util.List;
import java.util.Random;

import grids.Grid;
import javafx.scene.paint.Paint;

/**
 * Subclass for Rock Paper Scissors simulation.
 * 
 * @author Kelley Scroggs
 *
 */
public class RPSOccupant extends CellOccupant {

	private static final int ROCK_STATE = 3;
	private static final int SCISSORS_STATE = 2;
	private static final int PAPER_STATE = 1;
	private static final int NULL_STATE = 0;
	private static final int MAX_GRADIENT = 9;
	private static final int NUMBER_PLAYERS = 3;
	
	private int gradientLevel;
	
	/**
	 * Creates RPS occupant for RPS simulation upon initialization of the grid.
	 * 
	 * @param initState
	 * @param initLocation
	 * @param initColor
	 * @param colors
	 */
	public RPSOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		gradientLevel = 0;
	}

	/** 
	 * Calculate the next state of the RPS cell.
	 */
	@Override
	public void calculateNextState(Grid grid) {
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
	
	/**
	 * Returns true if typeOne beats typeTwo, otherwise returns false.
	 * 
	 * @param typeOne
	 * @param typeTwo
	 * @return
	 */
	private boolean cellEats(int typeOne, int typeTwo) {
		return typeOne%NUMBER_PLAYERS == ((typeTwo + 1)%NUMBER_PLAYERS);
	}
	
	/**
	 * returns gradient level of a cell. 
	 * 
	 * @return
	 */
	private int getGradientLevel() {
		return this.gradientLevel;
	}
	
	/**
	 * replaces current gradient level of a cell.
	 * 
	 * @param x
	 */
	private void replaceGradientLevel(int x) {
		this.gradientLevel = x + 1;
	}
	
	/**
	 * decreases the gradient level of a cell.
	 */
	private void upgradeGradientLevel() {
		this.gradientLevel--;
	}
	
	/** 
	 * increases the gradient level of a cell.
	 */
	private void downgradeGradientLevel() {
		this.gradientLevel++;
	}
}
