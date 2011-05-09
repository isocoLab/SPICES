package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class OutputMessage extends BaseModel implements IsSerializable{
	
	String uri = null;
	
	public OutputMessage () {
		
	}
	
	public OutputMessage (String uri) {
		this.uri = uri;
	}

	// Getters and Setters
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
