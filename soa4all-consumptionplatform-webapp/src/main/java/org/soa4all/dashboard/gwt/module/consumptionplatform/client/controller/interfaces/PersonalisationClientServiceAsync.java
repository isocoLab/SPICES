package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonalisationClientServiceAsync {
	public void getRecommendedServiceInput(
			Map<String, String> service, String opLabel, String openID,AsyncCallback<Map<String, String>> callBack);
}
