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
import userInterface.PauseButton;
import userInterface.StartButton;
import userInterface.StopButton;

/**
 * @author Belanie Nagiel
 * 
 * This is the class that runs the simulation. It sets up the user interface with calls
 * to other classes as well as creation in its own methods. It opens up the screen that
 * the simulations are displayed on as well as calls update methods and display methods 
 * in order to show the simulation move.
 * 
 * @see the user interface with buttons, simulation display, and graph of populations 
 * over time
 */
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

	/**
	 * Start method necessary for Application
	 * 
	 * @param the screen that the scene will be displayed on
	 * @see the user interface
	 */
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
	
	/**
	 * Sets the number of framesPerSecond in order to allow changes to the animation speed.
	 * 
	 * @param primaryStage which window the change is happening on
	 * @param framesPerSecond the desired frames per second
	 */
	public void makeFrames(Stage primaryStage, int framesPerSecond)
	{
		double millisecondDelay = 1000.0 / framesPerSecond;
		double secondDelay = 1.0/ framesPerSecond;
		KeyFrame frame = new KeyFrame(Duration.millis(millisecondDelay), e->updateAll(secondDelay, primaryStage));
		ANIMATION.setCycleCount(Timeline.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);
	}
	
	/**
	 * Sets the scene for the splash screen where no simulation is being displayed. Sets up
	 * only the buttons that allow users to pick existing simulations or create new simulations.
	 * 
	 * @param width
	 * @param height
	 * @param myBackground color of the screen
	 * @param primaryStage
	 * @return the BorderPane object with all of the buttons added to it
	 */
	private Scene openingScene(int width, int height, Paint myBackground, Stage primaryStage)
	{
		root = new BorderPane();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Properties prop;
		
		prop = loadUIConfigurations(primaryStage);
		
		root.setBottom(addTextFields(prop, primaryStage));
		
		return scene;	
	}
	
	/**
	 * Sets the scene for all simulation displays based on the XML file that was selected.
	 * Includes buttons for choosing and creating new simulation files as well as buttons
	 * to interact with the current simulation.
	 * 
	 * @param width
	 * @param height
	 * @param myBackground color of scene
	 * @param primaryStage
	 * @param SimulationFileName
	 * @return the BorderPane object with the buttons, simulation display, and populaiton graph 
	 * added to it
	 */
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
	
	/**
	 * Loads the properties file that contains user interface information into a 
	 * Properties object.
	 * 
	 * @param primaryStage
	 * @return Properties object that contains the text for all the buttons in the user interface
	 */
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

	/**
	 * Adds all the buttons and text fields that have to do with choosing simulations, making
	 * new XML files, and opening new windows.
	 * 
	 * @param prop The Properties object that references the file containing button text
	 * @param primaryStage
	 * @return a VBox that contains the buttons and text fields
	 */
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
		
		HBox randomSim = new HBox();
		TextField newRandomXML = new TextField();
		newRandomXML.setPromptText(prop.getProperty("XMLText"));
		TextField simType = new TextField();
		simType.setPromptText(prop.getProperty("PromptSimType"));
		TextField xSize = new TextField();
		xSize.setPromptText(prop.getProperty("PromptXSize"));
		TextField ySize = new TextField();
		ySize.setPromptText(prop.getProperty("PromptYSize"));
		TextField colors = new TextField();
		colors.setPromptText(prop.getProperty("PromptColors"));
		Button CREATE = new Button(prop.getProperty("RandomText"));
		CREATE.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
				{
				 if (newRandomXML.getText() != null && !newRandomXML.getText().isEmpty() && simType.getText() != null && !simType.getText().isEmpty() && xSize.getText() != null && !xSize.getText().isEmpty() && ySize.getText() != null && !ySize.getText().isEmpty() && colors.getText() != null)
				 {
					 ANIMATION.pause();
					 try 
					 {
					 XMLCreation currentConfigs = new XMLCreation(newRandomXML.getText());

					 List<String> colorsList = Arrays.asList(colors.getText().split(","));
					 String[] colors = new String[colorsList.size()];
					 for (int j = 0; j<colorsList.size(); j++)
					 {
						 colors[j] = colorsList.get(j);
					 }
					 currentConfigs.createRandomXML(simType.getText(), Integer.parseInt(xSize.getText()),Integer.parseInt(ySize.getText()),colors);
					 SimulationFileName = newRandomXML.getText() + ".xml";
					 resetSimulation(primaryStage);
					 }
					 catch(LoadGridException e1) {
						 hardReset(primaryStage);
					 }
					 catch(Exception e2)
					 {
						 hardReset(primaryStage);
					 }
				 }
				}
		});
		randomSim.getChildren().addAll(newRandomXML,simType,xSize,ySize, colors);
		randomSim.setSpacing(SPACING);
		
		TextField percentages = new TextField();
		percentages.setPromptText(prop.getProperty("PercentagePromp"));
		Button PERCENTAGE = new Button(prop.getProperty("PercentageText"));
		PERCENTAGE.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
			{
				if (newRandomXML.getText() != null && !newRandomXML.getText().isEmpty() && simType.getText() != null && !simType.getText().isEmpty() && xSize.getText() != null && !xSize.getText().isEmpty() && ySize.getText() != null && !ySize.getText().isEmpty() && colors.getText() != null)
				{
					ANIMATION.pause();
					XMLCreation currentConfigs = new XMLCreation(newRandomXML.getText());

					List<String> colorsList = Arrays.asList(colors.getText().split(","));
					String[] colors = new String[colorsList.size()];
					for (int j = 0; j<colorsList.size(); j++)
					{
						colors[j] = colorsList.get(j);
					}

					List<String> percentageList = Arrays.asList(percentages.getText().split(","));
					int[] percentages = new int[percentageList.size()];
					for (int i = 0; i<percentageList.size(); i++)
					{
						percentages[i] = Integer.parseInt(percentageList.get(i));
					}
					try
					{
						currentConfigs.createWithPopulationPercentages(simType.getText(), Integer.parseInt(xSize.getText()),Integer.parseInt(ySize.getText()),colors,percentages);
					}
					catch (NumberFormatException e1) 
					{
						// TODO Auto-generated catch block
						hardReset(primaryStage);
					}
					catch(LoadGridException e1)
					{
						hardReset(primaryStage);
					}

					SimulationFileName = newRandomXML.getText() + ".xml";
					resetSimulation(primaryStage);
				}
			}
		});
		HBox someButtons = new HBox();
		someButtons.getChildren().addAll(CREATE,percentages, PERCENTAGE);
		someButtons.setSpacing(SPACING);
		
		VBox result = new VBox();
		result.getChildren().addAll(randomSim, someButtons, controls);
		result.setSpacing(SPACING);
		
		return result;

		
	}

	/**
	 * Adds all the buttons and text fields that have to do with controlling the simulation
	 * while it is running such as pausing it, restarting it, and speeding it up.
	 * 
	 * @param prop
	 * @param primaryStage
	 * @return a VBox that contains the buttons and text fields
	 */
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
					try 
					{
						displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
					}
					catch(LoadGridException e)
					{
						hardReset(primaryStage);
					}
					root.setCenter(displays);
				});

		TextField newXML = new TextField();
		newXML.setPromptText(prop.getProperty("XMLText"));
		Button CREATE = new Button(prop.getProperty("XMLText"));
		CREATE.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
			{
				if (newXML.getText() != null && !newXML.getText().isEmpty())
				{
					ANIMATION.pause();
					XMLCreation currentConfigs = new XMLCreation(newXML.getText());
					currentConfigs.currentGridToXML(CURRENT_DISPLAY);
				}

			}
		});
		HBox xml = new HBox();
		xml.getChildren().addAll(newXML,CREATE);
		xml.setSpacing(SPACING);

		controls.getChildren().addAll(START.getMyButton(),PAUSE.getMyButton(), STOP.getMyButton(), STEP, INFO, ANIMATION_RATE, RATE, GRID_LINES, xml);
		controls.setSpacing(SPACING);
		return controls;
	}

	/**
	 * This is the game loop that updates the simulation as time passes. It calls 
	 * the simulation update methods and then resets the display to match the new configuration.
	 *
	 * @param secondDelay
	 * @param primaryStage
	 */
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
	
	/**
	 * Sends the user back to the splash screen. Useful for addressing errors.
	 * 
	 * @param primaryStage
	 */
	private void hardReset(Stage primaryStage)
	{
		Setup.SimulationFileName = "";
		resetSimulation(primaryStage);
	}
	
	/**
	 * Resets the current simulation to its original configuration.
	 * 
	 * @param primaryStage
	 */
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
