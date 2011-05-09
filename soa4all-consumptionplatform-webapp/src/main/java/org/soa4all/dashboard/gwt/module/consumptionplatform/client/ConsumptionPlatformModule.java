/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client;

import org.soa4all.coredashboard.gwt.core.shared.client.controller.AbstractModuleController;
import org.soa4all.coredashboard.gwt.core.shared.client.model.domain.AbstractModule;
import org.soa4all.coredashboard.gwt.core.shared.client.model.domain.DefaultModuleCategories;
import org.soa4all.coredashboard.gwt.core.shared.client.model.domain.ModuleCategory;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui.ConsumptionPlatformModuleMainContainer;

import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

/**
 * Profile module definition
 * 
 * @author Manuel J. Gallego <manuel.gallego@tieglobal.com>
 *
 */
public class ConsumptionPlatformModule extends AbstractModule {

    private ConsumptionPlatformModuleConstants consumptionPlatformModuleConstants = (ConsumptionPlatformModuleConstants) GWT.create(ConsumptionPlatformModuleConstants.class);

    public static int INFO_DISPLAY_TIME = 5000;
    
    public String getDescription() {

	return null;
    }

    public long getId() {
	return 0;
    }
    
    public String getName() {
	return getConsumptionPlatformModuleConstants().getModuleLabelText();
    }

    public ModuleCategory getModuleCategory() {
	return DefaultModuleCategories.getConsumptionPlatformModuleCategory();
    }

    public String getHomeButtonIconStyle() {
	return getConsumptionPlatformModuleConstants().getModuleHomeButtonIconStyle();
    }

    public LayoutContainer getMainContentContainer() {
    	return new ConsumptionPlatformModuleMainContainer();
    }

    public Image getPreviewImage() {
	return null;
    }

    public ConsumptionPlatformModuleConstants getConsumptionPlatformModuleConstants() {
	return consumptionPlatformModuleConstants;
    }

    public void setProfileModuleConstants(ConsumptionPlatformModuleConstants consumptionPlatformModuleConstants) {
	this.consumptionPlatformModuleConstants = consumptionPlatformModuleConstants;
    }

    public boolean isDashboardOverviewVisible() {
	return true;
    }

    public ToolItem getMainMenu() {
	ToolBar toolBar = new ToolBar();
// Add label items when they have functionality	
//	toolBar.add(new LabelToolItem("Consumption Platform Menu 1"));
	AdapterToolItem moduleMenu = new AdapterToolItem(toolBar);
	moduleMenu.setBorders(false);
	return moduleMenu;
    }

    public ToolItem getShortcutMenu() {
	ToolBar toolBar = new ToolBar();
// Add label items when they have functionality	
//	toolBar.add(new LabelToolItem("ConsumptionPlatformShortcut Menu 1"));
	AdapterToolItem shortcutMenu = new AdapterToolItem(toolBar);
	shortcutMenu.setBorders(false);
	return shortcutMenu;
    }

    public Controller getParentController() {
	return new AbstractModuleController(this) {
	};
    }

    public String getDashboardOverviewButtonIconStyle() {
	return getConsumptionPlatformModuleConstants().getModuleDashboardOverviewButtonStyle();
    }

    public String getDashboardOverviewButtonTitle() {
	return getConsumptionPlatformModuleConstants().getModuleDashboardOverviewButtonTitle();
    }

}
