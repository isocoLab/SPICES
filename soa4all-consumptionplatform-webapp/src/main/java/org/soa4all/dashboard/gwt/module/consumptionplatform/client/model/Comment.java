/**
 * 
 */
package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * @author GDiMatteo
 * Comment base model for Grid display
 */
public class Comment extends BaseModel
{
	private String text;
    private String user;
	
	// TODO: add Date?
    public Comment(String user, String text) {
    	this.text = text;
    	this.user = user;
	}

	/**
	 * @return the comment text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the user who made the comment
	 */
	public String getUser() {
		return user;
	}
	
	public String toString() {
	    return getUser() + " - " + getText();
	  }
    
}
