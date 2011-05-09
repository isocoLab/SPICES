package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.StringWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.ibm.wsdl.xml.WSDLReaderImpl;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPConstants;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import org.apache.abdera.i18n.templates.HashMapContext;
import org.apache.abdera.i18n.templates.Template;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.soa4all.dashboard.consumptionplatform.service.ConsumptionService;
import org.soa4all.dashboard.consumptionplatform.service.GoalConstants;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceOperationSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LiftingSchemaSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LoweringSchemaSrv;
import org.soa4all.dashboard.consumptionplatform.service.util.FileHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xerces.parsers.DOMParser;



/**
 * Service functionality for the Consumption Platform
 * 
 * @author Guillermo Alvaro and Ricardo Melero
 */
public class ConsumptionServiceImpl implements ConsumptionService, GoalConstants {

	// Storage Services URL
	public static final String STORAGE_SERVICE_ENDPOINT = "http://coconut.tie.nl:8080/storage";
  public static final String SAMPLE_REPOSITORY = "ConsumptionTaxonomies";
    
  public static String GROUNDING_SERVICE_ENDPOINT = "http://stronghold.ontotext.com:8080/groundingService";
	public static String SOAPVERSION = SOAPConstants.SOAP_1_1_PROTOCOL;
    
	public static String LUF_SERVICE_BASE = "http://soa4all.isoco.net/luf/api/";
    public static boolean getLUF = true;
    
    public static final int DECISION_TEMPERATURE = 20;
    public static final int DECISION_DISTANCE = 10;

	static Map<String,Map<String,String>> users = new HashMap<String,Map<String,String>>();
	// TODO: Remove this when retrieving services properly. It helps to retrieve specific service data
	static Map<String,String> lastfm = new HashMap<String,String>();
    
    private final static Logger log = Logger.getLogger(ConsumptionServiceImpl.class);

    private FileHelper fileHelper;

    public FileHelper getFileHelper() {
    	return fileHelper;
    }

    /**
     * TODO: Handle Goals in the future
     */
    public String[] getGoal(String id) {
    	log.debug("getGoal " + id);
		int idInt = Integer.parseInt(id);
		String file = goals[2][idInt].toString();
		String txt = null;
		try {
			txt = getFileHelper().readFile(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] goal = { goals[0][idInt], goals[1][idInt], txt };
		return goal;
    }


    public String getLabelsOfEntity() throws MalformedURLException, IOException {
	return null;
    }

    public String getLiteralsAttached2Entity() throws MalformedURLException, IOException {
	return null;
    }

    public String getRelationsFrom() throws MalformedURLException, IOException {
	return null;
    }

    public String getRelationsTo() throws MalformedURLException, IOException {
	return null;
    }

    public String getSubClasses() throws MalformedURLException, IOException {
	return null;
    }

    public void setFileHelper(FileHelper fileHelper) {
    	this.fileHelper = fileHelper;
    }

    /**
     * Returns the child nodes of a given one in the Categories Tree.
     * 
     *  @param uri  the identifier of the node
     *  @return     a list of child nodes
     */
    public List<String[]> getChilds(String uri) {
    	// TODO: Change all this when the Taxonomy Browsing widget is available
    	List<String[]> list = new ArrayList<String[]>();

    	if(uri.compareTo("null") == 0){
    		uri = "http://www.service-finder.eu/ontologies/ServiceCategories#Category";
    	}
    	
    	String requestURL = STORAGE_SERVICE_ENDPOINT 
        	+ "/repositories/"
        	+ SAMPLE_REPOSITORY;

        String sparql = "PREFIX s: <http://www.w3.org/s#> "
        	+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
        	+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
        	+ "SELECT ?category ?label ?cat2 "
        	+ "WHERE { { ?category rdfs:subClassOf <"+uri+"> . } " 
            + "{ ?category rdfs:label ?label . } "
        	+ "OPTIONAL { ?cat2 rdfs:subClassOf ?category . } }";

        GetMethod getMtd = new GetMethod(requestURL);
    	HttpClient httpclient = new HttpClient();
        
        try {
        	log.debug("Request URL: " + requestURL);
       		getMtd.addRequestHeader("sparql-q", sparql);

       		int result = httpclient.executeMethod(getMtd);
       		log.debug("Response status code: " + result);
        	if (result != 200) {
        		log.error("Error message: ");
        		log.error(getMtd.getResponseHeader("Error"));
        	}
        	else {
        		log.debug("Response OK");
        		list = parseChildsResponse(getMtd.getResponseBodyAsString());
        	}
    	} catch (Exception e) {
    	    System.err.println("Exception parsing reasoning result");
    	    e.printStackTrace();
        } finally {
        	getMtd.releaseConnection();
        }

    	return list;
    }
    
    /**
     * Extracts the relevant information returned by the storage services for the child nodes.
     * 
     * @param response
     * @return
     * @throws Exception
     */
    public static List<String[]> parseChildsResponse(String response) throws Exception {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	List<String[]> elements = new ArrayList<String[]>();
    
    	// Create the builder and parse the file
    	Reader reader=new CharArrayReader(response.toCharArray());
    	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
    	Element root = doc.getDocumentElement();
    	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
    	NodeList childNodes = resultsNode.getChildNodes();
    	for (int i = 0; i < childNodes.getLength(); ++i) {
			String hasGrand = "false";
			String uri = "null";
			Node nNode = childNodes.item(i);
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    			Element eElement = (Element) nNode;
    			Element binding1 = (Element) eElement.getElementsByTagName("binding").item(0);
    			String binding1name = binding1.getAttribute("name");
    			if(binding1name.compareTo("cat2")==0){
    				hasGrand = "true";
    				uri = eElement.getElementsByTagName("uri").item(1).getChildNodes().item(0).getNodeValue();
    			} else {
    				uri = eElement.getElementsByTagName("uri").item(0).getChildNodes().item(0).getNodeValue();
    			}
    			if(eElement.getElementsByTagName("literal").item(0).getChildNodes().getLength()>0){
    				String label = eElement.getElementsByTagName("literal").item(0).getChildNodes().item(0).getNodeValue();
    				String[] element = { uri, label, hasGrand };
    				boolean include = true;
    				for (String[] el : elements) {
    					if(el[0].compareTo(uri)==0){
    						include = false;
    					}
    				}
    				if(include){
    					elements.add(element);
    				}
    			}
    		}
		}
    	return elements;
    }
    
