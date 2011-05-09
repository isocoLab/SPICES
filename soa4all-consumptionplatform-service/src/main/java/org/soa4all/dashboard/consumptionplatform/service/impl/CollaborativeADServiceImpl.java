/**
 * 
 */
package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.soa4all.dashboard.consumptionplatform.service.CollaborativeADService;
import org.soa4all.dashboard.consumptionplatform.service.model.CollaborativeADUser;
import org.soa4all.dashboard.consumptionplatform.service.model.CollaborativeADAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author GDiMatteo
 *
 */
public class CollaborativeADServiceImpl implements CollaborativeADService
{
	public static final String COLLABAD_SERVICE_URL = "http://demos.txt.it/soa4all/collaborativeAD/rest/";
	
	public String[] getUsers()
	{
		//System.out.println("CollaborativeADServiceImpl.getUser() server side");
		String[] usernames = null;
		try
		{
			// Contacting the CollabAD REST service
			URL url = new URL(COLLABAD_SERVICE_URL);
			//System.out.println("Opening connection");
		    HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    
		    Element el = parseInputStream(connection.getInputStream());
		    usernames = new String[el.getChildNodes().getLength()];
			NodeList nodes = el.getChildNodes();

			for (int i=0; i<el.getChildNodes().getLength(); i++)
			{
				usernames[i] = nodes.item(i).getTextContent();
				System.out.println("user = "+usernames[i]);
			}
			
		    //System.out.println("Closing connection");
		    
		    connection.disconnect();
		}
		catch (Exception e){e.printStackTrace();}
		
		return usernames;
	}
	
	public CollaborativeADUser getUser(String id)
	{
		CollaborativeADUser user = null;
		
		try
		{
			// Contacting the CollabAD REST service
			// url is base + userId
			URL url = new URL(COLLABAD_SERVICE_URL+"user/"+id);
		    HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    
		    Element el = parseInputStream(connection.getInputStream());
		    
		    NodeList nodes = el.getChildNodes();
		    
		    System.out.println("username = "+nodes.item(0).getTextContent());
		    System.out.println("fullname = "+nodes.item(1).getTextContent());
		    System.out.println("beans = "+nodes.item(2).getTextContent());
		    
		    // Creating new user
		    user = new CollaborativeADUser(
		    							   // 1st item = username
								    	   nodes.item(0).getTextContent(),
								    	   // 2nd item = full name
								    	   nodes.item(1).getTextContent(),
								    	   // 3rd item = collected beans
								    	   Integer.parseInt(nodes.item(2).getTextContent()),
								    	   COLLABAD_SERVICE_URL+id);
		    
		    // getting user actions
		    CollaborativeADAction[] actions = getActionsInfo(id);
		    // Setting them to the user
		    if (null != actions)
		    {
		    	user.setActions(actions);
		    	user.setCredits(actions.length);
		    }
		    	
		}
		catch (Exception e) {e.printStackTrace();}
		return user;
	}
	
	@Override
	public CollaborativeADAction[] getActionsInfo(String userId)
	{
		ArrayList<CollaborativeADAction> clicksInfo = null;
		CollaborativeADAction[] actions = null;
		try
		{
			// Contacting the CollabAD REST service
			// url is base + "clicksinfo" + userid
			URL url = new URL(COLLABAD_SERVICE_URL+"clicksinfo/"+userId);
			HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			Element el = parseInputStream(connection.getInputStream());
	   	    NodeList nodes = el.getChildNodes();
	   	    
			clicksInfo = new ArrayList<CollaborativeADAction>();

	   	    for (int i=0; i<nodes.getLength(); i++)
	   	    {
	   	    	// Getting the node elements
	   	    	NodeList children = nodes.item(i).getChildNodes();
	   	    	// Creating the new clickInfo
	   	    	CollaborativeADAction clickInfo = new CollaborativeADAction(children.item(2).getTextContent(), children.item(4).getTextContent(), -1, children.item(1).getTextContent(), children.item(3).getTextContent());
	   	    	// Adding it to the list
	   	    	clicksInfo.add(clickInfo);   	    	
	   	    }
	   	    
	   	    // Storing retrieved actions in an array
	   	    actions = new CollaborativeADAction[clicksInfo.size()];
	   	    int i = 0;
	   	    for (CollaborativeADAction action : clicksInfo)
	   	    	actions[i++] = action;
		}
		catch (FileNotFoundException fnfe) {System.out.println("Resource not found for user "+userId+". Resource was: "+COLLABAD_SERVICE_URL+"clicksinfo/"+userId);}
		catch (Exception e) {e.printStackTrace();}
		
		return actions;
	}
	
	/*
	 * Method creating a Document starting from InputStream,
	 * then returning Document Element
	 */
	private Element parseInputStream(InputStream is)
	{
		Document dom;

		// Getting the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try
		{
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();			
			// Parsing using builder to get DOM representation of the XML file
			dom = db.parse(is);
			Element el = dom.getDocumentElement();

			return el;
		}
		catch(Exception e) {e.printStackTrace();return null;}
	}
}
