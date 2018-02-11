package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LangtonOccupant extends CellOccupant {
	
	private static final int NUM_STATES = 8;	
	private LangtonRules[] myStateRules= new LangtonRules[NUM_STATES];
	
	private void setRules() {
		for(int i = 0; i < NUM_STATES; i++) {
			myStateRules[i] = new LangtonRules(i);
		}
	}

	public LangtonOccupant(int initState, int[] initLocation, Paint initColor, Paint[] colors) {
		super(initState, initLocation, initColor, colors);
		setRules();
	}

	@Override
	public void calculateNextState(Grid grid) {
		List<Integer> neighborsStates = new ArrayList<>();
		List<CellOccupant> neighbors = grid.getNeighbors(this);
		for(CellOccupant neighbor : neighbors) {
			neighborsStates.add(neighbor.getCurrentState());
		}
		if(sumList(neighborsStates) >0) {
			int nextState = myStateRules[this.getCurrentState()].getNextState(neighborsStates);
			if(nextState != -1) {
				this.setNextState(nextState);
				this.setNextPaint(this.getTypeColors()[nextState]);
			} else {
				System.out.println("NO RULE FOR STATE: " + this.getCurrentState() + " with neighbors: " + neighborsStates.toString());
				this.setNextState(this.getCurrentState());
				this.setNextPaint(this.getCurrentPaint());
			}
		}
	}
	
	// to reduce time by eliminating meaningless calculations
	private int sumList(List<Integer> myList) {
		int sum = 0;
		for (Integer i : myList) {
			sum += i;
		}
		return sum;
	}
}
