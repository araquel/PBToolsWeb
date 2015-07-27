package org.pbtools.analysis.multisite.view.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.analysis.webservice.manager.RServeManager;
import org.pbtools.analysis.utilities.AnalysisUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


public class OtherOptions{

	//Managers

	//Zul file components

	//DataModels
	private ListModelList<String> factorModel;
	
	//textBoxes
	private Textbox envTextBox;
	private Textbox genotypeTextBox;
	private Textbox blockTextBox;
	private Textbox replicateTextBox;
	private Textbox rowTextBox;
	private Textbox columnTextBox;

	//imageButtons
	private Image addEnvButton;
	private Image addGenotypeButton;
	private Image addBlockButton;
	private Image addReplicateButton;
	private Image addRowButton;
	private Image addColumnButton;
	
	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) Component component,@ContextParam(ContextType.VIEW) Component view){
		Selectors.wireEventListeners(view, this);

	}
//
//	@Listen("onClick = #addEnvButton")
//	@NotifyChange("*")
//	public void addEnvVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, envTextBox)){//nag add ng variable
//			addEnvButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addEnvButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
//	
//	@Listen("onClick = #addGenotypeButton")
//	@NotifyChange("*")
//	public void addGenotypeVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, genotypeTextBox)){//nag add ng variable
//			addGenotypeButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addGenotypeButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
//	
//	@Listen("onClick = #addBlockButton")
//	@NotifyChange("*")
//	public void addBlockVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, blockTextBox)){//nag add ng variable
//			addBlockButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addBlockButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
//
//	@Listen("onClick = #addReplicateButton")
//	@NotifyChange("*")
//	public void addReplicateVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, replicateTextBox)){//nag add ng variable
//			addReplicateButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addReplicateButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
//	
//	@Listen("onClick = #addRowButton")
//	@NotifyChange("*")
//	public void addRowVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, rowTextBox)){//nag add ng variable
//			addRowButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addRowButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
//
//	@Listen("onClick = #addColumnButton")
//	@NotifyChange("*")
//	public void addColumnVariable(MouseEvent event) {
//		if(AnalysisUtils.chooseVariable(factorModel, columnTextBox)){//nag add ng variable
//			addColumnButton.setSrc("/images/leftarrow_g.png");
//		}else {
//			addColumnButton.setSrc("/images/rightarrow_g.png");
//		}
//	}
	
	// Customized Event
	public class ChooseEvent extends Event {
		private static final long serialVersionUID = -7334906383953342976L;

		public ChooseEvent(Component target, Set<String> data) {
			super("onChoose", target, data);
		}
	}

}
