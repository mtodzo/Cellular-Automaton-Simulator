package setupGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import simulation.SugarOccupant;

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

	public void fillSimulationArray() throws LoadGridException
	{
		try
		{
			File NEW_SIMULATION = new File("data/" + SimulationFileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document sim = db.parse(NEW_SIMULATION);
			sim.getDocumentElement().normalize();
			CURRENT_SIMULATION_TYPE = sim.getDocumentElement().getAttribute("type");
			if(CURRENT_SIMULATION_TYPE == "") {
				throw new LoadGridException("NO SIMULATION TYPE GIVEN");
			}
			NodeList SimulationProperties = sim.getElementsByTagName("Properties");
			int width = -1;
			int height = -1;
			
			for (int i = 0; i < SimulationProperties.getLength(); i++)
			{
				Node PROPERTY = SimulationProperties.item(i);
				if (PROPERTY.getNodeType() == Node.ELEMENT_NODE)
				{
					Element property = (Element) PROPERTY;
					width = Integer.parseInt(property.getElementsByTagName("Width").item(0).getTextContent());
					height = Integer.parseInt(property.getElementsByTagName("Height").item(0).getTextContent());
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
			if(width == -1 || height == -1) {
				throw new LoadGridException("INVALID WIDTH/HEIGHT INPUT");	
			}
			
			fillConfiguration(sim, simColors, width, height);
			
			CURRENT_SIMULATION = new Simulation(CURRENT_CONFIGURATION, CURRENT_SIMULATION_TYPE, numPopulations, simColors);
		}
		catch(Exception e)
		{
			throw new LoadGridException("ERROR LOADING XML FILE, TRY A NEW ONE");
		}
	}
	
	
	private void fillConfiguration(Document sim, Paint[] colors, int width, int height) throws LoadGridException
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
				//e.printStackTrace();
				throw new LoadGridException("COULD NOT FIND DEFAULT COLOR FILE");
			}
		}
		NodeList CellOccupants = sim.getElementsByTagName("CellOccupant");
		
		Set<String> occupantLocations = new HashSet<>(); 
		
		for (int i = 0; i < CellOccupants.getLength(); i++)
		{
			Node OCCUPANT = CellOccupants.item(i);
			if (OCCUPANT.getNodeType() == Node.ELEMENT_NODE)
			{
				Element occupant = (Element) OCCUPANT;
				int initState = Integer.parseInt(occupant.getElementsByTagName("CurrentState").item(0).getTextContent());
				if(initState < 0 || initState > this.numPopulations -1) {
					throw new LoadGridException("INPUT CELL HAS IMPOSSIBLE STATE");
				}
				
				int xCor = Integer.parseInt(occupant.getElementsByTagName("xLocation").item(0).getTextContent());
				int yCor = Integer.parseInt(occupant.getElementsByTagName("yLocation").item(0).getTextContent());
				
				if(xCor < 0 || xCor >= width || yCor < 0 || yCor >= height) {
					throw new LoadGridException("INPUT CELL HAS COORDINATES OUTSIDE OF THE GRID");
				}
				
				int[] initLocation = new int[2];
				initLocation[0] = xCor;
				initLocation[1] = yCor;
				
				String locationCheck = xCor+","+yCor;
				
				if(occupantLocations.contains(locationCheck)) {
					throw new LoadGridException("INPUT CELL HAS THE SAME COORDINATES AS ANOTHER INPUT CELL");
				} else {
					occupantLocations.add(locationCheck);
				}
				
				Paint initColor = simColors[initState];
				
				CURRENT_CONFIGURATION[xCor][yCor] = createCellOccupant(initState,initLocation,initColor);
			}
		}
		if(occupantLocations.size() != (width * height)) {
			throw new LoadGridException("INCORRECT AMOUNT OF CELLS IN INPUT FILE FOR SPECIFIED DIMENSIONS");
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
			return new LangtonOccupant(initState, initLocation, initColor, simColors);
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
		else if (CURRENT_SIMULATION_TYPE.equals("SugarScape"))
		{
			return new SugarOccupant(initState, initLocation, initColor, simColors);
		}
		else
		{
			return new FireOccupant(initState, initLocation, initColor, simColors);
		}
		
	}

	public GridPane displaySimulationConfiguration() throws LoadGridException
	{
		GridPane SIMULATION_DISPLAY = new GridPane();
		try 
		{
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
		}
		catch(Exception e) {
			throw new LoadGridException("Load a Simulation First");
		}
		return SIMULATION_DISPLAY;
	}

}
