package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CommentCli implements IsSerializable {

    public CommentCli() {
    }
   	
	// TODO: Date?
    public CommentCli(String t, String u) {
    	text = t;
    	user = u;
	}
    public String text;
    public String user;
}