package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

import java.util.ArrayList;
import java.util.Iterator;

public class ServiceAnnotationsSrv {
	

	// Service annotations properties
	String name = null;
	String uri = null;
	String idService = null;
	ArrayList<ServiceOperationSrv> operations = new ArrayList<ServiceOperationSrv>();	
	AuthenticationProtocolSrv authData = null;
	
	public ServiceAnnotationsSrv() {
	
	}
	
	public ServiceAnnotationsSrv(String id) {
		this.idService = id;
	}
	
	// Utility methods
	public void addOperation (ServiceOperationSrv operation) {
		this.operations.add(operation);
	}
	
	public ServiceOperationSrv getOperation(String name) {
		
		Iterator<ServiceOperationSrv> it = this.operations.iterator();
		while (it.hasNext()) {
			ServiceOperationSrv op = it.next();
			if (op.getUri().equalsIgnoreCase(name))
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
	
	/*
	 * Return the label for this service,
	 * parsing its name and just returning the REAL simple service name 
	 */
	public String getNameLabel()
	{
		try
		{
			return getName().substring(getName().lastIndexOf("(")+1, getName().lastIndexOf(")"));
		}
		// In case of error, I just return the complex service name
		catch (Exception e)
		{
			return getName();
		}
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
	public ArrayList<ServiceOperationSrv> getOperations() {
		return operations;
	}
	public void setOperations(ArrayList<ServiceOperationSrv> operations) {
		this.operations = operations;
	}
		
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	public AuthenticationProtocolSrv getAuthData() {
		return authData;
	}

	public void setAuthData(AuthenticationProtocolSrv authData) {
		this.authData = authData;
	}
	
	
	
}
