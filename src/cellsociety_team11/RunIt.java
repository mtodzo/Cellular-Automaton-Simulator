package cellsociety_team11;

public class RunIt {

	public static void main(String[] args) {
		Simulation myFirstSim = new SpreadingFire(20,20,0);
		//Simulation myFirstSim = new SegregationModel(30,30,1);
		myFirstSim.initializeSim();
		myFirstSim.printGrid();
		boolean simRunning = myFirstSim.getStatus();
		int numPasses = 0;
		while(simRunning && numPasses < 50) {
			numPasses++;
			myFirstSim.passOne(); 
			myFirstSim.passTwo();
			simRunning = myFirstSim.getStatus();
			myFirstSim.printGrid();
		}
	}
}
