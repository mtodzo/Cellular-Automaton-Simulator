package setupGUI;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.CheckBox;
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
	//private Scene SCENE;
	private static final String TITLE = "CA Simulations";
	private static final int WIDTH = 800;
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
		CURRENT_DISPLAY.fillSimulationArray();
		
		CURRENT_POPULATION_GRAPH = new PopulationGraph(CURRENT_DISPLAY);
		CURRENT_POPULATION_GRAPH.updatePopulationGraph();
		
		Text SimulationType = new Text(CURRENT_DISPLAY.getCURRENT_SIMULATION_TYPE());
		root.setTop(SimulationType);
		
		VBox displays = new VBox();
		displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
		root.setCenter(displays);
		
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
				 if (newRandomXML.getText() != null && !newRandomXML.getText().isEmpty() && simType.getText() != null && !simType.getText().isEmpty() && xSize.getText() != null && !xSize.getText().isEmpty() && ySize.getText() != null && !ySize.getText().isEmpty() && colors.getText() != null && !colors.getText().isEmpty() )
				 {
					 ANIMATION.pause();
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
				}
		});
		randomSim.getChildren().addAll(newRandomXML,simType,xSize,ySize, colors);
		randomSim.setSpacing(SPACING);
		
		VBox result = new VBox();
		result.getChildren().addAll(randomSim, CREATE, controls);
		result.setSpacing(SPACING);
		
		return result;
		
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
					displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
					root.setCenter(displays);
				});
		
		TextField newXML = new TextField();
		newXML.setPromptText(prop.getProperty("XMLText"));
		Button CREATE = new Button(prop.getProperty("CreateText"));
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
		HBox newFile = new HBox();
		newFile.getChildren().addAll(newXML,CREATE);
		
		controls.getChildren().addAll(START,PAUSE, STOP, STEP, INFO, ANIMATION_RATE, RATE, GRID_LINES, newFile);
		controls.setSpacing(SPACING);
		return controls;
	}

	private void updateAll(double secondDelay, Stage primaryStage)
	{
		CURRENT_DISPLAY.getCURRENT_SIMULATION().setNextStates();
			
		CURRENT_DISPLAY.getCURRENT_SIMULATION().updateStates();
		
		CURRENT_POPULATION_GRAPH.updatePopulationGraph();
	
		VBox displays = new VBox();
		displays.getChildren().addAll(CURRENT_DISPLAY.displaySimulationConfiguration(), CURRENT_POPULATION_GRAPH.displayPopulationGraph());
		root.setCenter(displays);
		
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
