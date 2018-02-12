package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import setupGUI.Setup;

/**
 * @author Belanie Nagiel
 * 
 * This is an abstract class that creates the basis for some of the buttons
 * in the UI. It specifically handles the buttons that deal with the Animation's 
 * running. It provides a start for the creation of the Start/Reset, Pause/Resume,
 * and Stop Buttons. 
 *
 */
public abstract class Buttons 
{
	private Button myButton;
	
	/**
	 * Class Constructor 
	 * 
	 * @param text the button text
	 * @param prop the Properties file that the button texts are being read from
	 * @param animation the current animation
	 * @param primaryStage the current window's stage
	 */
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
	
	/**
	 * Getter for the button associated with this object
	 * 
	 * @return Button
	 */
	public Button getMyButton()
	{
		return myButton;
	}
	
	/**
	 * Specifies the event that occurs when the button is pressed.
	 * 
	 * @param button
	 * @param prop the properties file that contains button text
	 * @param animation 
	 * @param primaryStage
	 */
	public abstract void buttonEvent(Button button, Properties prop, Timeline animation, Stage primaryStage);
	
	public void resetSimulation(Stage primaryStage, Timeline animation)
	{
		animation.stop();
		Setup newGame = new Setup();
		newGame.start(primaryStage);
	}

}
