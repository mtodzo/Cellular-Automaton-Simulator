package userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import setupGUI.DisplayGrid;
import setupGUI.XMLCreation;

public class CreateXML extends TextFields{

	public CreateXML(String text, Properties prop, Timeline animation, Stage primaryStage, String fileName, DisplayGrid currentDisplay,  List<TextField> texts) 
	{
		super(text, prop, animation, primaryStage, fileName, currentDisplay, texts);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TextField> addFields(Properties prop,  List<TextField> texts) 
	{
		List<TextField> text= new ArrayList<>();
		TextField newXML = new TextField();
		newXML.setPromptText(prop.getProperty("XMLText"));
		text.add(newXML);
		return text;
	}

	@Override
	public void buttonEvent(Button myButton, Timeline animation, Stage primaryStage, List<TextField> texts,
			String fileName, DisplayGrid currentDisplay) 
	{
		if (texts.get(0).getText() != null && !texts.get(0).getText().isEmpty())
		 {
			 animation.pause();
			 XMLCreation currentConfigs = new XMLCreation(texts.get(0).getText());
			 currentConfigs.currentGridToXML(currentDisplay);
		 }
		
	}

}
