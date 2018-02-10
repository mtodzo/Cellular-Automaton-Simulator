package setupGUI;

import java.util.Properties;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class StartButton extends Buttons
{

	public StartButton(String text) {
		super(text);
	}

	@Override
	public void buttonEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(Properties prop, Timeline animation, Stage primaryStage) 
	{
		Button b = this.getMyButton();
		b.setOnAction(new EventHandler<ActionEvent>()
		{
		public void handle (ActionEvent e)
			{
				if (b.getText().equals(prop.getProperty("StartText")))
				{
					animation.play();
					b.setText(prop.getProperty("ResetText"));
				}
				else
				{
					b.setText(prop.getProperty("StartText"));
					//resetSimulation(primaryStage);
				}
			}
		});
		
	}

}
