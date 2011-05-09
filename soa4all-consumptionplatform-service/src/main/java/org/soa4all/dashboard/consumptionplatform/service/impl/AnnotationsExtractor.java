package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Class used to SPARQL-query an external repository and iServe
 */
public class AnnotationsExtractor {
	
	// Storage Services URL
	public static final String STORAGE_SERVICE_ENDPOINT = "http://coconut.tie.nl:8080/storage";
	public static final String ANNOTATIONS_REPOSITORY = "ServiceAnnotationsSrv";
    private final static Logger log = Logger.getLogger(AnnotationsExtractor.class);


    /**
     * Performs a SPARQL query paramQuery against the repository repoID
     * @param repoID
     * @param paramQuery
     * @param headerQuery
     * @return
     */
    public static String evalSPARQLQuery(String repoID, String paramQuery, String headerQuery) {

        String requestURL = STORAGE_SERVICE_ENDPOINT 
        + "/repositories/"
        + repoID;

        boolean sendQueryInURL = true;

        try {
            if (sendQueryInURL) {
                requestURL += "?q=" + URLEncoder.encode(paramQuery, "UTF-8");
            }
		} catch(Exception err) {
			log.error("URLencoder error : " + err.getMessage());
		}

		log.debug("Request URL: " + requestURL);
        GetMethod getMtd = new GetMethod(requestURL);
    	HttpClient httpclient = new HttpClient();
		
		try {
	    	if (!sendQueryInURL) {
                getMtd.addRequestHeader("sparql-q", paramQuery);
            }

            int result = httpclient.executeMethod(getMtd);
			System.out.println("Response status code: " + result);
			if (result != 200) {
				System.out.println("Error message: ");
				System.out.println(getMtd.getResponseHeader("Error"));
			}
			else {
				log.debug("Response OK");
				return(getMtd.getResponseBodyAsString());
			}
		} catch(Exception err) {
			log.error("SPARQL error : " + err.getMessage());
		}finally {
			getMtd.releaseConnection();
		}    	
    	return null;
    }
    
    /**
     * Performs a SPARQL query paramQuery against iServe
     * @param repoID
     * @param paramQuery
     * @param headerQuery
     * @return
     */
    public static String iServeEvalSPARQLQuery(String paramQuery) {

    	log.setLevel(Level.DEBUG);
    	System.out.println(">>>> QUERY: " + paramQuery);
        String requestURL = "http://iserve.kmi.open.ac.uk/data/execute-query";

        try {
        	requestURL += "?query=" + URLEncoder.encode(paramQuery, "UTF-8");
		} catch(Exception err) {
			log.error("URLencoder error : " + err.getMessage());
		}

		log.debug("Request URL: " + requestURL);
		
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
				return(getMtd.getResponseBodyAsString());
			}
		} catch(Exception err) {
			log.error("SPARQL error : " + err.getMessage());
		} finally {
			getMtd.releaseConnection();
		}    	
    	return null;
    }


}