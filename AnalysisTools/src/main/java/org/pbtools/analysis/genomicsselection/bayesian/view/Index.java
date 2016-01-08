package org.pbtools.analysis.genomicsselection.bayesian.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.analysis.webservice.manager.RServeManager;
import org.apache.commons.io.input.ReaderInputStream;
import org.pbtools.analysis.utilities.FileUtilities;
import org.pbtools.analysis.view.model.GenomicSelectionModel;
import org.pbtools.filesystem.manager.UserFileManager;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import au.com.bytecode.opencsv.CSVReader;

public class Index {
	private RServeManager rServeManager;
	private GenomicSelectionModel bayesianModel;
	private String errorMessage;

	private ArrayList<String> listString;
	private List<String> phenotypeFormat, typeOfDesignList, dataFormatList, traitType, imputationTypes, pModel, pMethod, lociMethod, scanType, relMatrixTypes;
	private List<String> columnList = new ArrayList<String>();
	private List<String> continuousVarsList = new ArrayList<String>(); // variable names(columns) from the cross data with Numeric Levels.
	private List<String> binaryVarsList = new ArrayList<String>(); // variable names(columns) from the cross data with 2 Numeric Levels.
	private List<String> ordinalVarsList = new ArrayList<String>(); // variable names(columns) from the cross data with atleast 2 Levels.
	private List<String[]> dataList = new ArrayList<String[]>();
	private List<String> crosstypeList;
	private String[] crossTypeFunctions={"f2", "bc", "risib", "riself", "bcsft"};
	private List<String> phenotypeFileFormats, genoFileFormats; //{"csv","txt","cro","raw", "qtx"};
	private List<String> file2Formats; //{"csv","txt","maps"};
	BindContext ctx1, ctx2, ctx3;
	Component view3, view1, view2;
	InputStream in1, in2, in3;
	
	private String chosenCrosstype;
	private String chosenMapping, fileName1, fileName2, fileName3, dataFileName, fileName, comboboxMapping, coboboxmapping2, comboboxmapping3, comboboxmapping4;
	private String value1, value2, value3;

	private boolean mapping1, mapping2, mapping3, mapping4, isNewDataSet, isVariableDataVisible;
	public boolean isUpdateMode = false;
	public boolean isDataReUploaded = false, gridReUploaded = false;
	
	private int activePage = 0;
	private int pageSize=10;
	private int n;

	private String chosenDataFormat = null;
	private Integer selected = 0;
	private Integer selectedFileFormat = 1;

	private String selectedTraitType="Continuous";

	private File tempFile,  file1, file2;
	private Div defaultbox, phenobox, genobox, mfbox, datagroupbox, crossgroupbox, mapbox1, mapbox2, inputbox, divDatagrid, divDataCheckTxt, checkboxDiv;
	private Vlayout divVlayout;
	private Groupbox mfgroupbox, ggroupbox, pgroupbox, grpVariableData, grpDataCheckView;
	
	private Radio deleteRadioButton;
	private Radio imputeRadioButton;
	private Radio lijiRadioButton;
	private Radio numericalRadioButton;
	private Radio additiveRadioButton;
	private Radio dominanceRadioButton;
	private Radio mLRadioButton;
	private Radio remlRadioButton;

	private Doublespinner dbCutOffP;
	private Doublespinner dbPvalCutOff;
	private Doublespinner dbLodThreshold;
	private Doublespinner dbCutOff; 
	private Doublespinner dbMainEffects;
	private Spinner dbNPermutations;
	private Doublespinner dbCimStep; 
	private Doublespinner dbCimWin;
	private Doublespinner dbCimMinDist;
	private Doublespinner dbthresholdNumericalValue;
	private Doublespinner dbMqmStepVal;
	private Doublespinner dbMqmWinVal;
	private Doublespinner dbBayesianStepSize;

	private Combobox phenoFormat;
	private Combobox genoFormat;
	private Combobox mapFormat;
	private Combobox comboPMethod;
	
	private Checkbox cbSetup1;
	private Checkbox cbSetup2;
	private Checkbox cbTraitYield;
	private Checkbox cbHpresent;
	private Checkbox cbSetupModel;

