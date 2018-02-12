package userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import setupGUI.DisplayGrid;
import setupGUI.LoadGridException;
import setupGUI.XMLCreation;
/**
 * @author Belanie Nagiel
 * 
 * Creates an instance of the TextFields abstract class in order to create an XML
 * file based on the current configuration of the simulation.
 *
 */
public class CreateXML extends TextFields{

	/**
	 * Class constructor
	 * 
	 * @param text
	 * @param prop
	 * @param animation
	 * @param primaryStage
	 * @param fileName
	 * @param currentDisplay
	 * @param texts
	 */
	public CreateXML(String text, Properties prop, Timeline animation, Stage primaryStage, String fileName, DisplayGrid currentDisplay,  List<TextField> texts) 
	{
		super(text, prop, animation, primaryStage, fileName, currentDisplay, texts);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Adds file name prompt.
	 * 
	 * @param prop contains information for the naming of text fields
	 * @param texts
	 * 
	 * @return list of text fields
	 */
	@Override
	public List<TextField> addFields(Properties prop,  List<TextField> texts) 
	{
		List<TextField> text= new ArrayList<>();
		TextField newXML = new TextField();
		newXML.setPromptText(prop.getProperty("XMLText"));
		text.add(newXML);
		return text;
	}
	
	/**
	 * Defines the event for the button 
	 * 
	 * @param myButton
	 * @param animation
	 * @param primaryStage
	 * @param texts the relevant text fields
	 * @param fileName name for the newly created XML file
	 * @param currentDisplay
	 */
	@Override
	public void buttonEvent(Button myButton, Timeline animation, Stage primaryStage, List<TextField> texts,
			String fileName, DisplayGrid currentDisplay) 
	{
		if (texts.get(0).getText() != null && !texts.get(0).getText().isEmpty())
		 {
			 animation.pause();
			 XMLCreation currentConfigs = new XMLCreation(texts.get(0).getText());
			 try {
				currentConfigs.currentGridToXML(currentDisplay);
			} catch (LoadGridException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not convert grid to XML");
			}
		 }
		
	}

}
