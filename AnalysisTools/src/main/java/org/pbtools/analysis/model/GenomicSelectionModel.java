package org.pbtools.analysis.model;

import org.pbtools.analysis.utilities.AnalysisUtils;
import org.zkoss.zk.ui.Sessions;

public class GenomicSelectionModel {


	private static String BSLASH = "\\";
	private static String FSLASH = "/";
	private static String PATH1 = System.getProperty("user.dir")+ System.getProperty("file.separator") + "SampleData" + System.getProperty("file.separator");
	public static String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "SampleData" + System.getProperty("file.separator");
	
	String resultFolderPath;  // outputPath = "E:/App Files/workspace_Juno/RJavaManager/sample_datasets"

	String phenoFile;// = resultFolderPath + "phenoData2V.csv"; //fixed filename, output of GSDataCheck 
	String genoFile;// =  resultFolderPath + "synbreed.csv"; //fixed filename, output of GSDataImputation
	int markerFormat;// = 3;//c(1, 2, 3), ,
	String importRel;// = "FALSE"; 
	String relFile;// = "NULL";    
	String rMatType;// = "t1"; //c("t1", "t2", "t3", "t4"), 
	String mapFile;// = "NULL"; //# ped_file = NULL, #peFormat = NULL, #data quality check options, ...,
	//String[] traitNames = {"Trait.1"}; //
	String[] traitNames;// = {"Trait.1", "Trait2"};
	String[] covariates;// = {"NULL"}; 
	String doCV;// = "TRUE"; //"FALSE"; //
	//    String varCompEst = "BL"; //c("BL", "BRR"), 
	String samplingStrat;// = "random"; //c("random","within popStruc"),
	String popStrucFile;// = "NULL";
	int nfolds;// = 5; 
	int nrep;// = 2;

	//GSDataCheckVars
	double maxCorr;
	double maxMissingP;
	double minMAF;

	//GSDataImputation
	String impType;
	String familyTrait;
	String packageFormat; //c("synbreed", "rrBLUP", "BGLR")) 

	//GSDataPrep
	String covFile = "E:/StarPbtools/GS/data/maize_cov.csv";
	String pedFile = "E:/StarPbtools/GS/data/maize_ped.csv";
	String pFormat = "csv";
	String gFormat = "csv"; 
	String cFormat = "csv"; 
	String mFormat = "csv"; 
	String rFormat = "ttxt"; 
	String peFormat = "csv";

	public GenomicSelectionModel() {
		resetVars();
	}

	public void initTestVars(String testType) {
		setResultFolderPath(DATA_PATH);

		if(testType.equals("GSDataPrep")){//init vars for GSDataPrep
			setPhenoFile(resultFolderPath+"maize_phenoCov2.csv");
			setGenoFile(resultFolderPath + "genoData_qc.csv");
			setMapFile(resultFolderPath + "maize_map2.csv");
			setRelFile(resultFolderPath + "relMatFile2.m");
			setCovFile(resultFolderPath + "maize_cov.csv");
			setPedFile(resultFolderPath + "maize_ped.csv");
			setpFormat("csv");
			setgFormat("csv");
			setcFormat("csv");
			setmFormat("csv");
			setrFormat("ttxt");
			setPeFormat("csv");

			setImpType("random"); //c("random", "family")
			setFamilyTrait("NULL");
			setPackageFormat("synbreed");//c("synbreed", "rrBLUP", "BGLR")) 
		}

		if(testType.equals("GSDataCheck")){//init vars for GSDataCheck
			setMaxCorr(0.9);
			setMaxMissingP(0.1);
			setMinMAF(0.05);
			setGenoFile(resultFolderPath + "genoData.csv");
			setMarkerFormat(5);
		}

		if(testType.equals("GSDataImputation")){//init vars for GSDataImputation
			setPhenoFile("NULL");
			setGenoFile(resultFolderPath + "genoData_qc.csv");
			setImpType("random"); //c("random", "family")
			setFamilyTrait("NULL");
			setPackageFormat("synbreed");//c("synbreed", "rrBLUP", "BGLR")) 
		}

		if(testType.equals("GBLUP")){//init vars for doGBLUP
			String[] traitNames = {"Trait.1", "Trait2"};
			String[] covariates = {"NULL"}; 

			setPhenoFile(resultFolderPath + "phenoData2V.csv");
			setGenoFile( resultFolderPath + "synbreed.csv");
			setMarkerFormat(3);
			setImportRel("FALSE");
			setRelFile("NULL");  
			setrMatType("t1");
			setMapFile("NULL");
			setTraitNames(traitNames);
			setCovariates(covariates);
			setDoCV("TRUE");
			setSamplingStrat("random");
			setPopStrucFile("NULL");
			setNfolds(5);
			setNrep(2);
		}
	}

