package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.Map;

import org.soa4all.dashboard.consumptionplatform.service.impl.ConsumptionServiceImpl;
import org.soa4all.dashboard.consumptionplatform.service.impl.LiLoManager;
import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.adapter.OutputAdapter;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution.RestInvoker;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ConceptualConstants;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.MessageBox.MessageBoxType;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.*;

public class AuthenticationManager {
	
	final static String AUTH_SERVICE_BASE = "http://soa4all.isoco.net/authentication/";
	
	public static void checkCredentialsForService (final TabPanel tabPanel, final ServiceAnnotations service, final String operation, final Map<String, String> params) {
	 
		//TODO USE SERVICE INFO RELATED TO AUTH TYPE
		int authType = 0;
		if (service.getAuthData()==null) {
			//Service without authentication
			invokeService(tabPanel, service, operation, params);
			
		} else {
		    
			if (service.getAuthData()!=null) {
				if (service.getAuthData().getType().equals(ConceptualConstants.APIKEY)) {
					authType = 1;
				} else if (service.getAuthData().getType().equals(ConceptualConstants.OAUTH)) {
					authType = 2;
				}
			}	
					
			final String url = AUTH_SERVICE_BASE + URL.encodeComponent(URL.encodeComponent(service.getUri())) + "/" + authType + "/";	
			AsyncCallback<String> callback = new AsyncCallback<String>() {
			    public void onFailure(Throwable caught) {
			    }
			    public void onSuccess(String result) {
			    	try {
	    		    	if (!result.contains("credentials"))	{
	    		    		introduceCredentials(tabPanel, service, operation, params, url);		
	    		    	} else {
	    			    	InfoConfig infoConfig = new InfoConfig("MessageBox", "Service " + service.getName() + "has credentials stored ... ");
	    			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    			    	Info.display(infoConfig);
	    		    		try {
	    		    			Document customerDom = XMLParser.parse(result);
	    		    		    Element customerElement = customerDom.getDocumentElement();
	    		    		    XMLParser.removeWhitespace(customerElement);
	    		    		    //APIKey 
	    		    		    if (result.contains("apikey")) {
	    		    		    	params.put(ConceptualConstants.APIKEY, customerElement.getElementsByTagName("apikey").item(0).getFirstChild().getNodeValue());
	    		    		    	invokeService(tabPanel, service, operation, params);
	    		    		    } else { //OAuth
	    		    		    	params.put(ConceptualConstants.REQUEST_URL, customerElement.getElementsByTagName(ConceptualConstants.REQUEST_URL).item(0).getFirstChild().getNodeValue());
	    		    		    	params.put(ConceptualConstants.ACCESS_URL, customerElement.getElementsByTagName(ConceptualConstants.ACCESS_URL).item(0).getFirstChild().getNodeValue());
	    		    		    	params.put(ConceptualConstants.AUTHORIZATION_URL, customerElement.getElementsByTagName(ConceptualConstants.AUTHORIZATION_URL).item(0).getFirstChild().getNodeValue());
	    		    		    	params.put(ConceptualConstants.CONSUMER_KEY, customerElement.getElementsByTagName(ConceptualConstants.CONSUMER_KEY).item(0).getFirstChild().getNodeValue());
	    		    		    	params.put(ConceptualConstants.CONSUMER_SECRET, customerElement.getElementsByTagName(ConceptualConstants.CONSUMER_SECRET).item(0).getFirstChild().getNodeValue());
	    		    		    	invokeServiceOAuth(tabPanel, service, operation, params);	    		    		    	
	    		    		    }
	    		    		} catch (Exception e) {
	    		    	        e.printStackTrace();
	    		    		}     		    			
	    		    	} 	
	    		    			    		    	
	    		    } catch (Exception e) {
	    	    	    e.printStackTrace();
	    		    } 
			    	
			    }			
			};		
		    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url, "GET", "", callback);
		} 
	}
	
	private static void introduceCredentials(final TabPanel tabPanel, final ServiceAnnotations service, final String operation, final Map<String, String> params, final String url) {
		
		MessageBox box = new MessageBox();
		box.setMaxWidth(1800);
		box.setType(MessageBoxType.PROMPT);
	    box.setButtons(Dialog.OKCANCEL);
		box.setTitle("Service Credentials for " + service.getName());
		if (url.endsWith("/1/")) {
			box.setMessage("Please enter APIKEY credentials: ");			
		} else {
			box.setMessage("Please enter OAUTH credentials in the format: [request_url; authorization_url; access_url; oauth_consumer_key; oauth_consumer_secret] \n\n");						
		}
		box.show();		  
		box.addCallback(new Listener<MessageBoxEvent>() {  
		     public void handleEvent(MessageBoxEvent be) {  		         
		    	 if (be.buttonClicked.getText().equalsIgnoreCase("ok")) {
		    		 saveCredentials(tabPanel, service, operation, params, url, be.value);		    		 		    		 
		    	 }
		    	 InfoConfig infoConfig = new InfoConfig("MessageBox", "You entered '{0}'", new Params(be.value));
		    	 infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    	 Info.display(infoConfig);
		     }
					
		});
	}
	
	private static void invokeService(final TabPanel tabPanel, final ServiceAnnotations service, final String operation, final Map<String, String> params) {

		final ContentPanel panel = new ContentPanel();
		final ProgressBar pb = new ProgressBar();
		final TabItem item = new TabItem("Execution");
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		
		RestInvoker.invokeService(tabPanel, service, params, operation, panel, pb, item);
		
	}  
	
	private static void saveCredentials(final TabPanel tabPanel, final ServiceAnnotations service, final String operation, final Map<String, String> params, String url, final String data) {
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
		    public void onFailure(Throwable caught) {
		    }
		    public void onSuccess(String result) {
		    	try {
		    		Document customerDom = XMLParser.parse(result);
    			    Element customerElement = customerDom.getDocumentElement();
    			    XMLParser.removeWhitespace(customerElement);
    			    
		    		if (result.contains("error"))	{
    		    		MessageBox.alert("Credentials Error", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
    		    	} else {
    			    	InfoConfig infoConfig = new InfoConfig("MessageBox", "Service " + service.getName() + "has credentials stored ... ");
    			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    			    	Info.display(infoConfig);
    		    		if (result.contains("apikey")) {    		    			    		    			    
		    		    	params.put(ConceptualConstants.APIKEY, customerElement.getElementsByTagName("result").item(0).getFirstChild().getNodeValue());
		    		    	invokeService(tabPanel, service, operation, params);
    		    		} else if (result.contains("oauth")) {
    		    			params.put(ConceptualConstants.REQUEST_URL, customerElement.getElementsByTagName(ConceptualConstants.REQUEST_URL).item(0).getFirstChild().getNodeValue());
		    		    	params.put(ConceptualConstants.ACCESS_URL, customerElement.getElementsByTagName(ConceptualConstants.ACCESS_URL).item(0).getFirstChild().getNodeValue());
		    		    	params.put(ConceptualConstants.AUTHORIZATION_URL, customerElement.getElementsByTagName(ConceptualConstants.AUTHORIZATION_URL).item(0).getFirstChild().getNodeValue());
		    		    	params.put(ConceptualConstants.CONSUMER_KEY, customerElement.getElementsByTagName(ConceptualConstants.CONSUMER_KEY).item(0).getFirstChild().getNodeValue());
		    		    	params.put(ConceptualConstants.CONSUMER_SECRET, customerElement.getElementsByTagName(ConceptualConstants.CONSUMER_SECRET).item(0).getFirstChild().getNodeValue());
		    		    	invokeServiceOAuth(tabPanel, service, operation, params);
    		    		} else {
    		    			MessageBox.alert("Credentials Error", "Error storing the service credentials ... ", null);	
    		    		}
    		    		
    		    	}
    		    } catch (Exception e) {
    	    	    e.printStackTrace();
    		    } 
		    	
		    }			
		};		
		String postData = "data="+URL.encodeComponent(data);
	    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url, "POST", postData, callback);
		
	}

	/**
     * It performs the invocation of a service with OAuth (the whole OAuth message protocol)
     * and gets the response.
     * 
     * @param serviceId
     * @param userId
     * @param params
     * @return
     */
    public static void invokeServiceOAuth(final TabPanel tabPanel, ServiceAnnotations service, String operation, Map<String,String> params) {
    	
    	final ContentPanel panel = new ContentPanel();
		final ProgressBar pb = new ProgressBar();
		final TabItem item = new TabItem("Execution");
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		
		RestInvoker.invokeServiceWithAoauth(tabPanel, service, params, operation, panel, pb, item);
		 
    }

}
