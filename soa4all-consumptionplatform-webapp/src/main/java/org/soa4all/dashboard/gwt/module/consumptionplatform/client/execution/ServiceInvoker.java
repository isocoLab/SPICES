package org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.Cookies;

public abstract class ServiceInvoker {
	
	public abstract void executeService (final TabPanel tabPanel, ServiceAnnotations service, Map<String, String> params, String operation);	

	public static String getCookie() {
		String cookie = Cookies.getCookie("persistentSessionID");
	    if(cookie==null){
	        Date today = new Date();
	        Timestamp ts = new Timestamp(today.getTime());
	        long tsTime = ts.getTime();
	    	String timestamp = Long.toString(tsTime);
	    	int random = ((int) Math.random() * 1000);
	    	cookie = timestamp+random;
	    	Cookies.setCookie("persistentSessionID",cookie,null);	
	    }
	    return cookie;
	}
}
