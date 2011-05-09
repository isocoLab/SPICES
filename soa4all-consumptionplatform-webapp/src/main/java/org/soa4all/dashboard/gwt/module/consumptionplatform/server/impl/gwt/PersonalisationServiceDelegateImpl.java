package org.soa4all.dashboard.gwt.module.consumptionplatform.server.impl.gwt;

import java.util.Map;

import org.soa4all.dashboard.consumptionplatform.service.PersonalisationService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.PersonalisationClientService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PersonalisationServiceDelegateImpl extends RemoteServiceServlet
		implements PersonalisationClientService {
	
	private PersonalisationService personalisationService;
	@Override
	public Map<String, String> getRecommendedServiceInput(
			Map<String, String> service, String opLabel, String openID) {
		return this.personalisationService.getRecommendedServiceInput(service, opLabel, openID);
	}
	public PersonalisationService getPersonalisationService() {
		return personalisationService;
	}
	public void setPersonalisationService(
			PersonalisationService personalisationService) {
		this.personalisationService = personalisationService;
	}
	
	
	
}
