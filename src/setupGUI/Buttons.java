package setupGUI;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Buttons 
{
	private Button myButton;
	
	public Buttons(String text)
	{
		myButton = new Button(text);
		myButton.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
				{
					buttonEvent();
				}
		});
	}
	
	public Button getMyButton()
	{
		return myButton;
	}
	
	public abstract void buttonEvent();
	
	public abstract void buttonEvent(Properties prop, Timeline animation, Stage primaryStage);

}
