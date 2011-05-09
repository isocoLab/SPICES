/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
 *  http://www.tieGlobal.com
 *  info@tieGlobal.com

 *All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
 *The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientServiceAsync;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * Class : widgetRecommendedGoals
 * 
 */
public class WidgetRecommendedGoals extends Composite {

	private String userId;

	final static String RECOMMENDATION_SERVICE_BASE = "http://etechdemo.cefriel.it/rs-component-service/runtime/";

    /**
     * The widget Recommended Goals displays the relevant recommendations
     */
    public WidgetRecommendedGoals(final String userId)
    {
    	Listener<ComponentEvent> l = new Listener<ComponentEvent>() {
    		public void handleEvent(ComponentEvent ce) {
    			DataList l = (DataList) ce.component;
    			ServiceVisualizerManager.displayService(l.getSelectedItem().getId());
    		}
    	};
    	ContentPanel frame = new ContentPanel();
    	frame.setFrame(true);
    	frame.setCollapsible(true);
    	frame.setAnimCollapse(false);
    	frame.setHeading("Recommended services");

    	final DataList list = new DataList();
    	list.setFlatStyle(true);

    	list.addListener(Events.SelectionChange, l);

		// Loading suggestions for this user (Integration by RESTful service)
		//-------------------------------------------------------------------------------------------
    	
		// Recommendation System callback: Retrieve suggestions callback: 
	  	AsyncCallback<String> getRecommendationsCallback = new AsyncCallback<String>()
	  	{
		    public void onFailure(Throwable caught)
		    {
		    	throw new RuntimeException("Error retrieving Recommendations");
		    }
		    public void onSuccess(String result)
		    {
		    	try {
		    		Document customerDom = XMLParser.parse(result);
    			    Element customerElement = customerDom.getDocumentElement();
    			    XMLParser.removeWhitespace(customerElement);
		    		if (result.contains("error"))	{
    		    		MessageBox.alert("Error retrieving Recommendations", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
    		    	} else {
        		    	InfoConfig infoConfig = new InfoConfig("Info", "Suggestions retrieved");
        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
        		    	Info.display(infoConfig);
		    			NodeList recommendedServices = customerElement.getElementsByTagName("recommendedService");
		    			if(recommendedServices.getLength()<1){
    			    		System.out.println("No recommendations for user "+userId);
    			    		DataListItem dli = new DataListItem();
    						dli.setText("No recommendation"); 
    						dli.setEnabled(false);
    						list.removeAllListeners();
    						list.add(dli);
		    			} else {
			            	for (int i = 0; i < recommendedServices.getLength(); ++i) {
			            		Node node = recommendedServices.item(i);
			            		Element element = (Element) node;
		                    	String service = element.getElementsByTagName("service").item(0).getFirstChild().getNodeValue();
		                    	String proof = element.getElementsByTagName("proof").item(0).getFirstChild().getNodeValue();
		                    	String utility = element.getElementsByTagName("utility").item(0).getFirstChild().getNodeValue();
	    		    		
	    			    		final DataListItem dli = new DataListItem();
	    						dli.setText(ServiceVisualizerManager.getTrimmedName(service));
	    						dli.setId(service);
	    						dli.setEnabled(true);
    							double weight = Double.parseDouble(utility) * 10;
    							if (weight>85)
    								dli.setStyleAttribute("color", "#00CC00");
    							else if (weight>50)
    								dli.setStyleAttribute("color", "#008800");
    							else
    								dli.setStyleAttribute("color", "#003300");
    							NumberFormat nf = NumberFormat.getPercentFormat();
    							dli.setToolTip("Confidence of " + nf.format(weight/100) 
    									+ "<br/>" + proof);
	    						list.add(dli);
	    			    	}
						}
    		    	}
    		    } catch (Exception e) {
		    		MessageBox.alert("Error retrieving Recommendations", e.getMessage(), null);	
    	    	    e.printStackTrace();
    		    } 
		    }
		};

		// Recommendation Service invocation (per user)
		final String url = RECOMMENDATION_SERVICE_BASE + "by-user?num=3&user=" 
			+ URL.encodeComponent("http://profile.soa4all.org#"+userId);	
	    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url, "GET", null, getRecommendationsCallback);
    	
		frame.setLayout(new FitLayout());
		frame.add(list);

		initWidget(frame);
   	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
