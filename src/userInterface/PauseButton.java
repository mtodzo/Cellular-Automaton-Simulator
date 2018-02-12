package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Belanie Nagiel
 * 
 * Creates an instance of the abstract Buttons class for the Pause/Resume button.
 *
 */
public class PauseButton extends Buttons {

	/**
	 * Class constructor
	 * 
	 * @param text
	 * @param prop
	 * @param animation
	 * @param primaryStage
	 */
	public PauseButton(String text, Properties prop, Timeline animation, Stage primaryStage) 
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
		if (button.getText().equals(prop.getProperty("PauseText")))
		{
			animation.pause();
			button.setText(prop.getProperty("ResumeText"));
		}
		else
		{
			animation.play();
			button.setText(prop.getProperty("PauseText"));
		}
	}

}
