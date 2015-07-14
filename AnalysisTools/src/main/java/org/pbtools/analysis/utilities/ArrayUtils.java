package org.pbtools.analysis.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ArrayUtils {

	public static ArrayList<String> compareArraysOfString(ArrayList<ArrayList<String>> arrays) {

		HashMap<String, Integer> compareSet = new HashMap<String, Integer>();

		for (ArrayList<String> lstArr : arrays) {

			for (String str : lstArr) {
				if (compareSet.containsKey(str)) {
					compareSet.put(str, compareSet.get(str) + 1);
				} else {
					compareSet.put(str, 1);
				}
			}
		}
		ArrayList<String> returnVal = new ArrayList<String>();
		for (Entry<String, Integer> entry : compareSet.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value == arrays.size()) {
				returnVal.add(key);
			}

		}

		return returnVal;

	}

}
