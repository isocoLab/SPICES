/*Copyright (c) 2009 TIE Holding N.V. ALL RIGHTS RESERVED.
*  http://www.tieGlobal.com
*  info@tieGlobal.com

*All source code and material of this file is proprietary to TIE.  No part of this file may be changed, copied, or transmitted in any form or for any purpose without the express prior written permission of *TIE. The content of this file may not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission. 
*The material embodied on this software is provided to you "as-is" and without warranty of any kind, express, implied or otherwise, including without limitation, any warranty of merchantability or fitness *for a particular purpose.  In no event shall TIE be liable to you or anyone else for any direct, special, incidental, indirect or consequential damages of any kind, or any damages whatsoever, including *without limitation, loss of profit, loss of use, savings or revenue, or the claims of third parties, whether or not TIE has been advised of the possibility of such loss, however caused and on any theory of *liability, arising out of or in connection with the possession, use or performance of this software.
*/
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * Class : widgetTagCloud
 * 
 */
public class WidgetTagCloud extends Composite {
	
	/**
	 * The widget Tag Cloud permits finding Goals related to a set of tags
	 */
	public WidgetTagCloud() {
		
		Image image  =  new Image();
		image.setPixelSize(204, 105);
//		image.setUrl("http://localhost:8888/org.soa4all.dashboard.gwt.module.consumptionplatform.public.images.consumptionplatform/flickr_tagcloud.png");
		image.setVisible(true);
		  
    	initWidget(image);
	}
}
