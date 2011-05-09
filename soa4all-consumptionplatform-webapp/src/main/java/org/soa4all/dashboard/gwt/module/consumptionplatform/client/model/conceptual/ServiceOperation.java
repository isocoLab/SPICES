package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;


public class ServiceOperation extends BaseModel implements IsSerializable {
	
	// ServiceOperationSrv properties
	String name = null;
	String uri = null;
	String method = null;
	String address = null;
	ArrayList<InputMessage> inputs = new ArrayList<InputMessage>();
	ArrayList<OutputMessage> outputs = new ArrayList<OutputMessage>();
	LoweringSchema lowering = null;
	LiftingSchema lifting = null;
	
	
	public ServiceOperation(){
		
	}
	
	public ServiceOperation(String name, String uri, String method, String address) {
		this.name = name;
		this.uri = uri;
		this.method = method;
		this.address = address;
		
	}
	// Utility methods
	public void addInput (String uri){
		this.inputs.add(new InputMessage(uri));
	}
	
	public void addOutput (String uri){
		this.outputs.add(new OutputMessage(uri));
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
	public ArrayList<InputMessage> getInputs() {
		return inputs;
	}
	public void setInputs(ArrayList<InputMessage> inputs) {
		this.inputs = inputs;
	}
	public ArrayList<OutputMessage> getOutputs() {
		return outputs;
	}
	public void setOutputs(ArrayList<OutputMessage> outputs) {
		this.outputs = outputs;
	}
	public LoweringSchema getLowering() {
		return lowering;
	}
	public void setLowering(LoweringSchema lowering) {
		this.lowering = lowering;
	}
	public LiftingSchema getLifting() {
		return lifting;
	}
	public void setLifting(LiftingSchema lifting) {
		this.lifting = lifting;
	}
	
}
