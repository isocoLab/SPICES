package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.List;
import java.util.Map;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface ConsumptionClientServiceAsync {
    
    void getChilds(String id, AsyncCallback<List<String[]>> callback);
    void searchString(String query, AsyncCallback<List<String[]>> callback);
    void searchCategory(String category, AsyncCallback<List<String[]>> callback);
    void getService(String idService, String permanentSessionId, AsyncCallback<ServiceAnnotations> callback);
    void executeRestService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId, AsyncCallback<Map<String,String>> callback);
    void executeWsdlService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId, AsyncCallback<Map<String,String>> callback);
    void getInstancesOfConcept(String id, AsyncCallback<List<String[]>> callback);
    void interactRestServiceByUrl (String url, String method, String data, AsyncCallback<String> callback);
    void interactServiceOAuth(ServiceAnnotations service, String operation, Map<String,String> params, String permanentSessionId, AsyncCallback<Map<String,String>> callback);
}
