package simulation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import grids.DiagonalSquareGrid;
import grids.Grid;
import grids.SquareGrid;
import grids.WrapAroundGrid;
import javafx.scene.paint.Paint;

/**
* @author Miles Todzo
* Class used to move update each cell occupant and move to the next generation
* Initializes grid type
**/

public class Simulation {
	private boolean isRunning;
	private String mySimulationType;
	private Grid myGrid;
	private Map<String, String> simulationToGridMap;
	private int numPopulations;
	
	public Simulation(CellOccupant[][] grid, String simType, int population, Paint[] colors) {
		Properties properties = new Properties();
		try {
			InputStream in = new FileInputStream("data/SimulationGridTypes.properties");
			properties.load(in);
		} catch (IOException e) {
			System.out.println("Could not load .properties file into Properties object");
			return;
			//e.printStackTrace();
		}
		simulationToGridMap = new HashMap<>();
		for (String key: properties.stringPropertyNames()) {
			simulationToGridMap.put(key, properties.getProperty(key));
		}
		switch(simulationToGridMap.get(simType)) {
		case "SquareGrid": myGrid = new SquareGrid(grid);
		break;
		case "WrapAroundGrid": myGrid = new WrapAroundGrid(grid);
		break;
		case "DiagonalNeighborsSquareGrid": myGrid = new DiagonalSquareGrid(grid);
		break;
		}
		isRunning = true;
		numPopulations = population;
	}
	/*
	 * Change the run status in order to start, stop, or pause the simulation.
	 * @param status is a boolean: false to stop simulation, true to start
	 */
	public void setRunStatus(boolean status) {
		isRunning = status;
	}
	/*
	 * Returns the run status of the simulation
	 * @return boolean value corresponding to run status
	 */
	public boolean getStatus() {
		return isRunning;
	}
	
	/*
	 * Returns the type of the simulation
	 * @return String that is the name of the simulation
	 */
	public String getType() {
		return mySimulationType;
	}
	/*
	 * Returns the myOccupants grid
	 * @return 2D array retrieved from the Grid calss
	 */
	public CellOccupant[][] getOccupantGrid() {
		return myGrid.getGrid();
	}
	
	/*
	 * Iterates through every CellOccupant in the grid and calls calculateNextState method
	 * calculateNextState assigns a nextState value to each cell
	 * @param none
	 * @return nothing
	 */
	public void setNextStates() {
		for (int y=0; y< myGrid.getLength(); y++) {
			for (int x=0; x<myGrid.getWidth(); x++) {
				myGrid.getOccupant(x, y).calculateNextState(myGrid);
			}
		}
	}
	
	/*
	 * Iterates through every CellOccupant in the grid and sets currentState and currentPaint
	 * @param none
	 * @return nothing
	 */
	public  void updateStates() {
		for (int y=0; y< myGrid.getLength(); y++) {
			for (int x=0; x<myGrid.getWidth(); x++) {
				myGrid.getOccupant(x, y).setCurrentState();
				myGrid.getOccupant(x, y).setCurrentPaint();
			}
		}
	}
	
	/*
	 * @param none
	 * @return an int[]: each index corresponds to the state, and the value is the number of cells of that state
	 */
	public int[] getPopulationNumbers()
	{
		int[] result = new int[numPopulations];
		for (int y=0; y< myGrid.getLength(); y++) 
		{
			for (int x=0; x<myGrid.getWidth(); x++)
			{
				int state = myGrid.getOccupant(x, y).getCurrentState();
				result[state]++;
			}
		}
		return result;
	}
	
	/*
	 * @return the number of populations
	 */
	public int getNumPopulations()
	{
		return numPopulations;
	}
}