	public void resetVars() {
		setResultFolderPath("");
		setPhenoFile("");
		setGenoFile("");
		setMarkerFormat(0);
		setImportRel("");
		setRelFile("");
		setrMatType("");
		setMapFile("");
		setTraitNames(null);
		setCovariates(null);
		setDoCV("");
		setSamplingStrat("");
		setPopStrucFile("");
		setNfolds(0);
		setNrep(0);
		setCovFile("");
		setPedFile("");
		setpFormat("");
		setgFormat("");
		setcFormat("");
		setmFormat("");
		setrFormat("");
		setPeFormat("");
		setImpType(""); 
		setFamilyTrait("");
		setPackageFormat(""); 
		setMaxCorr(0);
		setMaxMissingP(0);
		setMinMAF(0);
	}

	public String toString(String testType) {
		StringBuilder sb = new StringBuilder();

		if(testType.equals("GSDataPrep")){//init vars for GSDataPrep

			sb.append("GSDataPrep \n");
			sb.append("resultFolderPath: "+ resultFolderPath);
			sb.append("\n genoFile: "+ genoFile);  
			sb.append("\n phenoFile: "+ phenoFile);
			sb.append("\n phenoFile: "+ mapFile);  
			sb.append("\n RelFile: "+ relFile);  
			sb.append("\n CovFile: "+ covFile);  
			sb.append("\n PedFile: "+ pedFile);
			sb.append("\n pFormat: "+ pFormat);  
			sb.append("\n gFormat: "+ gFormat);   
			sb.append("\n cFormat: "+ cFormat);   
			sb.append("\n mFormat: "+ mFormat);   
			sb.append("\n rFormat: "+ rFormat);   
			sb.append("\n PeFormat: "+ peFormat); 
			sb.append("\n impType: "+ impType); 
			sb.append("\n familyTrait: "+ familyTrait);
			sb.append("\n packageFormat: "+ packageFormat);
		}

		if(testType.equals("GSDataCheck")){//init vars for GSDataCheck
			sb.append("GSDataCheck \n");
			sb.append("resultFolderPath: "+ resultFolderPath);
			sb.append("\n genoFile: "+ genoFile);  
			sb.append("\n markerFormat: "+ Integer.toString(markerFormat)); 
			sb.append("\n maxCorr: "+ Double.toString(maxCorr));   
			sb.append("\n maxMissingP: "+ Double.toString(maxMissingP));   
			sb.append("\n minMAF: "+ Double.toString(minMAF));   
		}

		if(testType.equals("GSDataImputation")){//init vars for GSDataImputation
			sb.append("GSDataImputation \n");
			sb.append("resultFolderPath: "+ resultFolderPath);
			sb.append("\n phenoFile: "+ phenoFile);
			sb.append("\n genoFile: "+ genoFile);  
			sb.append("\n impType: "+ impType);  
			sb.append("\n familyTrait: "+ familyTrait);  
			sb.append("\n packageFormat: "+ packageFormat);  
		}

		if(testType.equals("GBLUP")){//init vars for doGBLUP
			sb.append("GblupModel \n");
			sb.append("resultFolderPath: "+ resultFolderPath);
			sb.append("\n phenoFile: "+ phenoFile);  
			sb.append("\n genoFile: "+ genoFile); 
			sb.append("\n markerFormat: "+ markerFormat); 
			sb.append("\n importRel: "+ importRel); 
			sb.append("\n relFile: "+ relFile); 
			sb.append("\n rMatType: "+ rMatType);  
			sb.append("\n mapFile: "+ mapFile); 
			sb.append("\n traitNames: "+ AnalysisUtils.arrayToString(traitNames));
			sb.append("\n covariates: "+ AnalysisUtils.arrayToString(covariates));
			sb.append("\n doCV: "+ doCV );
			sb.append("\n samplingStrat: "+ samplingStrat );
			sb.append("\n popStrucFile: "+ popStrucFile);
			sb.append("\n nfolds: "+ nfolds ); 
			sb.append("\n nrep: "+ nrep );
		}
		return sb.toString();
	}

