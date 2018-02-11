package userInterface;

import java.util.List;
import java.util.Properties;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import setupGUI.DisplayGrid;
import setupGUI.Setup;

public abstract class TextFields 
{
	private List<TextField> fields;
	private Button myButton;
	private HBox myNode;
	
	public TextFields(String text, Properties prop, Timeline animation, Stage primaryStage, String fileName, DisplayGrid currentDisplay)
	{
		myNode = new HBox();
		fields = addFields(prop);
		myButton = new Button(text);
		myButton.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle (ActionEvent e)
				{
					buttonEvent(myButton,animation,primaryStage, fields, fileName, currentDisplay);
				}
		});
		for (TextField f: fields)
		{
			myNode.getChildren().add(f);
		}
		//myNode.getChildren().add(myButton);
		
	}
	
	public HBox getMyNode()
	{
		return myNode;
	}
	
	public Button getMyButton()
	{
		return myButton;
	}

	public abstract List<TextField> addFields(Properties prop);
	
	public abstract void buttonEvent(Button myButton, Timeline animation, Stage primaryStage, List<TextField> texts, String fileName, DisplayGrid currentDisplay);

	public void resetSimulation(Stage primaryStage, Timeline animation)
	{
		animation.stop();
		Setup newGame = new Setup();
		newGame.start(primaryStage);
	}
}
