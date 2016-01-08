package org.pbtools.analysis.view.model;

import java.util.List;

import org.pbtools.analysis.utilities.AnalysisUtils;
import org.zkoss.zk.ui.Sessions;

public class SingleSiteAnalysisModel {
	private static String BSLASH = "\\";
	private static String FSLASH = "/";
	private static String PATH1 = System.getProperty("user.dir")+ System.getProperty("file.separator") + "SampleData" + System.getProperty("file.separator");
	public static String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "SampleData" + System.getProperty("file.separator");
	
	private String userAccount;
	private String resultFolderPath;
	private String outFileName;
	private String dataFileName;
	private int design;
	private String[] respvars= {};
	private String environment;
	private String[] environmentLevels = {};
	private String genotype;
	private String block;
	private String rep;
	private String row;
	private String column;
	private boolean descriptiveStat;
	private boolean varianceComponents;
	private boolean boxplotRawData;
	private boolean histogramRawData;
	private boolean heatmapResiduals;
	private String heatmapRow;
	private String heatmapColumn;
	private boolean diagnosticPlot;
	private boolean genotypeFixed;
	private boolean performPairwise;
	private String pairwiseAlpha;
	private String[] genotypeLevels = {};
	private String[] controlLevels = {};
	private boolean compareControl;
	private boolean performAllPairwise;
	private boolean genotypeRandom;
	private boolean excludeControls;
	private boolean genoPhenoCorrelation;
	private boolean specifiedContrast;
	private String contrastFileName;
	private boolean moransTest; // for BIMS always false
	private String[] spatialStruc;
	private String analysisResultFolder;
	private String[] dataHeader;
	private List<String[]> data;

	public SingleSiteAnalysisModel() {
//		String OUTPUTFOLDER_PATH =  Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ System.getProperty("file.separator")+
//				"user2"+ System.getProperty("file.separator")+"Single-Site"+ System.getProperty("file.separator")+"MyResult"+ System.getProperty("file.separator");
//		String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "sample_datasets" + System.getProperty("file.separator");
		String resultFolderPath = null; //OUTPUTFOLDER_PATH.replace(BSLASH, FSLASH);
		String outFileName  = null; //OUTPUTFOLDER_PATH.replace(BSLASH, FSLASH) + "SEA_output.txt";
		String dataFileName  = null; //DATA_PATH.replace(BSLASH, FSLASH) + "RCB_ME.csv";

		int design = 0;
		String[] respvars = {"NULL"};
		String environment = "NULL";
		String[] environmentLevels ={"NULL"};
		String genotype = "NULL";
		String block = "NULL";
		String rep = "NULL";
		String row = "NULL";
		String column = "NULL";
		boolean descriptiveStat = false; 
		boolean varianceComponents = false;
		boolean boxplotRawData = false;
		boolean histogramRawData = false;
		boolean heatmapResiduals = false;
		String heatmapRow = "NULL";
		String heatmapColumn = "NULL";
		boolean diagnosticPlot = false;
		boolean genotypeFixed = false;
		boolean performPairwise = false;
		String pairwiseAlpha = "0";
		String[] genotypeLevels = {"NULL"};
		String[] controlLevels = {"NULL"};
		boolean compareControl = false;
		boolean performAllPairwise = false;
		boolean genotypeRandom = false;
		boolean excludeControls = false;
		boolean genoPhenoCorrelation = false;
		boolean specifiedContrast = false;
		String contrastFileName  = "NULL";//DATA_PATH + "contrastData.csv";
		boolean moransTest =  false;// for BIMS always false
		String[] spatialStruc = {"NULL"};

		setEnvironmentLevels(environmentLevels);
		setRespvars(respvars);
		setEnvironment(environment);
		setResultFolderPath(resultFolderPath);
		setOutFileName(outFileName);
		setDataFileName(dataFileName);
		setDesign(design);
		setGenotype(genotype);
		setBlock(block);
		setRep(rep);
		setRow(row);
		setColumn(column);
		setDescriptiveStat(descriptiveStat);
		setVarianceComponents(varianceComponents);
		setBoxplotRawData(boxplotRawData);
		setHistogramRawData(histogramRawData);
		setHeatmapResiduals(heatmapResiduals);
		setHeatmapRow(heatmapRow);
		setHeatmapColumn(heatmapColumn);
		setDiagnosticPlot(diagnosticPlot);
		setGenotypeFixed(genotypeFixed);
		setPerformPairwise(performPairwise);
		setPairwiseAlpha(pairwiseAlpha);
		setGenotypeLevels(genotypeLevels);
		setControlLevels(controlLevels);
		setCompareControl(compareControl);
		setPerformAllPairwise(performAllPairwise);
		setGenotypeRandom(genotypeRandom);
		setExcludeControls(excludeControls);
		setGenoPhenoCorrelation(genoPhenoCorrelation);
		setSpecifiedContrast(specifiedContrast);
		setContrastFileName(contrastFileName);
		setMoransTest(moransTest);
		setSpatialStruc(spatialStruc);
	}

