/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
 *  http://www.tieGlobal.com
 *  info@tieGlobal.com

 *All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
 *The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.Stock;
import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.impl.GoalService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.impl.GoalServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.NumberCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Goal Manipulation Manager
 * 
 * TODO: Refine and align the code with the services manager.
 */
public class GoalManipulationManager {

    private RemoteServiceHelper remoteServiceHelper = RemoteServiceHelper.getInstance();

    /**
     * Shows the elements of the Goal
     * 
     * @param goalId  the identifier of the goal
     */
    public static void displayGoal(final String goalId) {
    	// TODO: DO NOT USE STATIC REFERENCE!!
    	// TODO: Get Goal from repository
    	AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
    		public void onFailure(Throwable caught) {
    			System.out.println(caught);
    		}

    		public void onSuccess(String[] result) {

    			ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    			if (instance != null) {
    				Portlet portlet = new Portlet();
				    portlet.setHeading(result[0]);
				    configPanel(portlet);
				    portlet.setLayout(new FitLayout());
				    portlet.add(createForm(result, goalId));
				    portlet.setHeight(250);
				    instance.getPortal().add(portlet, 0);
    			} else {
    				System.out.println("instance: null");
    			}
    		}
    	};

    	try {
    		// TODO: DO NOT USE STATIC REFERENCE!!
    		((GoalServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(GoalService.class))).getGoal(goalId, callback);
    	} catch (Exception e) {
    		callback.onFailure(e);
    	}

	/*
	 * TODO: Change to "Window-type" of Goals when UI is fine 
	 * final Window
	 * complex = new Window(); complex.setMaximizable(true);
	 * complex.setMinimizable(true); complex.setHeading("Goal Window");
	 * complex.setWidth(200); complex.setHeight(350); complex.show();
	 */
    }

    public RemoteServiceHelper getRemoteServiceHelper() {
    	return remoteServiceHelper;
    }

    public void setRemoteServiceHelper(RemoteServiceHelper remoteServiceHelper) {
    	this.remoteServiceHelper = remoteServiceHelper;
    }

    /**
     * Displays the relevant fields of the goal to be completed in the form.
     * 
     * @param result
     * @param goalId
     * @return
     */
    private static TabPanel createForm(String[] result, final String goalId) {
    	// TODO: Display proper fields, not hard-coded!
		final TabPanel tabPanel = new TabPanel();
	
		FormPanel formPanel = new FormPanel();
		HTML goalWSDL = new HTML(result[2]);
		if (goalId.equals("1")) {
		    String labelstr = "Origin";
		    Label label = new Label(labelstr);
		    formPanel.add(label);
		    TextArea txtarea = new TextArea();
		    txtarea.setText("Paris");
		    formPanel.add(txtarea);
	
		    Label label2 = new Label("Destination");
		    formPanel.add(label2);
		    TextArea txtarea2 = new TextArea();
		    txtarea2.setText("Madrid");
		    formPanel.add(txtarea2);
	
		    Label label3 = new Label("Going out");
		    formPanel.add(label3);
		    TextArea txtarea3 = new TextArea();
		    txtarea3.setText("23-02-2009");
		    formPanel.add(txtarea3);
	
		    Label label4 = new Label("Return");
		    formPanel.add(label4);
		    TextArea txtarea4 = new TextArea();
		    txtarea4.setText("23-02-2009");
		    formPanel.add(txtarea4);
	
		} else {
		    String labelstr = "Where";
		    Label label = new Label(labelstr);
		    formPanel.add(label);
		    TextArea txtarea = new TextArea();
		    txtarea.setText("Dublin");
		    formPanel.add(txtarea);
	
		    Label label2 = new Label("When");
		    formPanel.add(label2);
		    ListBox lbox = new ListBox();
		    lbox.addItem("Today");
		    lbox.addItem("Tomorrow");
		    formPanel.add(lbox);
		}

		SelectionListener listener = new SelectionListener<ComponentEvent>() {
		    public void componentSelected(ComponentEvent ce) {
			GoalManipulationManager.achieveGoal(goalId, tabPanel);
		    }
		};
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.add(new Button("Run", listener));
	
		SelectionListener listenerDiscover = new SelectionListener<ComponentEvent>() {
		    public void componentSelected(ComponentEvent ce) {
			Button btn = (Button) ce.component;
			Info.display("Click Event", "The '{0}' button was clicked.", btn.getText());
		    }
		};
	
		ButtonBar buttonBarDiscover = new ButtonBar();
		buttonBar.add(new Button("Discover", listenerDiscover));
	
		formPanel.add(buttonBar);
	
		TabItem item = new TabItem("Description");
		item.setScrollMode(Scroll.AUTO);
		item.add(formPanel);
	
		TabItem item2 = new TabItem("Code");
		item2.setScrollMode(Scroll.AUTO);
		item2.add(goalWSDL);
	
		tabPanel.add(item);
		tabPanel.add(item2);
		tabPanel.setTabScroll(true);
		tabPanel.setAnimScroll(true);
		return tabPanel;
    }

    /**
     * Shows the results of the invocation of a Goal.
     * 
     * @param goalId
     * @param tabPanel
     */
    protected static void achieveGoal(String goalId, TabPanel tabPanel) {
    	// TODO: Send the Goal and execute the service. Then, present results.
    	ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();

    	ContentPanel panel = new ContentPanel();

    	String serviceName = "";
		String serviceResult = "";
		if (goalId.equals("1")) {
		    serviceName = "Service Travel-Info";
		    serviceResult = "Paris-Madrid: 9:30 to 11:30 ; 1:30 to 15:30; 19:30 to 21:30";
		} else {
		    serviceName = "Service Weather inc";
		    serviceResult = "The weather forecast for tomorrow in Paris is: 10C";
		}
		panel.setHeading(serviceName);
		panel.addText(serviceResult);
		panel.setLayout(new FitLayout());
		// panel.setHeight(250);
	
		TabItem item = new TabItem("Execution");
		item.setScrollMode(Scroll.AUTO);
		item.add(panel);
		item.setClosable(true);
		tabPanel.add(item);
		tabPanel.setSelection(item);
    }
 
    /**
     * Sets the configurations and buttons of the boxes.
     * 
     * @param panel
     */
    private static void configPanel(final ContentPanel panel) {
    	// TODO: Align with configListPanel
		panel.setCollapsible(true);
		panel.setAnimCollapse(false);
		panel.getHeader().addTool(new ToolButton("x-tool-gear", new SelectionListener<ComponentEvent>() {
	
		    @Override
		    public void componentSelected(ComponentEvent ce) {
			panel.show();
		    }
	
		}));
		panel.getHeader().addTool(new ToolButton("x-tool-close", new SelectionListener<ComponentEvent>() {
	
		    @Override
		    public void componentSelected(ComponentEvent ce) {
			panel.removeFromParent();
		    }
	
		}));
    }
}
