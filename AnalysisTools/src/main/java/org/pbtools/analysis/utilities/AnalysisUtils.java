package org.pbtools.analysis.utilities;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.input.ReaderInputStream;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class AnalysisUtils {

	private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
//	public static final String WEB_SERVICE_ADDRESS  = "http://54.86.161.210:8080/"; // no result at all
//	public static final String WEB_SERVICE_ADDRESS  = "http://172.31.45.231:8080/"; // connection times out
//	public static final String WEB_SERVICE_ADDRESS  = "http://172.29.4.166:8080";  //Sir Alex
	public static final String WEB_SERVICE_ADDRESS  = "http://172.31.8.153:8080"; // "http://52.74.63.249:8080";//  //jack 172.31.8.153 //http://52.74.63.249:8080/
//	public static final String WEB_SERVICE_ADDRESS  = "http://localhost:8080"; //"http://172.29.4.166:8080";

	public static void disableRow(Row row, ListModelList<String> factorModel) {
		// TODO Auto-generated method stub
		Textbox textbox = (Textbox) row.getAttribute("Textbox");
		if(!textbox.getValue().isEmpty()){
			factorModel.add(textbox.getValue());
			textbox.setValue("");
		}
		row.setVisible(false);
	}
	
	public static ListModelList<String> getNumericAsListModel(ArrayList<String> variableInfo) {
		// TODO Auto-generated method stub
		ListModelList<String> modelList = new ListModelList<String>();
		for(String s : variableInfo){
			if(s.contains(":Numeric")) modelList.add(s.split(":")[0]);
		}
		return modelList;
	}
	
	public static ListModelList<String> getFactorsAsListModel(ArrayList<String> variableInfo) {
		// TODO Auto-generated method stub
		ListModelList<String> modelList = new ListModelList<String>();
		for(String s : variableInfo){
			if(s.contains(":Factor")) modelList.add(s.split(":")[0]);
		}
		return modelList;
	}

	public static boolean isColumnNumeric(ArrayList<String> varInfo,
			String selectedItem) {
		// TODO Auto-generated method stub
		for(String s : varInfo){
			if(s.split(":")[0].equals(selectedItem) && s.contains("Numeric")) return true;
		}
		return false;	
	}

	public static String getOutputFolderName(String fileName) {
		// TODO Auto-generated method stub
		return new File(fileName).getName()+getTimeStamp();
	}
	
	public static String getTimeStamp() {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		return Long.toString(now.getTimeInMillis());
	}
	
	public static String createOutputFolder(String fileName, String analysisType) {
		// TODO Auto-generated method stub
		String userFolderPath, outputStudyPath="";
			String dataFileName = fileName.replaceAll(".csv", "");
			dataFileName = dataFileName.replaceAll(".tmp", "");
			
		
		if(analysisType.equals("ssa")) userFolderPath=Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ FILE_SEPARATOR +"BIMS"+ FILE_SEPARATOR+ "Single-Site";
//				SecurityUtil.getDbUser().getUsername()+ FILE_SEPARATOR+ "Single-Site";
		else userFolderPath =Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ FILE_SEPARATOR +"BIMS"+FILE_SEPARATOR+ "Multi-Site";
//				SecurityUtil.getDbUser().getUsername()+ FILE_SEPARATOR+ "Multi-Site";
		
		String outputStudyPath1 = userFolderPath+ FILE_SEPARATOR + getOutputFolderName(dataFileName) +FILE_SEPARATOR;
		
		if(createFolder(userFolderPath)){	
			createFolder(outputStudyPath1);
		}
		
		return outputStudyPath1;
	}
	
	public static String getUserTempFolder() {
		// TODO Auto-generated method stub
		String tmpFolderPath="";

//		tmpFolderPath=Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ FILE_SEPARATOR +
//				SecurityUtil.getDbUser().getUsername()+ FILE_SEPARATOR+ "tmp"+ FILE_SEPARATOR;
//		
//		String outputStudyPath = userFolderPath+ FILE_SEPARATOR + getOutputFolderName(dataFileName) +FILE_SEPARATOR;
//		createTmpFolder(tmpFolderPath);
//		 System.out.println("created folder"+tmpFolderPath);
		return tmpFolderPath;
	}
	
	public static void createTmpFolder(String folderPath){
		File outputFolder = new File(folderPath);
		if(outputFolder.exists()){
			outputFolder.mkdir();
			outputFolder.deleteOnExit();
		}
	}
	
	public static boolean createFolder(String folderPath){
		File outputFolder = new File(folderPath);

		if(outputFolder.exists()) return true;
		return outputFolder.mkdir();
	}

	public static ArrayList<String> getVarNames(ArrayList<String> varInfo) {
		// TODO Auto-generated method stub
		ArrayList<String> modelList = new ArrayList<String>();
		for(String s : varInfo){
			modelList.add(s.split(":")[0]);
		}
		return modelList;
	}

	public static String getoutputFolderPath(String filenamePath) {
		// TODO Auto-generated method stub
		System.out.println("path:"+filenamePath);
		return  Sessions.getCurrent().getWebApp().getRealPath("")+filenamePath;
	}

	public static ListModelList<String> toListModelList(String[] stringArray) {
		// TODO Auto-generated method stub
		ListModelList<String> modelList = new ListModelList<String>();
		for(String s : stringArray){
			System.out.println(s);
		 modelList.add(s);
		}
		return modelList;
	}

	public static String[] getItemsFromListAsStringAyrray(Listbox controlsLb) {
		// TODO Auto-generated method stub
		ArrayList<String> stringList = new ArrayList<String>();
		for(Listitem li: controlsLb.getItems()){
			stringList.add(li.getLabel());
		}
		
		return stringList.toArray(new String[stringList.size()]);
	}
	
	public static String arrayToString(String[] stringArray) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for(String s : stringArray){
			sb.append(s+", ");
		}
		return sb.toString();
	}
}
