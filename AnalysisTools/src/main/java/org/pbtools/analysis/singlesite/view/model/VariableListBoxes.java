package org.pbtools.analysis.singlesite.view.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.analysis.webservice.manager.RServeManager;
import org.pbtools.analysis.model.SingleSiteAnalysisModel;
import org.pbtools.analysis.utilities.AnalysisUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


public class VariableListBoxes{

	//Managers
	private RServeManager rServeMgr;

	//Zul file components
	@Wire
	private Button chooseResponseBtn;

	private Listbox numericLb;
	private Listbox responseLb;
	private Listbox factorLb;


	//DataModels
	private ListModelList<String> numericModel;
	private ListModelList<String> responseModel;
	private ListModelList<String> factorModel;

	//Rserve Parameters
	private int fileFormat=1;
	private String separator="NULL";;
	private String tempFileName;
	private String fileName=System.getProperty("user.dir")+ System.getProperty("file.separator") + "sample_datasets" + System.getProperty("file.separator") + "RCB_ME.csv";

	//Class Variables
	private ArrayList<String> varInfo;

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

	private RServeManager rServeManager;

	private SingleSiteAnalysisModel ssaModel;

	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) Component component,@ContextParam(ContextType.VIEW) Component view){
		Selectors.wireEventListeners(view, this);

		numericLb = (Listbox) component.getFellow("numericLb");
		responseLb = (Listbox) component.getFellow("responseLb");
		factorLb = (Listbox) component.getFellow("factorLb");


		//wire textboxes
		envTextBox = (Textbox) component.getFellow("envTextBox");
		genotypeTextBox = (Textbox) component.getFellow("genotypeTextBox");
		blockTextBox = (Textbox) component.getFellow("blockTextBox");
		replicateTextBox = (Textbox) component.getFellow("replicateTextBox");
		rowTextBox = (Textbox) component.getFellow("rowTextBox");
		columnTextBox = (Textbox) component.getFellow("columnTextBox");

		//wire image buttons
		addEnvButton = (Image) component.getFellow("addEnvButton");
		addGenotypeButton = (Image) component.getFellow("addGenotypeButton");
		addBlockButton = (Image) component.getFellow("addBlockButton");
		addReplicateButton = (Image) component.getFellow("addReplicateButton");
		addRowButton = (Image) component.getFellow("addRowButton");
		addColumnButton = (Image) component.getFellow("addColumnButton");
	}

	@Listen("onClick = #chooseResponseBtn")
	@NotifyChange("*")
	public void addResponse(MouseEvent event) {
		chooseResponseVariable();
	}

	@Listen("onClick = #removeResponseBtn")
	@NotifyChange("*")
	public void removeResponse(MouseEvent event) {
		unchooseResponseVariable();
	}

	@Listen("onClick = #toNumericBtn")
	@NotifyChange("*")
	public void up() {
		toNumeric();
	}
	@Listen("onClick = #toFactorBtn")
	@NotifyChange("*")
	public void down() {
		toFactor();
	}

	@Listen("onClick = #addEnvButton")
	@NotifyChange("*")
	public void addEnvVariable(MouseEvent event) {
		if(chooseVariable(envTextBox)){//nag add ng variable
			addEnvButton.setSrc("/images/leftarrow_g.png");
		}else {
			addEnvButton.setSrc("/images/rightarrow_g.png");
		}
	}

	@Listen("onClick = #addGenotypeButton")
	@NotifyChange("*")
	public void addGenotypeVariable(MouseEvent event) {
		if(chooseVariable(genotypeTextBox)){//nag add ng variable
			addGenotypeButton.setSrc("/images/leftarrow_g.png");
		}else {
			addGenotypeButton.setSrc("/images/rightarrow_g.png");
		}
	}

	@Listen("onClick = #addBlockButton")
	@NotifyChange("*")
	public void addBlockVariable(MouseEvent event) {
		if(chooseVariable(blockTextBox)){//nag add ng variable
			addBlockButton.setSrc("/images/leftarrow_g.png");
		}else {
			addBlockButton.setSrc("/images/rightarrow_g.png");
		}
	}


	@Listen("onClick = #addReplicateButton")
	@NotifyChange("*")
	public void addReplicateVariable(MouseEvent event) {
		if(chooseVariable(replicateTextBox)){//nag add ng variable
			addReplicateButton.setSrc("/images/leftarrow_g.png");
		}else {
			addReplicateButton.setSrc("/images/rightarrow_g.png");
		}
	}

	@Listen("onClick = #addRowButton")
	@NotifyChange("*")
	public void addRowVariable(MouseEvent event) {
		if(chooseVariable(rowTextBox)){//nag add ng variable
			addRowButton.setSrc("/images/leftarrow_g.png");
		}else {
			addRowButton.setSrc("/images/rightarrow_g.png");
		}
	}

	@Listen("onClick = #addColumnButton")
	@NotifyChange("*")
	public void addColumnVariable(MouseEvent event) {
		if(chooseVariable(columnTextBox)){//nag add ng variable
			addColumnButton.setSrc("/images/leftarrow_g.png");
		}else {
			addColumnButton.setSrc("/images/rightarrow_g.png");
		}
	}

	/**
	 * Set new numeric ListModelList.
	 * 
	 * @param numeric
	 *            is the data of numeric list model
	 */
	public void setModel(List<String> numeric) {
		numericLb.setModel(this.numericModel = new ListModelList<String>(numeric));
		responseModel.clear();
	}

	/**
	 * @return current response data list
	 */
	public List<String> getresponseDataList() {
		return new ArrayList<String>(responseModel);
	}


	private void toFactor() {
		// TODO Auto-generated method stub

		Set<String> set = numericModel.getSelection();
		for (String selectedItem : set) {
			factorModel.add(selectedItem);
			numericModel.remove(selectedItem);
		}

	}

	private void toNumeric() {
		// TODO Auto-generated method stub
		Set<String> set = factorModel.getSelection();
		fileName = fileName.replace("\\", "/");
		for (String selectedItem : set) {
			if (AnalysisUtils.isColumnNumeric(varInfo, selectedItem)){
				numericModel.add(selectedItem);
				factorModel.remove(selectedItem);
			}
			else{
				Messagebox.show("Can't move variable.\n"+selectedItem+ " is not Numeric.");
				System.out.println("Not Numeric");
			}
		}
	}


	private boolean chooseVariable(Textbox varTextBox ) {
		Set<String> set = factorModel.getSelection();
		//				System.out.println("removeResponse");

		if(varTextBox.getValue().isEmpty() && !set.isEmpty()){
			for (String selectedItem : set) {
				varTextBox.setValue(selectedItem);
				factorModel.remove(selectedItem);
			}
			return true;
			//nag add
		}else if(!varTextBox.getValue().isEmpty()){
			factorModel.add(varTextBox.getValue());
			varTextBox.setValue(null);
		}
		return false;
		// TODO Auto-generated method stub
	}


	private void chooseResponseVariable() {
		Set<String> set = numericModel.getSelection();
		//		System.out.println("addResponse");
		for (String selectedItem : set) {
			responseModel.add(selectedItem);
			numericModel.remove(selectedItem);
		}
	}

	private void unchooseResponseVariable() {
		Set<String> set = responseModel.getSelection();
		//		System.out.println("removeResponse");
		for (String selectedItem : set) {
			numericModel.add(selectedItem);
			responseModel.remove(selectedItem);
		}
	}

	// Customized Event
	public class ChooseEvent extends Event {
		private static final long serialVersionUID = -7334906383953342976L;

		public ChooseEvent(Component target, Set<String> data) {
			super("onChoose", target, data);
		}
	}
	
	public SingleSiteAnalysisModel getSsaModel() {
		return ssaModel;
	}

	public void setSsaModel(SingleSiteAnalysisModel ssaModel) {
		this.ssaModel = ssaModel;
	}

}
