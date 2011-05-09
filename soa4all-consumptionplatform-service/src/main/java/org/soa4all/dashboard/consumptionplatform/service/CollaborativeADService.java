/**
 * 
 */
package org.soa4all.dashboard.consumptionplatform.service;

import org.soa4all.dashboard.consumptionplatform.service.model.CollaborativeADAction;
import org.soa4all.dashboard.consumptionplatform.service.model.CollaborativeADUser;

/**
 * @author GDiMatteo
 * Server-side interface for Collaborative Advertising service
 */
public interface CollaborativeADService
{
	public String[] getUsers();
	public CollaborativeADUser getUser(String id);
	public CollaborativeADAction[] getActionsInfo(String userId);
}
