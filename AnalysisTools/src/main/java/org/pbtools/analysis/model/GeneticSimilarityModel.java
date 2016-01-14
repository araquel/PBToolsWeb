package org.pbtools.analysis.model;

import org.zkoss.zk.ui.Sessions;

public class GeneticSimilarityModel {
	private String DATA_PATH;
	
	private static String BSLASH = "\\";
	private static String FSLASH = "/";

	String resultFolderPath; // outputPath = "E:/App Files/workspace_Juno/RJavaManager/sample_datasets"
	String doPedigree; 
	String fileFormat; //supply format of input file, whether "csv", "ctxt", "stxt", "ttxt", or "sctxt" 
	String fileName; //supply path and name of input file
	String relType;//supply type of relationship matrix to create: "dom", "add", "sm-smin","realized","realizedAB","sm","additive"
	String outFileName;
	int markerFormat;

	public GeneticSimilarityModel() {
		resetVars();
	}

	//Initialize variables to test the pedigree-based option
	public void initTestVarsPedigree() {
		setResultFolderPath(DATA_PATH);
		setDoPedigree("TRUE");
		setFileFormat("csv");
		setFileName(DATA_PATH + "pedFile.csv");
		setRelType("dom");
		setOutFileName("DATA_PATH + pedigreeRelation_" + relType);
		setMarkerFormat(0);
	}
	

	//Initialize variables to test the marker-based option
	public void initTestVarsMarker() {
		setResultFolderPath(DATA_PATH);
		setDoPedigree("FALSE");
		setFileFormat("csv");
		setFileName(DATA_PATH + "ori_file_type3_ed.csv");
		setRelType("realized"); //sm-smin";
		setOutFileName(DATA_PATH + "markerRelation_" + relType);
		setMarkerFormat(3);
	}

	public void resetVars() {
		setResultFolderPath("");
		setDoPedigree("FALSE");
		setFileFormat("");
		setFileName("");
		setRelType("");
		setOutFileName("");
		setMarkerFormat(0);
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("GenSimModel \n");
		sb.append("resultFolderPath: "+ resultFolderPath);
		sb.append("\n doPedigree: "+ doPedigree);  
		sb.append("\n fileFormat: "+ fileFormat);  
		sb.append("\n fileName: "+ fileName);  
		sb.append("\n relType: "+ relType);  
		sb.append("\n outFileName: "+ outFileName);  
		sb.append("\n markerFormat: "+ markerFormat);  
		return sb.toString();
	}


	public String getResultFolderPath() {
		return resultFolderPath;
	}


	public void setResultFolderPath(String resultFolderPath) {
		this.resultFolderPath = resultFolderPath;
	}


	public String getDoPedigree() {
		return doPedigree;
	}


	public void setDoPedigree(String doPedigree) {
		this.doPedigree = doPedigree;
	}


	public String getFileFormat() {
		return fileFormat;
	}


	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getRelType() {
		return relType;
	}


	public void setRelType(String relType) {
		this.relType = relType;
	}


	public String getOutFileName() {
		return outFileName;
	}


	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}


	public int getMarkerFormat() {
		return markerFormat;
	}


	public void setMarkerFormat(int markerFormat) {
		this.markerFormat = markerFormat;
	}
}
