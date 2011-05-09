/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;



import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.ui.Composite;
/**
 * Class : widgetSearch
 * 
 */
public class WidgetSearch extends Composite {
	
	/**
	 * The widget Search finds Goals that suit the query
	 */
	public WidgetSearch() {
		
     FormPanel form2 = new FormPanel();  
     form2.setFrame(true);  
     form2.setHeading("Find services...");  
     form2.setLayout(new FlowLayout()); 
		
     final TextArea textarea = new TextArea();
     textarea.setFieldLabel("searchBox"); 

     textarea.setWidth("170");
     textarea.setPreventScrollbars(true);
     
     form2.add(textarea);  
     form2.setButtonAlign(HorizontalAlignment.CENTER);  
      
     Button bsearch = new Button("Search");
     bsearch.addSelectionListener(new SelectionListener<ComponentEvent>(){
         @Override  
         public void componentSelected(ComponentEvent ce) { 
			  ServiceVisualizerManager.searchString(textarea.getValue());
		  }});
     form2.addButton(bsearch);
		  
     initWidget(form2);
	}
}
