package setupGUI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLCreation 
{
	private String sizeX;
	private String sizeY;
	private String numStates;
	private String simulationType;
	private String fileName;

	public XMLCreation(String name)
	{
		fileName = name;
	}

	public void createRandomXML()
	{

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
			newXML.appendChild(properties);
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
				}	
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		//	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		//	    Document doc = docBuilder.newDocument();
		//	    Element rootElement = doc.createElement("CONFIGURATION");
		//	    doc.appendChild(rootElement);
		//	    Element browser = doc.createElement("BROWSER");
		//	    browser.appendChild(doc.createTextNode("chrome"));
		//	    rootElement.appendChild(browser);
		//	    Element base = doc.createElement("BASE");
		//	    base.appendChild(doc.createTextNode("http:fut"));
		//	    rootElement.appendChild(base);
		//	    Element employee = doc.createElement("EMPLOYEE");
		//	    rootElement.appendChild(employee);
		//	    Element empName = doc.createElement("EMP_NAME");
		//	    empName.appendChild(doc.createTextNode("Anhorn, Irene"));
		//	    employee.appendChild(empName);
		//	    Element actDate = doc.createElement("ACT_DATE");
		//	    actDate.appendChild(doc.createTextNode("20131201"));
		//	    employee.appendChild(actDate);
		//	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		//	    Transformer transformer = transformerFactory.newTransformer();
		//	    DOMSource source = new DOMSource(doc);
		//	    StreamResult result = new StreamResult(new File("/Users/myXml/ScoreDetail.xml"));
		//	    transformer.transform(source, result);
		//	    System.out.println("File saved!");

	}

	public void createWithPopulationPercentages(int[] percentages)
	{

	}
}
