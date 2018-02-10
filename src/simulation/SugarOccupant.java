package simulation;

import java.util.List;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/*
 * if state is Agent --> will also need to display agent (or we can just set the whole cell to a color that is the agent color)
 */
public class SugarOccupant extends CellOccupant{
	private static final int PATCH = 0;
	private static final int AGENT = 0;
	private static final int SUGAR_GROW_BACK_RATE = 1;
	private static final Paint [] colorGradient = {Color.WHITE, Color.CORNSILK, Color.LIGHTSALMON, Color.ORANGE, Color.DARKORANGE, Color.RED}; 
	
	private int myPatchSugar = 0;
	private int mySugarCapacity = 5;
	
	private int myVision = 3;
	private int myAgentSugar = 0;
	private int mySugarMetabolism = 2;
	
	
	public SugarOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
	}

	@Override
	public void calculateNextState(Grid grid) {
		if (this.getCurrentState() == PATCH) {
			patchNextState();
		}
		else {
			agentNextState(grid);
		}
	}

	private void agentNextState(Grid grid) {
		SugarOccupant nextPatch = findMaxPatchInVision(grid.getCurrentAndNextPositionsOfType(PATCH), grid);
		nextPatch.setNextState(AGENT);
		nextPatch.myAgentSugar = this.myAgentSugar;
		nextPatch.myAgentSugar += nextPatch.myPatchSugar;
		nextPatch.myPatchSugar = 0;
		nextPatch.myVision = this.myVision;
		nextPatch.mySugarMetabolism = this.mySugarMetabolism;
		
		this.myAgentSugar = 0;
		this.myVision = 0;
		this.mySugarMetabolism = 0;
		
		nextPatch.setNextPaint(colorGradient[nextPatch.myPatchSugar]);
		nextPatch.myAgentSugar -= nextPatch.mySugarMetabolism;
		checkAgentState(nextPatch);
	}

	private void checkAgentState(SugarOccupant agent) {
		if (agent.myAgentSugar == 0) {
			agent.setNextState(PATCH);
			agent.mySugarMetabolism = 0;
			agent.myVision = 0;
		}
		
	}

	private SugarOccupant findMaxPatchInVision(List<int[]> allPatches, Grid grid) {
		int max = this.myPatchSugar;
		int distance = 0;
		SugarOccupant maxPatch = this; 
		for (int [] patch: allPatches) {
			if (patch[0] == this.getCurrentLocation()[0] && Math.abs(patch[0] - this.getCurrentLocation()[0]) <= this.myVision ||
					patch[1] == this.getCurrentLocation()[1] && Math.abs(patch[1] - this.getCurrentLocation()[1]) <= this.myVision) {
				SugarOccupant current = (SugarOccupant) grid.getOccupant(patch[0], patch[1]);
				if (current.myPatchSugar > max || (current.myPatchSugar == max && Math.abs(patch[0] - this.getCurrentLocation()[0]) < distance)) {
					max = current.myPatchSugar;
					maxPatch = current;
				}
			}
		}
		return maxPatch;
	}

	private void patchNextState() {
		if (this.myPatchSugar + SUGAR_GROW_BACK_RATE < this.mySugarCapacity) {
			this.myPatchSugar += SUGAR_GROW_BACK_RATE;
		}
		else {
			this.myPatchSugar = this.mySugarCapacity;
		}
		this.setNextPaint(colorGradient[this.myPatchSugar]);
	}

}
