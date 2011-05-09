package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

import java.io.Serializable;

public class InputMessageSrv {
	
	String uri = null;
	
	public InputMessageSrv (String uri) {
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
