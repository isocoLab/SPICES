package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RatingsResponseCli implements IsSerializable {

   	public double average;
   	public int ratings;

    public RatingsResponseCli() {
    }
   	
   	public RatingsResponseCli(double av, int rat) {
   		average = av;
   		ratings = rat;
	}

}
