package setupGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.paint.Paint;

public class XMLCreation 
{
	private String sizeX;
	private String sizeY;
	private String numStates;
	private String simulationType;
	private String fileName;
	private static final String propertiesFile = "data/SimulationToNumPopulations.properties";

	public XMLCreation(String name)
	{
		fileName = name;
	}

	public void createRandomXML(String simulationType, int xSize, int ySize, String[] colors)
	{
		Properties prop = new Properties();
		try
		{
			InputStream configs = new FileInputStream(propertiesFile);
			prop.load(configs);	
			
			int numStates = Integer.parseInt(prop.getProperty(simulationType));
			
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = df.newDocumentBuilder();
			Document newXML = db.newDocument();

			Element simulation = newXML.createElement("Simulation");
			simulation.setAttribute("type", simulationType);
			newXML.appendChild(simulation);
			Element properties = newXML.createElement("Properties");
			simulation.appendChild(properties);
			Element width = newXML.createElement("Width");
			width.setTextContent(Integer.toString(xSize));
			properties.appendChild(width);
			Element height = newXML.createElement("Height");
			height.setTextContent(Integer.toString(ySize));
			properties.appendChild(height);
			Element numPopulation = newXML.createElement("NumPopulations");
			numPopulation.setTextContent(Integer.toString(numStates));
			properties.appendChild(numPopulation);
			Element simColors = newXML.createElement("Colors");
			String allColors = "";
			for (String s: colors)
			{
				allColors += s + ",";
			}
			simColors.setTextContent(allColors.substring(0, allColors.length()-1));
			properties.appendChild(simColors);

			for (int i=0; i< xSize; i++) 
			{
				for (int j=0; j < ySize; j++)
				{
					Element cellOccupant = newXML.createElement("CellOccupant");
					simulation.appendChild(cellOccupant);
					Element currentState = newXML.createElement("CurrentState");
					currentState.setTextContent(Integer.toString((int) Math.floor(Math.random() * numStates)));
					cellOccupant.appendChild(currentState);
					Element xLocation = newXML.createElement("xLocation");
					xLocation.setTextContent(Integer.toString(i));
					cellOccupant.appendChild(xLocation);
					Element yLocation = newXML.createElement("yLocation");
					yLocation.setTextContent(Integer.toString(j));
					cellOccupant.appendChild(yLocation);
//					Element color = newXML.createElement("Color");
//					color.setTextContent(colors[Integer.parseInt(currentState.getTextContent())]); //MAKE ARRAYS OF COLORS
//					cellOccupant.appendChild(color);
				}	
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(newXML);
		    StreamResult result = new StreamResult(new File("./data/"+ fileName + ".xml"));
		    transformer.transform(source, result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

	public void currentGridToXML(DisplayGrid currentGrid)
	{
		sizeX = Integer.toString(currentGrid.getCURRENT_CONFIGURATION().length);
		sizeY = Integer.toString(currentGrid.getCURRENT_CONFIGURATION()[0].length);
		numStates = Integer.toString(currentGrid.getCURRENT_SIMULATION().getNumPopulations());
		simulationType = currentGrid.getCURRENT_SIMULATION_TYPE();

		try
		{
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = df.newDocumentBuilder();
			Document newXML = db.newDocument();

			Element simulation = newXML.createElement("Simulation");
			simulation.setAttribute("type", simulationType);
			newXML.appendChild(simulation);
			Element properties = newXML.createElement("Properties");
			simulation.appendChild(properties);
			Element width = newXML.createElement("Width");
			width.setTextContent(sizeX);
			properties.appendChild(width);
			Element height = newXML.createElement("Height");
			height.setTextContent(sizeY);
			properties.appendChild(height);
			Element numPopulation = newXML.createElement("NumPopulations");
			numPopulation.setTextContent(numStates);
			properties.appendChild(numPopulation);

			for (int i=0; i< currentGrid.getCURRENT_CONFIGURATION().length; i++) 
			{
				for (int j=0; j < currentGrid.getCURRENT_CONFIGURATION()[i].length; j++)
				{
					Element cellOccupant = newXML.createElement("CellOccupant");
					simulation.appendChild(cellOccupant);
					Element currentState = newXML.createElement("CurrentState");
					currentState.setTextContent(Integer.toString(currentGrid.getCURRENT_CONFIGURATION()[i][j].getCurrentState()));
					cellOccupant.appendChild(currentState);
					Element xLocation = newXML.createElement("xLocation");
					xLocation.setTextContent(Integer.toString(i));
					cellOccupant.appendChild(xLocation);
					Element yLocation = newXML.createElement("yLocation");
					yLocation.setTextContent(Integer.toString(j));
					cellOccupant.appendChild(yLocation);
					Element color = newXML.createElement("Color");
					color.setTextContent(currentGrid.getCURRENT_CONFIGURATION()[i][j].getCurrentPaint().toString());
					cellOccupant.appendChild(color);
				}	
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(newXML);
		    StreamResult result = new StreamResult(new File("./data/"+ fileName + ".xml"));
		    transformer.transform(source, result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void createWithPopulationPercentages(int[] percentages)
	{

	}
}
