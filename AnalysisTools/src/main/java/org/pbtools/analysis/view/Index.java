package org.pbtools.analysis.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.analysis.webservice.manager.RServeManager;
import org.analysis.webservice.manager.WebServiceManager;
import org.pbtools.analysis.utilities.AnalysisUtils;
import org.pbtools.analysis.view.model.MultiSiteAnalysisModel;
import org.pbtools.analysis.view.model.QTLAnalysisModel;
import org.pbtools.analysis.view.model.SingleSiteAnalysisModel;
import org.zkoss.bind.BindUtils;
//import org.pbtools.analysis.singlesite.view.model.MultiSiteAnalysisModel;
//import org.pbtools.analysis.singlesite.view.model.QTLAnalysisModel;
//import org.pbtools.analysis.singlesite.view.model.SingleSiteAnalysisModel;
//import org.strasa.middleware.model.Program;
//import org.strasa.middleware.model.Project;
//import org.strasa.middleware.model.StudyType;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class Index {	

	private Tab resultTab;
	private WebServiceManager webServiceManager;
	private RServeManager rServeManager;

		@AfterCompose
		public void init(@ContextParam(ContextType.COMPONENT) Component component,
				@ContextParam(ContextType.VIEW) Component view){
//			 Executions.getCurrent().getParameter("paramName");
			 
//			 if()
		}

//	@Init
//	public void init(@QueryParam("analysis") String analysis){
//		try{
//			if(analysis.equals("multisite")){
//				BindUtils.postGlobalCommand(null, null, "launchMultiSite", null);
//			}else BindUtils.postGlobalCommand(null, null, "launchSingleSite", null);
//		}catch(NullPointerException npe){
//			BindUtils.postGlobalCommand(null, null, "launchSingleSite", null);
//		}
//	}

	@NotifyChange("*")
	@GlobalCommand("launchSingleSite")
	public void launchSingleSite(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view) {
		//			Label tabLabel = (Label) component.getFellow("tabLabel");
		//			tabLabel.setValue("Single-site Analysis");
		Tabpanel specificationsPanel = (Tabpanel) component.getFellow("specificationsPanel");
		specificationsPanel.getChildren().get(0).detach();

		Include specificationPage = new Include();
		specificationPage.setParent(specificationsPanel);
		specificationPage.setSrc("/analysis/singlesite/specifications.zul");
	}

	@NotifyChange("*")
	@GlobalCommand("launchMultiSite")
	public void launchMultiSite(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view) {
		//			Label tabLabel = (Label) component.getFellow("tabLabel");
		//			tabLabel.setValue("Multi-site Analysis");
		Tabpanel specificationsPanel = (Tabpanel) component.getFellow("specificationsPanel");
		specificationsPanel.getChildren().get(0).detach();

		Include specificationPage = new Include();
		specificationPage.setParent(specificationsPanel);
		specificationPage.setSrc("/analysis/multisite/specifications.zul");
		//			tabLabel.setValue("Multi-site Analysis");
	}


	@NotifyChange("*")
	@GlobalCommand("launchQTL")
	public void launchQTL(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view) {
		Tabpanel specificationsPanel = (Tabpanel) component.getFellow("specificationsPanel");
		specificationsPanel.getChildren().get(0).detach();

		Include specificationPage = new Include();
		specificationPage.setParent(specificationsPanel);
		specificationPage.setSrc("/analysis/linkagemapping/index.zul");
	}

	@GlobalCommand("displaySsaResult")
	@NotifyChange("*")
	public void displaySsaResult(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("ssaModel") SingleSiteAnalysisModel ssaModel, @BindingParam("fileNames") String[] fileNames){

		Tabpanels tabPanels = (Tabpanels) component.getFellow("tabPanels");
		Tabs tabs = (Tabs) component.getFellow("tabs");

		Tabpanel newPanel = new Tabpanel();
		Tab newTab = new Tab();
		newTab.setLabel(ssaModel.getAnalysisResultFolder());
		newTab.setClosable(true);

		//initialize view after view construction.
		Include studyInformationPage = new Include();
		studyInformationPage.setParent(newPanel);
		studyInformationPage.setDynamicProperty("outputFolderPath", AnalysisUtils.WEB_SERVICE_ADDRESS+"/WS-RS/output/"+ssaModel.getAnalysisResultFolder()+"/");
		studyInformationPage.setDynamicProperty("fileNames", fileNames);
		studyInformationPage.setSrc("/analysis/resultviewer.zul");

		tabPanels.appendChild(newPanel);
		tabs.appendChild(newTab);

		newTab.setSelected(true); 
	}

	@GlobalCommand("displayMsaResult")
	@NotifyChange("*")
	public void displayMsaResult(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("msaModel") MultiSiteAnalysisModel msaModel, @BindingParam("fileNames") String[] fileNames) {

		Tabpanels tabPanels = (Tabpanels) component.getFellow("tabPanels");
		Tabs tabs = (Tabs) component.getFellow("tabs");

		Tabpanel newPanel = new Tabpanel();
		Tab newTab = new Tab();
		newTab.setLabel(msaModel.getAnalysisResultFolder());
		newTab.setClosable(true);

		//initialize view after view construction.
		Include studyInformationPage = new Include();
		studyInformationPage.setParent(newPanel);
		studyInformationPage.setDynamicProperty("outputFolderPath", AnalysisUtils.WEB_SERVICE_ADDRESS+"/WS-RS/output/"+msaModel.getAnalysisResultFolder()+"/");
		studyInformationPage.setDynamicProperty("fileNames", fileNames);
		studyInformationPage.setSrc("/analysis/resultviewer.zul");

		tabPanels.appendChild(newPanel);
		tabs.appendChild(newTab);

		newTab.setSelected(true); 
	}
	
