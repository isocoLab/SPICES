/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.google.gwt.user.client.ui.Composite;

/**
 * TODO: Implement and integrate the Wizard functionality
 * 
 */
public class WidgetWizard extends Composite {
	
	final Portlet portlet = new Portlet();
	//final TreevisualisationWidget tvw = new TreevisualisationWidget();
	
	/**
	 * The widget Wizard enables the help wizard
	 */
	public WidgetWizard()
	{
	
	    final Dialog simple = new Dialog();  
	    simple.setHeading("SPICES Wizard");  
	    simple.setButtons(Dialog.OK);  
	    simple.setBodyStyleName("pad-text");
	    String text = "Welcome to <b>SPICES</b>, the <b>Semantic Platform for the Interaction " +
	    		"and Consumption of Enriched Services</b>, where you can interact with " +
	    		"(semantically annotated) services (both WSDL and RESTful) and invoke them in a lightweight manner. " +
	    		"If you want to learn more about this platform, please visit the " +
	    		"<a href=\"http://soa4all.isoco.net/spices/about\" target=\"_blank\">about page</a>." +
	    		"<br/><br/>" +
	    		"To begin using SPICES, you can find some services by using the search box on the left. " +
	    		"Services are retrieved from " +
	    		"<a href=\"http://iserve.kmi.open.ac.uk/\" target=\"_blank\">iServe</a>, " +
	    		"a repository for semantic annotations on services." +
	    		"<br/><br/>" +
	    		"Tip: Search for \"lastfm\" for some Last.fm services. Then you can select for example the Events " +
	    		"service, which will be opened in a new portlet." +
	    		"<br/><br/>" +
	    		"Once you select a particular service and open it, it can be invoked by filling " +
	    		"the form (in the \"Events\" example, the latitude and the longitude information) " +
	    		"and clicking on the execute button.";
	    simple.addText(text);  
	    simple.setScrollMode(Scroll.AUTO);  
	    simple.setHideOnButtonClick(true);
	    simple.setWidth(500);
	    
	    ButtonBar buttonBar = new ButtonBar();  
	    buttonBar.add(new Button("SPICES Help", new SelectionListener<ButtonEvent>() {  
	      public void componentSelected(ButtonEvent ce) {  
	        simple.show();  
	      }  
	    }));
	    
	    buttonBar.addListener(Events.Render, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent event) {
    	        simple.show();  
            }
        });
	    
    	initWidget(buttonBar);
	}
}
