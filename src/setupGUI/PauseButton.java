package setupGUI;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PauseButton extends Buttons {

	public PauseButton(String text, Properties prop, Timeline animation, Stage primaryStage) 
	{
		super(text, prop, animation, primaryStage);
	}

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
