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

/**
 * @author Belanie Nagiel
 *
 *This is an abstract Class that creates the framework for buttons that involve information from 
 *text fields. I was only able to implement it for one such instance, but it helps define the
 *Text fields and relate them to the even for a specific button. Useful for creating buttons associated
 *with creating new XML files.
 *
 */
public abstract class TextFields 
{
	private List<TextField> fields;
	private Button myButton;
	private HBox myNode;
	
	/**
	 * Class constructor
	 * Creates the Button and calls the method to define the button event.
	 * 
	 * @param text the text for the Button
	 * @param prop contains the information for button text from the properties file
	 * @param animation 
	 * @param primaryStage
	 * @param fileName 
	 * @param currentDisplay
	 * @param texts
	 */
	public TextFields(String text, Properties prop, Timeline animation, Stage primaryStage, String fileName, DisplayGrid currentDisplay, List<TextField> texts)
	{
		myNode = new HBox();
		fields = addFields(prop, texts);
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
	
	/**
	 * getter for the HBox containing the text field/s and button
	 * 
	 * @return HBOx of the UI components
	 */
	public HBox getMyNode()
	{
		return myNode;
	}
	
	/**
	 * Getter for the button associated with this object
	 * 
	 * @return Button
	 */
	public Button getMyButton()
	{
		return myButton;
	}
	
	/**
	 * Getter for the list of text fields associated with the object
	 * 
	 * @return list of text fields
	 */
	public List<TextField> getTextFields()
	{
		return fields;
	}
	
	/**
	 * Setter for the list of text fields associated with the object
	 * 
	 * @param texts list of text fields
	 */
	public void setTextFields(List<TextField> texts)
	{
		fields = texts;
	}

	/**
	 * Abstract method that adds necessary text fields to the list of text fields
	 * that will be used to create the Node returned to the setup class.
	 * 
	 * @param prop contains information for the naming of text fields
	 * @param texts
	 * 
	 * @return list of text fields
	 */
	public abstract List<TextField> addFields(Properties prop, List<TextField> texts);
	
	/**
	 * Abstract method that specifies the event that occurs when the button is pressed.
	 * 
	 * @param myButton
	 * @param animation
	 * @param primaryStage
	 * @param texts the relevant text fields
	 * @param fileName name for the newly created XML file
	 * @param currentDisplay
	 */
	public abstract void buttonEvent(Button myButton, Timeline animation, Stage primaryStage, List<TextField> texts, String fileName, DisplayGrid currentDisplay);
}
