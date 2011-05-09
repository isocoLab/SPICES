package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.soa4all.dashboard.gwt.core.shared.client.util.rpc.RemoteServiceHelper;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.adapter.InputAdapter;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.CommentCli;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.ConsumptionClientServiceAsync;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.Comment;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.Service;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.conceptual.ServiceAnnotations;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavFolder;
import org.soa4all.dashboard.gwt.module.favorites.client.model.FavLink;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteService;
import org.soa4all.dashboard.gwt.module.favorites.client.model.business.delegate.impl.gwt.FavoritesRemoteServiceAsync;
import org.soa4all.uilibrary.ratingwidget.client.RatingWidget;
import org.soa4all.uilibrary.tagcloud.client.Tag;
import org.soa4all.uilibrary.tagcloud.client.TagCloud;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * Service Visualizer Manager 
 * 
 */
public class ServiceVisualizerManager {

	final static String LUF_SERVICE_BASE = "http://soa4all.isoco.net/luf/api/";
	final static String RECOMMENDATION_SERVICE_BASE = "http://etechdemo.cefriel.it/rs-component-service/runtime/";

	// creation of the rating widget for average and user's vote and setting of their parameters.
	// The widgets are created with a starting 0 value
	static RatingWidget avgRw = new RatingWidget(0, 5, 1); // RatingWidget.LTR or LEFT_TO_RIGHT?
	static RatingWidget myRw = new RatingWidget(0, 5, 1);
	
    public static VerticalPanel availablePanel;