	public void initTestVars(String testType) {
		setResultFolderPath(DATA_PATH);
	
		String dataFileName = DATA_PATH + "2013WSPYT_rawdata_prep.csv";
		String[] respvars = {"Plotyield.Adj"}; 
		String genotype = "ENTRY";
		String row = "ROW";
		String column = "COLUMN";
		String environment = null;
		boolean genotypeFixed = true;
		boolean genotypeRandom = true; 
		boolean excludeControls = false;
		String[] controlLevels = null; // c("CIHERANG","CIHERANGSUB1","IRRI105","IRRI119", "IRRI154","IRRI168") 
		boolean moransTest = false; // for BIMS always false
		String[] spatialStruc = {"none", "CompSymm", "Gaus", "Exp", "Spher"}; // {"none", "CompSymm", "Gaus", "Exp", "Spher"}, for BIMS include the five choices, for standalone determine by the user
		boolean descriptiveStat = true;
		boolean varianceComponents = true; 
		boolean boxplotRawData = true;
		boolean histogramRawData = true;
		boolean heatmapResiduals = true; 
		boolean diagnosticPlot = true;
		
		setEnvironmentLevels(environmentLevels);
		setRespvars(respvars);
		setEnvironment(environment);
		setResultFolderPath(resultFolderPath);
		setOutFileName(outFileName);
		setDataFileName(dataFileName);
		setDesign(design);
		setGenotype(genotype);
		setBlock(block);
		setRep(rep);
		setRow(row);
		setColumn(column);
		setDescriptiveStat(descriptiveStat);
		setVarianceComponents(varianceComponents);
		setBoxplotRawData(boxplotRawData);
		setHistogramRawData(histogramRawData);
		setHeatmapResiduals(heatmapResiduals);
		setHeatmapRow(heatmapRow);
		setHeatmapColumn(heatmapColumn);
		setDiagnosticPlot(diagnosticPlot);
		setGenotypeFixed(genotypeFixed);
		setPerformPairwise(performPairwise);
		setPairwiseAlpha(pairwiseAlpha);
		setGenotypeLevels(genotypeLevels);
		setControlLevels(controlLevels);
		setCompareControl(compareControl);
		setPerformAllPairwise(performAllPairwise);
		setGenotypeRandom(genotypeRandom);
		setExcludeControls(excludeControls);
		setGenoPhenoCorrelation(genoPhenoCorrelation);
		setSpecifiedContrast(specifiedContrast);
		setContrastFileName(contrastFileName);
		setMoransTest(moransTest);
		setSpatialStruc(spatialStruc);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SingleSiteAnalysisModel \n");
		sb.append("resultFolderPath: "+resultFolderPath);
		sb.append("\n outFileName: "+outFileName);
		sb.append("\n design: "+Integer.toString(design));
		sb.append("\n dataFileName: "+dataFileName);
		sb.append("\n environment: "+environment);
		sb.append("\n genotype: "+genotype);
		sb.append("\n block: "+block);
		sb.append("\n rep: "+rep);
		sb.append("\n row: "+row);
		sb.append("\n column: "+column);
		sb.append("\n descriptiveStat: "+descriptiveStat);
		sb.append("\n varianceComponents: "+varianceComponents);
		sb.append("\n boxplotRawData: "+boxplotRawData);
		sb.append("\n histogramRawData: "+histogramRawData);
		sb.append("\n heatmapResiduals: "+heatmapResiduals);
		sb.append("\n heatmapRow: "+heatmapRow);
		sb.append("\n heatmapColumn: "+heatmapColumn);
		sb.append("\n diagnosticPlot: "+diagnosticPlot);
		sb.append("\n genotypeFixed: "+genotypeFixed);
		sb.append("\n performPairwise: "+performPairwise);
		sb.append("\n pairwiseAlpha: "+pairwiseAlpha);
		sb.append("\n compareControl: "+compareControl);
		sb.append("\n performAllPairwise: "+performAllPairwise);
		sb.append("\n genotypeRandom: "+genotypeRandom);
		sb.append("\n excludeControls: "+excludeControls);
		sb.append("\n genoPhenoCorrelation: "+genoPhenoCorrelation);
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
		return design;
	}

