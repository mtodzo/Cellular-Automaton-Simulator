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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulation.CellOccupant;
import simulation.FireOccupant;
import simulation.LangtonOccupant;
import simulation.LifeOccupant;
import simulation.SegOccupant;
import simulation.Simulation;
import simulation.SugarOccupant;
import simulation.WatorOccupant;
import simulation.RPSOccupant;
import simulation.SugarOccupant;
import simulation.AntOccupant;
/**
 * @author Belanie Nagiel
 * 
 * This class handles setting up the original configuration based on the XML file
 * and displaying the simulation on the user interface.
 * 
 *@see the dynamic simulation 
 */
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
	private String cellShape;
	
	/**
	 * Class Constructor
	 * 
	 * @param smf simulation file name 
	 * @param ps the current window's stage
	 */
	public DisplayGrid(String smf, Stage ps)
	{
		SimulationFileName = smf;
		primaryStage = ps;
		showGridLines = false;
	}
	
	/**
	 * The getter for current simulation type 
	 * 
	 * @return string indicating the simulation type
	 */
	public String getCURRENT_SIMULATION_TYPE() 
	{
		return CURRENT_SIMULATION_TYPE;
	}

	/**
	 * The getter for the 2D grid of cell occupants
	 * 
	 * @return 2D grid of cell occupants 
	 */
	public CellOccupant[][] getCURRENT_CONFIGURATION() 
	{
		return CURRENT_CONFIGURATION;
	}

	/**
	 * The getter for the current Simulation object
	 * 
	 * @return Simulation object relating to the current simulation
	 */
	public Simulation getCURRENT_SIMULATION() 
	{
		return CURRENT_SIMULATION;
	}
	
	/**
	 * Boolean indicating whether grid lines can be seen in the display
	 * 
	 * @param val boolean indicating whether the checkbox in the user interface is enabled
	 */
	public void setShowGridLines(boolean val)
	{
		showGridLines = val;
	}

	/**
	 * Creates the Simulation object for a new simulation based on the XML
	 * file given. Also fills the 2D array associated with the Simulation
	 * object based on the XML file.
	 * 
	 * @throws LoadGridException Creates a dialogue box indicating an error in creating of a simulation
	 */
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
					
					cellShape = property.getElementsByTagName("Shape").item(0).getTextContent();
					
					
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
	
	/**
	 * Fills the 2D array of cell occupants with the information from the XML file. 
	 * 
	 * @param sim the document containing the information from the XML file
	 * @param colors the colors for the different states
	 * @param width	how many cells in the x direction
	 * @param height how many cells in the y direction
	 * @throws LoadGridException
	 */
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

	/**
	 * Returns the correct cell occupant based on the simulation type and the initial configuration
	 * of that cell occupant.
	 * 
	 * @param initState
	 * @param initLocation
	 * @param initColor
	 * @return
	 */
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
		else if (CURRENT_SIMULATION_TYPE.equals("AntForaging"))
		{
			return new AntOccupant(initState, initLocation, initColor, simColors);
		}
		else
		{
			return new FireOccupant(initState, initLocation, initColor, simColors);
		}
		
	}

	/**
	 * Creates the visual display for a specific simulation. Creates either a grid
	 * of rectancle or hexagons depending on the input in the XML file.
	 * 
	 * @return a Pane filled with polygons that will be added to the GUI
	 */
	public Pane displaySimulationConfiguration() throws LoadGridException
	{
		Pane SIMULATION_DISPLAY;
		if (cellShape.equals("HEXAGON")) {
			SIMULATION_DISPLAY = new Pane();
			SIMULATION_DISPLAY.setPrefSize(DISPLAY_SIZE,DISPLAY_SIZE+100);
			hexagonConfiguration(SIMULATION_DISPLAY);
		}
		
		else {
			SIMULATION_DISPLAY = new GridPane();
			rectangleConfiguration((GridPane) SIMULATION_DISPLAY);
			
		}
//		GridPane SIMULATION_DISPLAY = new GridPane();
//		rectangleConfiguration(SIMULATION_DISPLAY);
		
		
		
		return SIMULATION_DISPLAY;
	}

	/**
	 * Creates a GridPane of rectangles based on the current 2D array configuration.
	 * 
	 * @param simDisplay empty pane to fill
	 * @throws LoadGridException
	 */
	private void rectangleConfiguration(GridPane simDisplay) throws LoadGridException
	{
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
					simDisplay.add(r, i, j);
				}
			}
		}
		catch(Exception e) 
		{
			throw new LoadGridException("Load a Simulation First");
		}
	}

	/**
	 * Creates a Pane of hexagons based on the current 2D array configuration.
	 * 
	 * @param simDisplay empty pane to fill
	 * @throws LoadGridException
	 */
	private void hexagonConfiguration(Pane simDisplay) throws LoadGridException
	{
		try 
			{
			//int blockSizeX = 3*BlockSizeX/4;
			//int blockSizeY = 3*BlockSizeY/4;
			int blockSizeX = BlockSizeX;
			int blockSizeY = BlockSizeY;
			int xLocation = blockSizeX;
			for (int i = 0; i < CURRENT_CONFIGURATION.length; i++)
			{
				
				int yLocation = 0;
				for(int j = 0; j< CURRENT_CONFIGURATION[i].length; j++)
				{
					if(j%2==1)
					{
						xLocation += blockSizeX/2;
					}
					if(j%2==0)
					{
						xLocation -= blockSizeX/2;
					}
					Polygon hexagon = new Polygon();
					double xDir = (double) i;
					double yDir = (double) j;
					hexagon.getPoints().addAll(new Double[] {
							xDir,j+(0.25 * blockSizeY),
							xDir+(0.5 * blockSizeX), yDir,
							xDir+ blockSizeX, yDir+(0.25 * blockSizeY),
							xDir+ blockSizeX, yDir+(0.75 * blockSizeY),
							xDir+(0.5 * blockSizeX), yDir + blockSizeY,
							xDir, yDir+(0.75 * blockSizeY)			
					});
					hexagon.setFill(CURRENT_CONFIGURATION[i][j].getCurrentPaint());
					if(showGridLines)
					{
						hexagon.setStroke(Color.BLACK);
					}
					hexagon.relocate(xLocation, yLocation);
					simDisplay.getChildren().add(hexagon);
					yLocation += 3*blockSizeY/4;
					//yLocation += blockSizeY;
				}
				if(CURRENT_CONFIGURATION.length <= 5)
				{
					xLocation += blockSizeX/2;
				}
				xLocation += blockSizeX;
			}
		}
		catch(Exception e) {
			throw new LoadGridException("Load a Simulation First");
		}
	}
}