    /**
     * Gets the nodes below a selected one in order to form the categories tree for services.
     *  
     * @param treeItem  the node selected
     * @param id        the id of the item
     */
    public static void getServiceChilds(final TreeItem treeItem, final String id) {
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
   						child.setToolTip(res[1]);
   						if(res[2].compareTo("true")==0){
   							child.setLeaf(false);
   						}
   						treeItem.add(child);
   					}
    			// If there are no child nodes, open query for that concept:
    			// (also if it is not root node -> That might mean storage is not working fine	
   				} else if(!treeItem.isRoot()) {
   					searchCategory(treeItem.getId());
   				}
    		}
    		
    	};
    	((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).getChilds(id, callback);
    }

    /**
     * Discovers services by keywords.
     * 
     * @param query  a keyword-based query
     */
    public static void searchString(final String query) {
    	final ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    	final Portlet portlet = instance.getSearchPortlet();
    	final ProgressBar pb = new ProgressBar();
    	
    	InfoConfig infoConfig = new InfoConfig("Searching: " + query, "Searching services...");
    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	Info.display(infoConfig);

    	AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
    		public void onFailure(Throwable caught) {
    			// do something with errors
    		}

    		public void onSuccess(List<String[]> result) {
    			portlet.remove(pb);
    			if(result.size()>0){
    				portlet.add(displayGrid(result));
    			}else{
    				final Text noResultsText = new Text("No results found, please try again with a different search.");
    				portlet.add(noResultsText);
    			}
    			int col = instance.getPortal().getPortletColumn(portlet);
       			portlet.removeFromParent();
       			instance.getPortal().add(portlet, col-1);
    		}
    	};
    	if (instance != null) {
    		String searchHeading1 = "Search: ";
    		String searchHeading2 = (query!= null) ? query : "";
    		portlet.setHeading(searchHeading1+searchHeading2);
    		if(portlet.getItemCount()==0){
        		configListPanel(portlet);
    		}
    		portlet.setLayout(new FitLayout());
    		portlet.setHeight(250);
    		portlet.removeAll();
    		pb.auto();
    		portlet.add(pb);
    		if(instance.getPortal().getPortletIndex(portlet)==-1){
    			instance.getPortal().add(portlet, 0);
    		}
    		((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).searchString(query, callback);
    	} else {
    		System.out.println("instance: null");
    	}
    }

    /**
     * Discovers services by keywords.
     * 
     * @param query  a keyword-based query
     */
    public static void searchCategory(final String query) {
    	final ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    	final Portlet portlet = instance.getSearchPortlet();
    	final ProgressBar pb = new ProgressBar();

    	InfoConfig infoConfig = new InfoConfig("Category: " + query, "Searching services...");
    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	Info.display(infoConfig);

    	AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
    		public void onFailure(Throwable caught) {
    			// do something with errors
    		}

    		public void onSuccess(List<String[]> result) {
    			portlet.remove(pb);
    			if(result.size()>0){
        			portlet.add(displayGrid(result));
    			}else{
    				final Text noResultsText = new Text("No results found, please try again with a different category.");
    				portlet.add(noResultsText);
    			}
    			int col = instance.getPortal().getPortletColumn(portlet);
       			portlet.removeFromParent();
       			instance.getPortal().add(portlet, col-1);
    		}
    	};
    	if (instance != null) {
    		portlet.setHeading("Category: " + query);
    		if(portlet.getItemCount()==0){
        		configListPanel(portlet);
    		}
    		portlet.setLayout(new FitLayout());
    		portlet.setHeight(250);
    		portlet.removeAll();
    		pb.auto();
    		portlet.add(pb);
    		if(instance.getPortal().getPortletIndex(portlet)==-1){
        		instance.getPortal().add(portlet, 0);
    		}
    		((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).searchCategory(query, callback);
    	} else {
    		System.out.println("instance: null");
    	}
    }


    
    /**
     * Sets the configurations and buttons of the boxes.
     * 
     * @param panel  the box panel to configure
     */
    private static void configListPanel(final ContentPanel panel) {
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
    			panel.getHeader().removeTool(maximButt);
    			panel.getHeader().removeTool(closeButt);
    	    	panel.getHeader().addTool(maximButt);
    	    	panel.getHeader().addTool(closeButt);
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
    			panel.getHeader().removeTool(restoreButt);
    			panel.getHeader().removeTool(closeButt);
    	    	panel.getHeader().addTool(restoreButt);
    	    	panel.getHeader().addTool(closeButt);
    		}
    	});
    	maximButt.setId("maximizeButton");
    	
    	// Add header buttons to portlet
    	panel.getHeader().addTool(maximButt);
    	panel.getHeader().addTool(closeButt);

    	panel.addListener(Events.Render, new Listener<ComponentEvent>(){
    		@Override
    		public void handleEvent(ComponentEvent ce)
    		{
    			panel.getHeader().removeTool(maximButt);
    			panel.getHeader().removeTool(closeButt);
    	    	panel.getHeader().addTool(maximButt);
    	    	panel.getHeader().addTool(closeButt);
    		}
    	});
    }

    
    /**
     * Shows a set of services in a grid format.
     * 
     * @param info  a list with information about the services 
     * @return      the grid containing the services to be shown 
     */
    private static Grid<Service> displayGrid(List<String[]> info) {

    	List<Service> services = new ArrayList<Service>();
    	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    	GridCellRenderer<Service> link = new GridCellRenderer<Service>() {
    		public String render(Service model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Service> store) {
    			String str = model.get(property);
    			return "<span class=\"download\" style='color: green'>" + str + "</span>";
    		}
    	};

    	for (String[] res : info)
    	{
    		Service service = new Service(getTrimmedName(res[0]), res[1], res[2], Double.parseDouble(res[3]));
    		services.add(service);
    	}

    	ColumnConfig column = new ColumnConfig();
    	column.setId("name");
    	column.setHeader("Service");
    	column.setRenderer(link);
    	column.setWidth(210);

    	TextField<String> text = new TextField<String>();
    	text.setAllowBlank(false);
    	text.setAutoValidate(true);
    	column.setEditor(new CellEditor(text));

    	configs.add(column);

    	column = new ColumnConfig();
    	column.setId("rating");
    	column.setHeader("Rating");
    	column.setWidth(40);
    	configs.add(column);

    	ListStore<Service> store = new ListStore<Service>();
    	store.add(services);

    	ColumnModel cm = new ColumnModel(configs);

    	final Grid<Service> g = new Grid<Service>(store, cm);
    	g.setAutoExpandColumn("name");
    	g.setBorders(true);
    	g.addListener(Events.CellClick, new Listener<GridEvent>() {
    		public void handleEvent(GridEvent ge) {
    			int index = ge.rowIndex;
    			Service serv = g.getStore().getAt(index);
    			System.out.println("Clicking service with URI: "+serv.getUri());
    			displayService(serv.getUri());
    		}
    	});
    	System.out.println("Service grid: "+g);
    	return g;
    }

    /**
     * Displays the details of a service in a new box. 
     * It gets the details of the service from the server-side with the getService invocation.
     * Then the inputAdapter method is called to present the service options to the user.
     * 
     * @param serviceId  the identifier of the service
     */
    public static void displayService(final String serviceId) {
    	final Portlet portlet = new Portlet();
    	final ConsumptionPlatformModuleMainContainer instance = ConsumptionPlatformModuleMainContainer.getInstance();
    	final ProgressBar pb = new ProgressBar();
    	
    	AsyncCallback<ServiceAnnotations> callback = new AsyncCallback<ServiceAnnotations>() {
    		public void onFailure(Throwable caught) {
    			caught.printStackTrace();
  			    MessageBox.alert("Error", caught.getMessage(), null);    			
    		}

    		public void onSuccess(ServiceAnnotations result) {
    			portlet.remove(pb);
    			if (result!=null){
    		    	InfoConfig infoConfig = new InfoConfig("Opening service:", ServiceVisualizerManager.getTrimmedName(result.getName()));
    		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    		    	Info.display(infoConfig);
            		portlet.setHeading("Service: " + ServiceVisualizerManager.getTrimmedName(result.getName()));
    			}
    			
    			// Input Adapter
    			InputAdapter adapter = new InputAdapter();
    			portlet.add(new InputAdapter().generateServiceView(result));
    			portlet.removeFromParent();
    			instance.getPortal().add(portlet, 0);
    		}
    	};
    	if (instance != null) {
    		portlet.setHeading("Opening service: " + serviceId);
    		configListPanel(portlet);
    		portlet.setLayout(new FitLayout());
    		portlet.setHeight(250);
    		pb.auto();
    		portlet.add(pb);
    		instance.getPortal().add(portlet, 0);
    		
    		((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).getService(serviceId, getCookie(), callback);

    	} else {
    		System.out.println("instance: null");
    	}
    }

    private static String getCookie() {
		String cookie = Cookies.getCookie("persistentSessionID");
	    if(cookie==null){
	        Date today = new Date();
	        Timestamp ts = new Timestamp(today.getTime());
	        long tsTime = ts.getTime();
	    	String timestamp = Long.toString(tsTime);
	    	int random = ((int) Math.random() * 1000);
	    	cookie = timestamp+random;
	    	Cookies.setCookie("persistentSessionID",cookie,null);	
	    }
	    return cookie;
    }

    
    /**
     * Shows the information about the service, feedback, and permits adding more and bookmarking, etc.
     */
    public static void getDetailsForm(final ServiceAnnotations service, TabItem tabPanel)
    {
    	final String userId = ServiceVisualizerManager.getUserId();
    	final String userOpenId = ServiceVisualizerManager.getUserOpenId();
		final String itemId = service.getIdService();

		// Comments: List and grid where to display comments
		final Grid<Comment> commGrid = getCommentsGrid(null, null);
    	final List<CommentCli> comments = new ArrayList<CommentCli>();

		// Tags: List and tag cloud widget and setting of its parameters
    	final Set<Tag> tags = new HashSet<Tag>();
    	final TagCloud tc = new TagCloud();
    	tc.setCloudElemets(tags);
		final LabelField noTagsLabel = new LabelField("<i>This service has no tags associated</i>");
		
		// Details section
		//--------------------------------------------------------------------------------
		final ContentPanel detailsPanel = new ContentPanel();
		detailsPanel.setCollapsible(true);
		detailsPanel.setHeading("Service details");
		
		// Service description retrieved from the Service object data
		String description = service.getName(); 
		if (null != description)
			detailsPanel.addText(description+"<br/>");
		else
			detailsPanel.addText("<i>No description set</i><br/>");

		// Link to the service annotations in iServe
		detailsPanel.addText("<a target=\"_blank\"" +
				" href=\""+service.getIdService()+"\">See the service annotations in iServe</a><br/>");
		
		
		// Favorites
		// ---------
    	// Listener for the Fav button
		SelectionListener<ComponentEvent> favButtonListener = new SelectionListener<ComponentEvent>() {
			public void componentSelected(ComponentEvent ce) {

		    	InfoConfig infoConfig = new InfoConfig("Adding service for user "+userId, "Service "+service.getName()+" is being added to your favourites");
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
    	            	favFolder.add(new FavLink(ServiceVisualizerManager.getTrimmedName(service.getName()),
    	            			service.getIdService()+"##default", "service"));
    	            	// We add the old favLinks again:
    					List<FavLink> fList = favFolder.getChild(0).getChildren();
    					for(FavLink fl : fList){
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

	    Button  favButton = new Button("Add service to favourites", favButtonListener);
	    // If the user is not logged in, he cannot bookmark
		if (0 == "".compareTo(userId))
			favButton.setEnabled(false);
		detailsPanel.addButton(favButton);
		
		
		// * * * LUF callback 1/2: Retrieve Linked User Feedback callback: 
	  	AsyncCallback<String> getLUFCallback = new AsyncCallback<String>()
	  	{
		    public void onFailure(Throwable caught)
		    {
		    	throw new RuntimeException("Error retrieving Linked User Feedback");
		    }
		    public void onSuccess(String result)
		    {
				// * * * retrieve one comment callback: 
			  	AsyncCallback<String> getCommentCallback = new AsyncCallback<String>()
			  	{
				    public void onFailure(Throwable caught)
				    {
				    	throw new RuntimeException("Error retrieving comment information");
				    }
				    public void onSuccess(String result)
				    {
				    	try {
				    		Document customerDom = XMLParser.parse(result);
		    			    Element customerElement = customerDom.getDocumentElement();
		    			    XMLParser.removeWhitespace(customerElement);
		    			    
				    		if (result.contains("error"))	{
		    		    		MessageBox.alert("Error retrieving comment information", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
		    		    	} else {
				    			String text = customerElement.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
				    			String reviewer = customerElement.getElementsByTagName("reviewer").item(0).getFirstChild().getNodeValue();
					    		CommentCli comm = new CommentCli(text, reviewer); 
					    		comments.add(comm);
					    		getCommentsGrid(comments, commGrid);
		    		    	}
		    		    } catch (Exception e) {
		    	    	    e.printStackTrace();
		    		    } 
				    }
				};

		    	try {
		    		
		    		Document customerDom = XMLParser.parse(result);
    			    Element customerElement = customerDom.getDocumentElement();
    			    XMLParser.removeWhitespace(customerElement);
    			    
		    		if (result.contains("error"))	{
    		    		MessageBox.alert("Error retrieving Linked User Feedback", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
    		    	} else {
    		    		// ratings:
		    			String avgVote = customerElement.getElementsByTagName("ratingsAverage").item(0).getFirstChild().getNodeValue();
		    			String numVotes = customerElement.getElementsByTagName("ratingsNumber").item(0).getFirstChild().getNodeValue();
						if (Integer.parseInt(numVotes) < 1)
							((LabelField)(detailsPanel.getItemByItemId("serviceEvaluatedTextLabel"))).setText("This service has not been rated yet.");
						else {
							//Set the RatingWidget value
							int avg = (int) Double.parseDouble(avgVote);
//							avgRw.setRating(avg);
							if(avgVote.length()>4)
								avgVote = avgVote.substring(0,4);
							((LabelField)(detailsPanel.getItemByItemId("serviceEvaluatedTextLabel"))).setText("This service has a rating of <b>"+avgVote+"/5</b> out of <b>"+numVotes+"</b> ratings.");
						}

						// comments:
		    			NodeList commentUris = customerElement.getElementsByTagName("commentUri");
		            	for (int i = 0; i < commentUris.getLength(); ++i) {
		            		Node node = commentUris.item(i);
		            		Element element = (Element) node;
	                    	String commentUri = element.getFirstChild().getNodeValue();
							final String url = LUF_SERVICE_BASE + "comments/" + URL.encodeComponent(commentUri.split("comments/")[1]);	
						    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url, "GET", null, getCommentCallback);
						}
			    		
						// tags:
				    	tags.clear();
				    	boolean existTags = false;
		    			NodeList tagAggregates = customerElement.getElementsByTagName("tagAggregate");
		            	for (int i = 0; i < tagAggregates.getLength(); ++i) {
		            		existTags = true;
		            		Node node = tagAggregates.item(i);
		            		Element element = (Element) node;
	                    	String tagName = element.getElementsByTagName("tag").item(0).getFirstChild().getNodeValue();
	                    	String tagWeight = element.getElementsByTagName("times").item(0).getFirstChild().getNodeValue();

				     		tags.add(new Tag(tagName,tagName,Integer.parseInt(tagWeight)){public void onClick(){Info.display(this.getTitle(), "This tag has a weight of "+this.getWeight());}});
		            	}
				     	tc.clear();
				     	tc.setCloudElemets(tags);
				     	// If there are no tags for the service hide the widget and not the "no tags" label
				    	tc.setVisible(existTags);
				    	noTagsLabel.setVisible(!existTags);
				    	
    		    	}
    		    } catch (Exception e) {
    	    	    e.printStackTrace();
    		    } 
		    }
		};

		// * * * LUF callback 2/2: Retrieve Linked User Feedback (for a given user) callback: 
	  	AsyncCallback<String> getMyLUFCallback = new AsyncCallback<String>()
	  	{
		    public void onFailure(Throwable caught)
		    {
		    	throw new RuntimeException("Error retrieving My Linked User Feedback");
		    }
		    public void onSuccess(String result)
		    {
		    	try {
		    		Document customerDom = XMLParser.parse(result);
    			    Element customerElement = customerDom.getDocumentElement();
    			    XMLParser.removeWhitespace(customerElement);
    			    
		    		if (result.contains("error"))	{
    		    		MessageBox.alert("Error retrieving Linked User Feedback", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
    		    	} else {
    		    		// ratings:
		    			String avgVote = customerElement.getElementsByTagName("ratingsAverage").item(0).getFirstChild().getNodeValue();
		    			String numVotes = customerElement.getElementsByTagName("ratingsNumber").item(0).getFirstChild().getNodeValue();
						if (Integer.parseInt(numVotes) < 1)
							((LabelField)(detailsPanel.getItemByItemId("myServiceEvaluatedTextLabel"))).setText("<br/>You have not rated this service yet");
						else {
							//Set the RatingWidget value
							int avg = (int) Double.parseDouble(avgVote);
//							myRw.setRating(avg);
							if(avgVote.length()>4)
								avgVote = avgVote.substring(0,4);
							((LabelField)(detailsPanel.getItemByItemId("myServiceEvaluatedTextLabel"))).setText("<br/>Your rating on this service is <b>"+avgVote+"/5</b> out of <b>"+numVotes+"</b> ratings.");

						}
    		    	}
    		    } catch (Exception e) {
    	    	    e.printStackTrace();
    		    } 
		    }
		};

		
		// Ratings section
		// --------------------------------------------------------------------------------
    	// the average rating is not editable
    	avgRw.setReadOnly(true);
    	// label to textually express the service rating
    	final LabelField serviceEvaluatedText = new LabelField();
    	// Id to retrieve the label in the future
    	serviceEvaluatedText.setId("serviceEvaluatedTextLabel");
		serviceEvaluatedText.setText("This service has not been rated yet");
		// adding items to rating panel
		detailsPanel.add(serviceEvaluatedText);
		detailsPanel.add(avgRw);
		
    	// label to textually express the service rating
    	final LabelField myServiceEvaluatedText = new LabelField();
    	// Id to retrieve the label in the future
    	myServiceEvaluatedText.setId("myServiceEvaluatedTextLabel");
    	// Only logged users can rate services
    	if (0 != "".compareTo(userId))
    		myServiceEvaluatedText.setText("<br/> You have not rated this service yet<br/>");
    	else
    		myServiceEvaluatedText.setText("<br/> Only logged users can rate services<br/>");
    	// adding items to rating panel
    	detailsPanel.add(myServiceEvaluatedText);
    	// If the user is logged, he can express a rating
		if (userId.compareTo("")!=0)
   		{
	    	detailsPanel.addText("<br/>Rate the current item:");
			detailsPanel.add(myRw);
	   		try
			{
		   		SelectionListener rateListener = new SelectionListener<ComponentEvent>()
		   		{
					public void componentSelected(ComponentEvent ce)
					{
					  	AsyncCallback<String> rateItemCallback = new AsyncCallback<String>()
					  	{
						    public void onFailure(Throwable caught)
						    {
    	        		    	InfoConfig infoConfig = new InfoConfig("User rating","Error: Rating NOT saved!!");
    	        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	        		    	Info.display(infoConfig);
						    	throw new RuntimeException("Error: getDetailsForm() - rateItemCallback()");
						    }
						    public void onSuccess(String resp)
						    {
					    		Document customerDom = XMLParser.parse(resp);
			    			    Element customerElement = customerDom.getDocumentElement();
			    			    XMLParser.removeWhitespace(customerElement);
    	        		    	InfoConfig infoConfig = new InfoConfig("User rating",customerElement.getElementsByTagName("status").item(0).getFirstChild().getNodeValue());
    	        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	        		    	Info.display(infoConfig);
						    	if(customerElement.getElementsByTagName("status").item(0).getFirstChild().getNodeValue().compareTo("ok")==0){
	    	        		    	infoConfig = new InfoConfig("User rating","Rating saved");
	    	        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	        		    	Info.display(infoConfig);
						    	}
						    }
						};
						
						if ((userId.compareTo("")!=0)&&(userOpenId.compareTo("")!=0))
    					{
							// Getting expressed rating
							int selectedRating = myRw.getRating();
							if(selectedRating==0) {
    	        		    	InfoConfig infoConfig = new InfoConfig("Rating alert","You need to select a rating before...");
    	        		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
    	        		    	Info.display(infoConfig);
							} else {
								// Invoking the service to store the rating
	    						final String url_ratings = LUF_SERVICE_BASE + "ratings";
	    						String data = "itemId="+URL.encodeComponent(itemId)+"&userId="+URL.encodeComponent(userOpenId)+"&rating="+URL.encodeComponent(Double.toString(selectedRating));
	    					    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url_ratings, "POST", data, rateItemCallback);
	    					    // Updating textual label
	    					    ((LabelField)(detailsPanel.getItemByItemId("myServiceEvaluatedTextLabel"))).setText("<br/>You gave this service a rating of <b>"+selectedRating+"/5</b>");
	    					}
    					}
					}
		   		};
				
		   		detailsPanel.addText("<br/>");
		   		detailsPanel.addButton(new Button("Rate", rateListener));
		   		detailsPanel.addText("<br/>");
			}
			catch (Exception e)
			{
		    	InfoConfig infoConfig = new InfoConfig("Error", e.getMessage());
		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    	Info.display(infoConfig);
			    System.err.println("RateItem invocation failed: "+e+" "+e.getMessage());
			    e.printStackTrace();
			}
   		}


		// Comments section 
		// --------------------------------------------------------------------------------
		try
	    {
			if (0 != "".compareTo(userId))
			{
				detailsPanel.addButton(new Button("Add comment", new SelectionListener<ComponentEvent>()
				{
			      public void componentSelected(ComponentEvent ce)
			      {
			    	  final MessageBox box = MessageBox.prompt("New comment", "Please enter your comment:");
			    	  box.addCallback(new Listener<MessageBoxEvent>()
			    	  {   
			    		  public void handleEvent(MessageBoxEvent be)
			    		  {
			    			  final String comment = be.value;
			    			  InfoConfig infoConfig = new InfoConfig("New comment", "You added this comment: '{0}'", new Params(be.value));
			    			  infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
			    			  Info.display(infoConfig);
			    			  AsyncCallback<String> commentItemCallback = new AsyncCallback<String>()
			    			  {
			    				    public void onFailure(Throwable caught)
			    				    {
			    				    	InfoConfig infoConfig = new InfoConfig("Error", "Error invoking service to store comment");
			    				    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
			    				    	Info.display(infoConfig);
			    				    	throw new RuntimeException("Comments load fail");
			    				    }
			    				    public void onSuccess(String successCode)
			    				    {
			    				    	InfoConfig infoConfig = new InfoConfig("New comment", "Comment correctly saved");
			    				    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
			    				    	Info.display(infoConfig);
			    				    	commGrid.getStore().add(new Comment(userOpenId, comment));
			    				    }
			    			  };
				        	  
				        	  // If the user is logged, he can post a new comment
			    			  if ((userId.compareTo("")!=0)&&(userOpenId.compareTo("")!=0))
			    			  {
			    				  // Invoking the service to store the comment
			    				  final String url_comments = LUF_SERVICE_BASE + "comments";
			    				  String data = "itemId="+URL.encodeComponent(itemId)+"&userId="+URL.encodeComponent(userOpenId)+"&comment="+URL.encodeComponent(comment);
			    				  ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url_comments, "POST", data, commentItemCallback);
		  					  }
			    			  detailsPanel.show();
			    		  }
			    	  });
			      }
			    }));
			}
			// else, no comment allowed
			else
			{
				detailsPanel.addText("Only logged users can express comments<br/><br/>");
			}
	    }
		catch (Exception e)
		{
		    System.err.println(" getDetailsForm() posting comment failed...");
		}
		
		// Adding the grid to the panel
		detailsPanel.add(commGrid);

		
		// Tag section
		//---------------------------------------------------------------------------------
		detailsPanel.add(tc);
		detailsPanel.add(noTagsLabel);
		noTagsLabel.setVisible(false);
			
		final Vector<String> myTags = new Vector<String>();
		final LabelField tagsLabel = new LabelField("<ul></ul>");
		tagsLabel.setItemId("tagsLabel");
		if (0 != "".compareTo(userId))
		{
			detailsPanel.addText("<br/>Your tags for this service:<br/>");
			detailsPanel.add(tagsLabel);
			// Creating and adding the button to add tags
			detailsPanel.addButton(new Button("New tag", new SelectionListener<ComponentEvent>()
			{
		      public void componentSelected(ComponentEvent ce)
		      {
		    	  final MessageBox box = MessageBox.prompt("New tag", "Please enter your tag:");
		    	  box.addCallback(new Listener<MessageBoxEvent>()
		    	  {   
		    		  public void handleEvent(MessageBoxEvent be)
		    		  {
		    			  String tag = be.value;
		    			  InfoConfig infoConfig = new InfoConfig("New tag", "You added this tag: '{0}'. Do not forget to save tags when you've finished", new Params(tag));
		    			  infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    			  Info.display(infoConfig);
			        	  // Updating tags data structure
			        	  myTags.add(tag);
			        	  // Updating tags textual display
			        	  tagsLabel.setText(tagsLabel.getText().substring(0, tagsLabel.getText().length()-5)+"<li>"+tag+"</li></ul>");
		    		  }
		    	  });
		      }
		    }));
			
			detailsPanel.addText("<br/>");
			
			// Adding a button to save the previously inserted tags
			detailsPanel.addButton(new Button("Save tags", new SelectionListener<ComponentEvent>()
			{
				public void componentSelected(ComponentEvent ce)
				{
				  	AsyncCallback<String> tagAnItemCallback = new AsyncCallback<String>()
				  	{
					    public void onFailure(Throwable caught)
					    {
					    	InfoConfig infoConfig = new InfoConfig("Tags", "Error storing tags!");
					    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
					    	Info.display(infoConfig);
					    	throw new RuntimeException("getDetailsForm() - tagItem service load fail");
					    }
					    public void onSuccess(String successCode)
					    {
					    	InfoConfig infoConfig = new InfoConfig("Tags", "Tags "+successCode+" saved");
					    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
					    	Info.display(infoConfig);
					    	tagsLabel.setText("<ul></ul>");
					    	// Updating of TaggingWidget GUI
					    	String t;
					    	Tag exT;
					    	boolean flag = false;
					    	// Cycling over all my just inserted tags
					    	for (int i=0; i< myTags.size(); i++)
	   				     	{
					    		// I have not yet found my tag between services tags
					    		flag = false;
					    		t = myTags.elementAt(i);
					    		// Cycling over the service tags
					    		for (Iterator<Tag> j=tags.iterator(); j.hasNext(); )
					    		{
					    			exT = j.next();
					    			// if my specified tag already exists...
					    			if (0 == exT.getTitle().compareTo(t))
					    			{
					    				// ...increment its frequency
					    				exT.setWeight(exT.getWeight()+1);
					    				// I mark I found it
					    				flag = true;
					    				break;
					    			}
					    		}
					    		// If I didn't find my current tag...
					    		if (!flag)
				    			{
				    				// ...add a new Tag element to the TagCloud, with frequency = 1
		   				     		tags.add(new Tag(t,t,1){public void onClick(){;}});
				    			}
	   				     	}
					    	
					    	// clear the just inserted tags
					    	myTags.clear();
					    	noTagsLabel.setVisible(false);
					    	
   				     		tc.clear();
   				     		tc.setCloudElemets(tags);
   				     		tc.setVisible(true);
					    }
					};
	    			
					// If the user specified some new tags for the service
					if (0 != myTags.size())
					{
				    	InfoConfig infoConfig = new InfoConfig("Tags", "Storing tags");
				    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
				    	Info.display(infoConfig);
						String tags = "";
						boolean first = true;
						for (String t : myTags)
   				     	{
							if(first){
								first = false;
							} else {
								tags += ",";
							}
   				     		tags += t;
   				     	}
						// Invoking tags saving
						final String url_taggings = LUF_SERVICE_BASE + "taggings";
						String data = "itemId="+URL.encodeComponent(itemId)+"&userId="+URL.encodeComponent(userOpenId)+"&tags="+URL.encodeComponent(tags);
						((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url_taggings, "POST", data, tagAnItemCallback);
					}
					else
						MessageBox.alert("TAGS", "Before storing tags, you need to specify at least a new tab, pressing the button above", null);
	    		}
	    	}));
		}
		else
			detailsPanel.addText("Only logged users are allowed to tag services.");
		
		detailsPanel.addText("<br/>");
		
		
		// Add "details" panel containing the feedback information
		tabPanel.add(detailsPanel);
		detailsPanel.setVisible(true);
		
		
		// New REST Linked User Feedback Service (retrieves all the user feedback: ratings/comments/tags)
		final String url = LUF_SERVICE_BASE + "search?itemId=" + URL.encodeComponent(itemId);	
	    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url, "GET", null, getLUFCallback);

		if (userOpenId.compareTo("")!=0) {
			final String url_my = LUF_SERVICE_BASE + "search?itemId=" + URL.encodeComponent(itemId) + "&userId=" + URL.encodeComponent(userOpenId);	
		    ((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(url_my, "GET", null, getMyLUFCallback);
		}
				
		// Suggestions section
		//-------------------------------------------------------------------------------

		ContentPanel suggestionPanel = new ContentPanel();
		suggestionPanel.setBodyStyle("color: black;");
		suggestionPanel.setHeading("Suggestions");
		suggestionPanel.setEnabled(false);
		suggestionPanel.setButtonAlign(HorizontalAlignment.CENTER);
		suggestionPanel.setCollapsible(true);
		suggestionPanel.collapse();
		suggestionPanel.addText("If you are interested in this service, you could also check:<br/><br/>");
	    
		// Grid where to display comments
		final Grid<Service> suggestionsGrid = getSuggestionsGrid(null);
		suggestionPanel.add(suggestionsGrid);
		tabPanel.add(suggestionPanel);
		
		// Loading suggestions for this service
		//-------------------------------------------------------------------------------------------
		
		// Recommendation System callback: Retrieve suggestions callback: 
	  	AsyncCallback<String> getRecommendationsCallback = new AsyncCallback<String>()
	  	{
		    public void onFailure(Throwable caught)
		    {
		    	throw new RuntimeException("Error retrieving Recommendations");
		    }
		    public void onSuccess(String result)
		    {
		    	try {
		    		Document customerDom = XMLParser.parse(result);
    			    Element customerElement = customerDom.getDocumentElement();
    			    XMLParser.removeWhitespace(customerElement);
		    		if (result.contains("error"))	{
    		    		MessageBox.alert("Error retrieving Recommendations", customerElement.getElementsByTagName("error").item(0).getFirstChild().getNodeValue(), null);	
    		    	} else {
				    	InfoConfig infoConfig = new InfoConfig("Info", "Suggestions retrieved");
				    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
				    	Info.display(infoConfig);
		    			NodeList recommendedServices = customerElement.getElementsByTagName("recommendedService");
		    			if(recommendedServices.getLength()<1){
    			    		System.out.println("No recommendations for this service.");
		    			} else {
			            	for (int i = 0; i < recommendedServices.getLength(); ++i) {
			            		Node node = recommendedServices.item(i);
			            		Element element = (Element) node;
		                    	String service = element.getElementsByTagName("service").item(0).getFirstChild().getNodeValue();
		                    	String utility = element.getElementsByTagName("utility").item(0).getFirstChild().getNodeValue();
					    		addService2grid(service, utility, suggestionsGrid);
	    			    	}
						}
    		    	}
    		    } catch (Exception e) {
		    		MessageBox.alert("Error retrieving Recommendations", e.getMessage(), null);	
    	    	    e.printStackTrace();
    		    } 
		    }
		};

		// Recommendation Service invocation (per service or per service+user)
		if(userId.compareTo("")!=0){
			final String rsUrl = RECOMMENDATION_SERVICE_BASE + "by-user-and-service?num=3&service=" 
				+ URL.encodeComponent(itemId)
				+ "&user="
				+ URL.encodeComponent("http://profile.soa4all.org#"+userId);	
			((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(rsUrl, "GET", null, getRecommendationsCallback);
		} else {
			final String rsUrl = RECOMMENDATION_SERVICE_BASE + "by-service?num=3&service=" 
				+ URL.encodeComponent(itemId);	
			((ConsumptionClientServiceAsync) RemoteServiceHelper.getInstance().setupRemoteService((ServiceDefTarget) GWT.create(ConsumptionClientService.class))).interactRestServiceByUrl(rsUrl, "GET", null, getRecommendationsCallback);
		}
    }
    
    /**
     * Shows a set of comments in a grid format.
     * 
     * @param comments  a list of comments as returned by the feedback services 
     * @return      	the grid containing the comments to be shown 
     */
    private static Grid<Comment> getCommentsGrid(List<CommentCli> comments, Grid<Comment> grid)
    {
    	List<Comment> comments2show = new ArrayList<Comment>();
    	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    	GridCellRenderer<Service> link = new GridCellRenderer<Service>() {
    		public String render(Service model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Service> store) {
    			String str = model.get(property);
    			return str;
    		}
    	};
    	
    	GridCellRenderer<Comment> textStyle = new GridCellRenderer<Comment>() {
    		public String render(Comment model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Comment> store) {
    			return model.getText();
    		}
    	};
    	
    	GridCellRenderer<Comment> userStyle = new GridCellRenderer<Comment>() {
    		public String render(Comment model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Comment> store) {
    			return "<i>" + model.getUser() + "</i>";
    		}
    	};
    	
    	if (null != comments)
	    	// Converting comments in model for Comments
	    	for (CommentCli c : comments)
	    	{
	    		Comment cmnt = new Comment(c.user, c.text);
	    		//System.out.println("Comment: "+cmnt);
	    		comments2show.add(cmnt);
	    	}

    	// Creating grid columns
    	//-----------------------------------------------------------------
    	/**
    	 * TODO: Manage comment date!
    	 */
    	/*column = new ColumnConfig("date", "Date", 100);   
	    column.setAlignment(HorizontalAlignment.RIGHT);   
	    column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());   
	    configs.add(column);*/
    	
    	ColumnConfig column = new ColumnConfig();
    	column.setId("user");
    	column.setHeader("UserId");
    	column.setRenderer(userStyle);
    	column.setWidth(70);
    	column.setAlignment(HorizontalAlignment.CENTER);
    	configs.add(column);

    	column = new ColumnConfig();
    	column.setId("comm");
    	column.setHeader("Comment");
    	column.setRenderer(textStyle);
    	column.setWidth(300);
    	configs.add(column);
    	
	    // Adding columns to the grid
	    ColumnModel cm = new ColumnModel(configs);
	    // Adding data to the grid
    	ListStore<Comment> comm = new ListStore<Comment>();
    	comm.add(comments2show);

    	final Grid<Comment> g;
    	if (null == grid)
    	{
    		g = new Grid<Comment>(comm, cm);
    		g.setShadow(true);
    		g.setWidth(400);
    		g.setHeight(210);
    		g.setStripeRows(true);
    		g.setId("commentsGrid");
        	g.setAutoExpandColumn("user");
        	g.setBorders(true);
        	g.addListener(Events.CellClick, new Listener<GridEvent>()
        	{
        		public void handleEvent(GridEvent ge)
        		{
        			int index = ge.rowIndex;
        			Comment comm = g.getStore().getAt(index);
        			MessageBox.info(comm.getUser(), comm.getText(), null);
        		}
        	});
    	}
    	else
    	{
    		g = grid;
    		// remove all previous entries in the table
    		g.getStore().removeAll();
    		g.getStore().add(comments2show);
    	}
    	
    	if (null == comments)
    		g.getView().setEmptyText("No comments available for this service.");
    	
    	return g;
    }
    
    
    /**
     * Shows a set of comments in a grid format.
     * 
     * @param comments  a list of comments as returned by the feedback services 
     * @return      	the grid containing the comments to be shown 
     */
    private static Grid<Service> getSuggestionsGrid(Grid<Comment> grid)
    {
    	final List<Service> services2show = new ArrayList<Service>();
    	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    	GridCellRenderer<Service> uriStyle = new GridCellRenderer<Service>() {
    		public String render(Service model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Service> store) {
    			String str = "<span class=\"download\" style='color: green'>" + model.get(property) + "</span>";
    			return str;
    		}
    	};
    	
    	GridCellRenderer<Service> textStyle = new GridCellRenderer<Service>() {
    		public String render(Service model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Service> store) {
    			return model.get(property);
    		}
    	};
    	
    	GridCellRenderer<Service> numberStyle = new GridCellRenderer<Service>() {
    		public String render(Service model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Service> store) {
    			return "<font color=\"red\"><i>"+model.get(property)+"</i></font>";
    		}
    	};
    	
    	// Creating grid columns
    	//-----------------------------------------------------------------
    	ColumnConfig column = new ColumnConfig();
    	column.setId("name");
    	column.setHeader("Service");
    	column.setRenderer(uriStyle);
    	column.setWidth(210);
    	configs.add(column);

    	/*column = new ColumnConfig();
    	column.setId("desc");
    	column.setHeader("Description");
    	column.setRenderer(textStyle);
    	column.setWidth(150);
    	configs.add(column);*/

    	/*column = new ColumnConfig();
    	column.setId("uri");
    	column.setHeader("URI");
    	column.setRenderer(textStyle);
    	//column.setWidth(50);
    	configs.add(column);*/

    	column = new ColumnConfig();
    	column.setId("rating");
    	column.setHeader("%");
    	column.setRenderer(numberStyle);
    	column.setWidth(40);
    	configs.add(column);
    	
	    // Adding columns to the grid
	    ColumnModel cm = new ColumnModel(configs);
	    // Adding data to the grid
    	ListStore<Service> store = new ListStore<Service>();
    	store.add(services2show);
    	
    	// Creating grid
    	//--------------------------------------------------------------------------------
    	final Grid<Service> g;
		g = new Grid<Service>(store, cm);
		g.setShadow(true);
		g.setWidth(400);
		g.setHeight(210);
		g.setStripeRows(true);
		g.setId("suggestionsGrid");
    	//g.setAutoExpandColumn("name");
    	g.setBorders(true);
    	g.addListener(Events.CellClick, new Listener<GridEvent>()
    	{
    		public void handleEvent(GridEvent ge)
    		{
    			int index = ge.rowIndex;
    			Service serv = g.getStore().getAt(index);
    			// Opening selected service
    			displayService(serv.getUri());
    		}
    	});
    	
    	g.getView().setEmptyText("No suggestions available for this service.");
    	
    	return g;
    }
    
    private static void addService2grid(final String service, String utility, final Grid<Service> g)
    {
    	final double confidence = Double.parseDouble(utility)*10;
    	g.getStore().add(new Service(getTrimmedName(service), service, service, confidence));
    }
    
    public static String getUserId()
    {
    	// Current user's ID
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		String userId = module.getActiveUser();
		
		if ((null == userId)||(0 == "".compareTo(userId)))
			userId = "";
		
		return userId;		
    }
    
    public static String getUserOpenId()
    {
    	// Current user's OpenID
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		String userOpenId = module.getOpendIdUser();
		if ((null == userOpenId)||(0 == "".compareTo(userOpenId)))
			userOpenId = "";

		return userOpenId;
    }

	private static int getTrimmedNameIndex(String uri) {
		int separatorIdx = uri.indexOf('#');
		if (separatorIdx < uri.lastIndexOf('/'))
			separatorIdx = uri.lastIndexOf('/');
		if (separatorIdx < uri.lastIndexOf(':'))
			separatorIdx = uri.lastIndexOf(':');
		if (separatorIdx < 0)
			return -1;
		return separatorIdx + 1;
	}

	private static String getInParenthesis(String s) {
		int beginIdx = s.lastIndexOf('(');
		int endIdx = s.lastIndexOf(')');
		if ((beginIdx < 0)||(endIdx < 0)||(endIdx < beginIdx))
			return s;
		return s.substring(beginIdx+1,endIdx);
	}

	public static String getTrimmedName(String uriString) {
		int localNameIdx = getTrimmedNameIndex(uriString);
		if ((localNameIdx >= uriString.length())||(localNameIdx<0)) {
			return uriString;
		}
		return getInParenthesis(uriString.substring(localNameIdx));
	}
    
}