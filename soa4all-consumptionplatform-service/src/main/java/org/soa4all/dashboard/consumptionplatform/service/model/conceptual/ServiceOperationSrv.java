package org.soa4all.dashboard.consumptionplatform.service.model.conceptual;

import java.util.ArrayList;

public class ServiceOperationSrv  {
	
// ServiceOperationSrv properties
	String name = null;
	String uri = null;
	String method = null;
	String address = null;
	ArrayList<InputMessageSrv> inputs = new ArrayList<InputMessageSrv>();
	ArrayList<OutputMessageSrv> outputs = new ArrayList<OutputMessageSrv>();
	LoweringSchemaSrv lowering = null;
	LiftingSchemaSrv lifting = null;
	
	public ServiceOperationSrv(){
		
	}
	public ServiceOperationSrv(String name, String uri, String method, String address) {
		this.name = name;
		this.uri = uri;
		this.method = method;
		this.address = address;
		
	}
	
	// Utility methods
	public void addInput (String uri){
		this.inputs.add(new InputMessageSrv(uri));
	}
	
	public void addOutput (String uri){
		this.outputs.add(new OutputMessageSrv(uri));
	}
	
	/*
	 * Return the label for this method,
	 * parsing its name and just returning the REAL simple operation name 
	 */
	public String getNameLabel()
	{
		try
		{
			// TODO: Probably fix this:
			return getName().substring(getName().lastIndexOf("/")+1, getName().lastIndexOf(")"));
		}
		// In case of error, I just return the complex operation name
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
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ArrayList<InputMessageSrv> getInputs() {
		return inputs;
	}
	public void setInputs(ArrayList<InputMessageSrv> inputs) {
		this.inputs = inputs;
	}
	public ArrayList<OutputMessageSrv> getOutputs() {
		return outputs;
	}
	public void setOutputs(ArrayList<OutputMessageSrv> outputs) {
		this.outputs = outputs;
	}
	public LoweringSchemaSrv getLowering() {
		return lowering;
	}
	public void setLowering(LoweringSchemaSrv lowering) {
		this.lowering = lowering;
	}
	public LiftingSchemaSrv getLifting() {
		return lifting;
	}
	public void setLifting(LiftingSchemaSrv lifting) {
		this.lifting = lifting;
	}
	
}
