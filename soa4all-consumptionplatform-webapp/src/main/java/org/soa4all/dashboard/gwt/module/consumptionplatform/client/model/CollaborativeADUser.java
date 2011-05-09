/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model;

import java.util.Date;
import java.util.StringTokenizer;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * @author GDiMatteo
 * Model for a user in the collaborative advertisement environment
 * 
 * TODO: Manage user actions
 */
public class CollaborativeADUser extends BaseModel
{
	//private CollaborativeADAction[] actions;
	
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
		  /*actions = new CollaborativeADAction[3];
		  actions[0] = new CollaborativeADAction("click", new Date(new Long("1247198400000").longValue()),1);
		  actions[1] = new CollaborativeADAction("bookmark", new Date(20091008), 7);
		  actions[2] = new CollaborativeADAction("invoke", new Date(2009104810), 21);
		  */
		  set("actions", serialiseActions(actions));
	  }
	  
	  public void setSerialisedActions(String actions)
	  {
		  /*actions = new CollaborativeADAction[3];
		  actions[0] = new CollaborativeADAction("click", new Date(new Long("1247198400000").longValue()),1);
		  actions[1] = new CollaborativeADAction("bookmark", new Date(20091008), 7);
		  actions[2] = new CollaborativeADAction("invoke", new Date(2009104810), 21);
		  */
		  set("actions", actions);
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

	  public String getUri()
	  {
		  return (String) get("uri");
	  }
	  
	  /**
	   * Restituisce le azioni dell'utente deserializzandole da stringa ad array di azioni
	   */
	  public CollaborativeADAction[] getActions()
	  {
		  return deserialiseActions((String) get("actions"));
	  }
	  
	  /**
	   * Restituisce le azioni dell'utente serializzate in un'unica stringa
	   */
	  public String getSerialisedActions()
	  {
		  return (String) get("actions");
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
	  
	  private CollaborativeADAction[] deserialiseActions(String actionsSerialised)
	  {
		  //System.out.println("Deserialising actions string: "+actionsSerialised);
		  String tempString = actionsSerialised;
		  // counting the number of '#' in the serialised string
		  int numberOfActions = tempString.replaceAll("[^#]","").length();

		  String[] actions = new String[numberOfActions];
		  CollaborativeADAction[] objectActions = new CollaborativeADAction[numberOfActions];
		  
		  for (int i = 0 ; i < actions.length; i++)
		  {
			  // Saving the substring from 0 to the first '#'
			  actions[i] = actionsSerialised.substring(0, actionsSerialised.indexOf("#"));
			  //System.out.println("- "+actions[i]);
			  // Deleting the part of the string till the first '#', included
			  actionsSerialised = actionsSerialised.substring(actionsSerialised.indexOf("#")+1);
		  }
		  
		  String currAction, currDate, currCredits, currPublisher, currAffiliate;
		  
		  for (int i = 0 ; i < actions.length; i++)
		  {
			  try
			  {
				  currAction = actions[i].substring(0, actions[i].indexOf("$"));
				  actions[i] = actions[i].substring(actions[i].indexOf("$")+1);
				  
				  currDate = actions[i].substring(0, actions[i].indexOf("$"));
				  actions[i] = actions[i].substring(actions[i].indexOf("$")+1);
				  
				  currCredits = actions[i].substring(0, actions[i].indexOf("$"));
				  actions[i] = actions[i].substring(actions[i].indexOf("$")+1);
				  
				  currPublisher = actions[i].substring(0, actions[i].indexOf("$"));
				  actions[i] = actions[i].substring(actions[i].indexOf("$")+1);
				  
				  currAffiliate = actions[i];
				  //System.out.println("-> "+currAction+" "+currDate+" "+currCredits);
				  objectActions[i] = new CollaborativeADAction(currAction, currDate, new Integer(currCredits).intValue(), currPublisher, currAffiliate);
			  }
			  catch (Exception e){e.printStackTrace();objectActions[i] = null;}
		  }
		  return objectActions;
	  }

	  public String toString()
	  {
		  return getName() + " (" + getUsername() + ")-> " + getCredits();
	  }
}
