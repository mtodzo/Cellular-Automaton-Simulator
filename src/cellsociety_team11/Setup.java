package cellsociety_team11;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
	 * one.The display size of an individual cell should be calculated each time by the grid's total size, but the
	 * size of the visualization window should not change. Allow users to pause and resume the simulation, as 
	 * well as step forward through it.Allow users to speed up or slow down the simulation's animation rate.
	 * Any text displayed in the user interface should be set using resource files, not hard-coded.
	 */

	/*
	 * Read in an XML formatted file that contains the initial settings for a simulation. The file contains three parts:
	 * name of the kind of simulation it represents, as well as a title for this simulation and this simulation's author
	 * settings for global configuration parameters specific to the simulation dimensions of the grid and the initial
	 *  configuration of the states for the cells in the grid
	 */
	private Scene SCENE;
	private final String TITLE = "CA Simulations";
	private static final int WIDTH = 600;
	private static final int HEIGHT = 500;
	private static final Paint BACKGROUND = Color.WHITE;
	private static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Timeline ANIMATION = new Timeline();

	@Override
	public void start(Stage primaryStage)
	{
		SCENE = setupScene(WIDTH, HEIGHT, BACKGROUND, primaryStage);
		primaryStage.setScene(SCENE);
		primaryStage.setTitle(TITLE);
		primaryStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e->updateAll(SECOND_DELAY, primaryStage));
		ANIMATION.setCycleCount(Timeline.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);
		ANIMATION.play();
		//SCENE.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
	}
	
	
	//given a 2d array it has to create an image for it
	private Scene setupScene(int width, int height, Paint myBackground, Stage primaryStage) 
	{
		Group root = new Group();
		
		Scene scene = new Scene(root, width, height, myBackground);
		try{
			File GUIconfigurations = new File("data/GUIconfigurations.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document configs = db.parse(GUIconfigurations);
			configs.getDocumentElement().normalize();
			
			NodeList buttons = configs.getElementsByTagName("Button");
			NodeList textFields = configs.getElementsByTagName("TextField");
			addButtons(root, buttons);
			addTextFields(root, textFields);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		fillSimulationArray();
		
		showSimulationConfiguration();
		
		Rectangle r = new Rectangle(0,0,5,5);
		r.setFill(Color.BLUE);
		root.getChildren().add(r);
		
		return scene;
	}

	private void addButtons(Group root, NodeList buttons) 
	{
		for (int i = 0; i < buttons.getLength(); i++)
		{
			Node button = buttons.item(i);
			
			if (button.getNodeType() == Node.ELEMENT_NODE)
			{
				Element myButton = (Element) button;
				Button b = new Button(myButton.getElementsByTagName("Text1").item(0).getTextContent());
				b.setLayoutX(Integer.parseInt(myButton.getElementsByTagName("xCoordinate").item(0).getTextContent()));
				b.setLayoutY(Integer.parseInt(myButton.getElementsByTagName("yCoordinate").item(0).getTextContent()));
//				b.setOnAction(new EventHandler<ActionEvent>()
//				{
//					public void handle (ActionEvent e)
//						{
//						}
//				});
				root.getChildren().add(b);
			}
		}
		//What does he mean by "Any text displayed in the user interface should be set using resource files, not hard-coded"???
	}
	
	private void addTextFields(Group root, NodeList textFields) 
	{
		for (int i = 0; i < textFields.getLength(); i++)
		{
			Node textField = textFields.item(i);
			
			if (textField.getNodeType() == Node.ELEMENT_NODE)
			{
				Element myTextField = (Element) textField;
				TextField tf = new TextField();
				tf.setPromptText(myTextField.getElementsByTagName("Prompt").item(0).getTextContent());
				tf.setLayoutX(Integer.parseInt(myTextField.getElementsByTagName("xCoordinate").item(0).getTextContent()));
				tf.setLayoutY(Integer.parseInt(myTextField.getElementsByTagName("yCoordinate").item(0).getTextContent()));
				tf.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle (ActionEvent e)
						{
							System.out.println(tf.getText());
						}
				});
				root.getChildren().add(tf);
			}
		}		
	}
	
	private void fillSimulationArray() 
	{
		// TODO Auto-generated method stub
		
	}

	private void showSimulationConfiguration() 
	{
		// TODO Auto-generated method stub
		
	}

	private void updateAll(double secondDelay, Stage primaryStage)
	{
		
		
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
}
