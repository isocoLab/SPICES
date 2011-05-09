package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

import java.io.Serializable;

public class LiftingSchemaSrv {

	String schemaRef = null;
	
	public LiftingSchemaSrv () {
	}
	
	public LiftingSchemaSrv (String schemaRef) {
		this.schemaRef = schemaRef;
	}
	
	// Getters and Setters
	public String getSchemaReference() {
		return schemaRef;
	}

	public void setSchemaReference(String schemaRef) {
		this.schemaRef = schemaRef;
	}
}
