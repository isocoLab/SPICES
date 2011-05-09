package org.soa4all.dashboard.gwt.module.consumptionplatform.client;

import org.soa4all.dashboard.gwt.core.client.util.CoreDashboardURLInfo;
import org.soa4all.dashboard.gwt.core.client.util.URLUtils;
import org.soa4all.dashboard.gwt.core.client.view.ui.widget.ui.DashboardTaskToolBar;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.RSManager;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;
//import org.soa4all.dashboard.gwt.theme.client.Soa4AllBlack;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


public class ConsumptionPlatformModuleEntryPoint implements EntryPoint {

	private ConsumptionPlatformModule module;

	public void onModuleLoad() {
		if (!Window.Location.getParameterMap().containsKey("rsadmin"))
			initUserGUI();
		else RSManager.initAdminGUI();
	}
	
	
	private void initUserGUI(){

		/*this.module = new ConsumptionPlatformModule();

		ContentPanel panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setBorders(false);
		panel.setLayout(new FillLayout());
	
		ToolBar toolBar = new ToolBar();
		toolBar.add(module.getMainMenu());
		panel.setTopComponent(toolBar);
		
		panel.add(module.getMainContentContainer());

		Viewport viewport = new Viewport();
		viewport.setLayout(new FillLayout());
		viewport.add(panel);
		RootPanel.get().add(viewport);*/
		
		//Setup the main viewport
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());

		ContentPanel desktop = new ContentPanel(new BorderLayout());
		desktop.setHeaderVisible(true);
		viewport.add(desktop);

		DashboardTaskToolBar dashboardTaskToolBar = new DashboardTaskToolBar();
		desktop.setTopComponent(dashboardTaskToolBar);

		ContentPanel mainContentPanel = new ContentPanel();
		mainContentPanel.setHeaderVisible(true);
		mainContentPanel.setBorders(true);
		mainContentPanel.setHeaderVisible(true);
		mainContentPanel.setLayout(new FitLayout());
		BorderLayoutData centerBorderLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
		centerBorderLayoutData.setMargins(new Margins(0));

		ConsumptionPlatformModule module = new ConsumptionPlatformModule();
		LayoutContainer spicesMainContainer = module.getMainContentContainer();

		mainContentPanel.add(spicesMainContainer);

		desktop.add(mainContentPanel, centerBorderLayoutData);

		if (module.getHomeButtonIconStyle() != null) {
		    dashboardTaskToolBar.setHomeButtonText(DashboardTaskToolBar.EMPTY_LABEL_TEXT);
		    dashboardTaskToolBar.setHomeButtonIconStyle(module.getHomeButtonIconStyle());
		} else {
		    dashboardTaskToolBar.setDefaultDashboardHomeButtonIcon();
		}

		dashboardTaskToolBar.setModuleMenu(module.getMainMenu());
		dashboardTaskToolBar.setShortcutMenu(module.getShortcutMenu());

//		if (module.isUserLoggedIn()) {
//		    setUserStatusButtonText(module.getActiveUser());
//		} else {
//		    setUserStatusButtonText(module.getName());
//		}
		
		loadPopularServiceFromeCommerceDashboard();
		
		RootPanel.get().add(viewport);

		mainContentPanel.layout();
		dashboardTaskToolBar.recalculate();
	}
	
    private static CoreDashboardURLInfo location = URLUtils.getLocation();
    private static String  SOA4ALL_ECOMMERCE_SERVICEID 		= "soa4all_eCommerceServiceId";
    
    private void loadPopularServiceFromeCommerceDashboard() {
    	if (location != null) {
    		String  serviceId    = location.getParameter(SOA4ALL_ECOMMERCE_SERVICEID);
	    	InfoConfig infoConfig = new InfoConfig("serviceId :", serviceId);
	    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
	    	Info.display(infoConfig);
    		if (serviceId !=null && serviceId.length() > 0) {
    			ServiceVisualizerManager.displayService(serviceId);
    		}
    	}	
    }
}