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


public class GraphOptions{

	//Managers

	//Zul file components

	//DataModels
	private ListModelList<String> factorModel;
	
	@AfterCompose
	public void init(@ContextParam(ContextType.COMPONENT) Component component,@ContextParam(ContextType.VIEW) Component view){
		Selectors.wireEventListeners(view, this);

	}
	
	// Customized Event
	public class ChooseEvent extends Event {
		private static final long serialVersionUID = -7334906383953342976L;

		public ChooseEvent(Component target, Set<String> data) {
			super("onChoose", target, data);
		}
	}

}