	public String getResultFolderPath() {
		return resultFolderPath;
	}

	public void setResultFolderPath(String resultFolderPath) {
		this.resultFolderPath = resultFolderPath;
	}

	public String getPhenoFile() {
		return phenoFile;
	}

	public void setPhenoFile(String phenoFile) {
		this.phenoFile = phenoFile;
	}

	public String getGenoFile() {
		return genoFile;
	}

	public void setGenoFile(String genoFile) {
		this.genoFile = genoFile;
	}

	public int getMarkerFormat() {
		return markerFormat;
	}

	public void setMarkerFormat(int markerFormat) {
		this.markerFormat = markerFormat;
	}

	public String getImportRel() {
		return importRel;
	}

	public void setImportRel(String importRel) {
		this.importRel = importRel;
	}

	public String getRelFile() {
		return relFile;
	}

	public void setRelFile(String relFile) {
		this.relFile = relFile;
	}

	public String getrMatType() {
		return rMatType;
	}

	public void setrMatType(String rMatType) {
		this.rMatType = rMatType;
	}

	public String getMapFile() {
		return mapFile;
	}

	public void setMapFile(String mapFile) {
		this.mapFile = mapFile;
	}

	public String[] getTraitNames() {
		return traitNames;
	}

	public void setTraitNames(String[] traitNames) {
		this.traitNames = traitNames;
	}

	public String[] getCovariates() {
		return covariates;
	}

	public void setCovariates(String[] covariates) {
		this.covariates = covariates;
	}

	public String getDoCV() {
		return doCV;
	}

	public void setDoCV(String doCV) {
		this.doCV = doCV;
	}

	public String getSamplingStrat() {
		return samplingStrat;
	}

	public void setSamplingStrat(String samplingStrat) {
		this.samplingStrat = samplingStrat;
	}

	public String getPopStrucFile() {
		return popStrucFile;
	}

	public void setPopStrucFile(String popStrucFile) {
		this.popStrucFile = popStrucFile;
	}

	public int getNfolds() {
		return nfolds;
	}

	public void setNfolds(int nfolds) {
		this.nfolds = nfolds;
	}

	public int getNrep() {
		return nrep;
	}

	public void setNrep(int nrep) {
		this.nrep = nrep;
	}


	public double getMaxCorr() {
		return maxCorr;
	}


	public void setMaxCorr(double maxCorr) {
		this.maxCorr = maxCorr;
	}


	public double getMaxMissingP() {
		return maxMissingP;
	}


	public void setMaxMissingP(double maxMissingP) {
		this.maxMissingP = maxMissingP;
	}


	public double getMinMAF() {
		return minMAF;
	}


	public void setMinMAF(double minMAF) {
		this.minMAF = minMAF;
	}

	public String getImpType() {
		return impType;
	}

	public void setImpType(String impType) {
		this.impType = impType;
	}

	public String getFamilyTrait() {
		return familyTrait;
	}

	public void setFamilyTrait(String familyTrait) {
		this.familyTrait = familyTrait;
	}

	public String getPackageFormat() {
		return packageFormat;
	}

	public void setPackageFormat(String packageFormat) {
		this.packageFormat = packageFormat;
	}

	public String getCovFile() {
		return covFile;
	}

	public void setCovFile(String covFile) {
		this.covFile = covFile;
	}

	public String getPedFile() {
		return pedFile;
	}

	public void setPedFile(String pedFile) {
		this.pedFile = pedFile;
	}

	public String getpFormat() {
		return pFormat;
	}

	public void setpFormat(String pFormat) {
		this.pFormat = pFormat;
	}

	public String getgFormat() {
		return gFormat;
	}

	public void setgFormat(String gFormat) {
		this.gFormat = gFormat;
	}

	public String getcFormat() {
		return cFormat;
	}

	public void setcFormat(String cFormat) {
		this.cFormat = cFormat;
	}

	public String getmFormat() {
		return mFormat;
	}

	public void setmFormat(String mFormat) {
		this.mFormat = mFormat;
	}

	public String getrFormat() {
		return rFormat;
	}

	public void setrFormat(String rFormat) {
		this.rFormat = rFormat;
	}

	public String getPeFormat() {
		return peFormat;
	}

	public void setPeFormat(String peFormat) {
		this.peFormat = peFormat;
	}

}