	public void setDesign(int design) {
		this.design = design;
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

	public boolean isHeatmapResiduals() {
		return heatmapResiduals;
	}

	public void setHeatmapResiduals(boolean heatmapResiduals) {
		this.heatmapResiduals = heatmapResiduals;
	}

	public String getHeatmapRow() {
		return heatmapRow;
	}

	public void setHeatmapRow(String heatmapRow) {
		this.heatmapRow = heatmapRow;
	}

	public String getHeatmapColumn() {
		return heatmapColumn;
	}

	public void setHeatmapColumn(String heatmapColumn) {
		this.heatmapColumn = heatmapColumn;
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

	public boolean isExcludeControls() {
		return excludeControls;
	}

	public void setExcludeControls(boolean excludeControls) {
		this.excludeControls = excludeControls;
	}

	public boolean isGenoPhenoCorrelation() {
		return genoPhenoCorrelation;
	}

	public void setGenoPhenoCorrelation(boolean genoPhenoCorrelation) {
		this.genoPhenoCorrelation = genoPhenoCorrelation;
	}

	public boolean isSpecifiedContrast() {
		return specifiedContrast;
	}

	public void setSpecifiedContrast(boolean specifiedContrast) {
		this.specifiedContrast = specifiedContrast;
	}

	public String getContrastFileName() {
		return contrastFileName;
	}

	public void setContrastFileName(String contrastFileName) {
		this.contrastFileName = contrastFileName;
	}

	public boolean isMoransTest() {
		return moransTest;
	}

	public void setMoransTest(boolean moransTest) {
		this.moransTest = moransTest;
	}

	public String[] getSpatialStruc() {
		return spatialStruc;
	}

	public void setSpatialStruc(String[] spatialStruc) {
		this.spatialStruc = spatialStruc;
	}

	public void setUserAccount(String userAccount) {
		// TODO Auto-generated method stub
		this.userAccount = userAccount;
	}
	
	public String getUserAccount() {
		// TODO Auto-generated method stub
		return userAccount;
	}
	
	public void setAnalysisResultFolder(String analysisResultFolder) {
		// TODO Auto-generated method stub
		this.analysisResultFolder = analysisResultFolder;
	}
	
	public String getAnalysisResultFolder() {
		// TODO Auto-generated method stub
		return analysisResultFolder;
	}

	public void setDataHeader(String[] dataHeader) {
		// TODO Auto-generated method stub
		this.dataHeader = dataHeader;
	}
	
	public String[]  getDataHeader() {
		// TODO Auto-generated method stub
		return dataHeader;
	}

	public List<String[]> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	public void setData(List<String[]> data) {
		// TODO Auto-generated method stub
		this.data = data;
	}
}