	private Textbox tbMEffect;
	private Textbox tbAll;
	private Textbox tbMaxNumber;
	private File crossDataFile;
	private Doublespinner dbLodCutOff;
	
	private Label lblBCSpinner, lblFSpinner;
	private ArrayList<Checkbox> checkBoxList;
	private String fileType;

	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view){
		bayesianModel = new GenomicSelectionModel();
		phenotypeFileFormats = getPhenotypeFileFormats();
		genoFileFormats = getGenotypeFileFormats();
		
		defaultbox = (Div) component.getFellow("defaultbox");
		createPhenoBox();
		
	}

	private void makeDisable() {
		imputeRadioButton.setDisabled(true);
		dbCutOffP.setDisabled(true);
		dbLodCutOff.setDisabled(true);
		deleteRadioButton.setDisabled(true);
		dbPvalCutOff.setDisabled(true);
		dbLodThreshold.setDisabled(true);
		dbCutOff.setDisabled(true);
		cbSetup1.setDisabled(true);
		cbSetup2.setDisabled(true);
		dbMainEffects.setDisabled(true);
		tbMEffect.setDisabled(true);
		tbAll.setDisabled(true);
		tbMaxNumber.setDisabled(true);
	}

	@Command("validateInputFiles")
	public void validateInputFiles(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view){

	}

	@Command("runQTL")
	@NotifyChange("*")
	public void runQTL(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view){	
		if(validateQtlModel()){
			Map<String,Object> args = new HashMap<String,Object>();
			args.put("bayesianModel", bayesianModel);
			BindUtils.postGlobalCommand(null, null, "displayQtlResult", args);
		} else Messagebox.show(errorMessage);
	}

	private boolean validateQtlModel() {
		
		
		return true;	
	}

	private ArrayList<String>  getCheckedBoxes(ArrayList<Checkbox> checkBoxList) {
		ArrayList<String> ls= new ArrayList<String>();
		for(Checkbox c: checkBoxList){
			System.out.println(c.getLabel() +" is Checked?" +  c.isChecked());
			if(c.isChecked()) ls.add(c.getLabel());
		}
		return ls;
	}

	@Command("missingDataCheck")
	public void missingDataCheck(){

		if(imputeRadioButton.isDisabled()==true)
			imputeRadioButton.setDisabled(false);
		else 
			imputeRadioButton.setDisabled(true);

		if(dbCutOff.isDisabled()==true)
			dbCutOff.setDisabled(false);
		else
			dbCutOff.setDisabled(true);

		if(deleteRadioButton.isDisabled()==true)
			deleteRadioButton.setDisabled(false);
		else
			deleteRadioButton.setDisabled(true);	
	}

	@Command("aggregationCheck")
	public void aggregationCheck(){

		if(dbPvalCutOff.isDisabled()==true)
			dbPvalCutOff.setDisabled(false);
		else
			dbPvalCutOff.setDisabled(true);

	}

	@Command("genotypeCheck")
	public void genotypeCheck(){

		if(dbCutOffP.isDisabled()==true)
			dbCutOffP.setDisabled(false);
		else
			dbCutOffP.setDisabled(true);

	}

	@Command("markerCheck")
	public void markerCheck(){

		if(dbLodThreshold.isDisabled()==true)
			dbLodThreshold.setDisabled(false);
		else
			dbLodThreshold.setDisabled(true);
	}
	
	@Command("errorCheck")
	public void errorCheck(){

		if(dbLodCutOff.isDisabled()==true)
			dbLodCutOff.setDisabled(false);
		else
			dbLodCutOff.setDisabled(true);
	}

	@Command("QTLCheck")
	public void QTLCheck(){

		if(cbSetup1.isDisabled()==true)
			cbSetup1.setDisabled(false);
		else
			cbSetup1.setDisabled(true);

		if(cbSetup2.isDisabled()==true)
			cbSetup2.setDisabled(false);
		else
			cbSetup2.setDisabled(true);

		if(dbMainEffects.isDisabled()==true)
			dbMainEffects.setDisabled(false);
		else
			dbMainEffects.setDisabled(true);

		if(tbMEffect.isDisabled()==true)
			tbMEffect.setDisabled(false);

		else
			tbMEffect.setDisabled(true);

		if(tbAll.isDisabled()==true)
			tbAll.setDisabled(false);
		else
			tbAll.setDisabled(true);

		if(tbMaxNumber.isDisabled()==true)
			tbMaxNumber.setDisabled(false);
		else
			tbMaxNumber.setDisabled(true);
	}

	@Command("choosePModel")
	@NotifyChange("pMethod")
	public void choosePModel(@BindingParam("selected") Comboitem selected){
		//"Normal", "Binary", "Two-Part", "Non-parametric"
		comboPMethod.setDisabled(false);
		if(selected.getLabel().equals("Binary")){ //"Binary", 
			pMethod = pMethodBInaryList();
		} 
		else if(selected.getLabel().equals("Non-parametric")){ //Non-parametric
			comboPMethod.setDisabled(true);
		}
		else pMethod = pMethodList();
	}

	private void makeNull(){
		value1 = null;
		value2 = null;
		file1 = null;
		file2 = null;
		tempFile = null;
		fileName1 = null;
		fileName2 = null;
	}
	
	private void clearFileFormats() {
		// TODO Auto-generated method stub
		phenotypeFileFormats.clear();
		file2Formats.clear();
	}

	public void createPhenoBox(){ 
//		if (!defaultbox.getChildren().isEmpty())
//			defaultbox.getFirstChild().detach();
		Include includeDefaultzul = new Include();
		includeDefaultzul.setId("includeDefaultzul");
		includeDefaultzul.setSrc("/analysis/genomicsselection/bayesian/default.zul");
		includeDefaultzul.setParent(defaultbox);
		
	}

	public void detachDiv(Div div){
		if (!div.getChildren().isEmpty()){
			div.getFirstChild().detach();
		}
	}

	@Command
	@DependsOn("chosenMapping")
	@NotifyChange({"mapping1", "mapping2", "mapping3", "mapping4"})
	public void mappingVisibility(){
		if(chosenMapping=="IM"){
			mapping1 = true; mapping2=false;
			mapping3=false; mapping4=false;
		}
		else if(chosenMapping=="CIM"){
			mapping1 = false; mapping2=true;
			mapping3=false; mapping4=false;	
		}
		else if(chosenMapping=="MQM"){
			mapping1 = false; mapping2=false;
			mapping3=true; mapping4=false;
		}
		else if(chosenMapping=="Bayesian Mapping"){
			mapping1 = false; mapping2=false;
			mapping3=false; mapping4=true;
		}
	}
	
	@NotifyChange("pModel")
	@Command
	@DependsOn("selectedTraitType")
	public void updatePModel(){
		if(selectedTraitType=="Continuous"){
			pModel = continuousMList();
			setCoboboxmapping2("Normal");
			refreshTraitCheckboxes(continuousVarsList);
		}
		else if(selectedTraitType=="Binary"){
			pModel = binaryMList();
			setCoboboxmapping2("Binary");
			refreshTraitCheckboxes(binaryVarsList);
		}
		else{ // if(selectedTraitType=="Ordinal")
			pModel = ordinalMList();
			setCoboboxmapping2("Non-parametric");
			refreshTraitCheckboxes(ordinalVarsList);
		}
	}

	private void refreshTraitCheckboxes(List<String> varsListList) {
		divVlayout.getChildren().clear();
		createCheckboxes(varsListList);
	}

	@NotifyChange("*")
	@Command("chooseFile1value1")
	public void chooseFileValue1(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("fileFormat") Integer formatIndex) 
					throws Exception{

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		this.ctx1=ctx;
		this.view1=view;

		tempFile = new File(event.getMedia().getName());
		setFileName1(event.getMedia().getName());
		System.out.println(tempFile.getAbsolutePath());	
		fileType = fileName1.split("\\.")[1];
		
		value1 = tempFile.getAbsolutePath();
		if (file1 == null)
			try {
				file1 = File.createTempFile(fileName1, "."+fileType);
				System.out.println(file1.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		
		if (!phenotypeFileFormats.contains(fileType)) {
			errorMessage = "Error: File must be in a ";
			for(String s: phenotypeFileFormats){
				errorMessage = errorMessage+"."+s;
			}
			Messagebox.show(errorMessage+" format",
					"Upload Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		in1 = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
		bayesianModel.setPhenoFile(file1.getAbsolutePath());
		bayesianModel.setpFormat(fileType);

	}


	@NotifyChange("*")
	@Command("chooseFile1value2")
	public void chooseFileValue2(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("fileFormat") Integer formatIndex) 
					throws Exception{

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		this.ctx2=ctx;
		this.view2=view;
		tempFile = new File(event.getMedia().getName());
		setFileName2(event.getMedia().getName());
		System.out.println(tempFile.getAbsolutePath());	
		fileType = fileName2.split("\\.")[1];
		
		value2 = tempFile.getAbsolutePath();
		if (file2 == null)
			try {
					file2 = File.createTempFile(fileName2, "."+fileType);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		
		if (!genoFileFormats.contains(fileType)) {
			errorMessage = "Error: File must be in a ";
			for(String s: file2Formats){
				errorMessage = errorMessage+"."+s;
			}
			Messagebox.show(errorMessage+" format",
					"Upload Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		

		in2 = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
		
//		bayesianModel.setResultFolderPath(file2.getAbsolutePath());
		bayesianModel.setPhenoFile(file2.getAbsolutePath());
		bayesianModel.setpFormat(fileType);
	}

	@Command
	@DependsOn("selected")
	public void uploadFiles(){
		if(selected == 0){
			uploadFile1();
			uploadFile2();
			makeNull();
		}
		else if(selected == 1 || selected == 2){
			uploadFile1();
			uploadFile2();
			makeNull();
		}
		else if(selected == 3){
			uploadFile1();
			makeNull();
		}
		else{
			System.out.println("N/A");
		}
		rServeManager = new RServeManager();
		System.out.println(bayesianModel.toString());
		rServeManager.doGSDataPrep(bayesianModel);
		displayCrossData(bayesianModel.getResultFolderPath());
	}

	private void displayCrossData(String resultFolderPath) {
		crossDataFile = new File(resultFolderPath+"crossData.csv");

		if (crossDataFile == null)
			return;

		isVariableDataVisible = true;
		setDataFileName(crossDataFile.getName());

		isVariableDataVisible = true;
		dataFileName = crossDataFile.getName();
		refreshCsv();
		if (this.isUpdateMode) setNewDataSet(true);
		
	}

	private void uploadFile1(){

		System.out.println(file1.getAbsolutePath());
		FileUtilities.uploadFile(file1.getAbsolutePath(), in1);
		BindUtils.postNotifyChange(null, null, this, "*");

		File uploadedFile = FileUtilities.getFileFromUpload(ctx1, view1);

		UserFileManager userFileManager = new UserFileManager();
//		String filePath = userFileManager.uploadFileForAnalysis(fileName1, uploadedFile);
	}

	private void uploadFile2(){
		System.out.println(file2.getAbsolutePath());
		FileUtilities.uploadFile(file2.getAbsolutePath(), in2);
		BindUtils.postNotifyChange(null, null, this, "*");

		File uploadedFile = FileUtilities.getFileFromUpload(ctx2, view2);

		UserFileManager userFileManager = new UserFileManager();
//		String filePath = userFileManager.uploadFileForAnalysis(fileName2, uploadedFile);
	}

	@Command("refreshCsv")
	public void refreshCsv() {
		grpVariableData.setVisible(true);
		setActivePage(0);
		CSVReader reader;
		reloadCsvGrid();

		try {
			reader = new CSVReader(new FileReader(crossDataFile.getAbsolutePath()));
			List<String[]> rawData = reader.readAll();
			columnList.clear();
			dataList.clear();
			columnList = new ArrayList<String>(Arrays.asList(rawData.get(0)));
			rawData.remove(0);
			dataList = new ArrayList<String[]>(rawData);
			System.out.println(Arrays.toString(dataList.get(0)));
			
			continuousVarsList = getContinuousVarsFromList(columnList);
			binaryVarsList = getBinaryVarsFromList(continuousVarsList);
			ordinalVarsList = getOrdinalVarsFromList(continuousVarsList);
			
			createCheckboxes(continuousVarsList);
			
			if (!this.isDataReUploaded)
				System.out.println("gbUploadData.invalidate()");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createCheckboxes(List<String> varList) {
		Checkbox newCheckbox= new Checkbox();
		for(String s: varList){
			 newCheckbox= new Checkbox(s);
//			newCheckbox.setLabel(s);
			 divVlayout.appendChild(newCheckbox);
			newCheckbox.setParent(divVlayout);
			checkBoxList.add(newCheckbox);
			divVlayout.setVisible(true);
			
		}
	}

	private List<String> getOrdinalVarsFromList(List<String> numvarList) {
		List<String> varList = new ArrayList<String>();
		for(String s:numvarList){
			if(rServeManager.getLevels(columnList, dataList, s).length>=2){//if levels is atleast 2
				varList.add(s);
			}
		}
	return varList;
	}
	
	private List<String> getBinaryVarsFromList(List<String> numvarList) {
		List<String> varList = new ArrayList<String>();
			for(String s:numvarList){
				if(rServeManager.getLevels(columnList, dataList, s).length==2){//if levels is only 2
					varList.add(s);
				}
			}
		return varList;
	}
	private List<String> getContinuousVarsFromList(List<String> columnNames) {
		List<String> varList = new ArrayList<String>();
		rServeManager = new RServeManager();
		for(String s:columnNames){
			if(rServeManager.isColumnNumeric(bayesianModel.getResultFolderPath()+"crossData.csv", s).equals("TRUE")){
				varList.add(s);
			}
		}

		rServeManager.end();
		return varList;
	}
	
	public ArrayList<ArrayList<String>> getCsvData() {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		if (dataList.isEmpty())
			return result;
		for (int i = activePage * pageSize; i < activePage * pageSize + pageSize && i < dataList.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			row.addAll(Arrays.asList(dataList.get(i)));
			result.add(row);
			row.add(0, "  ");
			System.out.println(Arrays.toString(dataList.get(i)) + "ROW: " + row.get(0));
		}
		return result;
	}

	public void reloadCsvGrid() {
		if (gridReUploaded) {
			gridReUploaded = false;
			return;
		}	
		if (!divDatagrid.getChildren().isEmpty())
			divDatagrid.getFirstChild().detach();
		Include incCSVData = new Include();
		incCSVData.setSrc("/updatestudy/csvdata.zul");
		incCSVData.setParent(divDatagrid);
		gridReUploaded = true;
	}

	public void reloadTxtGrid() {
//		grpDataCheckView.setVisible(true);
//		
//		if (!divDataCheckTxt.getChildren().isEmpty())
//			divDataCheckTxt.getFirstChild().detach();
//
////		File fileToCreate = new File(bayes);--------------------------------------------------------------------------------------------------------------
//		byte[] buffer = new byte[(int) fileToCreate.length()];
//		FileInputStream fs;
//		try {
//			fs = new FileInputStream(fileToCreate);
//			fs.read(buffer);
//			fs.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		ByteArrayInputStream is = new ByteArrayInputStream(buffer);
//		AMedia fileContent = new AMedia("report", "text", "text/plain", is);
//		Include studyInformationPage = new Include();
//		studyInformationPage.setParent(divDataCheckTxt);
//		studyInformationPage.setDynamicProperty("txtFile", fileContent);
//		studyInformationPage.setSrc("/analysis/txtviewer.zul");
	}

	
	public static List<String> mMethod(){
		return Arrays.asList(new String[]{"IM", "CIM", "MQM", "Bayesian Mapping", "Two-d Mapping", "QTLRel", "MAGIC"});
	}
	public static List<String> getCrossType() {
		return Arrays.asList(new String[]{"F2", "BC", "RIL(SIB)", "RIL(SELF)", "[Other]"}); // "BC", "DH", "F", "RIL(self)", "Ril(sib)", "[Other]"}
	}

	public static List<String> scanTypeList(){
		return Arrays.asList(new String[]{"2logBF", "cell mean", "count", "deviance", "detection", "estimate", "heritability", "log10", "log10 (posterior)", "LPD", "LR", "nqtl", "posterior", "variance"});
	}

	public static List<String> dFormatList(){
		return Arrays.asList(new String[]{"default", "Map Maker", "QTL Cartographer", "Map Manager"});
	}

	public static List<String> phenotype(){
		return Arrays.asList(new String[]{"Space-separated (.txt)", "Comma-separated (.csv)", "Tab-separated (.txt)", "Semi-colon-separated (.txt)"});
	}

	public static List<String> lociMList(){
		return Arrays.asList(new String[]{"Mean", "Mode", "Scan"});
	}

	public static List<String> pMList(){
		return Arrays.asList(new String[]{"Normal", "Binary", "Two-Part", "Non-parametric"});
	}
	
	public static List<String> continuousMList(){
		return Arrays.asList(new String[]{"Normal", "Two-Part", "Non-parametric"});
	}
	
	public static List<String> binaryMList(){
		return Arrays.asList(new String[]{ "Binary"});
	}
	
	public static List<String> ordinalMList(){
		return Arrays.asList(new String[]{"Non-parametric"});
	}

	public static List<String> pMethodList(){
		return Arrays.asList(new String[]{"Maximum Likehood via EM", "Haley-Knott Regression", "Extended Haley-Knott Method", "Imputation"});
	}
	public static List<String> pMethodBInaryList(){
		return Arrays.asList(new String[]{"Maximum Likehood via EM", "Haley-Knott Regression"});
	}	
	
	public static List<String> genotype(){
		return Arrays.asList(new String[]{"Comma-separated (.csv) file", "Tab Delimited text (.txt) file"});
	}

	public static List<String> tType(){
		return Arrays.asList(new String[]{"Continuous", "Binary", "Ordinal"});
	}

	public List<String[]> getDataList() {
		System.out.println("DatALIST GEt");
		
		if (true)
			return dataList;

		ArrayList<String[]> pageData = new ArrayList<String[]>();
		for (int i = activePage * pageSize; i < activePage * pageSize + pageSize; i++) {
			pageData.add(dataList.get(i));
			System.out.println(Arrays.toString(dataList.get(i)));
		}
		reloadCsvGrid();
		return pageData;
	}

	public List<String> getTypeOfDesignList() {
		return typeOfDesignList;
	}

	public boolean checkVisibility(int num){
		return true;
	}

	public void setTypeOfDesignList(List<String> typeOfDesignList) {
		this.typeOfDesignList = typeOfDesignList;
	}

	public List<String> getDataFormatList() {
	 	return Arrays.asList(new String[]{"default","synbreed gpData"});
	}

	public void setDataFormatList(List<String> dataFormatList) {
		this.dataFormatList = dataFormatList;
	}

	public List<String> getTraitType() {
		return traitType;
	}

	public void setTraitType(List<String> traitType) {
		this.traitType = traitType;
	}

	public List<String> getImputationTypes() {
	 	return Arrays.asList(new String[]{"fix", "random", "family", "beagle", "beagleAfterFamily"});
	}

	public String getchosenCrosstype() {
		return chosenCrosstype;
	}

	public void setChosenCrosstype(String chosenCrosstype) {
		this.chosenCrosstype = chosenCrosstype;
	}


	public List<String> getpModel() {
		return pModel;
	}

	public void setpModel(List<String> pModel) {
		this.pModel = pModel;
	}

	public List<String> getpMethod() {
		return pMethod;
	}

	public void setpMethod(List<String> pMethod) {
		this.pMethod = pMethod;
	}

	public List<String> getLociMethod() {
		return lociMethod;
	}

	public void setLociMethod(List<String> lociMethod) {
		this.lociMethod = lociMethod;
	}

	public List<String> getScanType() {
		return scanType;
	}


	public void setScanType(List<String> scanType) {
		this.scanType = scanType;
	}

	public String getChosenMapping() {
		return chosenMapping;
	}

	public void setChosenMapping(String chosenMapping) {
		this.chosenMapping = chosenMapping;
	}

	public boolean isMapping1() {
		return mapping1;
	}

	public void setMapping1(boolean mapping1) {
		this.mapping1 = mapping1;
	}

	public boolean isMapping2() {
		return mapping2;
	}

	public void setMapping2(boolean mapping2) {
		this.mapping2 = mapping2;
	}

	public boolean isMapping3() {
		return mapping3;
	}

	public void setMapping3(boolean mapping3) {
		this.mapping3 = mapping3;
	}

	public boolean isMapping4() {
		return mapping4;
	}

	public void setMapping4(boolean mapping4) {
		this.mapping4 = mapping4;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isVariableDataVisible() {
		return isVariableDataVisible;
	}

	public void setVariableDataVisible(boolean isVariableDataVisible) {
		this.isVariableDataVisible = isVariableDataVisible;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	@NotifyChange("*")
	public int getActivePage() {

		return activePage;
	}

	@NotifyChange("*")
	public void setActivePage(int activePage) {
		System.out.println("pageSize");
		reloadCsvGrid();
		this.activePage = activePage;
	}


	public boolean isNewDataSet() {
		return isNewDataSet;
	}

	public void setNewDataSet(boolean isNewDataSet) {
		this.isNewDataSet = isNewDataSet;
	}

	public String getChosenDataFormat() {
		return chosenDataFormat;
	}

	public void setChosenDataFormat(String chosenDataFormat) {
		this.chosenDataFormat = chosenDataFormat;
	}

	public Div getPhenobox() {
		return phenobox;
	}

	public void setPhenobox(Div phenobox) {
		this.phenobox = phenobox;
	}

	public Div getGenobox() {
		return genobox;
	}

	public void setGenobox(Div genobox) {
		this.genobox = genobox;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}


	public Groupbox getMfgroupbox() {
		return mfgroupbox;
	}

	public void setMfgroupbox(Groupbox mfgroupbox) {
		this.mfgroupbox = mfgroupbox;
	}

	public Div getDatagroupbox() {
		return datagroupbox;
	}

	public void setDatagroupbox(Div datagroupbox) {
		this.datagroupbox = datagroupbox;
	}

	public Div getCrossgroupbox() {
		return crossgroupbox;
	}

	public void setCrossgroupbox(Div crossgroupbox) {
		this.crossgroupbox = crossgroupbox;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public int getTotalSize() {
		return dataList.size();
	}

	public Div getMapbox1() {
		return mapbox1;
	}

	public void setMapbox1(Div mapbox1) {
		this.mapbox1 = mapbox1;
	}

	public Div getMapbox2() {
		return mapbox2;
	}

	public void setMapbox2(Div mapbox2) {
		this.mapbox2 = mapbox2;
	}

	public Div getInputbox() {
		return inputbox;
	}

	public void setInputbox(Div inputbox) {
		this.inputbox = inputbox;
	}

	public List<String> getModelTypes(){
		return Arrays.asList(new String[]{ "Bayes A", "Bayes B", "Bayes C", "Bayesian Lasso", "Bayesian Ridge Regression"});
	}

	public List<String> getSamplingStrategies(){
		return Arrays.asList(new String[]{ "random", "within population structure", "across population structure"});
	}
	
	public List<String> getPhenotypeFormat() {
		return Arrays.asList(new String[]{"Comma-separated (.csv) ", "Comma-separated (.txt)", "Space-separated (.txt)", "Tab-separated (.txt)", "Semi-colon-separated (.txt)"});
	}

	public List<String> getPhenotypeFileFormats() {
		return Arrays.asList(new String[]{"csv", "ctxt", "stxt", "ttxt", "sctxt"});
	}
	
	public List<String> getRelMatrixTypes() {
		return Arrays.asList(new String[]{"realized relatedness (Habier/Van Raden)", "realized relatedness (Astle and Balding)", "realized relatedness by simple matching", "realized relatedness by corrected simple matching"});
	}
	
	public List<String> getTraitTypes() {
		return Arrays.asList(new String[]{"Yield", "Height"});
	}
	public List<String> getTraitHeritabilityTypes() {
		return Arrays.asList(new String[]{"High", "Medium", "Low"});
	}
	
	public List<String> getGenotypeFormat() {
		return Arrays.asList(new String[]{"Comma-separated (.csv) ", "Tab-separated (.txt)"});
	}

	public List<String> getGenotypeFileFormats() {
		return Arrays.asList(new String[]{"csv",  "ttxt" });
	}
	

	public void setPhenotypeFormat(List<String> phenotypeFormat) {
		this.phenotypeFormat = phenotypeFormat;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}


	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}


	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}


	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}


	public File getFile1() {
		return file1;
	}

	public void setFile1(File file1) {
		this.file1 = file1;
	}


	public File getFile2() {
		return file2;
	}

	public void setFile2(File file2) {
		this.file2 = file2;
	}


	public String getFileName1() {
		return fileName1;
	}

	public void setFileName1(String fileName1) {
		this.fileName1 = fileName1;
	}


	public String getFileName2() {
		return fileName2;
	}

	public void setFileName2(String fileName2) {
		this.fileName2 = fileName2;
	}


	public String getFileName3() {
		return fileName3;
	}

	public void setFileName3(String fileName3) {
		this.fileName3 = fileName3;
	}


	public int getPageSize() {
		return pageSize;
	}

	@NotifyChange("*")
	public void setPageSize(int pageSize) {
		if(pageSize>=0){
			pageSize=10;
		}
		this.pageSize = pageSize;
	}



	public String getSelectedTraitType() {
		return selectedTraitType;
	}

	public void setSelectedTraitType(String selectedTraitType) {
		this.selectedTraitType = selectedTraitType;
	}



	public Radio getDeleteRadioButton() {
		return deleteRadioButton;
	}

	public void setDeleteRadioButton(Radio deleteRadioButton) {
		this.deleteRadioButton = deleteRadioButton;
	}

	public String getComboboxMapping() {
		return comboboxMapping;
	}

	public void setComboboxMapping(String comboboxMapping) {
		this.comboboxMapping = comboboxMapping;
	}



	public String getCoboboxmapping2() {
		return coboboxmapping2;
	}

	@NotifyChange("coboboxmapping2")
	public void setCoboboxmapping2(String coboboxmapping2) {
		this.coboboxmapping2 = coboboxmapping2;
	}



	public String getComboboxmapping3() {
		return comboboxmapping3;
	}

	public void setComboboxmapping3(String comboboxmapping3) {
		this.comboboxmapping3 = comboboxmapping3;
	}



	public String getComboboxmapping4() {
		return comboboxmapping4;
	}

	public void setComboboxmapping4(String comboboxmapping4) {
		this.comboboxmapping4 = comboboxmapping4;
	}

	public Radio getImputeRadioButton() {
		return imputeRadioButton;
	}

	public void setImputeRadioButton(Radio imputeRadioButton) {
		this.imputeRadioButton = imputeRadioButton;
	}


	public Doublespinner getDbSignificance() {
		return dbPvalCutOff;
	}

	public void setDbSignificance(Doublespinner dbSignificance) {
		this.dbPvalCutOff = dbSignificance;
	}

	public Doublespinner getDbCutOff() {
		return dbCutOff;
	}

	public void setDbCutOff(Doublespinner dbCutOff) {
		this.dbCutOff = dbCutOff;
	}

	public Checkbox getCbSetup1() {
		return cbSetup1;
	}

	public void setCbSetup1(Checkbox cbSetup1) {
		this.cbSetup1 = cbSetup1;
	}


	public Checkbox getCbSetup2() {
		return cbSetup2;
	}

	public void setCbSetup2(Checkbox cbSetup2) {
		this.cbSetup2 = cbSetup2;
	}


	public Doublespinner getDbMainEffects() {
		return dbMainEffects;
	}

	public void setDbMainEffects(Doublespinner dbMainEffects) {
		this.dbMainEffects = dbMainEffects;
	}


	public Textbox getTbMEffect() {
		return tbMEffect;
	}

	public void setTbMEffect(Textbox tbMEffect) {
		this.tbMEffect = tbMEffect;
	}


	public Textbox getTbAll() {
		return tbAll;
	}

	public void setTbAll(Textbox tbAll) {
		this.tbAll = tbAll;
	}


	public Textbox getTbMaxNumber() {
		return tbMaxNumber;
	}

	public void setTbMaxNumber(Textbox tbMaxNumber) {
		this.tbMaxNumber = tbMaxNumber;
	}


	public List<String> getCrosstypeList() {
		return crosstypeList;
	}

	public void setCrosstypeList(List<String> crosstypeList) {
		this.crosstypeList = crosstypeList;
	}


	public Integer getSelectedFileFormat() {
		return selectedFileFormat;
	}

	public void setSelectedFileFormat(Integer selectedFileFormat) {
		this.selectedFileFormat = selectedFileFormat;
	}
}
