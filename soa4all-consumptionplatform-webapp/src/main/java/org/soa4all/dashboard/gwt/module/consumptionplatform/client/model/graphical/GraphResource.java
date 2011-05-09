package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphResource;

import com.extjs.gxt.ui.client.widget.Component;

public class GraphResource {

	String category = null;
	String name = null;
	String type = null;
	Component graphComponent;
	GraphResource dependentResource;
	
	public GraphResource(String category, String name, String type, Component component, GraphResource dependentResource) {		
		this.category = category;
		this.name = name;
		this.type = type;
		this.graphComponent = component;
		this.dependentResource = dependentResource;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Component getGraphComponent() {
		return graphComponent;
	}

	public void setGraphComponent(Component graphComponent) {
		this.graphComponent = graphComponent;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public GraphResource getDependentResource() {
		return dependentResource;
	}

	public void setDependentResource(GraphResource dependentResource) {
		this.dependentResource = dependentResource;
	}

}
