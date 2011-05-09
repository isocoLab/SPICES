package org.soa4all.dashboard.gwt.module.consumptionplatform.client.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ConceptToGraphicModelManager;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution.ServiceInvoker;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.execution.ServiceInvokerFactory;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ConceptualConstants;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.InputMessage;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceOperation;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphicModel;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavFolder;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavLink;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteService;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteServiceAsync;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class InputAdapter {
	
	/**
     * It prepares the parameters to be completed in order to execute the service.
     * 
     * @param service  the information of the service
     * @return  a panel prepared for the user to interact with the service
     */
    public TabPanel generateServiceView(ServiceAnnotations service) {
    	
    	final TabPanel panel = new TabPanel();
    	
    	System.out.println("########################### generateServiceView for " + service.getName());
    	if (service == null) {
	    	InfoConfig infoConfig = new InfoConfig("Error", "Undefined service Id");
	    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	Info.display(infoConfig);
    		TabItem item = new TabItem("Error");
    		item.setStyleAttribute("color", "#000000");
    		item.addText("Undefined Service Id");
	    	panel.add(item);
    	} else 	{
	    	InfoConfig infoConfig = new InfoConfig("Service id:", service.getIdService());
	    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	Info.display(infoConfig);
        	String serviceName = service.getName() == null ? service.getName() : service.getIdService();          	
    		panel.setTitle(serviceName);
    		
    		// Call for graphical representation
    		GraphicModel graphicModel = ConceptToGraphicModelManager.getGraphicalRepresentations(service);
    		Component compRoot = graphicModel.getResourceByName(ConceptualConstants.ROOT).getGraphComponent(); 
    		
    		// TODO: others views
    		panel.add(buildView(panel, service, graphicModel));
    		
     		TabItem item2 = new TabItem("Details");
	    	item2.setScrollMode(Scroll.AUTO);
	    	// TODO: Add code, ratings, comments, etc.
	    	// item2.add(form);
	    	
	    	ServiceVisualizerManager.getDetailsForm(service, item2);
	    	item2.setEnabled(true);
	
	    	//panel.add(item);
	    	panel.add(item2);
	    	panel.setTabScroll(true);
	    	panel.setAnimScroll(true);
    	}
    	return panel;
    }

	private static TabItem buildView(final TabPanel tabPanel, final ServiceAnnotations service, GraphicModel graphicModel) {
		
		System.out.println("########################### Building View ... ");		
		TabItem item = (TabItem) graphicModel.getResourceByName(ConceptualConstants.ROOT).getGraphComponent();
		item.setScrollMode(Scroll.AUTO);
		Iterator itOps = service.getOperations().iterator();
		while (itOps.hasNext()) {
						
			final FormPanel form = new FormPanel();
			form.setCollapsible(true);
			FormLayout opLayout = new FormLayout();  
			opLayout.setLabelAlign(LabelAlign.TOP);  
			form.setLayout(opLayout);
		    
			final Map<String,TextField<String>> inputList = new HashMap<String,TextField<String>>();
			final ServiceOperation operation = (ServiceOperation)itOps.next();	
            form.setHeading("Operation: "+ ServiceVisualizerManager.getTrimmedName(operation.getName()));
            System.out.println("########################### Operation:  " + ServiceVisualizerManager.getTrimmedName(operation.getName()));
            //form.add(graphicModel.getResourceByName(operation.getName()).getGraphComponent());
            
 			Iterator itInputs = operation.getInputs().iterator();
 			while (itInputs.hasNext()) {
 				
 				InputMessage input = (InputMessage) itInputs.next(); 			    				
 				//String paramShort = input.getUri().split("#")[input.getUri().split("#").length-1];
 				System.out.println("########################### Input:  " + input.getUri().split("#")[input.getUri().split("#").length-1]);
 				form.add(graphicModel.getResourceByName(operation.getName()+"_"+input.getUri()).getGraphComponent());
 			    form.add(graphicModel.getResourceByName(operation.getName()+"#"+input.getUri()).getGraphComponent());
 
    	    	inputList.put(operation.getName()+"#"+input.getUri(), (TextField)graphicModel.getResourceByName(operation.getName()+"#"+input.getUri()).getGraphComponent());
    	    
            } // while inputs
 			
 			SelectionListener<ComponentEvent> listener = new SelectionListener<ComponentEvent>()
 			{
    			public void componentSelected(ComponentEvent ce)
    			{
    				System.out.println(" ####################### EXECUTING ...");    				
    				Iterator itInputs = operation.getInputs().iterator();
    	        	Map<String, String> params = new HashMap<String, String>();
    	        	System.out.println(" ####################### EVALUATING INPUTS ...");    
    	        	while (itInputs.hasNext())
    	        	{
    		    		InputMessage input = (InputMessage) itInputs.next();
    		    		System.out.println(" ####################### INPUT VALUE : " + inputList.get(operation.getName()+"#"+input.getUri()).getValue());
    		    		params.put(input.getUri(), inputList.get(operation.getName()+"#"+input.getUri()).getValue());
    	    		}	        	        	
    	        	
    	        	//Create a service invoker
    	    		int serviceType = service.isWsdlService() ? 0 : 1;
    	    		System.out.println("serviceType: " + serviceType);
    	        	ServiceInvoker invoker  = ServiceInvokerFactory.getInvoker(serviceType);
    	        	System.out.println("invoker: "+invoker.getClass());
    	        	invoker.executeService(tabPanel, service, params, operation.getUri());
    	    		//ServiceVisualizerManager.invokeService(tabPanel, service, operation.getName(), params);        	    		
    			}
    		};
    		form.add(new Button("Execute", listener));
    		
    		
    		// Favorites per operation:
    		// ------------------------
        	final String userId = ServiceVisualizerManager.getUserId();

        	// Listener for the Fav button
			SelectionListener<ComponentEvent> favButtonListener = new SelectionListener<ComponentEvent>() {
				public void componentSelected(ComponentEvent ce) {

			    	InfoConfig infoConfig = new InfoConfig("Adding operation for user "+userId, "The operation "+operation.getUri()+" from service "+service.getName()+" is being added to your favourites");
			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
			    	Info.display(infoConfig);

					// 1/2: Load favs
		      	  	((FavoritesRemoteServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(FavoritesRemoteService.class))).load(userId, new AsyncCallback<FavFolder>() {
	    	    		public void onFailure(Throwable caught) {
	    	    			MessageBox.alert("FavTest load error", caught.getMessage(), null);
	    	    		}
	    	    		public void onSuccess(FavFolder result) {
	    	    			final FavFolder favFolder = result;
	        			
	    	            	favFolder.setUserName(userId);
	    	            	favFolder.add(new FavLink(ServiceVisualizerManager.getTrimmedName(service.getName())
	    	            			+" - "+ServiceVisualizerManager.getTrimmedName(operation.getName()),
	    	            			service.getIdService()+"##"+operation.getUri(), "service"));
	    	            	// We add the old favLinks again:
    						System.out.println("I o o o o new fav: "+ServiceVisualizerManager.getTrimmedName(service.getName())
	    	            			+" - "+ServiceVisualizerManager.getTrimmedName(operation.getName())
	    	            			+", "+service.getIdService()+"##"+operation.getUri());	
	    					List<FavLink> fList = favFolder.getChild(0).getChildren();
	    					for(FavLink fl : fList){
	    						System.out.println("I o o o o old fav: "+fl.getName()+", "+fl.getUri());	
	    						favFolder.add(new FavLink(fl.getName(), fl.getUri(), "service"));
	    					}

	    					// 2/2: Save favs
	    	            	((FavoritesRemoteServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(FavoritesRemoteService.class))).save(favFolder , new AsyncCallback<String>()  {
	    	            		public void onFailure(Throwable caught) {
	    	            			caught.printStackTrace();
	    	    			    	InfoConfig infoConfig = new InfoConfig("Error", caught.getMessage());
	    	    			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	    			    	Info.display(infoConfig);
	    	            			MessageBox.alert("Error", caught.getMessage(), null);
	    	            		}
	    	            		public void onSuccess(String result) {
	    	            			ConsumptionPlatformModuleMainContainer.getInstance().getBookmarkWidget().loadFavorites(userId);
	    	    			    	InfoConfig infoConfig = new InfoConfig("Success","New bookmark has been saved.");
	    	    			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	    			    	Info.display(infoConfig);
	    	            		}
	    	            	});
	    	    		}
		      	  	});
				}                               
			};

		    Button  favButton = new Button("Add operation to favourites", favButtonListener);
		    // If the user is not logged in, he cannot bookmark
			if (0 == "".compareTo(userId))
				favButton.setEnabled(false);
			form.addButton(favButton);
    		
    		item.add(form);

 	   } // while ops
		System.out.println("########################### View Generated... ");	
	   return item;			
	}
    		
        	

	
	
}
