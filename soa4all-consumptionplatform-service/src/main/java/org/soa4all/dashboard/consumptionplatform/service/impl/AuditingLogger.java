package org.soa4all.dashboard.consumptionplatform.service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class AuditingLogger implements Runnable {

	public NameValuePair[] actionData;
    private final static Logger log = Logger.getLogger(AuditingLogger.class);
	public static final String AUDITING_ENDPOINT = "http://soa4all.isoco.net/auditing/action";

    public void run() {
    	log.setLevel(Level.DEBUG);
    	
    	String requestURL = AUDITING_ENDPOINT;
        PostMethod postMtd = new PostMethod(requestURL);
    	
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setConnectionManagerTimeout(500); // 
        
        log.debug("Logging action...");
        try {
            postMtd.setRequestBody(actionData);
            int result = httpclient.executeMethod(postMtd);
            log.debug("Response status code: " + result);
            if (result != 200) {
            	log.error("Error message: ");
            	log.error(postMtd.getResponseHeader("Error"));
            }
            else {
            	log.debug("Response OK");
            	log.debug(postMtd.getResponseBodyAsString());
            }
		} catch (Exception e) {
			e.printStackTrace();
        } finally {
            postMtd.releaseConnection();
        }
    }

    public AuditingLogger (NameValuePair[] data) {
    	actionData = data; 
    }

}