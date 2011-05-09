/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import java.util.List;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;
import org.soa4all.dashboard.gwt.module.favorites.client.GUIHelper;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavFolder;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavLink;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteService;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteServiceAsync;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;


/**
 * Class : WidgetMyGoals
 * 
 */
public class WidgetMyGoals extends Composite {
	
	private TreeLoader loader;
	private Tree tree;
    private FavFolder favorites;
	
	/**
	 * The widget MyGoals displays the user's bookmarked services (Favorites)
	 */
	public WidgetMyGoals()
	{
		
    	final String userId = ServiceVisualizerManager.getUserId();

    	Listener<ComponentEvent> listener = new Listener<ComponentEvent>()
    	{
    		public void handleEvent(ComponentEvent ce)
    		{
    			try
    			{
    				Tree tree = (Tree)ce.component;
    				// If it's a FavLink and it's not a right click
    				if ((tree.getSelectedItem().getModel() instanceof FavLink)&&(ce.getEventType()!=8))
    				{
    					FavLink fl = (FavLink)tree.getSelectedItem().getModel();
        		    	InfoConfig infoConfig = new InfoConfig("Favorites Folder","Name: "+fl.getName()+". Uri: "+fl.getUri()+". Type: "+fl.getType());
        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
        		    	Info.display(infoConfig);
        				ServiceVisualizerManager.displayService(fl.getUri().split("##")[0]);
    				}
    				else {
    					//Info.display("Favorites Folder","Problem: You clicked on an item that is not a FavLink");
    				}
    			}
    			catch (Exception e){e.printStackTrace();}
    		}
    	};
    	
    	ContentPanel frame = new ContentPanel();
    	frame.setFrame(true);
    	frame.setCollapsible(true);
    	frame.setAnimCollapse(false);
    	frame.setHeading("Favourite services");
    	
    	tree = new Tree();
    	
    	// Delete option
    	Menu contextMenu = new Menu();  
    	MenuItem remove = new MenuItem();
	    remove.setText("Delete favourite");  
	    remove.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) {
  			try
			{
  		        List<TreeItem> selected = tree.getSelectionModel().getSelectedItems();  
  		        for (TreeItem sel : selected) {
					FavLink fl = (FavLink)sel.getModel();
    		    	InfoConfig infoConfig = new InfoConfig("Deleting favourite","Name: "+fl.getName()+". Uri: "+fl.getUri());
    		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    		    	Info.display(infoConfig);
					final String deleteUri = fl.getUri();
					// 1/2: Load favs
		      	  	((FavoritesRemoteServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(FavoritesRemoteService.class))).load(userId, new AsyncCallback<FavFolder>() {
	    	    		public void onFailure(Throwable caught) {
	    	    			MessageBox.alert("FavTest load error", caught.getMessage(), null);
	    	    		}
	    	    		public void onSuccess(FavFolder result) {
	    	    			final FavFolder favFolder = result;
	        			
	    	            	favFolder.setUserName(userId);
	    	            	// We add the old favLinks, except the selected:
	    					List<FavLink> fList = favFolder.getChild(0).getChildren();
    						System.out.println("W o o o o del fav: "+deleteUri);	
	    					for(FavLink fl : fList){
	    						if(fl.getUri().compareTo(deleteUri)!=0){
	        						System.out.println("W o o o o old fav: "+fl.getName()+", "+fl.getUri());	
	    							favFolder.add(new FavLink(fl.getName(), fl.getUri(), "service"));
	    						}
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
	    	        		    	InfoConfig infoConfig = new InfoConfig("Success","Bookmark has been deleted.");
	    	        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	        		    	Info.display(infoConfig);
	    	            		}
	    	            	});
	    	    		}
		      	  	});
					
  		        } 
			}
			catch (Exception e){e.printStackTrace();}
	      }  
	    });  
	    contextMenu.add(remove);  
	    tree.setContextMenu(contextMenu);
    	
    	loader = new BaseTreeLoader(new TreeModelReader());
	    TreeStore<ModelData> store = new TreeStore<ModelData>(loader);

	    TreeBinder binder = new TreeBinder(tree, store);
	    binder.setAutoLoad(true);
	    binder.setDisplayProperty("name");

	    GUIHelper.setServiceModelIcons(tree.getRootItem());
	    frame.add(tree);
	    tree.addListener(Events.SelectionChange, listener);

		initWidget(frame);
	}
	
	/**
	 * Loads a FavFolder in the widget
	 * @param favFold the favFolder to show
	 */
	public void load(FavFolder favFold)
	{
		if(favFold.getChild(0).getChildCount()==0){
			favFold.setUserName("No favorites selected yet.");
		}
		loader.load(favFold);
		// Expand all the tree
		tree.expandAll();
	}
	
	/**
	 * Loads an empty FavFolder in the widget for an anonymous user
	 */
	public void noUser()
	{
		FavFolder ff = new FavFolder();
		ff.setUserName("User is not logged in");
		loader.load(ff);
	}

	public void loadFavorites(String userId)
	{
		// Loading user bookmarked services
		((FavoritesRemoteServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(FavoritesRemoteService.class))).load(userId,
		    new AsyncCallback<FavFolder>()
		    {
				public void onFailure(Throwable caught)
				{
					MessageBox.alert("Error", "Failure in retrieving user bookmarked services: "+caught.getMessage(), null);
				}

				public void onSuccess(FavFolder result)
				{
    		    	InfoConfig infoConfig = new InfoConfig("Favorites Folder","result.getChildCount(): "+result.getChildCount()
							+". child0.childs:"+ ((FavFolder)result.getChild(0)).getChildCount()+". User: "+ ((FavFolder)result.getChild(0)).getName());
    		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    		    	Info.display(infoConfig);
					favorites = result;
					load(favorites);
				}
		    });
	}
	
}
