/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
 *  http://www.tieGlobal.com
 *  info@tieGlobal.com

 *All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
 *The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import org.soa4all.dashboard.gwt.core.shared.client.controller.DashboardDesktopControllerHelper;
import org.soa4all.dashboard.gwt.core.shared.client.view.ui.widget.adaptiveportal.AdaptivePortal;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;

/**
 * Main UI GWT container for the Consumption module
 * 
 * @author Manuel J. Gallego <manuel.gallego@tieglobal.com>
 */
public class ConsumptionPlatformModuleMainContainer extends LayoutContainer {

    private static ConsumptionPlatformModuleMainContainer singleton = new ConsumptionPlatformModuleMainContainer();

    private DashboardDesktopControllerHelper dashboardDesktopControllerHelper = DashboardDesktopControllerHelper.getInstance();

    private AdaptivePortal portal;

    private Portlet searchPortlet;

    private final WidgetMyGoals widgetMyGoals = new WidgetMyGoals();

    private WidgetRecommendedGoals widgetRecommendedGoals = new WidgetRecommendedGoals(getUserId());

    private WidgetCategories widgetCategories = new WidgetCategories();

    private WidgetSearch widgetSearch = new WidgetSearch();

    private WidgetWizard widgetWizard = new WidgetWizard();

    private WidgetSpecialActions widgetSpecialActions = new WidgetSpecialActions();

    private WidgetManagement widgetManagement = new WidgetManagement();

    private Label label;

    private BorderLayoutData centerData;
    
    
	/**
     * Gets the singleton ConsumptionPlatform instance.
     */
    public static ConsumptionPlatformModuleMainContainer getInstance() {
	return singleton;
    }

    public Label getLabel() {
	return label;
    }

    public void setLabel(Label label) {
	this.label = label;
    }

    /**
     * Creates the layout with a left hand panel and an "adaptive" (configurable) portal in the main area
     */
    public ConsumptionPlatformModuleMainContainer() {
    	singleton = this;
    	setLayout(new BorderLayout());

    	LayoutContainer north = new LayoutContainer();
    	BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 10);

    	ContentPanel west = new ContentPanel();
    	west.setBodyBorder(false);
    	west.setHeading("Consumption Menu");
    	west.setLayout(new AccordionLayout());
    	
    	// retrieving logged user's id
    	final String userId = getUserId();
    	
    	// Search panel
    	ContentPanel navSearch = new ContentPanel();
    	navSearch.setHeading("Search");
    	navSearch.setBorders(false);
    	navSearch.setBodyStyle("fontSize: 12px; padding: 6px");
    	navSearch.add(widgetSearch);
    	west.add(navSearch);

    	// Panel for "Favorite" services (if the user is logged in)
    	if (0 != "".compareTo(userId))
    	{
    		widgetMyGoals.loadFavorites(userId);
	    	ContentPanel navMyGoals = new ContentPanel();
	    	navMyGoals.setHeading("My Actions");
	    	navMyGoals.setBorders(false);
	    	navMyGoals.setBodyStyle("fontSize: 12px; padding: 6px");
	    	navMyGoals.add(widgetMyGoals);
	    	west.add(navMyGoals);
    	}

    	// Panel for recommended services (also if the user is logged in)
    	if (0 != "".compareTo(userId))
    	{
	    	ContentPanel navGoals = new ContentPanel();
	    	navGoals.setHeading("Recommendations");
	    	navGoals.setBorders(false);
	    	navGoals.setBodyStyle("fontSize: 12px; padding: 6px");
	    	navGoals.add(widgetRecommendedGoals);
	    	west.add(navGoals);
    	}

    	// Categories panel
    	ContentPanel navCategories = new ContentPanel();
    	navCategories.setHeading("Browse Categories");
    	navCategories.setBorders(false);
    	navCategories.setBodyStyle("fontSize: 12px; padding: 6px");
    	navCategories.add(widgetCategories);
    	west.add(navCategories);

    	// Wizard panel
    	ContentPanel navWizard = new ContentPanel();
    	navWizard.setHeading("Wizard");
    	navWizard.setBorders(false);
    	navWizard.setBodyStyle("fontSize: 12px; padding: 6px");
    	navWizard.add(widgetWizard);
    	west.add(navWizard);

/*    	
    	ContentPanel navSpecial = new ContentPanel();
    	navSpecial.setHeading("Special Actions");
    	navSpecial.setBorders(false);
    	navSpecial.setBodyStyle("fontSize: 12px; padding: 6px");
    	navSpecial.add(widgetSpecialActions);
    	west.add(navSpecial);
*/
    	
    	// Management panel
/*    	
    	ContentPanel navManage = new ContentPanel();
    	navManage.setHeading("Management");
    	navManage.setBorders(false);
    	navManage.setBodyStyle("fontSize: 12px; padding: 6px");
    	navManage.add(widgetManagement);
    	west.add(navManage);
*/
    	
    	BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200, 100, 300);
    	westData.setMargins(new Margins(5, 0, 5, 5));
    	westData.setCollapsible(true);

    	this.portal = new AdaptivePortal(3);
    	this.portal.setBorders(true);
    	this.portal.setStyleAttribute("backgroundColor", "white");

    	centerData = new BorderLayoutData(LayoutRegion.CENTER);
    	centerData.setMargins(new Margins(5));
        
    	this.add(north, northData);
    	this.add(west, westData);
    	this.add(this.portal, centerData);    	

    	this.searchPortlet = new Portlet();
    }

    public BorderLayoutData getCenterData() {
	return centerData;
    }

    public AdaptivePortal getPortal() {
	return portal;
    }

    public Portlet getSearchPortlet() {
    	return searchPortlet;
	}
    
    public DashboardDesktopControllerHelper getDashboardDesktopControllerHelper() {
	return dashboardDesktopControllerHelper;
    }

    @Override
    protected void onRender(Element parent, int pos) {
	super.onRender(parent, pos);
    }

    public void setDashboardDesktopControllerHelper(DashboardDesktopControllerHelper dashboardDesktopControllerHelper) {
	this.dashboardDesktopControllerHelper = dashboardDesktopControllerHelper;
    }

    private String getUserOpenId()
    {
    	// Current user's OpenID
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		String userOpenId = module.getOpendIdUser();
		if ((null == userOpenId)||(0 == "".compareTo(userOpenId)))
			userOpenId = "";
		
		return userOpenId;
    }
    
    private String getUserId()
    {
    	// Current user's ID
		final ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		String userId = module.getActiveUser();
		if ((null == userId)||(0 == "".compareTo(userId)))
			userId = "";
		
		return userId;
    }

	/**
	 * @return the widgetMyGoals
	 */
	public WidgetMyGoals getBookmarkWidget() {
		return widgetMyGoals;
	}
}
