package org.soa4all.dashboard.consumptionplatform.service.model.graphical;

import org.soa4all.dashboard.consumptionplatform.service.model.graphical.GraphResourceSrv;

import com.extjs.gxt.ui.client.widget.Component;

public class GraphResourceSrv {

	String category = null;
	String name = null;
	String type = null;
	Component graphComponent;
	GraphResourceSrv dependentResource;
	
	public GraphResourceSrv(String category, String name, String type, Component component, GraphResourceSrv dependentResource) {		
		this.category = category;
		this.name = name;
		this.type = type;
		this.graphComponent = component;
		this.dependentResource = dependentResource;
	}

}
