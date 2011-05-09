package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.util.Iterator;
import java.util.List;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ConceptualConstants;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.InputMessage;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceOperation;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphResource;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphicModel;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.graphical.GraphicalConstants;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ConceptToGraphicModelManager {
	
	public static GraphicModel getGraphicalRepresentations(ServiceAnnotations service) {
		
		GraphicModel graphicModel = new GraphicModel();
		System.out.println("########################### getGraphicalRepresentations for " + service.getName());
		
		// Root Resource
		graphicModel.addGraphElement(new GraphResource(ConceptualConstants.ROOT, ConceptualConstants.ROOT, GraphicalConstants.FORM,  new TabItem("Main"), null));
		
		Iterator itOps = service.getOperations().iterator();
		while (itOps.hasNext()) {
			ServiceOperation operation = (ServiceOperation)itOps.next();
			graphicModel.addGraphElement(new GraphResource(ConceptualConstants.OPERATION, operation.getName(), GraphicalConstants.TREE, new Tree(), graphicModel.getResourceByName(ConceptualConstants.ROOT)));
			System.out.println("########################### getGraphicalRepresentations for operation: " + operation.getName());
			Iterator itInputs = operation.getInputs().iterator();
			while (itInputs.hasNext()) {
			   InputMessage input = (InputMessage) itInputs.next();	
			   String paramShort = input.getUri().split("#")[input.getUri().split("#").length-1];
			   if (paramShort.equals(""))
				   paramShort = input.getUri();
			   System.out.println("########################### getGraphicalRepresentations for input: " + paramShort); 
     	       // TextField to fill
                final TextField<String> inputText = new TextField<String>();
     	       inputText.setLabelSeparator("");
     	       inputText.setToolTip(input.getUri());
				
			   graphicModel.addGraphElement(new GraphResource(ConceptualConstants.INPUT, operation.getName()+"#"+input.getUri(), GraphicalConstants.TEXT_FIELD, inputText, graphicModel.getResourceByName(ConceptualConstants.ROOT)));
			   
			   // Tree to show the input Concepts (expandable to show possible instances) 
    	       final Tree tree = new Tree();
			   TreeItem conceptItem = new TreeItem(paramShort);
			   conceptItem.setId(input.getUri());
			   conceptItem.setToolTip("Concept: "+ input.getUri());
    	       conceptItem.setIconStyle("icon-wsmo-concept");
    	       tree.addListener(Events.OnClick, new Listener<TreeEvent>() {
    	       	  public void handleEvent(TreeEvent te) {
  			    	InfoConfig infoConfig = new InfoConfig("Item selected",te.item.getId());
			    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
			    	Info.display(infoConfig);
    	        	selectItem(te.item, te.item.getId(), inputText);
    	       	  }
    	        });
    	      tree.getRootItem().add(conceptItem);
    	      graphicModel.addGraphElement(new GraphResource(ConceptualConstants.INPUT, operation.getName()+"_"+input.getUri(), GraphicalConstants.TREE, tree, graphicModel.getResourceByName(operation.getName()))); 	
    	       	
			}
		}		
		return graphicModel;
	}
	
	/**
	 * It handles the selection of the items (concept or instances) from a given parameter tree.
	 *  
	 * @param treeItem
	 * @param id
	 */
	public static void selectItem(final TreeItem treeItem, final String id, TextField<String> input) {
		
	  // If it is the Concept, get instances
	  if(treeItem.getParentItem().isRoot()){
		// but only if they have not been retrieved before
		if(treeItem.getItems().size()==0){
			AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
        		public void onFailure(Throwable arg0) {
        			// TODO Auto-generated method stub
        		}
        		public void onSuccess(List<String[]> result) {
        			// If there are child nodes, show them:
        			if (result.size()>0) {
        				for (String[] res : result) {
       						TreeItem child = new TreeItem(res[1]);
       						child.setId(res[0]);
       						child.setToolTip("Concept: "+res[0]);
       						child.setIconStyle("icon-wsmo-instance");
       						child.setId(res[0]);
       						child.setToolTip(res[1]);
     						child.setLeaf(true);
       						treeItem.add(child);
       					}
        				treeItem.setExpanded(true);
       				}
        		}
        	};        	
        	((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).getInstancesOfConcept(id, callback);
		}
      // If it's one of the instances, set the value of the input.
	  } else {
		input.setValue(id);
	  }
   }


}
