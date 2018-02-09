package setupGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public abstract class UserInteraction 
{
	private Button myButton;
	
	public UserInteraction(String text)
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
	
	public abstract void buttonEvent();

}
