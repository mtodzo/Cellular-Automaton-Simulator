package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Belanie Nagiel
 * 
 * Creates an instance of the abstract Buttons class for the Stop button.
 *
 */
public class StopButton extends Buttons {

	/**
	 * Class constructor
	 * 
	 * @param text
	 * @param prop
	 * @param animation
	 * @param primaryStage
	 */
	public StopButton(String text, Properties prop, Timeline animation, Stage primaryStage) 
	{
		super(text, prop, animation, primaryStage);
	}

	/**
	 * Defines the event for the button 
	 * 
	 * @param button
	 * @param prop
	 * @param animation
	 * @param primaryStage
	 */
	@Override
	public void buttonEvent(Button button, Properties prop, Timeline animation, Stage primaryStage) 
	{
		animation.stop();
	}

}
