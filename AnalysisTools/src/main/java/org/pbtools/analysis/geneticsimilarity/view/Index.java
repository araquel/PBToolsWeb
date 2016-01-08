package org.pbtools.analysis.geneticsimilarity.view;

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
import org.pbtools.analysis.view.model.GeneticSimilarityModel;
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
	BindContext ctx1, ctx2;
	Component view1, view2;
	InputStream in1, in2;
	
	private GeneticSimilarityModel gsModel;
	private RServeManager rServeManager;
	private String errorMessage="", fileType, genotypicFilePath, genotypicFileName, matrixFileName, pedigreeFilePath, pedigreeFileName;
	private List<String> dataFormats, markerFormats;
	private File tempFile, pedigreeFile, genotypicFile;
	
	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view){
		gsModel = new GeneticSimilarityModel();
		rServeManager = new RServeManager();
		dataFormats = getDataFormats();
		markerFormats = getMarkerFormats();	
	}

	@NotifyChange("pedigreeFilePath")
	@Command("choosePedigreeFile")
	public void choosePedigreeFile(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("fileFormat") Integer formatIndex)
			throws Exception{

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		this.ctx1=ctx;
		this.view1=view;

		tempFile = new File(event.getMedia().getName());
		setPedigreeFileName(event.getMedia().getName());
		System.out.println(tempFile.getAbsolutePath());
        fileType = pedigreeFileName.split("\\.")[1];
		
		 //check if filetype is valid
		if (!fileType.equals("csv") && !fileType.equals("txt")) {
			errorMessage = "Error: File must be in a txt or csv format";
			Messagebox.show(errorMessage+" format",
					"Upload Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
      //complete upload
        pedigreeFilePath = tempFile.getAbsolutePath();
		if (pedigreeFile == null)
			try {
				pedigreeFile = File.createTempFile(pedigreeFileName, "."+fileType);
				System.out.println(pedigreeFile.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		in1 = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
		
		System.out.println("succesfully uploaded pedigree file");
//		qtlModel.setFile1(file1.getAbsolutePath());
//		qtlModel.setFormat1(fileType);
	}
	
	@Command("updateRadioOptions")
	public void updateRadioOptions(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("chosenValue") String chosenValue){
		if(chosenValue.equals("pedigree")){
			enablePedigreeOptions(true);
			enablePerformAllOptions(false);
		}
		else if(chosenValue.equals("performAll")){
			enablePedigreeOptions(false);
			enablePerformAllOptions(true);
		}
	}
	
	private void enablePerformAllOptions(boolean state) {
		// TODO Auto-generated method stub
		
	}

	private void enablePedigreeOptions(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@NotifyChange("genotypicFilePath")
	@Command("chooseGenotypicFile")
	public void chooseGenotypicFile(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("fileFormat") Integer formatIndex) 
					throws Exception{

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		this.ctx2=ctx;
		this.view2=view;

		tempFile = new File(event.getMedia().getName());
		setGenotypicFileName(event.getMedia().getName());
		System.out.println(tempFile.getAbsolutePath());	
        fileType = genotypicFileName.split("\\.")[1];
        
        //check if filetype is valid
        if (!fileType.equals("csv") && !fileType.equals("txt")) {
			errorMessage = "Error: File must be in a txt or csv format";
			Messagebox.show(errorMessage+" format",
					"Upload Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
        
        //complete upload
        genotypicFilePath = tempFile.getAbsolutePath();
		if (genotypicFile == null)
			try {
				genotypicFile = File.createTempFile(genotypicFileName, "."+fileType);
				System.out.println(genotypicFile.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		in2 = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
//		qtlModel.setFile1(file1.getAbsolutePath());
//		qtlModel.setFormat1(fileType);
		System.out.println("succesfully uploaded genotypic file");
	}
	
	public List<String> getDataFormats() {
		return Arrays.asList(new String[]{"Space-separated (.txt)", "Comma-separated (.csv)", "Tab-separated (.txt)", "Semi-colon-separated (.txt)"});
	}

	public void setDataFormats(List<String> dataFormats) {
		this.dataFormats = dataFormats;
	}


	public List<String> getMarkerFormats() {
		return Arrays.asList(new String[]{"Space-separated (.txt)", "Comma-separated (.csv)", "Tab-separated (.txt)", "Semi-colon-separated (.txt)"});
	}


	public void setMarkerFormats(List<String> markerFormats) {
		this.markerFormats = markerFormats;
	}

	public String getPedigreeFileName() {
		return pedigreeFileName;
	}

	public void setPedigreeFileName(String pedigreeFileName) {
		this.pedigreeFileName = pedigreeFileName;
	}

	public String getGenotypicFileName() {
		return genotypicFileName;
	}

	public void setGenotypicFileName(String genotypicFileName) {
		this.genotypicFileName = genotypicFileName;
	}

	public String getGenotypicFilePath() {
		return genotypicFilePath;
	}

	public void setGenotypicFilePath(String genotypicFilePath) {
		this.genotypicFilePath = genotypicFilePath;
	}

	public String getPedigreeFilePath() {
		return pedigreeFilePath;
	}

	public void setPedigreeFilePath(String pedigreeFilePath) {
		this.pedigreeFilePath = pedigreeFilePath;
	}

	public String getMatrixFileName() {
		return matrixFileName;
	}

	public void setMatrixFileName(String matrixFileName) {
		this.matrixFileName = matrixFileName;
	}
	
}
