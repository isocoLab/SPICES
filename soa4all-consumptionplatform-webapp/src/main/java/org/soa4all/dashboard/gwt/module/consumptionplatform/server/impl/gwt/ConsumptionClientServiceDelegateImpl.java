package org.soa4all.dashboard.gwt.module.consumptionplatform.server.impl.gwt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.abdera.i18n.templates.HashMapContext;
import org.soa4all.dashboard.consumptionplatform.service.ConsumptionService;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.InputMessageSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.OutputMessageSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceOperationSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.AuthenticationProtocolSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LiftingSchemaSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LoweringSchemaSrv;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.AuthenticationProtocol;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.InputMessage;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.OutputMessage;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceOperation;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.LiftingSchema;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.LoweringSchema;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConsumptionClientServiceDelegateImpl extends RemoteServiceServlet implements ConsumptionClientService {

    private ConsumptionService consumptionService;

    public ConsumptionService getConsumptionService() {
    	return consumptionService;
    }
    
    // Configurator configurator = new Configurator();
    public void Init() {
	// Configurator.Init();
    }

    public void setConsumptionService(ConsumptionService consumptionService) {
    	this.consumptionService = consumptionService;
    }

    public List<String[]> getChilds(String uri) {
    	return getConsumptionService().getChilds(uri);
    }

    public ServiceAnnotations getService(String idService, String permanentSessionId) {
    	ServiceAnnotationsSrv service = getConsumptionService().getService(idService, permanentSessionId);
    	return doServiceModelIndirectCast(service);
    }

    public List<String[]> searchString(String query) {
    	return getConsumptionService().searchString(query);
    }

    public List<String[]> searchCategory(String query) {
    	return getConsumptionService().searchCategory(query);
    }

    public List<String[]> getInstancesOfConcept(String uri) {
    	return getConsumptionService().getInstancesOfConcept(uri);
    }

	public Map<String,String> executeRestService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId) {
		return getConsumptionService().executeRestService(doServiceModelDirectCast(service), params, operation, permanentSessionId);
	}
	
	public Map<String,String> executeWsdlService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId) {
		return getConsumptionService().executeWsdlService(doServiceModelDirectCast(service), params, operation, permanentSessionId);
	}
	
	public Map<String,String> interactServiceOAuth(ServiceAnnotations service, String operation, Map<String,String> params, String permanentSessionId){
		return getConsumptionService().interactServiceOAuth(doServiceModelDirectCast(service), operation, params, permanentSessionId);
	}
	
	public String interactRestServiceByUrl(String url, String method, String data) {
		return getConsumptionService().interactRestServiceByUrl(url, method, data);
	}

	private ServiceAnnotationsSrv doServiceModelDirectCast (ServiceAnnotations service) {
		ServiceAnnotationsSrv serviceSrv = null;
		if (service!=null) {
			serviceSrv = new ServiceAnnotationsSrv();
			serviceSrv.setIdService(service.getIdService());
			serviceSrv.setName(service.getName());
			serviceSrv.setUri(service.getUri());
    		Iterator itOps = service.getOperations().iterator();
    		while (itOps.hasNext()) {
    			
    			ServiceOperation operation = (ServiceOperation) itOps.next();    			
    			ServiceOperationSrv operationData = new ServiceOperationSrv(operation.getName(), operation.getUri(), operation.getMethod(), operation.getAddress());
    			Iterator itInputs = operation.getInputs().iterator();
    			if (!operation.getInputs().isEmpty()) {
	    			while (itInputs.hasNext()) {	    				
	    				InputMessage input = (InputMessage) itInputs.next();	    				
	    				operationData.addInput(input.getUri());	    				
	    			}
    			}
    			if (!operation.getOutputs().isEmpty()) {
    				Iterator itOutputs = operation.getOutputs().iterator();
	    			while (itOutputs.hasNext()) {
	    				OutputMessage output = (OutputMessage) itOutputs.next();	    				
	    				operationData.addOutput(output.getUri());
	    			}
    			}
    			if (operation.getLowering()!=null) {
    				operationData.setLowering(new LoweringSchemaSrv(operation.getLowering().getSchemaReference()));
    			}
    			if (operation.getLifting()!=null) {
    				operationData.setLifting(new LiftingSchemaSrv(operation.getLifting().getSchemaReference()));
    			}
    			serviceSrv.addOperation(operationData);
    		}
  
    	}
		return serviceSrv;
	}
	
	private ServiceAnnotations doServiceModelIndirectCast (ServiceAnnotationsSrv serviceSrv) {
		ServiceAnnotations service = null;
		if (serviceSrv!=null) {
			service = new ServiceAnnotations();			
			service.setIdService(serviceSrv.getIdService());
			service.setName(serviceSrv.getName());
			service.setUri(serviceSrv.getUri());
			if (serviceSrv.getAuthData()!=null) {
				AuthenticationProtocolSrv auth = serviceSrv.getAuthData();
				service.setAuthData(new AuthenticationProtocol(auth.getType(), auth.getValueKey(), auth.getLocation()));
			}
    		Iterator itOps = serviceSrv.getOperations().iterator();
    		while (itOps.hasNext()) {
    			
    			ServiceOperationSrv operation = (ServiceOperationSrv) itOps.next();    			
    			ServiceOperation operationData = new ServiceOperation(operation.getName(), operation.getUri(), operation.getMethod(), operation.getAddress());
    			Iterator itInputs = operation.getInputs().iterator();
    			if (!operation.getInputs().isEmpty()) {
	    			while (itInputs.hasNext()) {	    				
	    				InputMessageSrv input = (InputMessageSrv) itInputs.next();	    				
	    				operationData.addInput(input.getUri());
	    			}
    			}
    			if (!operation.getOutputs().isEmpty()) {
    				Iterator itOutputs = operation.getOutputs().iterator();
	    			while (itOutputs.hasNext()) {
	    				OutputMessageSrv output = (OutputMessageSrv) itOutputs.next();	    				
	    				operationData.addOutput(output.getUri());
	    			}
    			}
    			if (operation.getLowering()!=null) {
    				operationData.setLowering(new LoweringSchema(operation.getLowering().getSchemaReference()));
    			}
    			if (operation.getLifting()!=null) {
    				operationData.setLifting(new LiftingSchema(operation.getLifting().getSchemaReference()));
    			}
    			service.addOperation(operationData);
    		}
  
    	}
		System.out.println(" ------------------- URI TEMPLATE: " + service.getUri());
		return service;
	}

}

