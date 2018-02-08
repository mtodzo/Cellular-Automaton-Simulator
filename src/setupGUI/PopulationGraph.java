package setupGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopulationGraph 
{
	private int NUMBER_POPULATIONS;
	private DisplayGrid CURRENT_GRID;
	private Map<Integer, ArrayList<Integer>> POPULATION_DATA;
	
	public PopulationGraph(DisplayGrid currentGrid)
	{
		
		CURRENT_GRID = currentGrid;
		NUMBER_POPULATIONS = CURRENT_GRID.getCURRENT_SIMULATION().getNumPopulations();
		POPULATION_DATA = new HashMap<Integer, ArrayList<Integer>>();
		for(int j = 0; j < NUMBER_POPULATIONS; j++)
		{
			POPULATION_DATA.put(j, new ArrayList<Integer>());
		}
	}
	
	public void updatePopulationGraph()
	{
		int[] newPoints = new int[NUMBER_POPULATIONS];
		for(int j = 0; j < NUMBER_POPULATIONS; j++)
		{
			newPoints[j] = CURRENT_GRID.getCURRENT_SIMULATION().getPopulationNumbers()[j];
		}
		for (int i=0; i < newPoints.length; i++)
		{
			//System.out.println("EX: " + CURRENT_GRID.getCURRENT_SIMULATION().getPopulationNumbers()[i]);
			POPULATION_DATA.get(i).add(newPoints[i]);
		}
	}
	
	public VBox displayPopulationGraph()
	{
		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Time Point");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of Cells");

		LineChart lineChart = new LineChart(xAxis, yAxis);
		
		ArrayList<XYChart.Series> data = new ArrayList<XYChart.Series>();
		
		for(int i = 0; i < NUMBER_POPULATIONS; i++)
		{
			data.add(new XYChart.Series());
			data.get(i).setName("State = " + i);
		}
        
		for(int k = 0; k < data.size(); k++)
		{
			for(int j = 0; j< POPULATION_DATA.get(k).size(); j++)
			{
				int sum = POPULATION_DATA.get(k).get(j);
				data.get(k).getData().add(new XYChart.Data(j, POPULATION_DATA.get(k).get(j)));
			}
		}
		
		for (XYChart.Series pop: data)
		{
			lineChart.getData().add(pop);
		}
		
		VBox graph = new VBox();
		graph.getChildren().add(lineChart);
		
		return graph;
	}
}
