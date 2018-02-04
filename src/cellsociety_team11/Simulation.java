package cellsociety_team11;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Simulation {
	private boolean isRunning;
	private String mySimulationType;
	//private CellOccupant[][] myOccupants;
	private Grid myGrid;
	//private List<int[]> myEmptyLocations;
	//private static final int EMPTY = 0;
	private Map<String, String> simulationToGridMap;
	
	public Simulation(CellOccupant[][] grid, String simType) {
		Properties properties = new Properties();
		//InputStream in = this.getClass().getClassLoader().getResourceAsStream("data/SimulationGridTypes.properties");
		try {
			InputStream in = new FileInputStream("data/SimulationGridTypes.properties");
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//				ArrayList<CellOccupant> neighbors = getNeighbors(myOccupants[x][y]);
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
}