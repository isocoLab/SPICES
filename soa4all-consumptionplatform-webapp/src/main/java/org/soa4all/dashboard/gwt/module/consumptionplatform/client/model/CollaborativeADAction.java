package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * @author GDiMatteo
 * Collaborative user action model
 */
public class CollaborativeADAction extends BaseModel implements Serializable
{	
	public CollaborativeADAction() {}
	
	public CollaborativeADAction(String serialisedString)
	{
		
	}
	
	public CollaborativeADAction(String name, String date, int credits, String publisher, String affiliate)
	{
		set("action", name);
		set("date", date);
		set("credits", credits);
		set("publisher", publisher);
		set("affiliate", affiliate);;
	}

	/**
	 * @return the actionName
	 */
	public String getAction() {
		return (String)(get("action"));
	}
	
	/**
	 * @return the actionName
	 */
	public String getPublisher() {
		return (String)(get("publisher"));
	}
	
	/**
	 * @return the actionName
	 */
	public String getAffiliate() {
		return (String)(get("affiliate"));
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return (String)(get("date"));
	}

	/**
	 * @return the credits
	 */
	public int getCredits() {
		return ((Integer) get("credits")).intValue();
	}
	
	public String toString()
	{
		return getAction()+ " - "+ getDate() +" - " + getCredits() ;
	}
}
