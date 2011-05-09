/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
//import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author GDiMatteo
 *
 */
//@RemoteServiceRelativePath("RecommendClientService")
public interface RecommendClientService extends RemoteService
{
//	public List<RecommendedServiceCli> getRecommendationByUser(String userId, int n);
//	public List<RecommendedServiceCli> getRecommendationByService(String serviceId, int n);
//	public List<RecommendedServiceCli> getRecommendationByUserAndService(String userId, String serviceId, int n);
	// Administration functionalities
	public int start();
	public int reset();
	public List<String> importLogs(Date start, Date end);
	public Date getLastLogEntryDateAdded();
}
