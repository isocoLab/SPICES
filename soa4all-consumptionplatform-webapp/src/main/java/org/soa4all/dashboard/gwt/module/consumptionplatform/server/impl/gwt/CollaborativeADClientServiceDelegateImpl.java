/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.server.impl.gwt;

import org.soa4all.dashboard.consumptionplatform.service.CollaborativeADService;
import org.soa4all.dashboard.consumptionplatform.service.impl.CollaborativeADServiceImpl;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.interfaces.CollaborativeADClientService;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.CollaborativeADUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author GDiMatteo
 * Collaborative AD service client implementation
 */
public class CollaborativeADClientServiceDelegateImpl extends RemoteServiceServlet implements CollaborativeADClientService
{
	private CollaborativeADService collabADService;
	
    public CollaborativeADService getCollaborativeADService()
    {
    	return (null == collabADService)? new CollaborativeADServiceImpl() : collabADService;
    }
    
    public void setCollaborativeADService(CollaborativeADService collabADService)
    {
    	this.collabADService = collabADService;
    }

    public CollaborativeADUser[] getUsers()
    {
    	//System.out.println("In getUsers() client side - contacting service");
    	
    	// Get usernames from service
    	String[] usernames = getCollaborativeADService().getUsers();
    	System.out.println("Service responded");
    	CollaborativeADUser[] users = new CollaborativeADUser[usernames.length];
    	CollaborativeADUser currUser;
    	int i = 0;
    	// Foreach user...
    	for (String user : usernames)
    	{
    		// ...get single user data
			currUser = getUser(user);
			users[i++] = currUser;
		}
    	
    	return users;
    }
    
    public CollaborativeADUser getUser(String username)
    {
    	// Get user from service
    	org.soa4all.dashboard.consumptionplatform.service.model.CollaborativeADUser serverUser = getCollaborativeADService().getUser(username);
    	// Convert server model to client one
    	// TODO: add user ations
    	CollaborativeADUser clientUser = new CollaborativeADUser(serverUser.getUsername(), serverUser.getName(), serverUser.getCredits(), serverUser.getUri());
    	clientUser.setSerialisedActions(serverUser.getActions());
    	return clientUser;
    }

}
