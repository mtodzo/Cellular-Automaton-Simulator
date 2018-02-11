package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import setupGUI.Setup;

public abstract class Buttons 
{
	private Button myButton;
	
	public Buttons(String text, Properties prop, Timeline animation, Stage primaryStage)
	{
		myButton = new Button(text);
		myButton.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
				{
					buttonEvent(myButton, prop,animation,primaryStage);
				}
		});
	}
	
	public Button getMyButton()
	{
		return myButton;
	}
	
	
	public abstract void buttonEvent(Button button, Properties prop, Timeline animation, Stage primaryStage);
	
	public void resetSimulation(Stage primaryStage, Timeline animation)
	{
		animation.stop();
		Setup newGame = new Setup();
		newGame.start(primaryStage);
	}

}
