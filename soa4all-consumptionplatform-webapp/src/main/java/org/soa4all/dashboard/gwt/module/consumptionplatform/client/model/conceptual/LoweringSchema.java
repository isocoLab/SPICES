package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class LoweringSchema  extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	String schemaRef = null;
	
	public LoweringSchema () {		
	}
	
	public LoweringSchema (String schemaRef) {
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
