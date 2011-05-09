package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TagResponseCli implements IsSerializable {
	// TODO: Decide kind of response is intended when getting tags:
	//			Tags / Tag-user / Tag-frequency ? (not clear yet)
    
	public TagResponseCli() {
    }
   	
	public TagResponseCli(String t, int f) {
		tag = t;
		frequency = f;
		//user = u;
	}
	public String tag;
	public int frequency;
	//public String user;
}