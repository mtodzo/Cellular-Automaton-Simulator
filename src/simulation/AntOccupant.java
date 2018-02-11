package simulation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class AntOccupant extends CellOccupant{
	
	private static final int PATCH = 0;
	private static final int ANT = 1;
	private static final int FOOD_SOURCE = 2;
	private static final int NEST = 3;
	private static final Paint [] FOOD_PHEROMONE_PAINT = {Color.BLACK, Color.LIGHTGREEN, Color.MEDIUMAQUAMARINE, Color.GREENYELLOW, Color.CHARTREUSE, Color.FORESTGREEN};
	private static final Paint FOOD_ANT_PAINT = Color.RED;
	private static final Paint NO_FOOD_ANT_PAINT = Color.ORANGE;
	private static final Paint FOOD_PAINT = Color.BLUE;
	private static final Paint NEST_PAINT = Color.BROWN;
	private static final int PHEROMONE_DECREASE = 1;
	private static final int MAX_PHEROMONES = 7;

	private int patchHomePheromones;
	private int patchFoodPheromones;
	private boolean hasFood;
	
	
	public AntOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		patchHomePheromones = 0;
		patchFoodPheromones = 0;
		hasFood = false;
		
		if (initState == PATCH) {
			this.setNextPaint(FOOD_PHEROMONE_PAINT[this.patchFoodPheromones]);
			this.setCurrentPaint();
		}
		else if (initState == ANT) {
			this.setNextPaint(NO_FOOD_ANT_PAINT);
			this.setCurrentPaint();
		}
		else if (initState == FOOD_SOURCE) {
			this.patchFoodPheromones = MAX_PHEROMONES;
			this.setNextPaint(FOOD_PAINT);
			this.setCurrentPaint();
		}
		else {
			this.setNextPaint(NEST_PAINT);
			this.setCurrentPaint();
			this.patchHomePheromones = MAX_PHEROMONES;
		}
		
	}

	@Override
	public void calculateNextState(Grid grid) {
		if (this.getCurrentState() == ANT) {
			List<CellOccupant> neighbors = grid.getNeighbors(this);
				findNextAntLoc(neighbors);
		}
		else if (this.getCurrentState() == PATCH && this.getNextState() == PATCH) {
			updatePatch();
		}	
	}

	private void updatePatch() {
		if (this.patchFoodPheromones - PHEROMONE_DECREASE >= 0) {
			this.patchFoodPheromones -= PHEROMONE_DECREASE;
		}
		if (this.patchHomePheromones - PHEROMONE_DECREASE >= 0) {
			this.patchHomePheromones -= PHEROMONE_DECREASE;
		}
		this.setNextPaint(FOOD_PHEROMONE_PAINT[this.patchFoodPheromones]);
	}

	private void findNextAntLoc(List<CellOccupant> neighbors) {
		int maxNeighborFoodPheromones = 0;
		int maxNeighborHomePheromones = 0;
		boolean test = false;
		List<AntOccupant> patchNeighbors = new ArrayList<>();
		AntOccupant nextPatch = this;
		for (CellOccupant patch: neighbors) {
			if (patch.getCurrentState() != ANT && patch.getNextState() != ANT) {
				AntOccupant current = (AntOccupant) patch;
				if (current.getCurrentState() == PATCH) {
					patchNeighbors.add(current);
				}
				if (this.hasFood && current.patchHomePheromones > maxNeighborHomePheromones) {
					if (current.patchHomePheromones == 7) {
						test = true;
					}
					nextPatch = current;
				}
				else if (!this.hasFood && current.patchFoodPheromones > maxNeighborFoodPheromones) {
					nextPatch = current;
				}
				maxNeighborFoodPheromones = Math.max(maxNeighborFoodPheromones, current.patchFoodPheromones);
				maxNeighborHomePheromones = Math.max(maxNeighborHomePheromones, current.patchHomePheromones);
			}
		}
		if (nextPatch == this && patchNeighbors.size()>0) {
			Random randy = new Random();
			nextPatch = patchNeighbors.get(randy.nextInt(patchNeighbors.size()));
		}
		if (nextPatch.getCurrentState() == PATCH) {
			moveAnt(nextPatch, maxNeighborFoodPheromones, maxNeighborHomePheromones);
		}
		else if (nextPatch.getCurrentState() == NEST) {
			antReachedNest(nextPatch);
		}
		else if (nextPatch.getCurrentState() == FOOD_SOURCE) {
			antReachedFood(nextPatch, maxNeighborHomePheromones);
		}
		
	}
	
	private void antReachedFood(AntOccupant nextPatch, int maxHome) {
		nextPatch.hasFood = true;
		this.setNextState(PATCH);
		nextPatch.setNextPaint(FOOD_ANT_PAINT);
		nextPatch.setNextState(ANT);
		this.patchFoodPheromones = MAX_PHEROMONES - 2;
		this.patchHomePheromones = maxHome - 2;
		this.setNextPaint(FOOD_PHEROMONE_PAINT[this.patchFoodPheromones]);
	}

	private void antReachedNest(AntOccupant nextPatch) {
		this.hasFood = false;
		this.setNextPaint(NO_FOOD_ANT_PAINT);
		
	}

	private void moveAnt(AntOccupant nextPatch, int maxFood, int maxHome) {
		if (!this.hasFood && maxHome-2 > 0) {
			this.patchHomePheromones = maxHome-2;
		}
		else if (this.hasFood && maxFood-2 > 0) {
			this.patchFoodPheromones = maxFood-2;
		}
		nextPatch.hasFood = this.hasFood;
		nextPatch.setNextState(ANT);
		if(this.hasFood) {
			nextPatch.setNextPaint(FOOD_ANT_PAINT);
		}
		else {
			nextPatch.setNextPaint(NO_FOOD_ANT_PAINT);
		}
		this.hasFood = false;
		this.setNextState(PATCH);
		this.setNextPaint(FOOD_PHEROMONE_PAINT[this.patchFoodPheromones]);
	}
}
