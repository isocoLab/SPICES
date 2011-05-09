/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.RecommendClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.RecommendClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Claudio Stella
 *
 */
public class RSManager {
	static TextArea down;
	static Date startDate = null;
	static Date endDate   = null;
	static MessageBox  messageBox;
	static ProgressBar progressBar; 
	
	static TextField<String> startDateField = new TextField<String>();
	static TextField<String> endDateField   = new TextField<String>();
	
    public static void initAdminGUI(){ 
    	VerticalPanel left  = new VerticalPanel();
    	left.setHeight("400");
    	left.setWidth("20");
    	
    	VerticalPanel right = new VerticalPanel();
		right.setHeight("100%");
		right.setWidth("100%");
				
		VerticalPanel up = new VerticalPanel();
		up.setHeight("200");
		up.setWidth("600");
		
		down = new TextArea();
		down.setHeight("200");
		down.setWidth("600");
		down.setValue("");
    	
    	
		final SelectionListener<ComponentEvent> startListener = new SelectionListener<ComponentEvent>() {
		    public void componentSelected(ComponentEvent ce) {
			    messageBox = MessageBox.progress("Please wait", "Invoking start() method...", "Waiting the server Response...");
			    progressBar = messageBox.getProgressBar();
			    progressBar.auto();
    			((RecommendClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService(
    			(ServiceDefTarget) GWT.create(RecommendClientService.class))).start(new AsyncCallback<Integer>() {    				
					public void onFailure(Throwable caught) {
						messageBox.close();
						Window.alert("RPC error");
					}
					public void onSuccess(Integer result) {
//						Window.alert("Status:"+result);	
						messageBox.close();
						if (result!=200)
							MessageBox.alert("Server Error", "Server returned:"+result, null);
						else MessageBox.info("Server Response", "OK", null);
//						down.setValue("Start->Status code returned:"+result);
					}
				});    		    	
		    }    
		};
		
		final SelectionListener<ComponentEvent> resetListener = new SelectionListener<ComponentEvent>() {
			public void componentSelected(ComponentEvent ce) {
			    messageBox = MessageBox.progress("Please wait", "Invoking reset() method...", "Waiting the server Response...");
			    progressBar = messageBox.getProgressBar();
			    progressBar.auto();

    			((RecommendClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService(
    			(ServiceDefTarget) GWT.create(RecommendClientService.class))).reset(new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						messageBox.close();
						Window.alert("RPC error");
					}
					public void onSuccess(Integer result) {
						messageBox.close();
						if (result!=200)
							MessageBox.alert("Server Error", "Server returned:"+result, null);
						else MessageBox.info("Server Response", "OK", null);
//						down.setValue("Reset->Status code returned: "+result);
//						down.setValue(down.getValue()+"\n\nReset->Status code returned: "+result);
					}
				});    		    	
		    }    
		};
		
		final SelectionListener<ComponentEvent> getLastLogEntryDate = new SelectionListener<ComponentEvent>() {
		    public void componentSelected(ComponentEvent ce) {
			    messageBox = MessageBox.progress("Please wait", "Invoking getLastLogEntryDateAdded() method...", "Waiting the server Response...");
			    progressBar = messageBox.getProgressBar();
			    progressBar.auto();

    			((RecommendClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService(
    			(ServiceDefTarget) GWT.create(RecommendClientService.class))).getLastLogEntryDateAdded(new AsyncCallback<Date>() {
					public void onFailure(Throwable caught) {
						messageBox.close();
						Window.alert("RPC error");
					}
					public void onSuccess(Date result) {
						messageBox.close();
						MessageBox.info("Server Response", "Last log entry date : "+result, null);
//						Window.alert("Last log entry date : "+result);	
//						down.setValue("Last log entry date : "+result);
//						down.setValue(down.getValue()+"\n\nLast log entry date : "+result);
					}
				});    		    	
		    }    
		};
		
		final SelectionListener<ComponentEvent> importLogs = new SelectionListener<ComponentEvent>() {
		    public void componentSelected(ComponentEvent ce) {
			    messageBox = MessageBox.progress("Please wait", "Invoking importLogs() method...", "Waiting the server Response...");
			    progressBar = messageBox.getProgressBar();
			    progressBar.auto();

    			((RecommendClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService(
    			(ServiceDefTarget) GWT.create(RecommendClientService.class))).importLogs(startDate, endDate, new AsyncCallback<List<String>>() {
					public void onFailure(Throwable caught) {
						messageBox.close();
						Window.alert("RPC error");
					}
					public void onSuccess(List<String> result) {						
						messageBox.close();
						String logList = "";
						down.setValue("");
						Iterator<String> it_logs = result.iterator();
						while (it_logs.hasNext()) {
							String log = (String) it_logs.next();
							logList = logList + log;
						}						
//						down.setValue(down.getValue()+"\n\n"+logList);
						down.setValue(logList);
					}
				});    		    	
		    }    
		};

		up.addText("<h4>Recommender System: Batch Time Methods Manager</h4><br>");
		Button startButton = new Button("Start", startListener);
		startButton.setToolTip("This will <b>starts</b> the Recommender System");
		up.add(startButton);
		up.addText("<br>");
		
		Button resetButton = new Button("Reset", resetListener);
		resetButton.setToolTip("This will <b>resets</b> the Recommender System");
		up.add(resetButton);
		up.addText("<br>");
		
		Button getLastDateButton = new Button("Get Last Date", getLastLogEntryDate);
		getLastDateButton.setToolTip("This will retrieves the Date of the last log entry added");
		
		up.add(getLastDateButton);
		up.addText("<br>");

		
		VerticalPanel startDatePanel = new VerticalPanel();
		VerticalPanel endDatePanel = new VerticalPanel();
			
		
		final DateField startDateField = new DateField();
		startDateField.setEmptyText("Start Date");
		startDateField.addListener(Events.Change, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent ce) {
				startDate = startDateField.getValue();
		    	InfoConfig infoConfig = new InfoConfig("Start Date Selected", endDate.toGMTString());
		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    	Info.display(infoConfig);
			}
		});
		startDateField.setToolTip("Sets the Start Date of the range of logs to retrieve");
		
		final DateField endDateField = new DateField();
		endDateField.setEmptyText("End Date");
		endDateField.addListener(Events.Change, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent ce) {
				endDate = endDateField.getValue();
		    	InfoConfig infoConfig = new InfoConfig("End Date Selected", endDate.toGMTString());
		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    	Info.display(infoConfig);
			}
		});
		endDateField.setToolTip("Sets the End Date of the range of logs to retrieve");
	
		
		startDatePanel.add(startDateField);
		endDatePanel.add(endDateField);
		
		HorizontalPanel logsImportPanel = new HorizontalPanel();
		
		logsImportPanel.add(startDatePanel);
		logsImportPanel.add(endDatePanel);

		up.add(logsImportPanel);
		up.addText("<br><br>");
		Button importLogsButton = new Button("Import Logs", importLogs);
		importLogsButton.setToolTip("This will queries the RDF repository and imports the logs into the RS");
		up.add(importLogsButton);
		up.addText("<br><br>");
		
		up.addText("Output Console:<br>");

		right.add(up);
		right.add(down);

		HorizontalPanel main = new HorizontalPanel();
		main.add(left);
		main.add(right);
		
		RootPanel.get().add(main);
    }

}