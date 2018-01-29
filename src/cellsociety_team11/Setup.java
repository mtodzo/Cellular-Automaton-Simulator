package cellsociety_team11;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
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
	
	private Scene setupScene(int width, int height, Paint myBackground, Stage primaryStage) 
	{
		Group root = new Group();
		
		Scene scene = new Scene(root, width, height, myBackground);
		
		Button START = createButton("Start", 200, 200);
		root.getChildren().add(START);
		scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return scene;
	}

	private Button createButton(String text, int xCoordinate, int yCoordinate)
	{
		Button bt = new Button(text);
		bt.setLayoutX(xCoordinate);
		bt.setLayoutY(yCoordinate);
		return bt;
	}


	private void updateAll(double secondDelay, Stage primaryStage)
	{
		
	}
	
	private Object handleMouseInput(double x, double y) 
	{
		
		return null;
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
}
