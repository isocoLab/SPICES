package org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution;

import org.w3c.dom.*;
import java.io.*;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent; 
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.extjs.gxt.ui.client.util.Params;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.adapter.OutputAdapter;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.AuthenticationManager;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;

import com.ibm.wsdl.xml.WSDLReaderImpl;

public class WsdlInvoker extends ServiceInvoker {

	@Override
	public void executeService (final TabPanel tabPanel, final ServiceAnnotations service, final Map<String, String> params, final String operation) {
				
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		final ContentPanel panel = new ContentPanel();
		final ProgressBar pb = new ProgressBar();
		final TabItem item = new TabItem("Execution");
		
		String user = module.getActiveUser();
		if((user==null)||(user.compareTo("")==0)){
			user = "Anonymous user";
		}
		
    	InfoConfig infoConfig = new InfoConfig("Executing service " + service.getIdService(), "Service being executed by "+user);
    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	Info.display(infoConfig);
		
		invokeService(tabPanel, service, params, operation, panel, pb, item);
		
	}
	
	public static void invokeService(final TabPanel tabPanel, final ServiceAnnotations service, Map<String, String> params,
			String operation, final ContentPanel panel, final ProgressBar pb, final TabItem item) {

		AsyncCallback<Map<String,String>> callback = new AsyncCallback<Map<String,String>>() {
		    public void onFailure(Throwable caught) {
		    	// do something with errors 		   	
		    }

		    public void onSuccess(Map<String,String> result) {
				panel.remove(pb);
				panel.add(OutputAdapter.buildView(service.getIdService(), result));
				item.removeFromParent();
				tabPanel.add(item);
				tabPanel.setSelection(item);
		    }
		};
		
		panel.setHeading("Execution");
		pb.auto();
		panel.add(pb);
		panel.setLayout(new FitLayout());
	
		item.setScrollMode(Scroll.AUTO);
		item.add(panel);
		item.setClosable(true);
		tabPanel.add(item);
		tabPanel.setSelection(item);
				
		((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).executeWsdlService(service, params, operation, getCookie(), callback);
	}
	
}

