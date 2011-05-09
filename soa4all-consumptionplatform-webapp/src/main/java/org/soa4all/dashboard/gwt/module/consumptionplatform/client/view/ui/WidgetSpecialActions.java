/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
 *  http://www.tieGlobal.com
 *  info@tieGlobal.com

 *All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
 *The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Composite;

/**
 * Class : widget Special Actions
 * 
 */
public class WidgetSpecialActions extends Composite {

    /**
     * This widget can be used to display the actions that are considered relevant  
     */
    public WidgetSpecialActions()
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
    	frame.setHeading("Interesting actions");

    	final DataList list = new DataList();
    	list.setFlatStyle(true);

    	list.addListener(Events.SelectionChange, l);

    	DataListItem item = new DataListItem();  
        item.setText("Get Twitter followers");  
        item.setId("http://soa4all.eu/twitter/getFollowers");  
        list.add(item);  
        
        item = new DataListItem();  
        item.setText("Watson Search");  
        item.setId("http://soa4all.eu/watson/search");  
        list.add(item);  
        
        item = new DataListItem();  
        item.setText("BT Scenario 1");  
        item.setStyleAttribute("color", "#00FF00");  
        item.setId("http://soa4all.eu/composed/BTscenario1");  
        list.add(item); 
        
        item = new DataListItem();
        item.setText("Get Twitter Follower (Semantic)");
        item.setId("http://soa4all.eu/twitter/getFollowersSemantic");  
        list.add(item);  
        
        item = new DataListItem();
        item.setText("Profile Details (Fake Service)");
        item.setId("http://soa4all.eu/twitter/getProfileDetails");  
        list.add(item);  
        
        
		frame.setLayout(new FitLayout());
		frame.add(list);

		initWidget(frame);
   	}

}
