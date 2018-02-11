package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StopButton extends Buttons {

	public StopButton(String text, Properties prop, Timeline animation, Stage primaryStage) 
	{
		super(text, prop, animation, primaryStage);
	}

	@Override
	public void buttonEvent(Button button, Properties prop, Timeline animation, Stage primaryStage) 
	{
		animation.stop();
	}

}
