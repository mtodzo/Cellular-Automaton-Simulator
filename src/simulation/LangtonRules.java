package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangtonRules {
	
	private int myState;
	private Map<List<Integer>, Integer> rulesList = new HashMap<>();
	
	LangtonRules(int x){		
		myState = x;
		
		//init map
		String line = null;

        try {
        	FileReader fileReader = new FileReader("./data/langtonrules.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	//Map<List<Integer>, Integer> tempMap = new HashMap<>();
            	List<Integer> tempList = new ArrayList<>();
            	Integer firstInt = -1;
            	Integer lastInt = -1;
            	for (int i = 0; i < line.length(); i++){
            		//System.out.println(line.charAt(i));
            	    if(i == 0) {
            	    	firstInt = Character.getNumericValue(line.charAt(i));
            	    } else if(i == line.length() -1) {
            	    	lastInt = Character.getNumericValue(line.charAt(i));
            	    } else {
            	    	tempList.add(Character.getNumericValue(line.charAt(i)));
            	    }
            	}
            	if(firstInt == x && lastInt != -1) {
            		//Collections.sort(tempList);
            		rulesList.put(tempList, lastInt);
            	}         	
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open Rules file");                
        }
        catch(IOException ex) {
            System.out.println("Error reading Rules file");
        }
	}
	
	public int getState() {
		return myState;
	}
	
	public int getNextState(List<Integer> inputList) {
		List<ArrayList<Integer>> allDirections = getAllCompass(inputList);
		for(ArrayList<Integer> orientation : allDirections) {
			if(rulesList.containsKey(orientation)) {
				return rulesList.get(orientation);
			}
		}
		return -1;
	}
	
	private List<ArrayList<Integer>> getAllCompass(List<Integer> inputList){
		
		List<ArrayList<Integer>> retList = new ArrayList<ArrayList<Integer>>();

		for(int i = 0; i <4; i++) {
			ArrayList<Integer> oneDirection = rotateList(i, inputList);
			retList.add(oneDirection);
			
		}
		return retList;
	}
	
	private ArrayList<Integer> rotateList(int num, List<Integer> inputList){
		ArrayList<Integer> retList = new ArrayList<Integer>();
		for(int i = num; i < inputList.size(); i++) {
			retList.add(inputList.get(i));
		}
		for(int i = 0; i < num; i++) {
			retList.add(inputList.get(i));
		}
		return retList;
	}
	
	
	
	
}
