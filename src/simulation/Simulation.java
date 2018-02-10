package simulation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import grids.DiagonalSquareGrid;
import grids.Grid;
import grids.SquareGrid;
import grids.WrapAroundGrid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Simulation {
	private boolean isRunning;
	private String mySimulationType;
	//private CellOccupant[][] myOccupants;
	private Grid myGrid;
	//private List<int[]> myEmptyLocations;
	//private static final int EMPTY = 0;
	private Map<String, String> simulationToGridMap;
	private int numPopulations;
	private Paint[] popColors;
	
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
		Properties second = new Properties();
		if (colors[0] == null)
		{
			try
			{
				InputStream input = new FileInputStream("data/SimulationColors.properties");
				second.load(input);
				
				List<String> colorsList = Arrays.asList(second.getProperty(simType).split(","));
				for (int j = 0; j<colorsList.size(); j++)
				{
					colors[j] = Color.valueOf(colorsList.get(j));
				}
				popColors = colors;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			popColors = colors;
		}	
	}
	
	/*
	 * Sets the specified cell within myOccupants to hold the passed type
	 */
//	public void setOccupant(int x, int y, CellOccupant type) {
//		myOccupants[x][y] = type;
//	}
	
	/*
	 * Change the run status to start, stop, or pause the simulation.
	 */
	public void setRunStatus(boolean status) {
		isRunning = status;
	}
	/*
	 * Returns the run status of the simulation
	 */
	public boolean getStatus() {
		return isRunning;
	}
	
	/*
	 * Returns the type of the simulation
	 */
	public String getType() {
		return mySimulationType;
	}
	/*
	 * Returns the myOccupants grid
	 */
	public CellOccupant[][] getOccupantGrid() {
		return myGrid.getGrid();
	}
	
	/*
	 * Returns the CellOccupant at the specified location
	 */
//	public CellOccupant getOccupant(int x, int y) {
//		return myOccupants[x][y];
//	}
	
	
	public void setNextStates() {
		for (int y=0; y< myGrid.getLength(); y++) {
			for (int x=0; x<myGrid.getWidth(); x++) {
				myGrid.getOccupant(x, y).calculateNextState(myGrid);
			}
		}
	}
	
	public  void updateStates() {
		for (int y=0; y< myGrid.getLength(); y++) {
			for (int x=0; x<myGrid.getWidth(); x++) {
				myGrid.getOccupant(x, y).setCurrentState();
				myGrid.getOccupant(x, y).setCurrentPaint();
			}
		}
	}
	
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
	
	public int getNumPopulations()
	{
		return numPopulations;
	}
}