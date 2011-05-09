package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface PersonalisationClientService extends RemoteService{
	public Map<String, String> getRecommendedServiceInput(
			Map<String, String> service, String opLabel, String openID);
}
