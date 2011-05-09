package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.AuthenticationProtocolSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LiftingSchemaSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.LoweringSchemaSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceAnnotationsSrv;
import org.soa4all.dashboard.consumptionplatform.service.model.conceptual.ServiceOperationSrv;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RDFToModelManager {

	// Storage Services URL
	public static final String STORAGE_SERVICE_ENDPOINT = "http://coconut.tie.nl:8080/storage";
    public static final String ANNOTATIONS_REPOSITORY = "ServiceAnnotationsSrv";
    private final static Logger log = Logger.getLogger(RDFToModelManager.class);

    /**
     * It returns the information about a service, extracted from the semantic annotations. 
     * 
     * @param id
     * @return Hashmap that contains the information about the service.
     */
    public static ServiceAnnotationsSrv extractInformation(String id) {
    	
    	ServiceAnnotationsSrv service = extractInformationWithQueries(id);
    	
    	// Authentication methods not APIKey (OAuth) stated here
    	// TODO: Extract from the annotations
    	// (FireEagle)
    	if(id.startsWith("http://iserve.kmi.open.ac.uk/resource/services/2597a4d8-a914-403b-a9aa-6a22d24e5fd9#FireEagle")){
        	service.setAuthData(new AuthenticationProtocolSrv("OAuth", "pmYXjtpZOhqV", null));
//        	[request_url; authorization_url; access_url; oauth_consumer_key; oauth_consumer_secret] =>
//        	https://fireeagle.yahooapis.com/oauth/request_token;http://fireeagle.yahoo.net/oauth/authorize;https://fireeagle.yahooapis.com/oauth/access_token;pmYXjtpZOhqV;CcfY5DaGv9sIBuSpc1jMBySL3uOnrQhc
        }

    	return service;
    }

    public static ServiceAnnotationsSrv extractInformationWithQueries(String serviceId) {
  
    	ServiceAnnotationsSrv service = new ServiceAnnotationsSrv(serviceId);
		String result = "";
    	System.out.println("\n\nService id: "+serviceId+"\n");
    	log.setLevel(Level.DEBUG);
    	log.debug("serviceId: "+serviceId);
    	
		if(serviceId.startsWith("http://iserve.kmi.open.ac.uk")){
			
			log.debug("Extracting information of iServe service.");
			
	    	// Extraction Query #1
	    	// ===================
	    	// Extract details about the service: definedBy and label:
	    	String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
	    		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
	    		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
	    		+ " SELECT ?def ?labelSer "
	    		+ " WHERE " 
	    		+ " { "
	    		+ " <"+serviceId+"> rdfs:isDefinedBy ?def . "
	    		+ " OPTIONAL { <"+serviceId+"> rdfs:label ?labelSer . } "
	    		+ " }";
	 
	    	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(query);
	    	log.debug("[extractInformationWithQueries][service] response: "+response);

	    	try {
	        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        	Reader reader=new CharArrayReader(response.toCharArray());
	        	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
	    	
	        	Element root = doc.getDocumentElement();
	        	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
	        	NodeList childNodes = resultsNode.getChildNodes();

	        	for (int i = 0; i < childNodes.getLength(); ++i) {
	        		Node nNode = childNodes.item(i);
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
	                    				String uriValue = uri.getChildNodes().item(0).getNodeValue();
	                        			result += "\r\n id: "+uriValue;
	                        			if (service.getUri()==null)
	                        				service.setUri(uriValue);                        			
	                        		}
	                			} else if(nameAtt.compareTo("labelSer")==0){
	                    			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
	                    			if(literal!=null){
										Node litVal = literal.getChildNodes().item(0);
										if(litVal!=null) {
		                    				String name = literal.getChildNodes().item(0).getNodeValue();
		                        			result += "\r\n name: "+ name;
		                        			service.setName(name);
										}
	                        		}
	                			}
	                		}
	                	}
	    			}
	    		}
	    	} catch(Exception err) {
	    		log.error("SPARQL error : " + err.getMessage());
	    	}
	    	
	    	// If we don't find a label, we use the id
	    	if (service.getName()==null) {
	    		service.setName(serviceId);
	    	}
	    	

	    	// Extraction Query #2
	    	// ===================
	    	// Extract details about the operations: label, address, method:
	    	query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
	    		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
	    		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
	    		+ " PREFIX hr: <http://www.wsmo.org/ns/hrests#> "
	    		+ " SELECT ?op ?labelOp ?address ?method "
	    		+ " WHERE " 
	    		+ " { "
	    		+ " <"+serviceId+"> wsl:hasOperation ?op . "
	    		+ " OPTIONAL { ?op rdfs:label ?labelOp . } "
	    		+ " OPTIONAL { ?op hr:hasMethod ?method . } "
	    		+ " OPTIONAL { ?op hr:hasAddress ?address . } "
	    		+ " }";
	 
	    	response = AnnotationsExtractor.iServeEvalSPARQLQuery(query);
	    	log.debug("[extractInformationWithQueries][operations] response: "+response);

	    	List<String> operations = new ArrayList<String>();
	    	
	    	try {
	        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        	Reader reader=new CharArrayReader(response.toCharArray());
	        	Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
	    	
	        	Element root = doc.getDocumentElement();
	        	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
	        	NodeList childNodes = resultsNode.getChildNodes();

	        	boolean newOp = false;
	        	for (int i = 0; i < childNodes.getLength(); ++i) {
	        		Node nNode = childNodes.item(i);
	        		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        			Element eElement = (Element) nNode;
	                	NodeList bindingNodes = eElement.getElementsByTagName("binding");
	                	ServiceOperationSrv operation = new ServiceOperationSrv();
	                	
	                	for (int j = 0; j < bindingNodes.getLength(); ++j) {
	                		                		
	                		Node bNode = bindingNodes.item(j);
	                		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
	                		               			
	                			Element bElement = (Element) bNode;
	                			String nameAtt = bElement.getAttribute("name"); 
	                			if(nameAtt.compareTo("op")==0){
	                    			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
	                    			if(uri!=null){
	                    				String uriValue = uri.getChildNodes().item(0).getNodeValue();
	                        			result += "\r\n op uri: "+uriValue;
	                        			operation.setUri(uriValue);
	                        			newOp = true;
	                    			}
	                			} else if(nameAtt.compareTo("labelOp")==0){
	                    			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
	                    			if(literal!=null){
										Node litVal = literal.getChildNodes().item(0);
										if(litVal!=null) {
		                    				String name = literal.getChildNodes().item(0).getNodeValue();
		                        			result += "\r\n op: "+ name;                        			                        			
		                        			operation.setName(name);
		                        			newOp = true;
										}
	                        		}
	                			} else if(nameAtt.compareTo("address")==0){
	                    			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
	                    			if(literal!=null){
										Node litVal = literal.getChildNodes().item(0);
										if(litVal!=null) {
		                    				String address = literal.getChildNodes().item(0).getNodeValue();
		                        			result += "\r\n op.address: "+ address;
		                        			operation.setAddress(address);
		                        			newOp = true;
		                        			// If the URI template contains "API key", we set the auth protocol to APIKey
		                        			// TODO: Use a better way to find the auth protocol
		                        			if(address.contains("APIKey")){
			                        			service.setAuthData(new AuthenticationProtocolSrv("APIKey", null, null));
		                        			}
										}
	                        		}
	                			} else if(nameAtt.compareTo("method")==0){
	                    			Element literal = (Element) bElement.getElementsByTagName("literal").item(0);
	                    			if(literal!=null){
										Node litVal = literal.getChildNodes().item(0);
										if(litVal!=null) {
		                    				String method = literal.getChildNodes().item(0).getNodeValue();
		                        			result += "\r\n op.method: "+ method;
		                        			operation.setMethod(method);                        			
		                        			newOp = true;
										}
	                        		}
	                			}
	                		}
	                	}
	                	if(newOp){
	            	    	// If we don't find a label, we use the operation uri
	            	    	if (operation.getName()==null) {
	            	    		operation.setName(operation.getUri());
	            	    	}
	            			service.addOperation(operation);
	            			operations.add(operation.getUri());
	            			newOp = false;
	                	}
	    			}
	    		}
	    	} catch(Exception err) {
	    		log.error("SPARQL error : " + err.getMessage());
	    	}

	    	// TODO: Do something if label is not used
	    	// TODO: Check "Get Method" is ok
	    	// TODO: Grab loweringSchemaMapping and lifting
	    	
	    	
	    		    	
	    	// Input:
	    	Integer opNum = 0;
	    	for(String operation : operations){
	        	// Extraction Query #3a
	        	// ====================
	        	// Extract details about the input messages:
	        	
	        	try {
	            		        	
	            	Element root = searchInputs(1, operation);
	            	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
	            	NodeList childNodes = resultsNode.getChildNodes();

	            	for (int i = 0; i < childNodes.getLength(); ++i) {
	            		Node nNode = childNodes.item(i);
	            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            			Element eElement = (Element) nNode;
	                    	NodeList bindingNodes = eElement.getElementsByTagName("binding");
	                    	for (int j = 0; j < bindingNodes.getLength(); ++j) {
	                    		Node bNode = bindingNodes.item(j);
	                    		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
	                    			Element bElement = (Element) bNode;
	                    			String nameAtt = bElement.getAttribute("name"); 
	                    			if(nameAtt.compareTo("mod")==0){
	                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
	                        			if(uri!=null){
	                        				String inputUri = uri.getChildNodes().item(0).getNodeValue();
	                            			result += "\r\n op.input.model: "+ inputUri;
	                            			ServiceOperationSrv serviceOp = service.getOperation(operation);
	                            			serviceOp.addInput(inputUri);
	                            		}
	                    			}
	                    		}
	                    	}
	        			}
	        		}
	        	} catch(Exception err) {
	        		log.error("SPARQL error : " + err.getMessage());
	        	}
	    	}

	    	// Output:

	    	for(String operation : operations){
	        	// Extraction Query #3b
	        	// ====================
	        	// Extract details about the output messages:
	        	
	        	try {
	        		Element root = searchOutputs(1, operation);
	        		Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
	            	NodeList childNodes = resultsNode.getChildNodes();

	            	Integer modNum = 0;
	            	for (int i = 0; i < childNodes.getLength(); ++i) {
	            		Node nNode = childNodes.item(i);
	            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            			Element eElement = (Element) nNode;
	                    	NodeList bindingNodes = eElement.getElementsByTagName("binding");
	                    	for (int j = 0; j < bindingNodes.getLength(); ++j) {
	                    		Node bNode = bindingNodes.item(j);
	                    		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
	                    			Element bElement = (Element) bNode;
	                    			String nameAtt = bElement.getAttribute("name"); 
	                    			if(nameAtt.compareTo("mod")==0){
	                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
	                        			if(uri!=null){
	                        				String outputUri = uri.getChildNodes().item(0).getNodeValue();
	                            			result += "\r\n op.output.model: "+ outputUri;
	                            			ServiceOperationSrv serviceOp = service.getOperation(operation);
	                            			serviceOp.addOutput(outputUri);                            			
	                            		}
	                    			}
	                    		}
	                    	}
	        			}
	        		}
	        	} catch(Exception err) {
	        		log.error("SPARQL error : " + err.getMessage());
	        	}
	    	}

	    	// Mechanisms:
	    	opNum = 0;
	    	for(String operation : operations){
	        	// Extraction Query #3c
	        	// ====================
	        	// Extract lowering and lifting mechanisms:	        	
	        		        	
	        	try {
	        		Element root = searchMechanisms(1, operation);
	            	Element resultsNode = (Element) root.getElementsByTagName("results").item(0);
	            	NodeList childNodes = resultsNode.getChildNodes();

	            	for (int i = 0; i < childNodes.getLength(); ++i) {
	            		Node nNode = childNodes.item(i);
	            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            			Element eElement = (Element) nNode;
	                    	NodeList bindingNodes = eElement.getElementsByTagName("binding");
	                    	for (int j = 0; j < bindingNodes.getLength(); ++j) {
	                    		Node bNode = bindingNodes.item(j);
	                    		if (bNode.getNodeType() == Node.ELEMENT_NODE) {
	                    			Element bElement = (Element) bNode;
	                    			String nameAtt = bElement.getAttribute("name"); 
	                    			if(nameAtt.compareTo("lowerMap")==0){
	                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
	                        			if(uri!=null){
	                        				String lowerUri = uri.getChildNodes().item(0).getNodeValue();
	                            			result += "\r\n op"+Integer.toString(opNum)+".input.loweringMechanism: "+lowerUri;
	                            			ServiceOperationSrv serviceOp = service.getOperation(operation);
	                            			serviceOp.setLowering(new LoweringSchemaSrv(lowerUri)); 
	                            		}
	                    			} else if(nameAtt.compareTo("liftMap")==0){
	                        			Element uri = (Element) bElement.getElementsByTagName("uri").item(0);
	                        			if(uri!=null){
	                        				String lifUri = uri.getChildNodes().item(0).getNodeValue();
	                            			result += "\r\n op"+Integer.toString(opNum)+".output.liftingMechanism: "+lifUri;                            			
	                            			ServiceOperationSrv serviceOp = service.getOperation(operation);
	                            			serviceOp.setLifting(new LiftingSchemaSrv(lifUri)); 
	                            		}
	                    			}
	                    		}
	                    	}
	        			}
	        		}
	        	} catch(Exception err) {
	        		log.error("SPARQL error : " + err.getMessage());
	        	}
	        	++opNum;
	    	}
		}
    	
    	System.out.println(" #################### SERVICE RDF SUMMARY ##################\n"+result);
    	return service;
    }
    
    private static Element searchInputs (int type, String operation) throws Exception{
    	
    	String header = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
    		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
    		+ " PREFIX hr: <http://www.wsmo.org/ns/hrests#> "
    		+ " PREFIX sawsdl: <http://www.w3.org/ns/sawsdl#> "
    		+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
    		+ " SELECT ?mod "
    		+ " WHERE " 
    		+ " { "
    		+ " <"+operation+"> wsl:hasInput ?message . ";
    	String query = "";
    
    	if (type==1) { // Service that follows the new MSM without Lilo    		
    		query = header + " ?message wsl:hasPart ?params . "
    		+ " ?params wsl:hasPart ?part . "
    		+ " ?part sawsdl:modelReference ?mod . "
    		+ " }";
    	} else if (type==2){ // Service that follows within Lilo
    		query = header + " ?message wsl:hasPart ?params . "
    		+ " ?params wsl:hasPart ?part . "
    		+ " ?part wsl:hasPart ?part2 . "
    		+ " ?part2 sawsdl:modelReference ?mod . "
    		+ " }";
    	} else if (type==3) { // Service that NOT follows the MSM
    		query = header + " ?message sawsdl:modelReference ?mod . "
            + " }";
    	}
 
    	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(query);
	    log.debug("[searchInputs][type="+Integer.toString(type)+"] response: "+response);
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    Reader reader=new CharArrayReader(response.toCharArray());
	    Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
		
	    Element root = doc.getDocumentElement();
	    if (!root.getElementsByTagName("results").item(0).hasChildNodes() && type<3) {
	    	root = searchInputs(type+1, operation);
	    } 	    		    	
	    return root;
    		
    }
    
    private static Element searchOutputs (int type, String operation) throws Exception{
    	
    	String header = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
    		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
    		+ " PREFIX hr: <http://www.wsmo.org/ns/hrests#> "
    		+ " PREFIX sawsdl: <http://www.w3.org/ns/sawsdl#> "
    		+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
    		+ " SELECT ?mod "
    		+ " WHERE " 
    		+ " { "
    		+ " <"+operation+"> wsl:hasOutput ?message . ";
    	String query = "";
    
    	if (type==1) { // Service that follows the new MSM without Lilo    		
    		query = header + " ?message wsl:hasPart ?params . "
    		+ " ?params wsl:hasPart ?part . "
    		+ " ?part sawsdl:modelReference ?mod . "
    		+ " }";
    	} else if (type==2){ // Service that follows within Lilo
    		query = header + " ?message wsl:hasPart ?params . "
    		+ " ?params wsl:hasPart ?part . "
    		+ " ?part wsl:hasPart ?part2 . "
    		+ " ?part2 sawsdl:modelReference ?mod . "
    		+ " }";
    	} else if (type==3) { // Service that NOT follows the MSM
    		query = header + " ?message sawsdl:modelReference ?mod . "
            + " }";
    	}
 
    	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(query);
	    log.debug("[searchOutputs][type="+Integer.toString(type)+"] response: "+response);
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    Reader reader=new CharArrayReader(response.toCharArray());
	    Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
		
	    Element root = doc.getDocumentElement();
	    if (!root.getElementsByTagName("results").item(0).hasChildNodes() && type<3) {
	    	root = searchOutputs(type+1, operation);
	    } 	    		    	
	    return root;
    		
    }
    
    private static Element searchMechanisms (int type, String operation) throws Exception{
    	   	
    	String header = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    		+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
    		+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
    		+ " PREFIX hr: <http://www.wsmo.org/ns/hrests#> "
    		+ " PREFIX sawsdl: <http://www.w3.org/ns/sawsdl#> "
    		+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
    		+ " SELECT ?lowerMap ?liftMap "
    		+ " WHERE "; 
    	
    	String query = "";
    
    	if (type==1) { // // Service that NOT follows MSM     		
    		query = header + " { "
    		+ " <"+operation+"> wsl:hasInput ?inputMessage . "
    		+ " ?inputMessage sawsdl:loweringSchemaMapping ?lowerMap . "
    		+ " <"+operation+"> wsl:hasOutput ?outputMessage . "
    		+ " ?outputMessage sawsdl:liftingSchemaMapping ?liftMap . "
    		+ " }";
    	} else if (type==2){ // Service that follows MSM 
    		query = header + " { "
    		+ " <"+operation+"> wsl:hasInput ?inputMessage . "
    		+ " ?inputMessage wsl:hasPart ?inputParams . "
    		+ " ?inputParams wsl:hasPart ?inputPart . "
    		+ " ?inputPart sawsdl:loweringSchemaMapping ?lowerMap . "
    		+ " <"+operation+"> wsl:hasOutput ?outputMessage . "
    		+ " ?outputMessage wsl:hasPart ?outputParams . "
    		+ " ?outputParams wsl:hasPart ?outputPart . "
    		+ " ?outputPart sawsdl:liftingSchemaMapping ?liftMap . "
    		+ " }";
    	} 
    	
    	String response = AnnotationsExtractor.iServeEvalSPARQLQuery(query);
	    log.debug("[searchMechanisms][type="+Integer.toString(type)+"] response: "+response);
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    Reader reader=new CharArrayReader(response.toCharArray());
	    Document doc = factory.newDocumentBuilder().parse(new InputSource(reader));
		
	    Element root = doc.getDocumentElement();
	    if (!root.getElementsByTagName("results").item(0).hasChildNodes() && type<2) {
	    	root = searchMechanisms(type+1, operation);
	    } 	    		    	
	    return root;
    		
    }
    
    // TEST
    public static void main (String args[]){
    	//extractInformationWithQueries("http://iserve.kmi.open.ac.uk/resource/services/9dea6831-f0ae-471e-bd24-56a4dcf41330#wsdl.service(ProductWebServiceService)");
    	extractInformationWithQueries("http://iserve.kmi.open.ac.uk/resource/services/03450dd6-c655-4584-9135-e9d8ef4fe8b4#wsdl.service(GetCitizenEmailsService)");
    }
    
 
}

