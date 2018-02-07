package setupGUI;

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
import simulation.CellOccupant;

public class Setup extends Application
{	
	/*
	 * Allow users to save the current state of the simulation as an XML configuration file
	 * Implement error checking for incorrect file data
	 * Allow simulations initial configuration to be set by list of specific locations and states, 
	 * 	completely randomly based on a total number of locations to occupy, or randomly based on 
	 * 	probability/concentration distributions
	 * Allow simulations to be "styled", such as (but not necessarily limited to): kind of grid to use,
	 * 	both by shapes and by edges, size of each grid location (instead of it being calculated, requires 
	 * 	that scrolling is implemented), whether or not grid locations should be outlined (i.e., to be able
	 * 	to "see" the grid or not), color of cell or patch states (at least support empty to represent a water
	 * 	world or space world, etc.), shape of cells or patches within the grid's shape (i.e., circles, rectangles,
	 * 	or arbitrary images), neighbors to consider (i.e., cardinal directions, diagonal directions, or all directions)
	 * 	with appropriate error checking (e.g., hexagonal grids do not have cardinal directions)
	 * Display a graph of the populations of all of the "kinds" of cells over the time of the simulation
	 * Allow users to interact with the simulation dynamically to change the values of its parameters
	 * Allow users to interact with the simulation dynamically to create or change a state at a grid location
	 * DONE: Allow users to run multiple simulations at the same time so they can compare the results side by side 
	 * 	(i.e., do not use tabs like a browser).
	 */
	//exception handling
	//make separate classes for buttons?
	private Scene SCENE;
	private final String TITLE = "CA Simulations";
	private static final int WIDTH = 600;
	private static final int HEIGHT = 500;
	private static final Paint BACKGROUND = Color.WHITE;
	private static int FRAMES_PER_SECOND = 1;
	private static double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Timeline ANIMATION = new Timeline();

	private BorderPane root;
	private DisplayGrid CURRENT_DISPLAY;
	
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
		
		makeFrames(primaryStage, FRAMES_PER_SECOND);
	}
	
	public void makeFrames(Stage primaryStage, int framesPerSecond)
	{
		double millisecondDelay = 1000.0 / framesPerSecond;
		double secondDelay = 1.0/ framesPerSecond;
		KeyFrame frame = new KeyFrame(Duration.millis(millisecondDelay), e->updateAll(secondDelay, primaryStage));
		ANIMATION.setCycleCount(Timeline.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);
	}

	private Scene openingScene(int width, int height, Paint myBackground, Stage primaryStage)
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop;
		
		prop = loadUIConfigurations(primaryStage);
		
		root.setBottom(addTextFields(prop, primaryStage));
		
		
		return scene;	
	}

	private Scene setupScene(int width, int height, Paint myBackground, Stage primaryStage, String SimulationFileName) 
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop = loadUIConfigurations(primaryStage);
		root.setLeft(addButtons(prop, primaryStage));
		root.setBottom(addTextFields(prop, primaryStage));
		
		CURRENT_DISPLAY = new  DisplayGrid(SimulationFileName, primaryStage);
		CURRENT_DISPLAY.fillSimulationArray();
		
		Text SimulationType = new Text(CURRENT_DISPLAY.getCURRENT_SIMULATION_TYPE());
		root.setTop(SimulationType);
		
		root.setCenter(CURRENT_DISPLAY.displaySimulationConfiguration());
		
		return scene;
	}
	
	private Properties loadUIConfigurations(Stage primaryStage)
	{
		Properties prop = new Properties();
		try
		{
			InputStream configs = new FileInputStream("data/UserInterfaceConfigurations.properties");
			prop.load(configs);	
		}
		catch (FileNotFoundException e)
		{
			System.out.println("UI Configurations file not found");
			primaryStage.close();
		}
		catch(IOException e)
		{
			System.out.println("Could not load .properties file into Properties object");
			primaryStage.close();
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
		FileChooser SECOND_SIMULATION = new FileChooser();
		Button SECOND = new Button(prop.getProperty("SecondChooserText"));
		SECOND.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle (ActionEvent e)
						{
							SECOND_SIMULATION.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
							File path = new File("./data");
							SECOND_SIMULATION.setInitialDirectory(path);
							File file = CHOOSE_SIMULATION.showOpenDialog(primaryStage);
							if (file != null)
							{
								Stage secondStage = new Stage();
								SimulationFileName = file.getName();
								resetSimulation(secondStage);
							}
						}
				});
		
		controls.getChildren().addAll(CHOOSER,SECOND);
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
		
		Text INFO = new Text("Frames Per Second:");
		Text RATE = new Text();
		Slider ANIMATION_RATE = new Slider(0.1,10, FRAMES_PER_SECOND);
		ANIMATION_RATE.valueProperty().addListener(new ChangeListener<Number>() 
				{
			       @Override
			        public void changed(ObservableValue<? extends Number> observable, 
			        		Number oldValue, Number newValue)
			        {
			    	    makeFrames(primaryStage, newValue.intValue());
			    	    RATE.setText(ANIMATION_RATE.valueProperty().getValue().toString());
			        }
				});
		RATE.setText(ANIMATION_RATE.valueProperty().getValue().toString());
		
		controls.getChildren().addAll(START,PAUSE, STOP, STEP, INFO, ANIMATION_RATE, RATE);
		controls.setSpacing(10);
		return controls;
	}

	private void updateAll(double secondDelay, Stage primaryStage)
	{
		CURRENT_DISPLAY.getCURRENT_SIMULATION().setNextStates();
			
		CURRENT_DISPLAY.getCURRENT_SIMULATION().updateStates();
	
		root.setCenter(CURRENT_DISPLAY.displaySimulationConfiguration());
		
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
