package org.soa4all.dashboard.consumptionplatform.service;

import java.util.Map;

public interface PersonalisationService {
	public Map<String, String> getRecommendedServiceInput(
			Map<String, String> service, String opLabel, String openID) ;
}
