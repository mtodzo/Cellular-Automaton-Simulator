package userInterface;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class StartButton extends Buttons
{

	public StartButton(String text, Properties prop, Timeline animation, Stage primaryStage) {
		super(text,prop,animation,primaryStage);
	}


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
