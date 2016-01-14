package org.pbtools.analysis.model;

import org.zkoss.zk.ui.Sessions;

public class TemplateModel {
	private static String BSLASH = "\\";
	private static String FSLASH = "/";

	public TemplateModel() {
		
	}


	public void initTestVars() {
		
	}
	
	public void resetVars() {

	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TemplateModel \n");
		sb.append("var1: "+ BSLASH);
		sb.append("\n var2: "+ FSLASH);  

		return sb.toString();
	}
}
