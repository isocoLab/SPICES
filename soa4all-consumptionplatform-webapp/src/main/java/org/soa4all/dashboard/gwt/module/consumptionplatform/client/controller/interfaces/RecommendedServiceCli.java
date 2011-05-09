/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author GDiMatteo
 *
 */
public class RecommendedServiceCli implements IsSerializable
{
	public String serviceId;
	public double weight;
	
	public RecommendedServiceCli(){
		
	}
	
	public RecommendedServiceCli(String serviceId, double weight) {
		this.serviceId = serviceId;
		this.weight = weight;
	}

}
