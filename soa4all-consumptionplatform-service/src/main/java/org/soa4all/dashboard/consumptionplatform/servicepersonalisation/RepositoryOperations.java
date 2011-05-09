package org.soa4all.dashboard.consumptionplatform.servicepersonalisation;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLParser;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

/**
 * @author aviandri
 *
 */
public class RepositoryOperations {
	private final String STORAGE_SERVICE_ENDPOINT = "http://coconut.tie.nl:8080/storage";
	private final String REPO_ID = "profile";
	
	
	
	/**
	 * @param rdfData
	 * @throws Exception
	 * Post statement topple to repository
	 */
	public void postStatements(String rdfData) throws Exception {

		String requestURL = STORAGE_SERVICE_ENDPOINT + "/repositories/"
				+ REPO_ID + "/statements/";

		PostMethod postMtd = new PostMethod(requestURL);
		HttpClient httpclient = new HttpClient();

		try {

			postMtd.setRequestEntity(new StringRequestEntity(URLEncoder.encode(
					rdfData, "UTF-8"), "application/xml", "UTF-8"));

			int result = httpclient.executeMethod(postMtd);
			System.out.println("Response status code: " + result);
			if (result != 200) {
				System.out.println("Error message: ");
				System.out.println(postMtd.getResponseHeader("Error"));
			} else {
				System.out.println("Response OK");
			}
		} finally {
			postMtd.releaseConnection();
		}
	}

	/**
	 * @param subj
	 * @throws Exception
	 * Delete statements on repository based on subject value
	 */
	public void deleteStatements(String subj) throws Exception {
		String requestURL = STORAGE_SERVICE_ENDPOINT + "/repositories/"
				+ REPO_ID + "/statements/?subj="
				+ URLEncoder.encode(subj, "UTF-8") + "&context="
				+ URLEncoder.encode("defaultContext", "UTF-8");

		DeleteMethod deleteMtd = new DeleteMethod(requestURL);
		HttpClient httpclient = new HttpClient();
		String error = null;
		try {
			int result = httpclient.executeMethod(deleteMtd);
			if (result != 200) {
				error = ((deleteMtd.getResponseHeader("Error") != null) ? deleteMtd
						.getResponseHeader("Error").getValue()
						: "unknown");
			}
		} finally {
			deleteMtd.releaseConnection();
		}
		if (error != null) {
			throw new Exception(error);
		}
	}

	/**
	 * @param userID
	 * @param propID
	 * @param value
	 * @throws Exception
	 * Add property and value to a subject in the repository 
	 */
	public void addProperty(String userID, String propID, String value)
			throws Exception {

		ValueFactory factory = new ValueFactoryImpl();
		StringWriter buffer = new StringWriter();
		RDFXMLWriter writer = new RDFXMLWriter(buffer);
		writer.startRDF();
		// writer.writeStatement(factory.createURI(userID),
		// factory.createURI(propID), factory.createLiteral(value));
		writer.handleStatement(factory.createStatement(factory
				.createURI(userID), factory.createURI(propID), factory
				.createLiteral(value)));
		writer.endRDF();
		postStatements(buffer.getBuffer().toString());
	}

	/**
	 * @param userID
	 * @param propID
	 * @return
	 * @throws Exception
	 * Read property of a statement in the repository
	 */
	public List<String> readProperty(String userID, String propID)
			throws Exception {

		String rdfData = getRDFData(userID, propID, null);
		final List<String> props = new LinkedList<String>();
		if (rdfData != null) {
			RDFXMLParser parser = new RDFXMLParser();
			parser.setRDFHandler(new RDFHandler() {
				public void handleStatement(Statement arg)
						throws RDFHandlerException {
					props.add(arg.getObject().toString());

				}

				public void endRDF() throws RDFHandlerException {
				}

				public void handleComment(String arg0)
						throws RDFHandlerException {
				}

				public void handleNamespace(String arg0, String arg1)
						throws RDFHandlerException {
				}

				public void startRDF() throws RDFHandlerException {
				}
			});
			parser.parse(new StringReader(rdfData), "");
		}
		return props;
	}

