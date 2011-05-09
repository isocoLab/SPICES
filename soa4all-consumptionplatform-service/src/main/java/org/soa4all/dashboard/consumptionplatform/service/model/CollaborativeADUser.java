/**
 * 
 */
package org.soa4all.dashboard.consumptionplatform.service.model;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * @author GDiMatteo
 * Model for a user in the collaborative advertisement environment
 */
public class CollaborativeADUser extends BaseModel
{
	public CollaborativeADUser() {}
	  
	  public CollaborativeADUser(String username, String fullname, int credits, String uri)
	  {
		  set("username", username);
		  set("fullname", fullname);
		  set("credits", credits);
		  set("uri", uri);
	  }
	  
	  public void setActions(CollaborativeADAction[] actions)
	  {
		  set("actions", serialiseActions(actions));
	  }

	  public String getUsername()
	  {
		  return (String) get("username");
	  }
	  
	  public String getName()
	  {
		  return (String) get("fullname");
	  }

	  public int getCredits()
	  {
		  return ((Integer) get("credits")).intValue();
	  }
	  
	  public void setCredits(int beans)
	  {
		  set("credits", beans);
	  }

	  public String getUri()
	  {
		  return (String) get("uri");
	  }
	  
	  public String getActions()
	  {
		  return (String) get("actions");
	  }

	  public String toString()
	  {
		  return getName();
	  }
	  
	  private String serialiseActions(CollaborativeADAction[] actions)
	  {
		  String serialised = "";
		  for (CollaborativeADAction a : actions)
		  {
			  serialised += a.getAction() + "$" + a.getDate() + "$"+a.getCredits() + "$"+a.getPublisher() + "$"+a.getAffiliate() + "#";
		  }
		  return serialised;
	  }
}
