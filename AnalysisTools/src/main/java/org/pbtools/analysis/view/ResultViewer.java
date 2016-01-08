package org.pbtools.analysis.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.analysis.webservice.manager.WebServiceManager;
import org.apache.commons.io.input.ReaderInputStream;
import org.pbtools.analysis.result.view.model.FileComposer;
import org.pbtools.analysis.utilities.FileUtilities;
import org.zkoss.bind.BindContext;
//import org.pbtools.analysis.singlesite.view.model.SingleSiteAnalysisModel;
//import org.spring.security.model.SecurityUtil;
//import org.strasa.web.analysis.result.view.model.FileComposer;
//import org.strasa.web.analysis.result.view.model.FileModel;
//import org.strasa.web.analysis.result.view.model.FileModelTreeNode;
//import org.strasa.web.utilities.FileUtilities;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Treeitem;

import com.google.gson.JsonSyntaxException;

import au.com.bytecode.opencsv.CSVReader;

public class ResultViewer {
	String textFileContent = null;
	private AMedia fileContent;
	private File tempFile;
	private static final String IMAGE_THUMBNAIL_HEIGHT = "150px";
	private static final String IMAGE_THUMBNAIL_WIDTH = "150px";
	private static String RESULT_ANALYSIS_PATH;

	private String outputFolderPath;
	private ArrayList<String> filePaths;
	//Viewer for webservice
	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) final Component component,
			@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("outputFolderPath") String outputFolderPath, @ExecutionArgParam("fileNames") String[] files){
		//		RESULT_ANALYSIS_PATH = outputFolderPath;
		this.outputFolderPath = outputFolderPath;
		filePaths = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();

		//outputTextViewer
		File outputFolder = new File(outputFolderPath);
		for(String file: files){

			String ouputFilePath = outputFolderPath+file;
			filePaths.add(ouputFilePath);
			if(file.endsWith(".txt") && !file.contains("elapsed")){
				String path = ouputFilePath;
				Tabpanel txtResultPanel = (Tabpanel) component.getFellow("txtResultTab");
				//File fileToRead = new File(path.replaceAll("\\\\", "//"));

				URL u = null;
				try {
					u = new URL(ouputFilePath);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(u.openStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//				String inputLine;
				//				while ((inputLine = in.readLine()) != null){
				//					rawData.add(inputLine.replaceAll("\"", "").split(","));
				//				}
				//				in.close();
				//				
				//				byte[] buffer = new byte[(int) fileToRead.length()];
				//				FileInputStream fs;
				//				try {
				//					fs = new FileInputStream(fileToRead);
				//					fs.read(buffer);
				//					fs.close();
				//				} catch (FileNotFoundException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				} catch (IOException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}
				//				ByteArrayInputStream is = new ByteArrayInputStream(buffer);

				AMedia fileContent = new AMedia("report", "text", "text/plain", in);

				System.out.println("txtPath "+ouputFilePath);
				Include studyInformationPage = new Include();
				studyInformationPage.setParent(txtResultPanel);
				studyInformationPage.setDynamicProperty("txtFile", fileContent);
				//				studyInformationPage.setSrc("/user/analysis/txtviewer.zul");
				//				sb.append("txt");

				//				Include studyInformationPage = new Include();
				//				studyInformationPage.setParent(txtResultPanel);
				//				studyInformationPage.setDynamicProperty("path", path);
				studyInformationPage.setSrc("/analysis/txtviewer.zul");
				sb.append("txt");
			}

			if(file.endsWith(".png")){

				Div div = (Div) component.getFellow("graphResultDiv");
				final String path = ouputFilePath;
				final Groupbox newGroupBox = new Groupbox();
				newGroupBox.setTitle(file.replaceAll(".csv", ""));
				newGroupBox.setHeight(IMAGE_THUMBNAIL_HEIGHT);
				newGroupBox.setWidth(IMAGE_THUMBNAIL_WIDTH);
				newGroupBox.setClosable(false);

				System.out.println("imgPath "+path);
				newGroupBox.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						zoomImage(path.replaceAll("\\\\", "//"), component);
					}
				});

				RenderedImage image = null;
				byte[] bytes = null;
				try {
					URL url = new URL(ouputFilePath);
					image = ImageIO.read(url);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, "jpg", baos);
					bytes = baos.toByteArray();

				} catch (IOException e) {
					e.printStackTrace();
				} catch(IllegalArgumentException e){
					e.printStackTrace();
				}
				try {
					if(bytes.length>0){
						Include studyInformationPage = new Include();
						studyInformationPage.setDynamicProperty("height", IMAGE_THUMBNAIL_HEIGHT);
						studyInformationPage.setDynamicProperty("width", IMAGE_THUMBNAIL_WIDTH);
						studyInformationPage.setDynamicProperty("image", image);
						studyInformationPage.setSrc("/analysis/imgviewer.zul");
						studyInformationPage.setParent(newGroupBox);
						div.appendChild(newGroupBox);
						Separator sep = new Separator();
						sep.setHeight("30px");
						div.appendChild(sep);
						sb.append(".png");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} 
			}

			if(file.endsWith(".csv")){
				//						System.out.println("display image:" + outputFolderPath+file);
				Tabpanel tabPanel = (Tabpanel) component.getFellow("csvResultTab");

				Groupbox newGroupBox = new Groupbox();
				newGroupBox.setTitle(file.replaceAll(".csv", ""));
				newGroupBox.setMold("3d");
				newGroupBox.setHeight("500px");
				//				final String path = ouputFilePath;
				List<String[]> rawData;

				try {
					rawData=new ArrayList<String[]>();

					URL u = new URL(ouputFilePath);
					BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null){
						rawData.add(inputLine.replaceAll("\"", "").split(","));
					}
					in.close();

					//FileUtilities.uploadFile(tempFile.getAbsolutePath(), in);	
					//CSVReader reader = new CSVReader(new FileReader(tempFile.getAbsolutePath()));
					Include studyInformationPage = new Include();
					studyInformationPage.setDynamicProperty("rawData", rawData);
					studyInformationPage.setDynamicProperty("name", file.replaceAll(".csv", ""));
					studyInformationPage.setSrc("/analysis/csvviewer.zul");

					studyInformationPage.setParent(newGroupBox);
					tabPanel.appendChild(newGroupBox);

					Separator sep = new Separator();
					sep.setHeight("20px");
					tabPanel.appendChild(sep);
					sb.append("csv");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//					treeTabs.appendChild(newTab);
			}
		}
		String s = sb.toString();
		Tabpanel tabanel = null;
		Tab tab = null;
		if(!s.contains("txt")){
			tabanel = (Tabpanel) component.getFellow("txtResultTab");
			tab = (Tab) component.getFellow("txtResult");
			detach(tabanel,tab);
		}
		if(!s.contains("csv")){
			tabanel = (Tabpanel) component.getFellow("csvResultTab");
			tab = (Tab) component.getFellow("csvResult");
			detach(tabanel,tab);
		}
		if(!s.contains("png")){
			tabanel = (Tabpanel) component.getFellow("graphResultTab");
			tab = (Tab) component.getFellow("graphResult");
			detach(tabanel,tab);
		}
		Tabbox tabBox = (Tabbox) component.getFellow("tabBox");
		tabBox.setSelectedIndex(0);
	}	

	@Command("exportFolderToZip")
	public void exportFolderToZip(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx, @ContextParam(ContextType.VIEW) Component view) {
		WebServiceManager webServiceManager = new WebServiceManager();
		webServiceManager.getMultiTrialResultZip(outputFolderPath);
	}

	//	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
	//		System.out.println("Writing '" + fileName + "' to zip file");
	//
	//		File file = new File(fileName);
	//		FileInputStream fis = new FileInputStream(file);
	//		ZipEntry zipEntry = new ZipEntry(fileName);
	//		zos.putNextEntry(zipEntry);
	//
	//		byte[] bytes = new byte[1024];
	//		int length;
	//		while ((length = fis.read(bytes)) >= 0) {
	//			zos.write(bytes, 0, length);
	//		}
	//
	//		zos.closeEntry();
	//		fis.close();
	//	}

	protected void zoomImage(String dynamicProperty, Component component) {
		// TODO Auto-generated method stub
		Div div = (Div) component.getFellow("zoomDiv");
		if(div.getChildren().size()>0) div.getChildren().get(0).detach();

		RenderedImage image = null;
		byte[] bytes = null;

		try {
			URL url = new URL(dynamicProperty);
			image = ImageIO.read(url);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			bytes = baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		try {
			if(bytes.length>0){
				Include studyInformationPage = new Include();
				studyInformationPage.setDynamicProperty("height", FileComposer.IMAGE_HEIGHT);
				studyInformationPage.setDynamicProperty("width", FileComposer.IMAGE_WIDTH);
				studyInformationPage.setDynamicProperty("image", image);
				studyInformationPage.setDynamicProperty("imagePath", dynamicProperty);
				studyInformationPage.setSrc("/analysis/imgviewer.zul");
				studyInformationPage.setParent(div);
				div.appendChild(studyInformationPage);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}

	private void detach(Tabpanel tabanel, Tab tab) {
		// TODO Auto-generated method stub
		tabanel.detach();
		tab.detach();
	}
}