package org.pbtools.analysis.utilities;

import java.util.ArrayList;


public class InputTransform {

	public InputTransform() {

	}

	/**
	 * creates an R vector based on the input array of strings
	 * 
	 * @param stringArrayJava - contains array of items that will be contained in the R vector
	 * @return string of the form: c("Var1", "Var2", ...)
	 */
	public String createRVector(String[] stringArrayInput) {
		String stringVectorR = "c(";

		for (int i = 0; i < stringArrayInput.length; i++) {
			if (i > 0)
				stringVectorR = stringVectorR + "," + "\"" + stringArrayInput[i] + "\"";
			else
				stringVectorR = stringVectorR + "\"" + stringArrayInput[i] + "\"";
		}

		stringVectorR = stringVectorR + ")";

		return stringVectorR; 
	}

	public String createRNumVector(int[] intArrayInput) {
		String stringNumVectorR = "c(";

		for (int i = 0; i < intArrayInput.length; i++) {
			if (i > 0)
				stringNumVectorR = stringNumVectorR + ", " + intArrayInput[i];
			else
				stringNumVectorR = stringNumVectorR + intArrayInput[i];
		}

		stringNumVectorR = stringNumVectorR + ")";

		return stringNumVectorR; 
	}


	public String createRNumVector(Integer[] intArrayInput) {
		String stringNumVectorR = "c(";

		for (int i = 0; i < intArrayInput.length; i++) {
			if (i > 0)
				stringNumVectorR = stringNumVectorR + ", " + intArrayInput[i];
			else
				stringNumVectorR = stringNumVectorR + intArrayInput[i];
		}

		stringNumVectorR = stringNumVectorR + ")";

		return stringNumVectorR; 
	}

	public String createRNumVector(double[] doubleArrayInput) {
		String stringNumVectorR = "c(";

		for (int i = 0; i < doubleArrayInput.length; i++) {
			if (i > 0)
				stringNumVectorR = stringNumVectorR + ", " + doubleArrayInput[i];
			else
				stringNumVectorR = stringNumVectorR + doubleArrayInput[i];
		}

		stringNumVectorR = stringNumVectorR + ")";

		return stringNumVectorR; 
	}

	public String createRNumVector(String[] stringArrayInput) {
		String stringNumVectorR = "c(";

		for (int i = 0; i < stringArrayInput.length; i++) {
			if (i > 0)
				stringNumVectorR = stringNumVectorR + ", " + stringArrayInput[i];
			else
				stringNumVectorR = stringNumVectorR + stringArrayInput[i];
		}

		stringNumVectorR = stringNumVectorR + ")";

		return stringNumVectorR; 
	}

	public String createRList(String[] stringArrayInput, Integer[] integerLevelInput) {
		String tempList = "list(";

		for (int i = 0; i < stringArrayInput.length; i++) {
			if (i > 0)
				tempList = tempList + ", " + stringArrayInput[i] + " = " + integerLevelInput[i];
			else
				tempList = tempList + stringArrayInput[i] + " = " + integerLevelInput[i];
		}

		tempList = tempList + ")";

		return tempList; 
	}

	public String createRList(String[] stringArrayInput, Integer[] startVal, Integer[] integerLevelInput) {
		String tempList = "list(";
		
		for (int i = 0; i < stringArrayInput.length; i++) {
			if (i > 0)
				tempList = tempList + ", " + stringArrayInput[i] + " = c(" + startVal[i] + ":" + integerLevelInput[i] +")";
			else
				tempList = tempList + stringArrayInput[i] + " = c(" + startVal[i] + ":" + integerLevelInput[i] + ")";
		}
		
		tempList = tempList + ")";
		
		return tempList;
	}

	public String createRList(String[] stringArrayInput, Integer[] integerLevelInput, String[] stringArrayIDInput) {
		String tempList = "list(";

		for (int i = 0; i < stringArrayInput.length; i++) {
			String stringVectorR = "c(";
			for (int j = 1; j <= integerLevelInput[i]; j++) {
				if (j > 1)
					stringVectorR = stringVectorR + ", " + "\"" + stringArrayIDInput[i] + j + "\"";
				else
					stringVectorR = stringVectorR + "\"" + stringArrayIDInput[i] + j + "\"";
			}
			stringVectorR = stringVectorR + ")";

			if (i > 0) 
				tempList = tempList + ", " + stringArrayInput[i] + " = "+ stringVectorR;
			else
				tempList = tempList + stringArrayInput[i] + " = "+ stringVectorR;
		}

		tempList = tempList + ")";

		return tempList; 
	}

	public String createRVector(ArrayList<String[]> stringArrayListInput){

		boolean inputOne=false;
		String toreturn="";
		for(String[] s:stringArrayListInput){
			if(stringArrayListInput.size()>1){
				toreturn += createRVector(s)+",";
			}else{
				inputOne=true;
				toreturn = createRVector(s);
			}
		}
		if(!inputOne){
			toreturn=toreturn.substring(0, toreturn.length()-1);
		}

		return toreturn;
	}

	/**
	 * creates an R vector containing R vectors of RBG values
	 * 
	 * @param stringArrayJava - contains array of items that will be contained in the R vector
	 * @return string of the form: c("Var1", "Var2", ...)
	 */
	public String createRColorRGBVector(String[] stringArrayInput) {
		String stringVectorR = "c(";

		for (int i = 0; i < stringArrayInput.length; i++) {
			if (i > 0)
				stringVectorR = stringVectorR + "," + "" + stringArrayInput[i] + "";
			else
				stringVectorR = stringVectorR + "" + stringArrayInput[i] + "";
		}

		stringVectorR = stringVectorR + ")";

		return stringVectorR; 
	}

	public String subSetInputTransform(ArrayList<String> subsetConditions) {
		String toreturn="c(";
		if(subsetConditions.size() > 1) {
			toreturn = "c((";
		}

		for(String s:subsetConditions){
			String[] con=s.split(":");
			if(con[2].contains(",")){
				String[] value=con[2].split(",");
				for(int i=0;i<value.length;i++) {
					if (con[3].equals("Numeric")) {
						toreturn=toreturn+con[0]+con[1]+value[i]+"|";
					} else {
						toreturn=toreturn+con[0]+con[1]+"\""+value[i]+"\"|";
					}

				}
				toreturn =toreturn.substring(0,toreturn.length()-1)+")";
			} else {
				if (con[3].equals("Numeric")) {
					toreturn=toreturn+con[0]+con[1]+con[2]+")";	
				} else {
					toreturn=toreturn+con[0]+con[1]+"\""+con[2]+"\")";
				}

			}
			toreturn=toreturn+"&(";
		}

		if(subsetConditions.size() > 1) {
			toreturn=toreturn.substring(0, toreturn.length()-2)+")";
		}else{
			toreturn=toreturn.substring(0, toreturn.length()-2);
		}
		return toreturn; 
	}

}
