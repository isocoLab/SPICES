/**
 * Portlet to manage CollaborativeAD module
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.CollaborativeADClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.CollaborativeADClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.CollaborativeADAction;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.CollaborativeADUser;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author GDiMatteo
 * Main panel for WP9 CollaborativeAdvertising management
 */
public class CollaborativeADManager
{
	private static Grid<CollaborativeADUser> g = null;
	
	private static FormPanel tabPanel;
	
	public static void show(String username)
	{
    	final Portlet portlet = new Portlet();
    	final ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    	//final ProgressBar pb = new ProgressBar();

    	AsyncCallback<CollaborativeADUser[]> callback = new AsyncCallback<CollaborativeADUser[]>() {
    		public void onFailure(Throwable caught)
    		{
    			// do something with errors
    		}

    		public void onSuccess(CollaborativeADUser[] result)
    		{
    			System.out.println(">>>> Users retrieving from REST succedeed!");
    			portlet.setScrollMode(Scroll.AUTOY);
    			portlet.add(displayGrid(result));
    			
    			portlet.removeFromParent();
    			instance.getPortal().add(portlet, 0);
    		}
    	};
    	
    	AsyncCallback<CollaborativeADUser> callbackUser = new AsyncCallback<CollaborativeADUser>() {
    		public void onFailure(Throwable caught)
    		{
    			// do something with errors
    		}

    		public void onSuccess(CollaborativeADUser result)
    		{
    			System.out.println(">>>> User details retrieving from REST succedeed!\nUser action: "+result.getActions()[0]+" - "+result.get("actions"));
    			
    			portlet.add(createUserForm(result));
    			portlet.removeFromParent();
    			instance.getPortal().add(portlet, 0);
    		}
    	};
    	
    	if (instance != null)
    	{
    		portlet.setHeading("Collaborative AD Manager");
    		configListPanel(portlet);
    		
    		instance.getPortal().add(portlet, 0);
    		
    		if (null == username)
    			// Requesting Collab.AD users view
        		// TODO: DO NOT USE STATIC REFERENCE!!
        		((CollaborativeADClientServiceAsync)RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(CollaborativeADClientService.class))).getUsers(callback);
    		else
	    		// Requesting user's details view
	    		// TODO: DO NOT USE STATIC REFERENCE!!
	    		((CollaborativeADClientServiceAsync)RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(CollaborativeADClientService.class))).getUser(username, callbackUser);
    	}
    	else
    		System.out.println("[ERROR] CollaborativeADManager.show(): Instance is null");
    }
	
	private static Grid<CollaborativeADUser> displayGrid(CollaborativeADUser[] users)
	{
		List<CollaborativeADUser> usersList = new ArrayList<CollaborativeADUser>();
    	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    	GridCellRenderer<CollaborativeADUser> link = new GridCellRenderer<CollaborativeADUser>() {
    		public String render(CollaborativeADUser model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<CollaborativeADUser> store) {
    			String str = model.get(property);
    			return "<span class=\"download\" style='color: green'>" + str + "</span>";
    		}
    	};

    	for (CollaborativeADUser user : users)
    		usersList.add(user);
    	
    	int i = 0;
    	
    	for (CollaborativeADUser u : usersList)
		{
			System.out.println(i++ + ") "+u);
		}
    	
    	ColumnConfig column = new ColumnConfig();
    	column.setId("username");
    	column.setHeader("Username");
    	column.setRenderer(link);
    	column.setWidth(100);
    	
    	TextField<String> text = new TextField<String>();
    	text.setAllowBlank(false);
    	text.setAutoValidate(true);
    	column.setEditor(new CellEditor(text));

    	configs.add(column);

    	column = new ColumnConfig();
    	column.setId("credits");
    	column.setHeader("Credits");
    	column.setWidth(50);
    	column.setAlignment(HorizontalAlignment.CENTER);
    	configs.add(column);

    	column = new ColumnConfig();
    	column.setId("uri");
    	column.setHeader("Uri");
    	column.setWidth(121);
    	configs.add(column);
    	
    	GridCellRenderer<CollaborativeADUser> delete = new GridCellRenderer<CollaborativeADUser>() {
    		public String render(CollaborativeADUser model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<CollaborativeADUser> store) {
    			return "<img src='ConsumptionPlatform/icons/x-red4.gif' />";
    		}
    	};
    	
    	column = new ColumnConfig();
    	column.setId("del");
    	column.setHeader("Actions");
    	column.setWidth(50);
    	column.setRenderer(delete);
    	column.setAlignment(HorizontalAlignment.CENTER);
    	configs.add(column);

    	ListStore<CollaborativeADUser> store = new ListStore<CollaborativeADUser>();
    	store.add(usersList);

    	ColumnModel cm = new ColumnModel(configs);

    	g = new Grid<CollaborativeADUser>(store, cm);
    	g.setBorders(true);
    	// Adding click listener
    	g.addListener(Events.CellClick, new Listener<GridEvent>()
    	{
    		public void handleEvent(GridEvent ge)
    		{
    			int index = ge.rowIndex;
    			CollaborativeADUser u = g.getStore().getAt(index);
    			System.out.println("Clicked on user with URI: "+u.getUri());
    			CollaborativeADManager.show(u.getUsername());
    		}
    	});
    	
    	g.setStripeRows(true);
    	g.setVisible(true);
//    	g.setSize(500, 400);
    	return g;
	}
	
    
    /**
     * Sets the configurations and buttons of the boxes.
     * 
     * @param panel  the box panel to configure
     */
    private static void configListPanel(final ContentPanel panel)
    {
    	// TODO: Align with configPanel
    	panel.setCollapsible(true);
    	panel.setAnimCollapse(false);
    	
    	final ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    	
    	// header buttons definition
    	// need to instantiate the maximButt, because I need it 'final' to use it in restoreButt listener. So I begin instantiating it without listener
    	final ToolButton maximButt = new ToolButton("x-tool-maximize",null), closeButt, restoreButt;
    	
    	// Close button
    	closeButt = new ToolButton("x-tool-close", new SelectionListener<ComponentEvent>() {
    		@Override
    		public void componentSelected(ComponentEvent ce) {
    			instance.getPortal().closePortlet((Portlet)panel);
    		}
    	});
    	closeButt.setId("closeButton");
    	
    	// Restore button
    	restoreButt = new ToolButton("x-tool-restore", new SelectionListener<ComponentEvent>() {
    		@Override
    		public void componentSelected(ComponentEvent ce)
    		{
    			instance.getPortal().restorePortlet((Portlet)panel, maximButt);
    		}
    	});
    	restoreButt.setId("restoreButton");
    	
    	// Maximization button
    	// Just adding the selectionListener
    	maximButt.addSelectionListener(new SelectionListener<ComponentEvent>(){
    		@Override
    		public void componentSelected(ComponentEvent ce)
    		{
    			instance.getPortal().maximizePortlet((Portlet)panel, restoreButt);
    		}
    	});
    	maximButt.setId("maximizeButton");
    	
    	// Add header buttons to portlet
    	panel.getHeader().addTool(closeButt);
    	panel.getHeader().addTool(maximButt);
    }

    private static ContentPanel createUserForm(CollaborativeADUser u)
    {
        FormData formData = new FormData("100%");   
        tabPanel = new FormPanel();   
        tabPanel.setHeading("User details");
        tabPanel.setPadding(0);   
        tabPanel.setFrame(false);
        tabPanel.setBodyBorder(false);   
        tabPanel.setButtonAlign(HorizontalAlignment.CENTER);
        tabPanel.setLayout(new FitLayout());
        
        TabPanel tabs = new TabPanel();   
        
        TabItem personal = new TabItem();   
        personal.setStyleAttribute("padding", "10px");   
        personal.setText("Personal Details");   
        personal.setLayout(new FormLayout());   
        
        TextField<String> username = new TextField<String>();   
        username.setFieldLabel("Username");
        username.setValue(u.getUsername());
        username.setEnabled(false);
        personal.add(username, formData);
        
        TextField<String> name = new TextField<String>();
        name.setFieldLabel("Name");
        name.setValue(u.getName());
        name.setEnabled(false);
        personal.add(name, formData);
        
        TextField<Integer> credits = new TextField<Integer>();
        credits.setFieldLabel("Credits");
        credits.setValue(u.getCredits());
        credits.setEnabled(false);
        personal.add(credits, formData);
        
        tabs.add(personal);
        
        TabItem actions = new TabItem();
        actions.setStyleAttribute("padding", "10px");
        actions.setText("Actions");
        actions.setLayout(new FormLayout());
        actions.setScrollMode(Scroll.ALWAYS);
        
        addActionsToPanel(actions, u.getActions());
        
        tabs.add(actions);
        
        tabPanel.add(tabs);   
        tabPanel.addButton(new Button("Update"));
        
        tabPanel.setHeight(300);
        return tabPanel;
      }
    
    private static void addActionsToPanel(TabItem panel, CollaborativeADAction[] acts)
    {
    	List<CollaborativeADAction> actions = new ArrayList<CollaborativeADAction>();
    	
    	for (CollaborativeADAction a : acts)
    	{
			actions.add(a);
//			System.out.println("* "+a);
		}
    	
    	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
    	
    	ColumnConfig column = new ColumnConfig();
    	column.setId("date");
    	column.setHeader("Date");
    	column.setWidth(80);
    	column.setAlignment(HorizontalAlignment.CENTER);
    	configs.add(column);
    	
    	GridCellRenderer<CollaborativeADAction> colorise = new GridCellRenderer<CollaborativeADAction>(){
    		public String render(CollaborativeADAction model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<CollaborativeADAction> store)
    		{
    			String str = model.get(property);
    			String color = "black";
    			if (0 == "click on banner".compareToIgnoreCase(str))
    				color = "green";
    			else if (0 == "registration".compareToIgnoreCase(str))
    				color = "red";
    			
    			return "<span class=\"download\" style='color: "+color+"'>" + str + "</span>";
    		}
    	};
    	
    	column = new ColumnConfig();
    	column.setId("action");
    	column.setHeader("Action");
    	column.setWidth(80);
    	column.setRenderer(colorise);
    	
    	/*
    	TextField<String> text = new TextField<String>();
    	text.setAllowBlank(false);
    	text.setAutoValidate(true);
    	text.setEnabled(false);
    	column.setEditor(new CellEditor(text));
    	*/
    	configs.add(column);

    	column = new ColumnConfig();
    	column.setId("publisher");
    	column.setHeader("Banner");
    	column.setWidth(50);
    	configs.add(column);
    	
    	column = new ColumnConfig();
    	column.setId("affiliate");
    	column.setHeader("Website");
    	column.setWidth(70);
    	configs.add(column);
    	
    	column = new ColumnConfig();
    	column.setId("credits");
    	column.setHeader("Credits");
    	column.setWidth(40);
    	column.setAlignment(HorizontalAlignment.CENTER);
    	configs.add(column);
    	

    	ListStore<CollaborativeADAction> store = new ListStore<CollaborativeADAction>();
    	store.add(actions);

    	ColumnModel cm = new ColumnModel(configs);

    	final Grid<CollaborativeADAction> g = new Grid<CollaborativeADAction>(store, cm);
//    	g.setAutoExpandColumn("action");
    	g.setBorders(true);
    	g.setStripeRows(true);
    	
    	panel.add(g);
    }
}