    /**
     * 
     */
    public ServiceAnnotationsSrv getService(String id, String permanentSessionId) {
    	// TODO: getRatings, comments, etc.??

    	// Log action:
    	if(permanentSessionId.compareTo("")!=0){
            NameValuePair[] actionData = {
                	new NameValuePair("action", "ItemSelection"),
                	new NameValuePair("item", id),
                	new NameValuePair("persistentSessionId", permanentSessionId),
                };
            Thread t = new Thread(new AuditingLogger(actionData));
            t.start();
    	}
    	
    	ServiceAnnotationsSrv serviceInfo = RDFToModelManager.extractInformation(id);    	
    	return serviceInfo;
    }

    public Map<String,String> getUser(String id) {
    	
    	// TODO: Remove duplication of FireEagle tokens
    	String fireEagleServiceId = "http://iserve.kmi.open.ac.uk/resource/services/2597a4d8-a914-403b-a9aa-6a22d24e5fd9#FireEagle";
    	    	
		Map<String,String> user = new HashMap<String,String>();
		String userId = "http://soa4all.eu/users/user1";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);
	
		user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user2";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);
	
		user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user3";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

		user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user4";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);
    	
    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user5";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user6";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user7";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user8";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user9";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = new HashMap<String,String>();
		userId = "http://soa4all.eu/users/user10";
		user.put("id", userId);
		user.put(fireEagleServiceId+".oauth_token", "xxx");
		user.put(fireEagleServiceId+".oauth_token_secret", "xxx");
    	users.put(userId, user);

