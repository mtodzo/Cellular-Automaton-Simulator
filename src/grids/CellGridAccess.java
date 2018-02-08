package grids;

import java.util.List;

import simulation.CellOccupant;

public interface CellGridAccess 
{
	public List<CellOccupant> getNeighbors(CellOccupant current);
	
	public List<int[]> getNextPositionsOfType(int state);
	
	public CellOccupant getNeighborOfType(CellOccupant cell, int state);
}
