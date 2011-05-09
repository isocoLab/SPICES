package org.soa4all.dashboard.gwt.module.consumptionplatform.server.impl.gwt;

import org.soa4all.dashboard.consumptionplatform.service.ConsumptionService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.GoalService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side delegate implementation of service. This IS intended to be
 * instantiated by spring app context so the business services would be wired.
 * 
 * @author Manuel J. Gallego <manuel.gallego@tieglobal.com>
 */
public class GoalServiceDelegateImpl extends RemoteServiceServlet implements GoalService {

    private ConsumptionService consumptionService;

    public ConsumptionService getConsumptionService() {
	return consumptionService;
    }

    public void setConsumptionService(ConsumptionService consumptionService) {
	this.consumptionService = consumptionService;
    }

    public String[] getGoal(String id) {
	return getConsumptionService().getGoal(id);
    }

}
