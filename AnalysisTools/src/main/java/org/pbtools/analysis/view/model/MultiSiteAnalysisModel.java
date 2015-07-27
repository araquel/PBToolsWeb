package org.pbtools.analysis.view.model;

import java.util.List;

import org.pbtools.analysis.utilities.AnalysisUtils;
import org.zkoss.zk.ui.Sessions;

public class MultiSiteAnalysisModel {
	private static String BSLASH = "\\";
	private static String FSLASH = "/";
	private static String OUTPUTFOLDER_PATH =  Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ System.getProperty("file.separator")+
			"user2"+ System.getProperty("file.separator")+"Single-Site"+ System.getProperty("file.separator")+"MyResult"+ System.getProperty("file.separator");
	public static String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "sample_datasets" + System.getProperty("file.separator");

	private String userAccount;

	private String resultFolderPath;
	private String outFileName;
	private String dataFileName;
	
	private int designIndex;
	private String[] respvars = {"Yield"};
	private String environment;
	private String[] environmentLevels = {"E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "E11"};
	private String genotype = "Genotype";
	private String block = "Block";
	private String rep;
	private String row;
	private String column;
	private boolean descriptiveStat;
	private boolean varianceComponents;
	private boolean boxplotRawData;
	private boolean histogramRawData;
	private boolean diagnosticPlot;
	private boolean genotypeFixed;
	private boolean performPairwise;
	private String pairwiseAlpha;
	private String[] genotypeLevels = {"GEN1", "GEN2", "GEN3", "GEN4", "GEN5", "GEN6", "GEN7", "GEN8", "GEN9", "GEN10", "GEN11", "GEN12", "GEN13", "GEN14", "GEN15"};
	private String[] controlLevels = {"GEN1"};
	private boolean compareControl;
	private boolean performAllPairwise;
	private boolean genotypeRandom;
	private boolean stabilityFinlay;
	private boolean stabilityShukla;
	private boolean specifiedContrastGeno;
	private String contrastGenoFilename;
	private boolean specifiedContrastEnv;
	private String contrastEnvFilename;
	private boolean ammi;
	private boolean gge;
	private String analysisResultFolder;
	private String[] dataHeader;
	private List<String[]> data;
	
	public MultiSiteAnalysisModel() {
		resultFolderPath = OUTPUTFOLDER_PATH.replace(BSLASH, FSLASH);
		outFileName = OUTPUTFOLDER_PATH.replace(BSLASH, FSLASH) + "MEA_output.txt";
		dataFileName = DATA_PATH.replace(BSLASH, FSLASH) + "RCB_ME.csv";
		contrastGenoFilename = DATA_PATH.replace(BSLASH, FSLASH) + "contrastMEOGeno_RCB_ME.csv";
		

		 int designIndex = 0;
		 String[] respvars =  {"NULL"};
		 String environment =  "NULL";
		 String[] environmentLevels = {"NULL"};
		 String genotype =  "NULL";
		 String block =  "NULL";
		 String rep =  "NULL";
		 String row =  "NULL";
		 String column =  "NULL";
		 boolean descriptiveStat = false;
		 boolean varianceComponents= false;
		 boolean boxplotRawData= false;
		 boolean histogramRawData= false;
		 boolean diagnosticPlot= false;
		 boolean genotypeFixed= false;
		 boolean performPairwise= false;
		 String pairwiseAlpha = "0";
		 String[] genotypeLevels= {"NULL"};
		 String[] controlLevels = {"NULL"};
		 boolean compareControl= false;
		 boolean performAllPairwise= false;
		 boolean genotypeRandom= false;
		 boolean stabilityFinlay= false;
		 boolean stabilityShukla= false;
		 boolean specifiedContrastGeno= false;
		 String contrastGenoFilename =  "NULL";
		 boolean specifiedContrastEnv= false;
		 String contrastEnvFilename =  "NULL";
		 boolean ammi= false;
		 boolean gge= false;
		
		setEnvironmentLevels(environmentLevels);
		setGenotypeLevels(genotypeLevels);
		setControlLevels(controlLevels);
		setContrastGenoFilename(contrastGenoFilename);
		setRespvars(respvars);
		setDesign(designIndex);
		setEnvironment(environment);
		setGenotype(genotype);
		setBlock(block);
		setRep(rep);
		setRow(row);
		setColumn(column);
		setDescriptiveStat(descriptiveStat); 
		setVarianceComponents(varianceComponents);
		setBoxplotRawData(boxplotRawData);
		setHistogramRawData(histogramRawData);
		setDiagnosticPlot(diagnosticPlot);
		setGenotypeFixed(genotypeFixed);
		setPerformPairwise(performPairwise);
		setPairwiseAlpha(pairwiseAlpha);
		setCompareControl(compareControl);
		setPerformAllPairwise(performAllPairwise);
		setGenotypeRandom(genotypeRandom);
		setStabilityFinlay(stabilityFinlay); 
		setStabilityShukla(stabilityShukla);
		setSpecifiedContrastGeno(specifiedContrastGeno);
		setSpecifiedContrastEnv(specifiedContrastEnv); 
		setContrastEnvFilename(contrastEnvFilename);
		setAmmi(ammi);
		setGge(gge);
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MultSiteAnalysisModel: \n");
		sb.append("resultFolderPath: "+resultFolderPath);
		sb.append("\n outFileName: "+outFileName);
		sb.append("\n dataFileName: "+dataFileName);
		sb.append("\n environment: "+environment);
		sb.append("\n designIndex: "+Integer.toString(designIndex));
		sb.append("\n genotype: "+genotype);
		sb.append("\n block: "+block);
		sb.append("\n row: "+row);
		sb.append("\n column: "+column);
		sb.append("\n descriptiveStat: "+descriptiveStat);
		sb.append("\n varianceComponents: "+varianceComponents);
		sb.append("\n boxplotRawData: "+boxplotRawData);
		sb.append("\n histogramRawData: "+histogramRawData);
		sb.append("\n diagnosticPlot: "+diagnosticPlot);
		sb.append("\n genotypeFixed: "+genotypeFixed);
		sb.append("\n performPairwise: "+performPairwise);
		sb.append("\n pairwiseAlpha: "+pairwiseAlpha);
		sb.append("\n compareControl: "+compareControl);
		sb.append("\n performAllPairwise: "+performAllPairwise);
		sb.append("\n genotypeRandom: "+genotypeRandom);
		sb.append("\n respvars: "+AnalysisUtils.arrayToString(respvars));
		sb.append("\n environmentLevels: "+AnalysisUtils.arrayToString(environmentLevels));
		sb.append("\n genotypeLevels: "+AnalysisUtils.arrayToString(genotypeLevels));
		sb.append("\n controlLevels: "+AnalysisUtils.arrayToString(controlLevels));

		return sb.toString();
	}
	
	public String getResultFolderPath() {
		return resultFolderPath;
	}

	public void setResultFolderPath(String resultFolderPath) {
		this.resultFolderPath = resultFolderPath;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public int getDesign() {
		return designIndex;
	}

	public void setDesign(int designIndex) {
		this.designIndex = designIndex;
	}

	public String[] getRespvars() {
		return respvars;
	}

	public void setRespvars(String[] respvars) {
		this.respvars = respvars;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String[] getEnvironmentLevels() {
		return environmentLevels;
	}

	public void setEnvironmentLevels(String[] environmentLevels) {
		this.environmentLevels = environmentLevels;
	}

	public String getGenotype() {
		return genotype;
	}

	public void setGenotype(String genotype) {
		this.genotype = genotype;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getRep() {
		return rep;
	}

	public void setRep(String rep) {
		this.rep = rep;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isDescriptiveStat() {
		return descriptiveStat;
	}

	public void setDescriptiveStat(boolean descriptiveStat) {
		this.descriptiveStat = descriptiveStat;
	}

	public boolean isVarianceComponents() {
		return varianceComponents;
	}

	public void setVarianceComponents(boolean varianceComponents) {
		this.varianceComponents = varianceComponents;
	}

	public boolean isBoxplotRawData() {
		return boxplotRawData;
	}

	public void setBoxplotRawData(boolean boxplotRawData) {
		this.boxplotRawData = boxplotRawData;
	}

	public boolean isHistogramRawData() {
		return histogramRawData;
	}

	public void setHistogramRawData(boolean histogramRawData) {
		this.histogramRawData = histogramRawData;
	}

	public boolean isDiagnosticPlot() {
		return diagnosticPlot;
	}

	public void setDiagnosticPlot(boolean diagnosticPlot) {
		this.diagnosticPlot = diagnosticPlot;
	}

	public boolean isGenotypeFixed() {
		return genotypeFixed;
	}

	public void setGenotypeFixed(boolean genotypeFixed) {
		this.genotypeFixed = genotypeFixed;
	}

	public boolean isPerformPairwise() {
		return performPairwise;
	}

	public void setPerformPairwise(boolean performPairwise) {
		this.performPairwise = performPairwise;
	}

	public String getPairwiseAlpha() {
		return pairwiseAlpha;
	}

	public void setPairwiseAlpha(String pairwiseAlpha) {
		this.pairwiseAlpha = pairwiseAlpha;
	}

	public String[] getGenotypeLevels() {
		return genotypeLevels;
	}

	public void setGenotypeLevels(String[] genotypeLevels) {
		this.genotypeLevels = genotypeLevels;
	}

	public String[] getControlLevels() {
		return controlLevels;
	}

	public void setControlLevels(String[] controlLevels) {
		this.controlLevels = controlLevels;
	}

	public boolean isCompareControl() {
		return compareControl;
	}

	public void setCompareControl(boolean compareControl) {
		this.compareControl = compareControl;
	}

	public boolean isPerformAllPairwise() {
		return performAllPairwise;
	}

	public void setPerformAllPairwise(boolean performAllPairwise) {
		this.performAllPairwise = performAllPairwise;
	}

	public boolean isGenotypeRandom() {
		return genotypeRandom;
	}

	public void setGenotypeRandom(boolean genotypeRandom) {
		this.genotypeRandom = genotypeRandom;
	}

	public boolean isStabilityFinlay() {
		return stabilityFinlay;
	}

	public void setStabilityFinlay(boolean stabilityFinlay) {
		this.stabilityFinlay = stabilityFinlay;
	}

	public boolean isStabilityShukla() {
		return stabilityShukla;
	}

	public void setStabilityShukla(boolean stabilityShukla) {
		this.stabilityShukla = stabilityShukla;
	}

	public boolean isSpecifiedContrastGeno() {
		return specifiedContrastGeno;
	}

	public void setSpecifiedContrastGeno(boolean specifiedContrastGeno) {
		this.specifiedContrastGeno = specifiedContrastGeno;
	}

	public String getContrastGenoFilename() {
		return contrastGenoFilename;
	}

	public void setContrastGenoFilename(String contrastGenoFilename) {
		this.contrastGenoFilename = contrastGenoFilename;
	}

	public boolean isSpecifiedContrastEnv() {
		return specifiedContrastEnv;
	}

	public void setSpecifiedContrastEnv(boolean specifiedContrastEnv) {
		this.specifiedContrastEnv = specifiedContrastEnv;
	}

	public String getContrastEnvFilename() {
		return contrastEnvFilename;
	}

	public void setContrastEnvFilename(String contrastEnvFilename) {
		this.contrastEnvFilename = contrastEnvFilename;
	}

	public boolean isAmmi() {
		return ammi;
	}

	public void setAmmi(boolean ammi) {
		this.ammi = ammi;
	}

	public boolean isGge() {
		return gge;
	}

	public void setGge(boolean gge) {
		this.gge = gge;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getAnalysisResultFolder() {
		return analysisResultFolder;
	}

	public void setAnalysisResultFolder(String analysisResultFolder) {
		this.analysisResultFolder = analysisResultFolder;
	}

	public String[] getDataHeader() {
		return dataHeader;
	}

	public void setDataHeader(String[] dataHeader) {
		this.dataHeader = dataHeader;
	}

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}
}
