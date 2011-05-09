/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces;

import java.util.List;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.CollaborativeADUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author GDiMatteo
 *
 */
public interface CollaborativeADClientServiceAsync
{
	void getUsers(AsyncCallback<CollaborativeADUser[]> callback);
	void getUser(String id, AsyncCallback<CollaborativeADUser> callback);
}
