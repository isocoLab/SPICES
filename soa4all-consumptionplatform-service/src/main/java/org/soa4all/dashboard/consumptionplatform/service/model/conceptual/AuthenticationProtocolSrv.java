package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

import java.io.Serializable;

public class AuthenticationProtocolSrv {

	String type = "none";
	String valueKey = null;
	String location = null;
	
	public AuthenticationProtocolSrv(String authType, String key, String url) {
	   this.type = authType;
	   this.valueKey = key;
	   this.location = url;
	}
	// Getters and Setters
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValueKey() {
		return valueKey;
	}
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
		

}
