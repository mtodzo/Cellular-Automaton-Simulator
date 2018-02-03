package cellsociety_team11;

import javafx.scene.paint.Color;

public class RunIt {

	public static void main(String[] args) {
		CellOccupant[][] miles = init();
		Simulation myFirstSim = new Simulation(miles);
		//Simulation myFirstSim = new SegregationModel(30,30,1);
		//myFirstSim.init();
		myFirstSim.printGrid();
		boolean simRunning = myFirstSim.getStatus();
		int numPasses = 0;
		while(simRunning && numPasses < 50) {
			numPasses++;
			myFirstSim.setNextStates(); 
			myFirstSim.updateStates();
			simRunning = myFirstSim.getStatus();
			myFirstSim.printGrid();
		}
	}
	
	public static CellOccupant[][] init(){
		CellOccupant[][] ret = new CellOccupant[10][10];
		for (int y=0; y<10; y++) {
			for (int x=0; x<10; x++) {
				int[] pos = { x, y };
				int rand = (int)Math.random()*4;
				switch(rand) {
				case 1: ret[x][y] = new SegOccupant(0, pos, Color.WHITE); 
				break;
				case 2: ret[x][y] = new SegOccupant(1, pos, Color.BLUE);
				break;
				case 3: ret[x][y] = new SegOccupant(2, pos, Color.GREEN);
				break;
				}
			}
		}
		return ret;
	}
}
