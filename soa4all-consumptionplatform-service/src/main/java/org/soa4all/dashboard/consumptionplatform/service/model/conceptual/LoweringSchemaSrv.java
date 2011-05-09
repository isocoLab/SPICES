package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

public class LoweringSchemaSrv {

	String schemaRef = null;
	
	public LoweringSchemaSrv () {
	}
	
	public LoweringSchemaSrv (String schemaRef) {
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
