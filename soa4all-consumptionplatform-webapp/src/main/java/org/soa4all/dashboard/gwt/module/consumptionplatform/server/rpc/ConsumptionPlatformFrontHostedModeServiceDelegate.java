package org.soa4all.dashboard.gwt.module.consumptionplatform.server.rpc;

import org.soa4all.dashboard.gwt.server.rpc.AbstractFrontHostedModeServiceDelegate;

public class ConsumptionPlatformFrontHostedModeServiceDelegate extends AbstractFrontHostedModeServiceDelegate {

    @Override
    public String getClassPathXmlApplicationContextDefinition() {
	return "classpath:soa4all_dashboard_gwt_module_consumptionplatform_applicationContext.xml";
    }

}
