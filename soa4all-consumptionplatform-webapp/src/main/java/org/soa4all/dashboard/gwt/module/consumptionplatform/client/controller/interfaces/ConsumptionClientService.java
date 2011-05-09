package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ConsumptionClientService")
public interface ConsumptionClientService extends RemoteService {

    List<String[]> getChilds(String id);

    List<String[]> searchString(String query);

    List<String[]> searchCategory(String category);

    ServiceAnnotations getService(String idService, String permanentSessionId);

    Map<String,String> executeRestService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId);
    
    Map<String, String> executeWsdlService(ServiceAnnotations service, Map<String,String> params, String operation, String permanentSessionId);    

    List<String[]> getInstancesOfConcept(String id);
    
    String interactRestServiceByUrl(String url, String method, String data);
    
    Map<String,String> interactServiceOAuth(ServiceAnnotations service, String operation, Map<String,String> params, String permanentSessionId);

}