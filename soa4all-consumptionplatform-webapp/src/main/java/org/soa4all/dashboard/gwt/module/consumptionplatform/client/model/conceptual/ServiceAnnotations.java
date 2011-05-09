package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual;

import java.util.ArrayList;
import java.util.Iterator;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;


public class ServiceAnnotations extends BaseModel implements IsSerializable {
		
	// Service annotations properties
	String name = null;
	String uri = null;
	String idService = null;
	ArrayList<ServiceOperation> operations = new ArrayList<ServiceOperation>();
	AuthenticationProtocol authData = null;
	
	public ServiceAnnotations() {
	
	}
	
	public ServiceAnnotations(String id) {
		this.idService = id;
	}
	
	// Utility methods
	public void addOperation (ServiceOperation operation) {
		this.operations.add(operation);
	}
	
	public ServiceOperation getOperation(String name) {
		
		Iterator<ServiceOperation> it = this.operations.iterator();
		while (it.hasNext()) {
			ServiceOperation op = it.next();
			if (op.getName().equalsIgnoreCase(name))
			   return op;	 
		}
		return null;
	}
	
	public boolean isWsdlService () {
		if (uri!=null)
			return ((uri.toLowerCase().contains("wsdl")) || (idService.toLowerCase().contains("wsdl")));
		else
			return false; 
	}
	
	// Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdService() {
		return idService;
	}
	public void setIdService(String idService) {
		this.idService = idService;
	}
	public ArrayList<ServiceOperation> getOperations() {
		return operations;
	}
	public void setOperations(ArrayList<ServiceOperation> operations) {
		this.operations = operations;
	}
		
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	public AuthenticationProtocol getAuthData() {
		return authData;
	}

	public void setAuthData(AuthenticationProtocol authData) {
		this.authData = authData;
	}
	
	
}
