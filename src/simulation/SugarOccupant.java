package simulation;

import java.util.List;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/*
 * @author Miles Todzo
 * Subclass of CellOccupant for the SugarScape simulation.
 */
public class SugarOccupant extends CellOccupant{
	private static final int PATCH = 0;
	private static final int AGENT = 1;
	private static final int SUGAR_GROW_BACK_RATE = 1;
	private static final Paint [] PATCH_PAINT = {Color.WHITE, Color.CORNSILK, Color.LIGHTSALMON, Color.ORANGE, Color.DARKORANGE, Color.RED};
	private static final Paint AGENT_PAINT = Color.BLACK;
	
	private int myPatchSugar = 3;
	private int mySugarCapacity = 5;
	
	private int myVision = 3;
	private int myAgentSugar = 0;
	private int mySugarMetabolism = 2;
	
	
	public SugarOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		if (initState == PATCH) {
			this.setNextPaint(PATCH_PAINT[myPatchSugar]);
			this.setCurrentPaint();
		}
		else {
			this.setNextPaint(AGENT_PAINT);
			this.setCurrentPaint();
			this.myAgentSugar = this.myPatchSugar;
			this.myPatchSugar = 0;
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.CellOccupant#calculateNextState(grids.Grid)
	 */
	@Override
	public void calculateNextState(Grid grid) {
		if (this.getCurrentState() == PATCH && this.getNextState() == PATCH) {
			patchNextState();
		}
		else if (this.getCurrentState() == AGENT) {
			agentNextState(grid);
		}
	}

	/*
	 * Moves the agent to the nextPatch and leaves that old agent cell as a patch.
	 * @param grid
	 */
	private void agentNextState(Grid grid) {
		SugarOccupant nextPatch = findMaxPatchInVision(grid.getCurrentAndNextPositionsOfType(PATCH), grid);
		if(nextPatch == this) {
			this.noChange();
		}
		else {
			nextPatch.setNextState(AGENT);
			nextPatch.myAgentSugar = this.myAgentSugar;
			nextPatch.myAgentSugar += nextPatch.myPatchSugar;
			nextPatch.myPatchSugar = 0;
			nextPatch.myVision = this.myVision;
			nextPatch.mySugarMetabolism = this.mySugarMetabolism;
			
			this.myAgentSugar = 0;
			this.myVision = 0;
			this.mySugarMetabolism = 0;
			this.setNextState(PATCH);
			this.setNextPaint(PATCH_PAINT[this.myPatchSugar]);
			
			nextPatch.setNextPaint(AGENT_PAINT);
			nextPatch.myAgentSugar -= nextPatch.mySugarMetabolism;
			checkAgentState(nextPatch);
		}
	}

	/*
	 * Checks to see if the agent cell is dead
	 * @param agent is the agent in question
	 */
	private void checkAgentState(SugarOccupant agent) {
		if (agent.myAgentSugar == 0) {
			agent.setNextState(PATCH);
			agent.setNextPaint(PATCH_PAINT[agent.myPatchSugar]);
			agent.mySugarMetabolism = 0;
			agent.myVision = 0;
		}
		
	}

	/*
	 * Finds the patch with the max sugar that is within the agent's vision
	 * @param allPatches is the locations of all patch cells
	 * @grid is the grid for the simulation
	 */
	private SugarOccupant findMaxPatchInVision(List<int[]> allPatches, Grid grid) {
		int max = this.myPatchSugar;
		int distance = 0;
		int currentDistance;
		int currentSugar;
		SugarOccupant maxPatch = this; 
		for (int [] patch: allPatches) {
			if (patch[0] == this.getCurrentLocation()[0] && Math.abs(patch[1] - this.getCurrentLocation()[1]) <= this.myVision ||
					patch[1] == this.getCurrentLocation()[1] && Math.abs(patch[0] - this.getCurrentLocation()[0]) <= this.myVision) {
				SugarOccupant current = (SugarOccupant) grid.getOccupant(patch[0], patch[1]);
				currentSugar = current.myPatchSugar;
				if ((patch[0] == this.getCurrentLocation()[0])){
					currentDistance = Math.abs(patch[1] - this.getCurrentLocation()[1]);
					if (patch[1] < this.getCurrentLocation()[1] && current.getNextPaint() != current.getCurrentPaint()) {
						currentSugar -= SUGAR_GROW_BACK_RATE;
					}
				}
				else {
					currentDistance = Math.abs(patch[0] - this.getCurrentLocation()[0]);
					if (patch[0] < this.getCurrentLocation()[0] && current.getNextPaint() != current.getCurrentPaint()) {
						currentSugar -= SUGAR_GROW_BACK_RATE;
					}
				} 
				if (currentSugar > max || (currentSugar == max && currentDistance < distance)) {
					max = currentSugar;
					maxPatch = current;
					distance = currentDistance;
				}
			}
		}
		return maxPatch;
	}

	/*
	 * Sets next state for patch. Grows sugar.
	 */
	private void patchNextState() {
		if (this.myPatchSugar + SUGAR_GROW_BACK_RATE < this.mySugarCapacity) {
			this.myPatchSugar += SUGAR_GROW_BACK_RATE;
		}
		else {
			this.myPatchSugar = this.mySugarCapacity;
		}
		this.setNextPaint(PATCH_PAINT[this.myPatchSugar]);
	}

}
