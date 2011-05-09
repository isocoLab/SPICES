/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.CollaborativeADUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author GDiMatteo
 *
 */
//@RemoteServiceRelativePath("CollaborativeADClientService")
public interface CollaborativeADClientService extends RemoteService
{
	CollaborativeADUser[] getUsers();
	CollaborativeADUser getUser(String id);
}
