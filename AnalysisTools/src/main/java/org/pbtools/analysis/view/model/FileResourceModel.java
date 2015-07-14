package org.pbtools.analysis.view.model;

import org.zkoss.zk.ui.Sessions;

public class FileResourceModel {
	private static String BSLASH = "\\";
	private static String FSLASH = "/";
	
	private String folderResource;
	private String[] fileListResource;
	
	public FileResourceModel() {
		
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

	public String getFolderResource() {
		return folderResource;
	}

	public void setFolderResource(String folderResource) {
		this.folderResource = folderResource;
	}

	public String[] getFileListResource() {
		return fileListResource;
	}

	public void setFileListResource(String[] fileListResource) {
		this.fileListResource = fileListResource;
	}
}
