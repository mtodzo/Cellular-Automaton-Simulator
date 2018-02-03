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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	//change simulation animation rate
	//change the exception handling
	
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
	private static int FRAMES_PER_SECOND = 1;
	private static int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Timeline ANIMATION = new Timeline();
	
	private Simulation CURRENT_SIMULATION;
	private String CURRENT_SIMULATION_TYPE;
	private GridPane CURRENT_DISPLAY;
	private BorderPane root;
	private CellOccupant[][] CURRENT_CONFIGURATION;
	private int BlockSize;
	
	private static String SimulationFileName = "";

	@Override
	public void start(Stage primaryStage)
	{
		if (SimulationFileName.equals(""))
		{
			SCENE = openingScene(WIDTH, HEIGHT, BACKGROUND, primaryStage);
		}
		else
		{
			SCENE = setupScene(WIDTH, HEIGHT, BACKGROUND, primaryStage, SimulationFileName);
		}
		primaryStage.setScene(SCENE);
		primaryStage.setTitle(TITLE);
		primaryStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e->updateAll(SECOND_DELAY, primaryStage));
		ANIMATION.setCycleCount(Timeline.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);
	}

	private Scene openingScene(int width, int height, Paint myBackground, Stage primaryStage)
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop = loadUIConfigurations();
		root.setBottom(addTextFields(prop, primaryStage));
		return scene;	
	}

	private Scene setupScene(int width, int height, Paint myBackground, Stage primaryStage, String SimulationFileName) 
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop = loadUIConfigurations();
		root.setLeft(addButtons(prop, primaryStage));
		root.setBottom(addTextFields(prop, primaryStage));
		
		fillSimulationArray(SimulationFileName);
		
		Text SimulationType = new Text(CURRENT_SIMULATION_TYPE);
		root.setTop(SimulationType);
		
		CURRENT_DISPLAY = displaySimulationConfiguration(CURRENT_SIMULATION.getOccupantGrid(), BlockSize);
		root.setCenter(CURRENT_DISPLAY);
		
		return scene;
	}
	
	private Properties loadUIConfigurations() 
	{
		Properties prop = new Properties();
		try
		{
			InputStream configs = new FileInputStream("data/UserInterfaceConfigurations.properties");
			prop.load(configs);	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return prop;
	}

	private javafx.scene.Node addTextFields(Properties prop, Stage primaryStage) 
	{
		HBox controls = new HBox();
		FileChooser CHOOSE_SIMULATION = new FileChooser();
		Button CHOOSER = new Button(prop.getProperty("FileChooserText"));
		CHOOSER.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle (ActionEvent e)
						{
							CHOOSE_SIMULATION.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
							File path = new File("./data");
							CHOOSE_SIMULATION.setInitialDirectory(path);
							File file = CHOOSE_SIMULATION.showOpenDialog(primaryStage);
							if (file != null)
							{
								SimulationFileName = file.getName();
								resetSimulation(primaryStage);
							}
						}
				});
		controls.getChildren().add(CHOOSER);
		controls.setSpacing(10);
		return controls;
		
	}

	private javafx.scene.Node addButtons(Properties prop, Stage primaryStage) 
	{
		VBox controls = new VBox();
		
		Button START = new Button(prop.getProperty("StartText"));
		START.setOnAction(new EventHandler<ActionEvent>()
				{
				public void handle (ActionEvent e)
					{
						if (START.getText().equals(prop.getProperty("StartText")))
						{
							ANIMATION.play();
							START.setText(prop.getProperty("ResetText"));
						}
						else
						{
							START.setText(prop.getProperty("StartText"));
							resetSimulation(primaryStage);
						}
					}
				});
		
		Button PAUSE = new Button(prop.getProperty("PauseText"));
		PAUSE.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						if (PAUSE.getText().equals(prop.getProperty("PauseText")))
						{
							ANIMATION.pause();
							PAUSE.setText(prop.getProperty("ResumeText"));
						}
						else
						{
							ANIMATION.play();
							PAUSE.setText(prop.getProperty("PauseText"));
						}
					}
			});
		
		Button STOP = new Button(prop.getProperty("StopText"));
		STOP.setOnAction(new EventHandler<ActionEvent>()
				{
				public void handle (ActionEvent e)
					{
						ANIMATION.stop();
					}
				});
		
		Button STEP = new Button(prop.getProperty("StepText"));
		STEP.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						ANIMATION.pause();
						updateAll(SECOND_DELAY, primaryStage);
						PAUSE.setText(prop.getProperty("ResumeText"));
					}
			});
		
		Slider ANIMATION_RATE = new Slider(0,10,1);
		ANIMATION_RATE.valueProperty().addListener(new ChangeListener<Number>() 
				{
			       @Override
			        public void changed(ObservableValue<? extends Number> observable, 
			        		Number oldValue, Number newValue)
			       {
			    	    FRAMES_PER_SECOND = newValue.intValue();
			        }
				});
		
		controls.getChildren().addAll(START,PAUSE, STOP, STEP, ANIMATION_RATE);
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
					
					BlockSize = 400/width;
					CURRENT_CONFIGURATION = new CellOccupant[width][height];
					
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
					
					CURRENT_CONFIGURATION[xCor][yCor] = createCellOccupant(CURRENT_SIMULATION_TYPE, initState,initLocation, initColor);
				}
			}
			
			CURRENT_SIMULATION = new Simulation(CURRENT_CONFIGURATION);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private CellOccupant createCellOccupant(String simulationType, int initState, int[] initLocation, Paint initColor)
	{
		if (simulationType.equals("SpreadingFire"))
		{
			return new FireOccupant(initState, initLocation, initColor);
		}
		else if (simulationType.equals("GameOfLife"))
		{
			return new LifeOccupant(initState, initLocation, initColor);
		}
		else
		{
			return new FireOccupant(initState, initLocation, initColor);
		}
		
	}

	private GridPane displaySimulationConfiguration(CellOccupant[][] CONFIGURATION, int BlockSize) 
	{
		GridPane SIMULATION_DISPLAY = new GridPane();
		for (int i = 0; i < CONFIGURATION.length; i++)
		{
			for(int j = 0; j<CONFIGURATION[i].length; j++)
			{
				Rectangle r = new Rectangle(BlockSize, BlockSize);
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
		
		CURRENT_DISPLAY = displaySimulationConfiguration(CURRENT_SIMULATION.getOccupantGrid(), BlockSize);
		root.setCenter(CURRENT_DISPLAY);
	}
	
	private void resetSimulation(Stage primaryStage)
	{
		ANIMATION.stop();
		Setup newGame = new Setup();
		newGame.start(primaryStage);
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
}
