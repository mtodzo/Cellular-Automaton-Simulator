package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * @author Belanie Nagiel
 * 
 * Creates an instance of the abstract Buttons class for the Start/Reset button.
 *
 */
public class StartButton extends Buttons
{

	/**
	 * Class constructor
	 * 
	 * @param text
	 * @param prop
	 * @param animation
	 * @param primaryStage
	 */
	public StartButton(String text, Properties prop, Timeline animation, Stage primaryStage) {
		super(text,prop,animation,primaryStage);
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

		if (button.getText().equals(prop.getProperty("StartText")))
		{
			animation.play();
			button.setText(prop.getProperty("ResetText"));
		}
		else
		{
			button.setText(prop.getProperty("StartText"));
			resetSimulation(primaryStage, animation);
		}	
	}

}
