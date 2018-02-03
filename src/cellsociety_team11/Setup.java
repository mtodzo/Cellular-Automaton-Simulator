package cellsociety_team11;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Setup extends Application
{
	/*
	 * Display the current states of the 2D grid and animate the simulation from its initial state until the user
	 * stops it. Allow users to load a new configuration file, which stops the current simulation and starts the new 
	 * one. The display size of an individual cell should be calculated each time by the grid's total size, but the
	 * size of the visualization window should not change. Allow users to pause and resume the simulation, as 
	 * well as step forward through it.Allow users to speed up or slow down the simulation's animation rate.
	 * Any text displayed in the user interface should be set using resource files, not hard-coded.
	 */
	
	//button functionality:
	//start, reset, do we want to do a start/stop button?
	//stop
	//step
	//go
	//pause/resume
	//change simulation animation rate
	//load new file
	
	//make a hashmap for simulations to simulation name
	//make a hashmap for celloccupant type to cell occupant name
	
	/*
	 * Read in an XML formatted file that contains the initial settings for a simulation. The file contains three parts:
	 * name of the kind of simulation it represents, as well as a title for this simulation and this simulation's author
	 * settings for global configuration parameters specific to the simulation dimensions of the grid and the initial
	 * configuration of the states for the cells in the grid
	 */
	
	private Scene SCENE;
	private final String TITLE = "CA Simulations";
	private static final int WIDTH = 600;
	private static final int HEIGHT = 500;
	private static final Paint BACKGROUND = Color.WHITE;
	private static final int FRAMES_PER_SECOND = 1;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Timeline ANIMATION = new Timeline();
	
	private Simulation CURRENT_SIMULATION;
	private GridPane CURRENT_DISPLAY;
	private BorderPane root;
	private CellOccupant[][] CURRENT_CONFIGURATION;
	

	@Override
	public void start(Stage primaryStage)
	{
		String SimulationFileName = "SampleSpreadingFire.xml";
		SCENE = setupScene(WIDTH, HEIGHT, BACKGROUND, primaryStage, SimulationFileName);
		primaryStage.setScene(SCENE);
		primaryStage.setTitle(TITLE);
		primaryStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e->updateAll(SECOND_DELAY, primaryStage));
		ANIMATION.setCycleCount(Timeline.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);
		ANIMATION.play(); // move this to start stop eventually
	}

	private Scene setupScene(int width, int height, Paint myBackground, Stage primaryStage, String SimulationFileName) 
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop = new Properties();
		try
		{
			InputStream configs = new FileInputStream("data/UserInterfaceConfigurations.properties");
			prop.load(configs);
			
			root.setLeft(addButtons(prop));
			root.setBottom(addTextFields(prop));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		fillSimulationArray(SimulationFileName);
		
		//create new simulation
		
		//or we could put the fill array in the simulaiton class and call that with a properies file?
		
		CURRENT_DISPLAY = displaySimulationConfiguration(CURRENT_SIMULATION.getOccupantGrid());
		root.setCenter(CURRENT_DISPLAY);
		
		return scene;
	}

	private javafx.scene.Node addTextFields(Properties prop) 
	{
		HBox controls = new HBox();
		TextField CHOOSE_SECTOR = new TextField();
		CHOOSE_SECTOR.setPromptText(prop.getProperty("SectorFieldText"));
		TextField CHOOSE_SIMULATION = new TextField();
		CHOOSE_SIMULATION.setPromptText(prop.getProperty("SimulationFieldText"));
		Button GO = new Button(prop.getProperty("GoText"));
		GO.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						System.out.println("pressed go button");
						if(CHOOSE_SECTOR.getText() != null && !CHOOSE_SECTOR.getText().isEmpty() && CHOOSE_SIMULATION.getText() != null && !CHOOSE_SIMULATION.getText().isEmpty())
							{
								System.out.println(CHOOSE_SECTOR.getText());
								System.out.println(CHOOSE_SIMULATION.getText());
							}
					}
			});
		controls.getChildren().addAll(CHOOSE_SECTOR,CHOOSE_SIMULATION,GO);
		controls.setSpacing(10);
		return controls;
		
	}


	private javafx.scene.Node addButtons(Properties prop) 
	{
		VBox controls = new VBox();
		
		Button START = new Button(prop.getProperty("StartText"));
		START.setOnAction(new EventHandler<ActionEvent>()
				{
				public void handle (ActionEvent e)
					{
						System.out.println("pressed start button");
						if (START.getText().equals(prop.getProperty("StartText")))
						{
							START.setText(prop.getProperty("ResetText"));
						}
						else
						{
							START.setText(prop.getProperty("StartText"));
						}
					}
				});
		
		Button PAUSE = new Button(prop.getProperty("PauseText"));
		PAUSE.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						System.out.println("pressed pause button");
						if (PAUSE.getText().equals(prop.getProperty("PauseText")))
						{
							PAUSE.setText(prop.getProperty("ResumeText"));
						}
						else
						{
							PAUSE.setText(prop.getProperty("PauseText"));
						}
					}
			});
		
		Button STEP = new Button(prop.getProperty("StepText"));
		STEP.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						System.out.println("pressed step button");
					}
			});
		
		Button STOP = new Button( prop.getProperty("StopText"));
		STOP.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						System.out.println("pressed stop button");
					}
			});
		
		Slider ANIMATION_RATE = new Slider(0, 20, 1);
		
		controls.getChildren().addAll(START,PAUSE, STEP, STOP, ANIMATION_RATE);
		controls.setSpacing(10);
		return controls;
	}

	private void fillSimulationArray(String SimulationFileName) 
	{
		try
		{
			File NEW_SIMULATION = new File("data/" + SimulationFileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document sim = db.parse(NEW_SIMULATION);
			sim.getDocumentElement().normalize();
			
			NodeList SimulationProperties = sim.getElementsByTagName("Properties");
			for (int i = 0; i < SimulationProperties.getLength(); i++)
			{
				Node PROPERTY = SimulationProperties.item(i);
				if (PROPERTY.getNodeType() == Node.ELEMENT_NODE)
				{
					Element property = (Element) PROPERTY;
					String type = property.getElementsByTagName("Type").item(0).getTextContent();
					int width = Integer.parseInt(property.getElementsByTagName("Width").item(0).getTextContent());
					int height = Integer.parseInt(property.getElementsByTagName("Height").item(0).getTextContent());
					
					CURRENT_CONFIGURATION = new CellOccupant[width][height];
					
//					CURRENT_SIMULATION = new Simulation(CURRENT_CONFIGURATION);

//					CURRENT_CONFIGURATION = new CellOccupant[width][height];
					
//					CellOccupant[][] CURRENT_CONFIGURATION = new CellOccupant[width][height];
					
//					CURRENT_SIMULATION = createSimulation(type, CURRENT_CONFIGURATION);
					
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
					String COLOR = occupant.getElementsByTagName("Color").item(0).getTextContent();
					int[] initLocation = new int[2];
					initLocation[0] = xCor;
					initLocation[1] = yCor;
					Paint initColor = Color.valueOf(COLOR);
					
					CURRENT_CONFIGURATION[xCor][yCor] = new FireOccupant(initState, initLocation, initColor);
					
//					CURRENT_SIMULATION.getOccupantGrid()[xCor][yCor] = fire;
				
//					CellOccupant x = createCellOccupant(simulationtype, initstate, initlocation, initcolor)
//					CURRENT_SIMULATION.getGrid()[xCor][yCor] 
//					CURRENT_CONFIGURATION[xCor][yCor] = new SegOccupant(initState, initLocation, initColor);
				}
			}
			
			CURRENT_SIMULATION = new Simulation(CURRENT_CONFIGURATION);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

//	private Simulation createSimulation(String type, CellOccupant[][] configuration) 
//	{
//		if (type.equals("SegregationModel"))
//		{
//			return new SegregationModel(configuration);
//		}
//		else if(type.equals("SpreadingFire"))
//		{
//			return new SpreadingFire(configuration);
//		}
//		
//	}

//	private CellOccupant createCellOccupant(String simulationType, int initState, int initLocation, Paint initColor)
//	{
//		
//	}

	private GridPane displaySimulationConfiguration(CellOccupant[][] CONFIGURATION) 
	{
		GridPane SIMULATION_DISPLAY = new GridPane();
		for (int i = 0; i < CONFIGURATION.length; i++)
		{
			for(int j = 0; j<CONFIGURATION[i].length; j++)
			{
				Rectangle r = new Rectangle(20,20);
				r.setFill(CONFIGURATION[i][j].getCurrentPaint());
				r.setStroke(Color.BLACK);
				SIMULATION_DISPLAY.add(r, i, j);
			}
		}
		return SIMULATION_DISPLAY;
	}

	private void updateAll(double secondDelay, Stage primaryStage)
	{
		CURRENT_SIMULATION.setNextStates();
		CURRENT_SIMULATION.updateStates();
		root.getChildren().remove(CURRENT_DISPLAY);
		
		CURRENT_DISPLAY = displaySimulationConfiguration(CURRENT_SIMULATION.getOccupantGrid());
		System.out.println(CURRENT_SIMULATION.getOccupant(1, 2).getCurrentState());
		System.out.println(CURRENT_SIMULATION.getOccupant(1, 2).getCurrentPaint().toString());
		root.setCenter(CURRENT_DISPLAY);
	
		//simulation.update based on seconds
		
		//displaySimulationConfiguration(CURRENT_CONFIGURATION);	
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
}