    	user = users.get(id);
    	return user;
    }

    /**
     * Queries the discovery component for relevant items that satisfy the given string.
     * 
     * @param response
     * @return
     * @throws Exception
     */
    public List<String[]> searchString(String query) {
    	
    	log.setLevel(Level.DEBUG);
    	log.debug("search string: "+query);

    	// TODO: We need to call the Discovery component with the suitable format.
    	// Change the harcoded response by a proper call.
    	if (query==null) {
    		query = "";
    	}
    	List<String[]> servicesList = new ArrayList<String[]>();

    	// Retrieving services from iServe
    	try {
    		String sparqlQuery = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
        		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
        		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
        		+ " SELECT ?service ?labelSer "
        		+ " WHERE " 
        		+ " { "
        		+ " ?service rdf:type wsl:Service . "
        		+ " FILTER regex(str(?service), \""+query+"\", \"i\" ) . "
        		+ " OPTIONAL { ?service rdfs:label ?labelSer . } "
        		+ " }";
    		
    		
        	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(sparqlQuery);
        	log.debug("response: "+response);

        	// Put the response in the expected format:
        	String result = "";
        	try {
            	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            	Reader reader=new CharArrayReader(response.toCharArray());
            	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
            	
            	result = "<result><head>\r\n";

            	Element root = doc.getDocumentElement();
            	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
            	NodeList childNodes = resultsNode.getChildNodes();
            	
            	Map<String,String> idList = new HashMap<String,String>();

            	for (int i = 0; i < childNodes.getLength(); ++i) {
            		Node nNode = childNodes.item(i);
                	String id = "";
                	String defined = "";
                	String label = "";
            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            			Element eElement = (Element) nNode;
                    	NodeList bindingNodes = eElement.getElementsByTagName("binding");
                    	for (int j = 0; j < bindingNodes.getLength(); ++j) {
                    		Node bNode = bindingNodes.item(j);
                    		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
                    			Element bElement = (Element) bNode;
                    			String nameAtt = bElement.getAttribute("name"); 
                    			if(nameAtt.compareTo("service")==0){
                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
                        			if(uri!=null){
                        				String s = uri.getChildNodes().item(0).getNodeValue();
                        				id = s;
                        				defined = id;
                            		}
                    			} else if(nameAtt.compareTo("labelSer")==0){
                        			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
                        			if(literal!=null){
										Node litVal = literal.getChildNodes().item(0);
										if(litVal!=null) {
	                        				String s = literal.getChildNodes().item(0).getNodeValue();
	                        				label = s;
										}
                            		}
                    			}
                    		}
                    	}
                    	if(id.compareTo("")!=0) {
                    		if(label.compareTo("")==0) {
                    			label = id;
                    		}
                    		// We retrieve each service Id only once (to deal with the duplicate label problem)
                    		if(idList.get(id)==null){
                            	result += "<service id=\""+id+"\" defined=\""+defined+"\">"+label+"</service>\r\n";
                            	idList.put(id, "used");
                    		} 
                    	}
        			}
        		}
            	result += "</head></result>";
            	log.debug("result: "+result);

        	} catch(Exception err) {
        		log.error("SPARQL error : " + err.getMessage());
        	}
        	
    	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    	    // Create the builder and parse the file
    	    Reader reader = new CharArrayReader(result.toCharArray());
    	    Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));

    	    Element root = doc.getDocumentElement();
    	    Element head = (Element) root.getElementsByTagName("head").item(0);
    	    NodeList childNodes = head.getChildNodes();

    	    //Try to get the ratings for each service, unless the service has errors
    	    getLUF = true;
    	    for (int i = 0; i < childNodes.getLength(); ++i) {
    	    	Node nNode = childNodes.item(i);
    	    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    	    		Element eElement = (Element) nNode;
    	    		String value = eElement.getChildNodes().item(0).getNodeValue();
    	    		String defined = eElement.getAttribute("defined");
    	    		String id = eElement.getAttribute("id");
    	    		// We also get the average rating
    	    	    String average = getAverageRating(id);
    	    		String[] serviceInfo = { value, defined, id, average };
    	    		servicesList.add(serviceInfo);
    	    	}
    	    }

    	} catch (SAXException e) {
    	    System.err.println("SAXException parsing reasoning result");
    	    e.printStackTrace();
    	} catch (ParserConfigurationException e) {
    	    System.err.println("ParserConfigurationException parsing reasoning result");
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    System.err.println("IOException parsing reasoning results");
    	    e.printStackTrace();
    	}
    	
    	
    	
    	return servicesList;
    }


    public String getAverageRating(String id) {
   	
    	String averageRating = "3.0";

    	if(getLUF){
	   		try {
	   			String requestURL = LUF_SERVICE_BASE 
	   				+ "search?itemId="
	   				+ URLEncoder.encode(id,"UTF-8");
	
	   			GetMethod getMtd = new GetMethod(requestURL);
	   			HttpClient httpclient = new HttpClient();
	    	
	   			try {
	   				int result = httpclient.executeMethod(getMtd);
	   				log.debug("Response status code: " + result);
	   				if (result != 200) {
	   					log.error("Error message: ");
						log.error(getMtd.getResponseHeader("Error"));
					}
					else {
						log.debug("Response OK");
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            	Reader reader=new CharArrayReader(getMtd.getResponseBodyAsString().toCharArray());
		            	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
		            	Element root = doc.getDocumentElement();
		            	
		            	NodeList averageNodeList = root.getElementsByTagName("ratingsAverage");
		            	if(averageNodeList.getLength()>0){
			    			averageRating = averageNodeList.item(0).getFirstChild().getNodeValue();
			    			log.debug("Average rating: "+averageRating);
		            	} else {
		            		log.debug("Average rating not found.");
		            	}
		        	}
		    	} catch (Exception e) {
		    	    System.err.println("Exception parsing http response getting rating.");
		    	    // We don't continue trying to get the ratings for the other services
		    	    getLUF = false;
		    	    e.printStackTrace();
		        } finally {
		        	getMtd.releaseConnection();
		        }
	    	} catch (Exception e) {
	    	    System.err.println("Exception encoding URL for getting rating.");
	    	    e.printStackTrace();
	        }
		
	    	// If there is no data, we assign a 3.0 to be shown
	    	if(averageRating.compareTo("0.0")==0)
	    		averageRating = "3.0";
	    	if(averageRating.length()>4)
	    		averageRating = averageRating.substring(0,4);
    	}

    	return averageRating;
    }

    
    /**
     * Queries iServe for the services with a given category.
     * 
     * @param response
     * @return
     * @throws Exception
     */
    public List<String[]> searchCategory(String category) {

    	log.setLevel(Level.DEBUG);
    	log.debug("search category: "+category);

    	if (category==null) {
    		category = "";
    	}
    	List<String[]> servicesList = new ArrayList<String[]>();

    	// Retrieving services from iServe
    	try {

    		String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
        		+ " PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> "
        		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
        		+ " SELECT ?service "
        		+ " WHERE " 
        		+ " { "
        		+ " ?service rdf:type wsl:Service . "
        		+ " ?service sawsdl:modelReference <"+category+"> . "
        		+ " }";
        	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(sparqlQuery);
        	log.debug("response: "+response);

        	// Put the response in the expected format:
        	String result = "";
        	try {
            	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            	Reader reader=new CharArrayReader(response.toCharArray());
            	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
            	
            	result = "<result><head>\r\n";

            	Element root = doc.getDocumentElement();
            	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
            	NodeList childNodes = resultsNode.getChildNodes();
            	
            	Map<String,String> idList = new HashMap<String,String>();

            	for (int i = 0; i < childNodes.getLength(); ++i) {
            		Node nNode = childNodes.item(i);
                	String id = "";
                	String defined = "";
                	String label = "";
            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            			Element eElement = (Element) nNode;
                    	NodeList bindingNodes = eElement.getElementsByTagName("binding");
                    	for (int j = 0; j < bindingNodes.getLength(); ++j) {
                    		Node bNode = bindingNodes.item(j);
                    		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
                    			Element bElement = (Element) bNode;
                    			String nameAtt = bElement.getAttribute("name"); 
                    			if(nameAtt.compareTo("def")==0){
                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
                        			if(uri!=null){
                        				String s = uri.getChildNodes().item(0).getNodeValue();
                        				defined = s;
                            		}
                    			} else if(nameAtt.compareTo("service")==0){
                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
                        			if(uri!=null){
                        				String s = uri.getChildNodes().item(0).getNodeValue();
                        				id = s;
                            		}
                    			} else if(nameAtt.compareTo("labelSer")==0){
                        			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
                        			if(literal!=null){
                        				String s = literal.getChildNodes().item(0).getNodeValue();
                        				label = s;
                            		}
                    			}
                    		}
                    	}
                    	if(id.compareTo("")!=0) {
                    		if(label.compareTo("")==0) {
                    			label = id;
                    		}
                    		if(defined.compareTo("")==0) {
                    			defined = id;
                    		}
                    		// We retrieve each service Id only once (to deal with the duplicate label problem)
                    		if(idList.get(id)==null){
                            	result += "<service id=\""+id+"\" defined=\""+defined+"\">"+label+"</service>\r\n";
                            	idList.put(id, "used");
                    		} 
                    	}
        			}
        		}
            	result += "</head></result>";
            	log.debug("result: "+result);

        	} catch(Exception err) {
        		log.error("SPARQL error : " + err.getMessage());
        	}
        	
    	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    	    // Create the builder and parse the file
    	    Reader reader = new CharArrayReader(result.toCharArray());
    	    Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));

    	    Element root = doc.getDocumentElement();
    	    Element head = (Element) root.getElementsByTagName("head").item(0);
    	    NodeList childNodes = head.getChildNodes();

    	    for (int i = 0; i < childNodes.getLength(); ++i) {
    	    	Node nNode = childNodes.item(i);
    	    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    	    		Element eElement = (Element) nNode;
    	    		String value = eElement.getChildNodes().item(0).getNodeValue();
    	    		String defined = eElement.getAttribute("defined");
    	    		String id = eElement.getAttribute("id");
    	    		// We also get the average rating
    	    	    String average = getAverageRating(id);
    	    		String[] serviceInfo = { value, defined, id, average };
    	    		servicesList.add(serviceInfo);
    	    	}
    	    }

    	} catch (SAXException e) {
    	    System.err.println("SAXException parsing reasoning result");
    	    e.printStackTrace();
    	} catch (ParserConfigurationException e) {
    	    System.err.println("ParserConfigurationException parsing reasoning result");
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    System.err.println("IOException parsing reasoning results");
    	    e.printStackTrace();
    	}
    	
    	return servicesList;
    }


    /**
     * It performs the invocation of the a Rest service and gets the response.
     * 
     * @param url
     * @param method
     * @return Response of the service
     */
    public Map<String, String> executeRestService(ServiceAnnotationsSrv service, Map<String,String> params, String operation, String permanentSessionId) {
    	
    	if(permanentSessionId.compareTo("")!=0){
            NameValuePair[] actionData = {
                	new NameValuePair("action", "ItemInvocation"),
                	new NameValuePair("item", service.getIdService()),
                	new NameValuePair("persistentSessionId", permanentSessionId),
                };
            Thread t = new Thread(new AuditingLogger(actionData));
            t.start();
        }

    	// Interact Service
    	String result = interactRestService(service, params, operation);    	
    	log.info(" #### RESPONSE (Rest Service):  " + result);
    	
    	// Lifting
    	return LiLoManager.doLifting(service.getIdService(), result);    	
   	}
    
    /**
     * It performs the invocation of the a WSDL service and gets the response.
     * 
     * @param url
     * @param method
     * @return Response of the service
     */
    public Map<String,String> executeWsdlService(ServiceAnnotationsSrv service, Map<String,String> params, String operation, String permanentSessionId) {
    	
    	String wsdlURI= service.getUri();
    	String serviceName = service.getNameLabel();
    	ServiceOperationSrv serviceOp = service.getOperation(operation);
    	String operationName =  serviceOp.getNameLabel();
    	String lowering_schema_url = (LoweringSchemaSrv)serviceOp.getLowering()!=null ? ((LoweringSchemaSrv)serviceOp.getLowering()).getSchemaReference() : ""; 
    	String lifting_schema_url = (LiftingSchemaSrv)serviceOp.getLifting()!=null ? ((LiftingSchemaSrv)serviceOp.getLifting()).getSchemaReference() : "";
    	String inputURI = (serviceOp.getInputs()).get(0).getUri();
    	String inputValue = params.get(inputURI);    	
    	String uriRoot = inputURI.substring(0, inputURI.lastIndexOf("/")+1);
    	String concept = inputURI.substring(inputURI.lastIndexOf("/")+1);
    	
    	String lifted_input_header = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "+
								    "xmlns:oms=\"" + uriRoot + "\" " + 
								    "xmlns:owl=\"http://www.w3.org/2002/07/owl#\" " +
								    "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
								    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" "+
								    "xmlns:daml=\"http://www.daml.org/2001/03/daml+oil#\" " +
								    "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"> "+
								    "<rdf:Description rdf:about=\"http://anonymous.generated/iOnto#_oms" + concept + Math.random() + "\"> " +			       
								    "<rdf:type rdf:resource=\"" +  inputURI + "\"/> ";
    	
    	String lifted_input = lifted_input_header;
    	String lifted_output = "";
    	String lowered_input = "";
    	String lowered_output = ""; 
    	
		String format = "rdf/xml-abbrev";
		String lifting_requestURL = GROUNDING_SERVICE_ENDPOINT + "/doLifting/";
		String lowering_requestURL = GROUNDING_SERVICE_ENDPOINT + "/doLowering/";
  
		try {
    		
    	DOMParser parser = new DOMParser();
        parser.parse(lowering_schema_url);
        Document doc = parser.getDocument();
        Element root = doc.getDocumentElement();
        
        NodeList templates = root.getElementsByTagName("xsl:call-template");
        Node template = null;

        for (int i=0; i<templates.getLength(); ++i){
        	template = templates.item(i);       	
        	NodeList childNodes = template.getChildNodes();
        	for (int j = 0; j < childNodes.getLength(); ++j) {        		
        		Node nNode = childNodes.item(j);
	    		if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName().equals("xsl:with-param")) {
	    			Node param = nNode.getAttributes().getNamedItem("name"); 
	    			if (param!=null) {	    				
	    				String name = param.getNodeValue();
	    				lifted_input += "<oms:" + name + "> " + inputValue + "</oms:" + name + "> "; 
	    			}
	    		}
        	}        	
        }
        
        lifted_input += "</rdf:Description> " +
					    "<rdf:Description rdf:about=\"http://anonymous.generated/iOnto\"> " +
					      "<owl:imports rdf:resource=\"" + uriRoot + "\"/> " +
					      "<rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#Ontology\"/> " +
					    "</rdf:Description> " +
					    "</rdf:RDF>";
        	
      	System.out.println(" PROCESSING LILO LIFTED INPUT: " + lifted_input);
        
		HttpClient httpclient = new HttpClient();
        		
		// Invoke Lowering Service 		
     	PostMethod LOWpostMtd = new PostMethod(lowering_requestURL);
        LOWpostMtd.addRequestHeader("xslt-url",URLEncoder.encode(lowering_schema_url, "UTF-8"));
        RequestEntity RE = new ByteArrayRequestEntity(lifted_input.getBytes(), "xml/application");
        LOWpostMtd.setRequestEntity(RE);
        
        int LOWresult = httpclient.executeMethod(LOWpostMtd);

        System.out.println("LOWERING SERVICE response status code: " + LOWresult);
        if (LOWresult != 200) {
            System.out.println("LOWERING SERVICE Error message: ");
            System.out.println(LOWpostMtd.getResponseHeader("Error"));
            lowered_input = "";
        }
        else {
            System.out.println("LOWERING Response OK");
            lowered_input = LOWpostMtd.getResponseBodyAsString();
            // NOW WE CAN INVOKE THE WSDL SERVICE:
            System.out.println("LOWERED input = " + lowered_input);
            lowered_output = interactWsdlService(wsdlURI, serviceName, operationName, lowered_input);		
	        System.out.println("LOWERED output = " + lowered_output);
            
        }
        LOWpostMtd.releaseConnection();
	    
        // FINALLY, INVOKE THE LIFTING SERVICE
		if (lowered_output != "") {
			// INVOKE LIFTING SERVICE			
			PostMethod LIFTpostMtd = new PostMethod(lifting_requestURL);
	        LIFTpostMtd.addRequestHeader("xslt-url",URLEncoder.encode(lifting_schema_url, "UTF-8"));
	        LIFTpostMtd.addRequestHeader("format", URLEncoder.encode(format, "UTF-8"));
	        RequestEntity RE2 = new ByteArrayRequestEntity(lowered_output.getBytes(), "xml/application");
	        
	        LIFTpostMtd.setRequestEntity(RE2);
	        int LIFTresult_code = httpclient.executeMethod(LIFTpostMtd);
	
	        System.out.println("LIFTING SERVICE Response status code: " + LIFTresult_code);
	        if (LIFTresult_code != 200) {
	            System.out.println("LIFTING SERVICE Error message: ");
	            System.out.println(LIFTpostMtd.getResponseHeader("Error"));
	            lifted_output = "";
	        }
	        else {
	            System.out.println("LIFTING SERVICE Response OK");
	            lifted_output = LIFTpostMtd.getResponseBodyAsString();	            
	            System.out.println("LIFTED OUTPUT = " + lifted_output);
	        }
	        LIFTpostMtd.releaseConnection();
		 }
		}catch (Exception e) {
			System.out.println(" Error executing WSDL Service:  " + e.getMessage());
		}
        return LiLoManager.doLifting(service.getIdService(), lifted_output); 
   	}

	private String interactRestService(ServiceAnnotationsSrv service, Map<String, String> params, String operation) {

		log.info(" Applying template process for the URI Operation: "+operation );
		Iterator it = params.keySet().iterator();
		while (it.hasNext()) {
			log.info(" #### PARAM " + (String)it.next());
		}
		
		//URI Template
		log.info(" TEMPLATE for the URI Operation: "+service.getOperation(operation).getAddress() );
		Template template = new Template(service.getOperation(operation).getAddress());
		String url = template.expand(params);
		String method = service.getOperation(operation).getMethod();
		// TODO: Also method (GET, POST, PUT, DELETE)
    	if(method==null){
    		method = "GET";
    	}
    	
    	log.info("URL to invoke the Rest Service: "+url);
    	
    	String result = "Error executing service: " +url;
    	try {
    	    URL url2 = new URL(url);
    	    HttpURLConnection  connection = (HttpURLConnection) url2.openConnection();
    	    connection.setRequestMethod(method);
    	    connection.setUseCaches(false);
    	    connection.setDoOutput(true);
    	    BufferedReader in = (new BufferedReader(new InputStreamReader(connection.getInputStream())));
    	    StringBuffer strbuf = new StringBuffer();
    	    String line;
    	    while ((line = in.readLine()) != null) {
    	    	strbuf.append(line + " ");
    	    }
    	    result = strbuf.toString();
    	    // Lifting
    	} catch (Exception e) {
    	    e.printStackTrace();
       	}
		return result;
	}
	
	public String interactRestServiceByUrl(String url, String method, String data) {

		log.info("URL to invoke the Service: "+url);
    	
    	String result = "Error executing service: " +url;
    	try {
    		URL url2 = new URL(url);
    	    HttpURLConnection  connection = (HttpURLConnection) url2.openConnection();
    		connection.setRequestMethod(method);
    	    connection.setUseCaches(false);
    	    connection.setDoOutput(true);
    	    if (method.equals("POST")) {
    	    	
    	    	if(url.startsWith(LUF_SERVICE_BASE)){
    	    		// Special case to interact with the Linked User Feedback using OAuth
    	    		result = postLUFOAuth(url,data);
    	    		return result;
    	    	}
    	    	
    	    	String paramStr = data;
    			log.info("Data to POST: "+paramStr);

        		// POST requests are required to have Content-Length
        		String lengthString = String.valueOf(paramStr.length());
    	    	// Set the content type we are POSTing and length of the data.
    	    	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	    	connection.setRequestProperty("Content-Length", lengthString);

    	    	OutputStream os = connection.getOutputStream();
    	    	OutputStreamWriter osw = new OutputStreamWriter(os);
    	    	// Send data
    	    	osw.write(paramStr);
    	    	osw.flush();
    	    	osw.close();
    	    }
    	    
    	    //Read the input from that application
    	    BufferedReader in = (new BufferedReader(new InputStreamReader(connection.getInputStream())));
    	    StringBuffer strbuf = new StringBuffer();
    	    String line;
    	    while ((line = in.readLine()) != null) {
    	    	strbuf.append(line + " ");
    	    }
    	    result = strbuf.toString();
    	    
    	} catch (Exception e) {
    	    e.printStackTrace();
       	}
		log.info("interactRestServiceByUrl result: "+result);
	
		return result;
	}
    
 
    /**
     * It performs the invocation of a service with OAuth (the whole OAuth message protocol)
     * and gets the response.
     * 
     * @param service
     * @param operation
     * @param params
     * @return
     */
    public Map<String, String> interactServiceOAuth(ServiceAnnotationsSrv service, String operation, Map<String,String> params, String permanentSessionId) {
       	
    	// Log action:
    	if(permanentSessionId.compareTo("")!=0){
            NameValuePair[] actionData = {
                	new NameValuePair("action", "ItemInvocation"),
                	new NameValuePair("item", service.getIdService()),
                	new NameValuePair("persistentSessionId", permanentSessionId),
                };
            Thread t = new Thread(new AuditingLogger(actionData));
            t.start();
    	}

    	
    	Map<String, String> resultMap = new HashMap<String, String>();  
    	// TODO: Enable OAuth online
    	Map<String, String> result = new HashMap<String, String>();
    	
    	//URI Template
		Template template = new Template(service.getOperation(operation).getAddress());
		String url = template.expand(params);
    	String request_url = params.get("request_url");
    	String authorization_url = params.get("authorization_url");
    	String access_url = params.get("access_url");
    	String consumer_key = params.get("consumer_key");
    	String consumer_secret = params.get("consumer_secret");
    	
    	// We find the user to interact with (it can be other than the one invoking the service)
    	String key = "http://xmlns.com/foaf/0.1/Person";
    	String userId = params.get(key);
    	
    	Map<String,String> user = getUser(userId);
    	
    	if (user!=null) {
	    	String serviceId = service.getIdService();
	    	String oauth_token = user.get(serviceId+".oauth_token");
	    	String oauth_token_secret = user.get(serviceId+".oauth_token_secret");
	    		 	
	    	OAuthServiceProvider provider = new OAuthServiceProvider(request_url, authorization_url, access_url);
	   	    OAuthConsumer consumer = new OAuthConsumer("", consumer_key, consumer_secret, provider);
	   	    OAuthAccessor accessor = new OAuthAccessor(consumer);
	   	    OAuthClient client = new OAuthClient(new HttpClient4());
	   	       	    
	   	    accessor.accessToken = oauth_token;
	   	    accessor.tokenSecret = oauth_token_secret;
	   	    
	   	    log.debug("Invoke service");
	        try {
	       	    OAuthMessage response = client.invoke(accessor, url, null);
	            String oauthResponse = response.readBodyAsString();
	            // Lifting
	        	result= LiLoManager.doLifting(service.getIdService(), oauthResponse);    
	        } catch (Exception e) { 
	       	    log.debug("auth: error");
	            e.printStackTrace();  
	        }
    	}    
        return result;
    }
    
    public static String interactWsdlService(String wsdlURL, String serviceName, String operationName, String lowered_XML)
	{
		String result = "";
		String namespace = "";
		final String PREFFIX = "cp";
		QName portQN = new QName (namespace, "");
		
		
		// Get all info from the WSDL
		WSDLReaderImpl WRead = new WSDLReaderImpl();
		try {
			javax.wsdl.Definition WDef = WRead.readWSDL(wsdlURL);
			namespace = WDef.getTargetNamespace();
			QName serviceQN = new QName(namespace, serviceName);
			//Service srvmt = WDef.getService(serviceQN);
			
			/** Create a service and add at least one port to it. **/
			Service service = Service.create(new URL(wsdlURL), serviceQN);
			//service.addPort(portQN, SOAPBinding.SOAP11HTTP_BINDING, wsdlURL);
			
			for (Iterator<QName> i = service.getPorts(); i.hasNext(); )
			{
				portQN = i.next();
				System.out.println("Found port: "+ portQN.toString());
			}	
			System.out.println("Selected PortQN: "+portQN);
		
			/** Create a Dispatch instance from a service.**/ 
			System.out.println("Create a Dispatch instance from a service .............. ");
			Dispatch<SOAPMessage> dispatch = service.createDispatch(portQN, SOAPMessage.class, Service.Mode.MESSAGE);
	        
			/** Create SOAPMessage request. **/
			//InvocationControllerFactory icf = (InvocationControllerFactory) FactoryRegistry.getFactory(InvocationControllerFactory.class);
			// compose a request message
			System.out.println("Create SOAPMessage request.............. ");
			MessageFactory mf = MessageFactory.newInstance(SOAPVERSION);
			
			// Create a message.  This example works with the SOAPPART.
			SOAPMessage request = mf.createMessage();
			SOAPPart part = request.getSOAPPart();
			
			// Obtain the SOAPEnvelope and header and body elements.
			SOAPEnvelope env = part.getEnvelope();
			env.addNamespaceDeclaration(PREFFIX, namespace);
			SOAPHeader header = env.getHeader();
			SOAPBody body = env.getBody();
			
			
			SOAPElement operation = body.addChildElement(operationName, PREFFIX);
			
			// Now we should add the lowered XML ....
			
			// XML_STRING 2 DOM 		
			String strDOM = lowered_XML;		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document d = parser.parse(new ByteArrayInputStream(strDOM.getBytes()));
			Element e = d.getDocumentElement();
		    
		    
		    // Fill the SOAP Message structure
		    //BrowseNode((Node)e, operation);
			BrowseNodeChildren((Node)e, operation);
			
		    /** Save Request and invoke  **/
			request.saveChanges();
			
			/** Invoke the service endpoint. **/			
			SOAPMessage response = dispatch.invoke(request/*r*/);
			
			// We return first child under SOAP:BODY -
			// This is the node which incapsulates all the output
			// We also remove the SOAP prefix from it
			String prefix = response.getSOAPBody().getFirstChild().getPrefix() + ":";
  	        Document doc = response.getSOAPBody().extractContentAsDocument();
			OutputFormat format    = new OutputFormat (doc);
  	        StringWriter stringOut = new StringWriter ();    
		    XMLSerializer serial   = new XMLSerializer (stringOut, format);
		    serial.serialize(doc);	    		    
		    result = stringOut.toString().replaceAll(prefix, "");
		    System.out.println(" ########### RESULT : " + result);
	
		}
		catch (Exception e){e.printStackTrace();}
		
		return result;
	}
	
	public static void BrowseNodeChildren(Node xmlNode, SOAPElement eleSOAP)
	{
		try
		{
			if(xmlNode.hasChildNodes())
            {
                NodeList nodeList = xmlNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++)
                {
                    //processo le foglie di tipo testo
                    if (nodeList.item(i).getNodeType() == Node.TEXT_NODE)
                    {
                        String strTextValue = nodeList.item(i).getNodeValue().trim();
                        if(!strTextValue.equals(""))
                        	eleSOAP.addTextNode(strTextValue);
                    }
                    else
                    {
                        Node xNode = nodeList.item(i);
                        if(xNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            String strNameTreeNode = xNode.getNodeName();
                            SOAPElement value = eleSOAP.addChildElement(strNameTreeNode);
                            BrowseNodeChildren(xNode, value);
                        }
                    }
                }
            }
		}
		catch(Exception ex){ ex.printStackTrace(); }
	}

    public List<String[]> getInstancesOfConcept(String uri) {

    	List<String[]> list = new ArrayList<String[]>();

    	// TODO: Retrieve instances from WATSON
    	
    	String [] instance1 = {"http://www.example.com/instance1","Instance 1"};
    	String [] instance2 = {"http://www.example.com/instance2","Instance 2"};
    	String [] instance3 = {"http://www.example.com/instance3","Instance 3"};
    	list.add(instance1);
    	list.add(instance2);
    	list.add(instance3);
    	
    	return list;
    }

	private String postLUFOAuth(String url, String data) {

		String result = "Error POSTing Linked User Feedback"; 
			
    	String consumer_key = "xxx";
    	String oauth_token = "xxx";
    	String consumer_secret = "xxx";
    	String oauth_token_secret = "xxx";

    	OAuthServiceProvider provider = new OAuthServiceProvider(null, null, null);
   	    OAuthConsumer consumer = new OAuthConsumer("", consumer_key, consumer_secret, provider);
   	    OAuthAccessor accessor = new OAuthAccessor(consumer);
   	    OAuthClient client = new OAuthClient(new HttpClient4());

   	    accessor.accessToken = oauth_token;
   	    accessor.tokenSecret = oauth_token_secret;

   	    String params[] = data.split("&"); 
   	    try {

   	    	List<Map.Entry<String, String>> ps = new ArrayList<Map.Entry<String, String>>();
	    	for(String param : params){
	    		String pair[] = param.split("=");
	        	ps.add(new OAuth.Parameter(pair[0],URLDecoder.decode(pair[1], "UTF-8")));
	    	}
	    	OAuthMessage response = client.invoke(accessor, OAuthMessage.POST, url, ps);
	    	
	    	result = response.readBodyAsString();
	   	    log.debug("oauth result: "+result);
   	    
	    } catch (Exception e) { 
	   	    log.debug("auth: error");
	        e.printStackTrace();  
	    }

   	    
   	    
		return result;
	}
    


}
