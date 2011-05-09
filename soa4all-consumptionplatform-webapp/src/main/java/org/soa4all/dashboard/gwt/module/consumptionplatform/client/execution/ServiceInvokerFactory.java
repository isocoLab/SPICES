package org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution;


public class ServiceInvokerFactory {

	static final int WSDL_SERVICE = 0;
	static final int REST_SERVICE = 1;
	static final int COMPOSE_SERVICE = 2;
	
	public static ServiceInvoker getInvoker(int serviceType){
        
    	if (serviceType == WSDL_SERVICE){
            return new WsdlInvoker();
        } else if (serviceType == REST_SERVICE){
            return new RestInvoker();
        } else if (serviceType == COMPOSE_SERVICE){
            return new ServiceComposedInvoker(); 
        } else {
        	return null;
        }	
    }


}