//	@GlobalCommand("displayMsaResult")
//	@NotifyChange("*")
//	public void displayMsaResult(@ContextParam(ContextType.COMPONENT) Component component,
//			@ContextParam(ContextType.VIEW) Component view, @BindingParam("msaModel") MultiSiteAnalysisModel msaModel) {
//		rServeManager = new RServeManager();
//		rServeManager.doMultiEnvironmentOneStage(msaModel);
//
//		Tabpanels tabPanels = (Tabpanels) component.getFellow("tabPanels");
//		Tabs tabs = (Tabs) component.getFellow("tabs");
//
//		Tabpanel newPanel = new Tabpanel();
//		Tab newTab = new Tab();
//		newTab.setLabel(new File(msaModel.getOutFileName()).getParentFile().getName());
//		newTab.setClosable(true);
//
//		//initialize view after view construction.
//		Include studyInformationPage = new Include();
//		studyInformationPage.setParent(newPanel);
//		studyInformationPage.setDynamicProperty("outputFolderPath", msaModel.getResultFolderPath().replaceAll("\\\\", "/"));
//		studyInformationPage.setSrc("/analysis/rserveresultviewer.zul");
//
//		tabPanels.appendChild(newPanel);
//		tabs.appendChild(newTab);
//	}

	@GlobalCommand("displayQtlResult")
	@NotifyChange("*")
	public void displayQtlResult(@ContextParam(ContextType.COMPONENT) Component component,
			@ContextParam(ContextType.VIEW) Component view, @BindingParam("qtlModel") QTLAnalysisModel qtlModel) {
		System.out.println("pasing variables");
		rServeManager = new RServeManager();
		rServeManager.doQtl(qtlModel);

		Tabpanels tabPanels = (Tabpanels) component.getFellow("tabPanels");
		Tabs tabs = (Tabs) component.getFellow("tabs");

		Tabpanel newPanel = new Tabpanel();
		Tab newTab = new Tab();
		newTab.setLabel(new File(qtlModel.getOutFileName()).getParentFile().getName());
		newTab.setClosable(true);

		//initialize view after view construction.
		Include studyInformationPage = new Include();
		studyInformationPage.setParent(newPanel);
		studyInformationPage.setDynamicProperty("outputFolderPath", qtlModel.getResultFolderPath().replaceAll("\\\\", "/"));
		studyInformationPage.setSrc("/analysis/resultviewer.zul");

		tabPanels.appendChild(newPanel);
		tabs.appendChild(newTab);
	}

	public Tab getResultTab(){
		return resultTab;
	}

	public void setResultTab(Tab resultTab) {
		this.resultTab = resultTab;
	}

}
