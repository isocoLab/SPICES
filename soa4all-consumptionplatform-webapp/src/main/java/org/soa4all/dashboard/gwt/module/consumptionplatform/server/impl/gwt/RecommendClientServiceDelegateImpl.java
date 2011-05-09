/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.server.impl.gwt;

import org.soa4all.dashboard.recommendersystem.server.RecommendedService;
import org.soa4all.dashboard.recommendersystem.server.RecommService;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.RecommendClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.RecommendedServiceCli;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author GDiMatteo
 *
 */
public class RecommendClientServiceDelegateImpl extends RemoteServiceServlet implements RecommendClientService {
	private RecommService recommService;

    public RecommService getRecommService() {
	return recommService;
    }

    public void setRecommService(RecommService recommService) {
	this.recommService = recommService;
    }

	public Date getLastLogEntryDateAdded() {
		return getRecommService().getLastLogEntryDateAdded();
	}

	public List<String> importLogs(Date start, Date end) {
		return getRecommService().importLogs(start, end);
	}

	public int reset() {
		return getRecommService().reset();
	}

	public int start() {
		return getRecommService().start();
	}



}
