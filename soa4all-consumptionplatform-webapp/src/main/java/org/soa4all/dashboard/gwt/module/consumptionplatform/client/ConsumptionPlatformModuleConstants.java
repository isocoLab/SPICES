/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Profile module internationalization properties
 * 
 * @author Manuel J. Gallego <manuel.gallego@tieglobal.com>
 *
 */
public interface ConsumptionPlatformModuleConstants extends Constants {

    @Key(value = "consumptionPlatformModule.label.text")
    public String getModuleLabelText();
    
    @Key(value = "consumptionPlatformModule.home.button.iconstyle")
    public String getModuleHomeButtonIconStyle();

    @Key(value = "consumptionPlatformModule.dashboard.overview.button.style")
    public String getModuleDashboardOverviewButtonStyle();

    @Key(value = "consumptionPlatformModule.dashboard.overview.button.title")
    public String getModuleDashboardOverviewButtonTitle();
}
