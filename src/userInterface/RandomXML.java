package userInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import setupGUI.DisplayGrid;
import setupGUI.XMLCreation;

public class RandomXML extends TextFields
{

	public RandomXML(String text, Properties prop, Timeline animation, Stage primaryStage, String fileName, DisplayGrid currentDisplay) {
		super(text, prop, animation, primaryStage, fileName, currentDisplay);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TextField> addFields(Properties prop) 
	{
		List<TextField> text= new ArrayList<>();
		TextField newRandomXML = new TextField();
		newRandomXML.setPromptText(prop.getProperty("XMLText"));
		text.add(newRandomXML);
		TextField simType = new TextField();
		simType.setPromptText(prop.getProperty("PromptSimType"));
		text.add(simType);
		TextField xSize = new TextField();
		xSize.setPromptText(prop.getProperty("PromptXSize"));
		text.add(xSize);
		TextField ySize = new TextField();
		ySize.setPromptText(prop.getProperty("PromptYSize"));
		text.add(ySize);
		TextField colors = new TextField();
		colors.setPromptText(prop.getProperty("PromptColors"));
		text.add(colors);
		return text;
	}

	@Override
	public void buttonEvent(Button myButton, Timeline animation, Stage primaryStage, List<TextField> texts, String fileName, DisplayGrid currentDisplay) 
	{
		boolean isValid = true;
		for (TextField f: texts)
		{
			if (f.getText() == null)
			{
				isValid = false;
			}
			if(f.getText().isEmpty())
			{
				isValid = false;
			}
		}
		if (isValid)
		 {
			 animation.pause();
			 XMLCreation currentConfigs = new XMLCreation(texts.get(0).getText());

			 List<String> colorsList = Arrays.asList(texts.get(4).getText().split(","));
			 String[] colors = new String[colorsList.size()];
			 for (int j = 0; j<colorsList.size(); j++)
			 {
				 colors[j] = colorsList.get(j);
			 }
			 currentConfigs.createRandomXML(texts.get(1).getText(), Integer.parseInt(texts.get(2).getText()),Integer.parseInt(texts.get(3).getText()),colors);
			 fileName = texts.get(0).getText() + ".xml";
			 resetSimulation(primaryStage, animation);
		 }
		
	}

}
