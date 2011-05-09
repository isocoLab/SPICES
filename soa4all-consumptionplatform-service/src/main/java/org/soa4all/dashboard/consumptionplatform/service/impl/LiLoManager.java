package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamConstants;

import org.codehaus.jettison.AbstractXMLStreamReader;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;
import com.google.gwt.user.client.ui.HTML;
import org.mule.module.json.transformers.JsonToXml;
import net.sf.json.JSONObject;


public class LiLoManager {
	
	/**
     * Creates the URL to invoke a RESTful service, from the details of the service 
     * and the information received.
     * 
     * @param serviceId
     * @param params
     * @return URL to invoke
     */
    public String doLowering(ServiceAnnotationsSrv service, String operation, Map<String,String> params) {
    	
    	String url = service.getAuthData().getLocation();
    	
    	// Replace the API key in the relevant position:
    	String apiKey = service.getAuthData().getValueKey();
    	if(apiKey!=null) {
    		url = url.replace("$APIkey$", apiKey);
    	}

    	return url;
    }
	
	
	/**
     * It extracts the relevant concepts from the response.
     * 
     * @param serviceId
     * @param params
     * @param response
     * @return
     */
    public static Map<String,String> doLifting(String serviceId, String response) {
    	
    	Map<String,String> result = new HashMap<String,String>();
    	String xmlMessage = response;
    	
    	if((response.compareTo("")==0)||(response==null)){
        	result.put("response", "Error executing service.");
    	} else {
        	// To check errors:
        	result.put("response", response);
        	
        	// TODO: Use proper adapters ...
        	try {
        		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        		if (response.startsWith("{")) {
            	   	System.out.println(" ############# JSON transform 1");
        			JsonToXml transformer = new JsonToXml();
            	   	System.out.println(" ############# JSON transform 2");
        			xmlMessage = (String)transformer.doTransform(JSONObject.fromObject(response), "UTF-8");	
            	   	System.out.println(" ############# JSON transform 3");
        		}

        	   	System.out.println(" ############# post JSON 1");

        	   	Reader reader=new CharArrayReader(xmlMessage.toCharArray());
        	   	System.out.println(" ############# post JSON 2");
        	   	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));  
        	   	System.out.println(" ############# post JSON 3");
        	   	Element root = doc.getDocumentElement();
        	   	System.out.println(" ############# post JSON 4");
        	   	 	   	
        	   	String fatherTag = visitBranch(root, root.getTagName(), root.getTagName());
        	   
        	   	System.out.println(" ############# TAG PADRE: " + fatherTag);
        	   	
        	   	NodeList elements = root.getElementsByTagName(fatherTag);
        	   	for (int i = 0; i < elements.getLength(); ++i) {
        	   		Element element = (Element) elements.item(i);
        	   		NodeList childs = element.getChildNodes();
        	   		if (childs.getLength() > 0) {
	        	   		for (int j =0, k=0; j< childs.getLength(); ++j) {
		        	   		Node childNode = (Node) childs.item(j);
		        	   		if (childNode.getNodeType() == Element.ELEMENT_NODE) {
		        	   			System.out.println(" ############# FIELD: " + ((Element)childNode).getTagName());
		        	   			if (i==0) {
					    			System.out.println(" ############# FIELD COLUMN: " + ((Element)childNode).getTagName());
					    			result.put("field"+k, cleanPreffix(fatherTag) + ": " + cleanPreffix(((Element)childNode).getTagName()));
					    			k++;
					    		}
					    		
					    		NodeList nodeList = ((Element)childNode).getChildNodes(); 
					    		String value = "";
					    		if (nodeList.getLength() > 0) {
					    			value = nodeList.item(0).getNodeValue();
					    		}
					    		result.put(cleanPreffix(fatherTag) + ": " + cleanPreffix(((Element)childNode).getTagName())+"."+Integer.toString(i), value);					    		
		        	   		}
			    		}
        	   		} else {
        	   			if (i==0) {
			    			result.put("field"+i, cleanPreffix(fatherTag) + ": " + cleanPreffix(element.getTagName()));
			    		}	
			    		result.put(cleanPreffix(fatherTag) + ": " + cleanPreffix(element.getTagName())+Integer.toString(i), element.getNodeValue());        	   			
        	   		}
		    	}
        	} catch (Exception e) {
	    	    e.printStackTrace();
		    } 
	    	
	    	
    	}
    	
    	return result;
    }
 
    private static String visitBranch (Node node, String fatherTag, String childTag) {
    	String tag = fatherTag;
    	if (node!=null) {
    		System.out.println(" ############# NODE NAME: " + node.getNodeName());
    		NodeList childs = (NodeList) node.getChildNodes();
    		int i=0;
    		boolean elementFounded = false;
    		while (i<childs.getLength() && !elementFounded) {
    			Node grandsonNode = (Node) childs.item(i);
    			System.out.println(" ############# NODE CHILD: " + grandsonNode.getNodeName());
    			if (grandsonNode.getNodeType() == Element.ELEMENT_NODE && !(grandsonNode.getNodeName().contains("imports"))) {    				    				
    				tag = visitBranch(grandsonNode, childTag, ((Element)grandsonNode).getTagName());
    				elementFounded = (tag!=fatherTag);
    			} 
    			i++;
    		}    		
    	}
    	return tag;
    }
    
    private static String cleanPreffix (String tag) {
    	String result = tag;
    	if (tag.contains(":")) {
    		result = tag.substring(tag.indexOf(":")+1);
    	}
    	return result;
    }
}
