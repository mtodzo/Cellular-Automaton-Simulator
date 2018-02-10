package setupGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulation.CellOccupant;
import simulation.FireOccupant;
import simulation.LangtonOccupant;
import simulation.LifeOccupant;
import simulation.SegOccupant;
import simulation.Simulation;
import simulation.WatorOccupant;
import simulation.RPSOccupant;

public class DisplayGrid {
	
	private String CURRENT_SIMULATION_TYPE;
	private int BlockSizeX;
	private int BlockSizeY;
	private int numPopulations;
	private CellOccupant[][] CURRENT_CONFIGURATION;
	private Simulation CURRENT_SIMULATION;
	private String SimulationFileName;
	private Stage primaryStage;
	private boolean showGridLines;
	private int DISPLAY_SIZE = 400;
	private Paint[] simColors;
	
	public DisplayGrid(String smf, Stage ps)
	{
		SimulationFileName = smf;
		primaryStage = ps;
		showGridLines = false;
	}
	
	public String getCURRENT_SIMULATION_TYPE() 
	{
		return CURRENT_SIMULATION_TYPE;
	}

	public CellOccupant[][] getCURRENT_CONFIGURATION() 
	{
		return CURRENT_CONFIGURATION;
	}

	public Simulation getCURRENT_SIMULATION() 
	{
		return CURRENT_SIMULATION;
	}
	
	public void setShowGridLines(boolean val)
	{
		showGridLines = val;
	}

	public void fillSimulationArray() 
	{
		try
		{
			File NEW_SIMULATION = new File("data/" + SimulationFileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document sim = db.parse(NEW_SIMULATION);
			sim.getDocumentElement().normalize();
			
			CURRENT_SIMULATION_TYPE = sim.getDocumentElement().getAttribute("type");
			NodeList SimulationProperties = sim.getElementsByTagName("Properties");
			for (int i = 0; i < SimulationProperties.getLength(); i++)
			{
				Node PROPERTY = SimulationProperties.item(i);
				if (PROPERTY.getNodeType() == Node.ELEMENT_NODE)
				{
					Element property = (Element) PROPERTY;
					int width = Integer.parseInt(property.getElementsByTagName("Width").item(0).getTextContent());
					int height = Integer.parseInt(property.getElementsByTagName("Height").item(0).getTextContent());
					numPopulations = Integer.parseInt(property.getElementsByTagName("NumPopulations").item(0).getTextContent());
					simColors = new Paint[numPopulations];
					if(property.getElementsByTagName("Colors").item(0).getTextContent().length() != 0)
					{
						List<String> colorsList = Arrays.asList(property.getElementsByTagName("Colors").item(0).getTextContent().split(","));
						for (int j = 0; j<colorsList.size(); j++)
						{
							simColors[j] = Color.valueOf(colorsList.get(j));
						}
					}
					
					BlockSizeX = DISPLAY_SIZE/width;
					BlockSizeY = DISPLAY_SIZE/height;
					CURRENT_CONFIGURATION = new CellOccupant[width][height];
					
				}
			}
			
			fillConfiguration(sim, simColors);
			
			CURRENT_SIMULATION = new Simulation(CURRENT_CONFIGURATION, CURRENT_SIMULATION_TYPE, numPopulations, simColors);
		}
		catch(ParserConfigurationException e)
		{
			System.out.println("Could not parse through XML configuration file.");
			primaryStage.close();
			return;
		}
		catch(Exception e)
		{
			System.out.println("Could not load XML file");
			primaryStage.close();
			e.printStackTrace();
			return;
		}
	}

	private void fillConfiguration(Document sim, Paint[] colors) 
	{
		Properties second = new Properties();
		if (colors[0] == null)
		{
			try
			{
				InputStream input = new FileInputStream("data/SimulationColors.properties");
				second.load(input);
				
				List<String> colorsList = Arrays.asList(second.getProperty(CURRENT_SIMULATION_TYPE).split(","));
				for (int j = 0; j<colors.length; j++)
				{
					colors[j] = Color.valueOf(colorsList.get(j));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		NodeList CellOccupants = sim.getElementsByTagName("CellOccupant");
		for (int i = 0; i < CellOccupants.getLength(); i++)
		{
			Node OCCUPANT = CellOccupants.item(i);
			if (OCCUPANT.getNodeType() == Node.ELEMENT_NODE)
			{
				Element occupant = (Element) OCCUPANT;
				int initState = Integer.parseInt(occupant.getElementsByTagName("CurrentState").item(0).getTextContent());
				int xCor = Integer.parseInt(occupant.getElementsByTagName("xLocation").item(0).getTextContent());
				int yCor = Integer.parseInt(occupant.getElementsByTagName("yLocation").item(0).getTextContent());
				//String COLOR = occupant.getElementsByTagName("Color").item(0).getTextContent();
				int[] initLocation = new int[2];
				initLocation[0] = xCor;
				initLocation[1] = yCor;
				Paint initColor = simColors[initState];
				
				CURRENT_CONFIGURATION[xCor][yCor] = createCellOccupant(initState,initLocation, initColor);
			}
		}
	}

	public CellOccupant createCellOccupant(int initState, int[] initLocation, Paint initColor)
	{
		if (CURRENT_SIMULATION_TYPE.equals("SpreadingFire"))
		{
			return new FireOccupant(initState, initLocation, initColor, simColors);
		}
		else if (CURRENT_SIMULATION_TYPE.equals("Langton"))
		{	
			return new LangtonOccupant(initState, initLocation, initColor);
		}
		else if (CURRENT_SIMULATION_TYPE.equals("RPS"))
		{
			return new RPSOccupant(initState, initLocation, initColor, simColors);
		}
		else if (CURRENT_SIMULATION_TYPE.equals("Wator"))
		{
			return new WatorOccupant(initState, initLocation, initColor, simColors);
		}
		else if (CURRENT_SIMULATION_TYPE.equals("GameOfLife"))
		{
			return new LifeOccupant(initState, initLocation, initColor, simColors);
		}
		else if (CURRENT_SIMULATION_TYPE.equals("Segregation"))
		{
			return new SegOccupant(initState, initLocation, initColor, simColors);
		}
		else
		{
			return new FireOccupant(initState, initLocation, initColor, simColors);
		}
		
	}

	public GridPane displaySimulationConfiguration() 
	{
		GridPane SIMULATION_DISPLAY = new GridPane();
		for (int i = 0; i < CURRENT_CONFIGURATION.length; i++)
		{
			for(int j = 0; j<CURRENT_CONFIGURATION[i].length; j++)
			{
				Rectangle r = new Rectangle(BlockSizeX, BlockSizeY);
				r.setFill(CURRENT_CONFIGURATION[i][j].getCurrentPaint());
				if(showGridLines)
				{
					r.setStroke(Color.BLACK);
				}
				SIMULATION_DISPLAY.add(r, i, j);
			}
		}
		return SIMULATION_DISPLAY;
	}

}
