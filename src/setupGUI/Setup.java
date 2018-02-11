package setupGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import userInterface.Buttons;
import userInterface.CreateXML;
import userInterface.PauseButton;
import userInterface.RandomXML;
import userInterface.StartButton;
import userInterface.StopButton;
import userInterface.TextFields;

public class Setup extends Application
{	
	private static final String TITLE = "CA Simulations";
	private static final int WIDTH = 900;
	private static final int HEIGHT = 700;
	private static final Paint BACKGROUND = Color.WHITE;
	private static int FRAMES_PER_SECOND = 1;
	private static double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Timeline ANIMATION = new Timeline();

	private BorderPane root;
	private DisplayGrid CURRENT_DISPLAY;
	private PopulationGraph CURRENT_POPULATION_GRAPH;
	private String filterType = "XML Files";
	private String filterExtension= "*.xml";
	private String dataFilePath = "./data";
	private double maxAnimationSpeed = 10;
	private double animationSpeedStep = 0.1;
	private int SPACING = 10;
	
	private static String SimulationFileName = "";

	@Override
	public void start(Stage primaryStage)
	{
		if (SimulationFileName.equals(""))
		{
			Scene SCENE = openingScene(WIDTH, HEIGHT, BACKGROUND, primaryStage);
			primaryStage.setScene(SCENE);
		}
		else
		{
			Scene SCENE = setupScene(WIDTH, HEIGHT, BACKGROUND, primaryStage, SimulationFileName);
			primaryStage.setScene(SCENE);
		}
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
		try 
		{
			CURRENT_DISPLAY.fillSimulationArray();
			
			CURRENT_POPULATION_GRAPH = new PopulationGraph(CURRENT_DISPLAY);
			CURRENT_POPULATION_GRAPH.updatePopulationGraph();
			
			Text SimulationType = new Text(CURRENT_DISPLAY.getCURRENT_SIMULATION_TYPE());
			root.setTop(SimulationType);
			
			VBox displays = new VBox();
			displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
			root.setCenter(displays);
			
		}
		catch(LoadGridException e)
		{
			hardReset(primaryStage);
		}
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
							CHOOSE_SIMULATION.getExtensionFilters().add(new ExtensionFilter(filterType, filterExtension));
							File path = new File(dataFilePath);
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
							SECOND_SIMULATION.getExtensionFilters().add(new ExtensionFilter(filterType, filterExtension));
							File path = new File(dataFilePath);
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
		controls.setSpacing(SPACING);
		
		TextFields randomSim = new RandomXML(prop.getProperty("RandomText"), prop, ANIMATION, primaryStage, SimulationFileName, CURRENT_DISPLAY);
		randomSim.getMyNode().setSpacing(SPACING);
		
		VBox result = new VBox();
		result.getChildren().addAll(randomSim.getMyNode(), randomSim.getMyButton(), controls);
		result.setSpacing(SPACING);
		
		return result;
		
	}

	private javafx.scene.Node addButtons(Properties prop, Stage primaryStage) 
	{
		VBox controls = new VBox();
	
		Buttons START = new StartButton(prop.getProperty("StartText"),prop,ANIMATION, primaryStage);
		Buttons PAUSE = new PauseButton(prop.getProperty("PauseText"),prop, ANIMATION, primaryStage);
		Buttons STOP = new StopButton(prop.getProperty("StopText"),prop, ANIMATION, primaryStage);
		
		Button STEP = new Button(prop.getProperty("StepText"));
		STEP.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle (ActionEvent e)
					{
						ANIMATION.pause();
						updateAll(SECOND_DELAY, primaryStage);
						PAUSE.getMyButton().setText(prop.getProperty("ResumeText"));
					}
			});
		
		Text INFO = new Text(prop.getProperty("FrameText"));
		Text RATE = new Text();
		Slider ANIMATION_RATE = new Slider(animationSpeedStep, maxAnimationSpeed, FRAMES_PER_SECOND);
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
		
		CheckBox GRID_LINES = new CheckBox(prop.getProperty("GridLinesText"));
		GRID_LINES.selectedProperty().addListener((ObservableValue<? extends Boolean> ov,
				Boolean old_val, Boolean new_val) -> 
				{
					CURRENT_DISPLAY.setShowGridLines(new_val);
					VBox displays = new VBox();
//					try 
//					{
						displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
//					}
//					catch(LoadGridException e)
//					{
//						hardReset(primaryStage);
//					}
					root.setCenter(displays);
				});
		
		TextFields newXML = new CreateXML(prop.getProperty("XMLText"), prop, ANIMATION, primaryStage, SimulationFileName, CURRENT_DISPLAY);
		newXML.getMyNode().getChildren().add(newXML.getMyButton());
		newXML.getMyNode().setSpacing(SPACING);
		
		controls.getChildren().addAll(START.getMyButton(),PAUSE.getMyButton(), STOP.getMyButton(), STEP, INFO, ANIMATION_RATE, RATE, GRID_LINES, newXML.getMyNode());
		controls.setSpacing(SPACING);
		return controls;
	}

	private void updateAll(double secondDelay, Stage primaryStage)
	{
		try 
		{
		CURRENT_DISPLAY.getCURRENT_SIMULATION().setNextStates();
			
		CURRENT_DISPLAY.getCURRENT_SIMULATION().updateStates();
		
		CURRENT_POPULATION_GRAPH.updatePopulationGraph();
	
		VBox displays = new VBox();
		displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
		root.setCenter(displays);
		}
		catch(Exception e) {
			try {
				throw new LoadGridException("Load a file before running it");
			} catch (LoadGridException e1) {
				hardReset(primaryStage);
			}
			
		}
	}
	
	private void hardReset(Stage primaryStage)
	{
		Setup.SimulationFileName = "";
		resetSimulation(primaryStage);
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
