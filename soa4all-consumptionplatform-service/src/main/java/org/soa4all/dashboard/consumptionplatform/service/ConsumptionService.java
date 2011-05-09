package org.soa4all.dashboard.consumptionplatform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;


public interface ConsumptionService {

	public List<String[]> getChilds(String uri);
    public List<String[]> searchString(String query);
    public List<String[]> searchCategory(String category);
    public String[] getGoal(String id);
    public ServiceAnnotationsSrv getService(String idService, String permanentSessionId);
    public Map<String,String> executeRestService(ServiceAnnotationsSrv service, Map<String,String> params, String operation, String permanentSessionId);
    public Map<String,String> executeWsdlService(ServiceAnnotationsSrv service, Map<String,String> params, String operation, String permanentSessionId);
	public List<String[]> getInstancesOfConcept(String uri);
	public String interactRestServiceByUrl(String url, String method, String data);
	public Map<String,String> interactServiceOAuth(ServiceAnnotationsSrv service, String operation, Map<String,String> params, String permanentSessionId);

}