/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author GDiMatteo
 *
 */
public interface RecommendClientServiceAsync {
//	public void getRecommendationByUser(String userId, int n, AsyncCallback<List<RecommendedServiceCli>> callback);
//	public void getRecommendationByService(String serviceId, int n, AsyncCallback<List<RecommendedServiceCli>> callback);
//	public void getRecommendationByUserAndService(String userId, String serviceId, int n, AsyncCallback<List<RecommendedServiceCli>> callback);
//	// Administration functionalities
//	public void resetDb(AsyncCallback<Boolean> callback);
//	public void importLogs(Date start, Date end, AsyncCallback<Boolean> callback);
	public void start(AsyncCallback<Integer> callback);
	public void reset(AsyncCallback<Integer> callback);
	public void importLogs(Date start, Date end, AsyncCallback<List<String>> callback);
	public void getLastLogEntryDateAdded(AsyncCallback<Date> callback);
}