	/**
	 * @param propID
	 * @param objID
	 * @return
	 * @throws Exception
	 * Get the subject of a statement 
	 */
	public List<String> readSubject(String propID, String objID)
			throws Exception {

		String rdfData = getRDFData(null, propID, objID);
		final List<String> props = new LinkedList<String>();
		if (rdfData != null) {
			RDFXMLParser parser = new RDFXMLParser();
			parser.setRDFHandler(new RDFHandler() {
				public void handleStatement(Statement arg)
						throws RDFHandlerException {
					props.add(arg.getSubject().toString());
				}

				public void endRDF() throws RDFHandlerException {
				}

				public void handleComment(String arg0)
						throws RDFHandlerException {
				}

				public void handleNamespace(String arg0, String arg1)
						throws RDFHandlerException {
				}

				public void startRDF() throws RDFHandlerException {
				}
			});
			parser.parse(new StringReader(rdfData), "");
		}
		return props;
	}

	/**
	 * @param subj
	 * @param prop
	 * @param obj
	 * @return
	 * @throws Exception
	 * Get the value of a subject, property or object 
	 */
	public String getRDFData(String subj, String prop, String obj)
			throws Exception {
		String requestURL = STORAGE_SERVICE_ENDPOINT + "/repositories/"
				+ REPO_ID + "/statements";
		boolean paramAdded = false;
		if (subj != null) {
			requestURL += ((paramAdded) ? '&' : '?') + "subj="
					+ URLEncoder.encode(subj, "UTF-8");
			paramAdded = true;
		}
		if (prop != null) {
			requestURL += ((paramAdded) ? '&' : '?') + "pred="
					+ URLEncoder.encode(prop, "UTF-8");
			paramAdded = true;
		}
		if (obj != null) {
			requestURL += ((paramAdded) ? '&' : '?') + "obj="
					+ URLEncoder.encode(obj, "UTF-8");
		}

		requestURL += ((paramAdded) ? '&' : '?') + "context="
				+ URLEncoder.encode("defaultContext", "UTF-8");

		GetMethod getMtd = new GetMethod(requestURL);
		HttpClient httpclient = new HttpClient();
		String error = null;
		String rdfData = null;

		try {
			int result = httpclient.executeMethod(getMtd);
			if (result != 200) {
				if (getMtd.getResponseHeader("Error") != null) {
					error = getMtd.getResponseHeader("Error").getValue();
				}
			} else {
				rdfData = getMtd.getResponseBodyAsString();
			}
		} finally {
			getMtd.releaseConnection();
		}
		if (error != null) {
			throw new Exception(error);
		}
		return rdfData;
	}
	
	/**
	 * @param resource
	 * @param property
	 * @param value
	 * @return
	 * @throws RDFHandlerException
	 * Create string representation of statements
	 */
	public String createStatementToString(String resource,String property,String value) throws RDFHandlerException{
		
		 ValueFactory factory = new ValueFactoryImpl();
	        StringWriter buffer = new StringWriter();
	        RDFXMLWriter writer = new RDFXMLWriter(buffer);

	        writer.startRDF();
	        
            writer.handleStatement(
            		factory.createStatement(
            				factory.createURI(resource), 
            				factory.createURI(property), 
            				factory.createLiteral(value)));
	            
	        
	        writer.endRDF();
	        
	        return buffer.toString();
	}
	
	/**
	 * @throws Exception
	 * Print all the statements in the repository
	 */
	public void listRepositoryStatements() throws Exception {

		String requestURL = STORAGE_SERVICE_ENDPOINT + "/repositories/"
				+ REPO_ID + "/statements";

		System.out.println("Request URL: " + requestURL);
		GetMethod getMtd = new GetMethod(requestURL);
		HttpClient httpclient = new HttpClient();

		try {
			int result = httpclient.executeMethod(getMtd);
			System.out.println("Response status code: " + result);
			if (result != 200) {
				System.out.println("Error message: ");
				System.out.println(getMtd.getResponseHeader("Error"));
			} else {
				System.out.println("Response OK");
				System.out.println(getMtd.getResponseBodyAsString());
			}
		} finally {
			getMtd.releaseConnection();
		}
	}

}
