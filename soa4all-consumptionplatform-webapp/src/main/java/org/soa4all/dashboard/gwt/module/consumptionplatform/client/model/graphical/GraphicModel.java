package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical;

import java.util.ArrayList;
import java.util.Iterator;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphResource;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.Component;


public class GraphicModel extends BaseModel{
	
	ArrayList<GraphResource> graphElements = new ArrayList();
	
	// Getters and Setters
	public ArrayList<GraphResource> getGraphElements() {
		return graphElements;
	}

	public void addGraphElement(GraphResource resource) {
		this.graphElements.add(resource);
	}
	
	public GraphResource getResourceByName(String name) {
		
		Iterator it = (Iterator) graphElements.iterator();
		while (it.hasNext()) {
			GraphResource resource = (GraphResource) it.next();
			if (resource.getName().equalsIgnoreCase(name)) {
				return resource;
			}
		}
		return null;

	}

}
