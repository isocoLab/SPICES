package org.soa4all.dashboard.consumptionplatform.servicepersonalisation.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLParser;

public class ConceptFetcher {
	public List<StatementHelperModel> getConceptDetails(String conceptURI) throws IOException{
		String rdf = fetchRDF(conceptURI);
		
		
		return parseRDF(rdf, conceptURI);
		
	}
	
	private List<StatementHelperModel> parseRDF(String rdf, String conceptURI){
		final List<StatementHelperModel> statementList = new ArrayList<StatementHelperModel>();
		final String concept = conceptURI;
		try {
			RDFXMLParser parser = new RDFXMLParser();
			
			parser.setRDFHandler(new RDFHandler() {

				@Override
				public void endRDF() throws RDFHandlerException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void handleComment(String arg0)
						throws RDFHandlerException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void handleNamespace(String arg0, String arg1)
						throws RDFHandlerException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void handleStatement(Statement arg)
						throws RDFHandlerException {
					System.out.println("handle statemenet");
					if(arg.getSubject().toString().equals(concept)){
						StatementHelperModel model = new StatementHelperModel();
						model.setSubject(arg.getSubject().toString());
						model.setPredicate(arg.getPredicate().toString());
						model.setObject(arg.getObject().toString());
						
						statementList.add(model);
					}
					
				}

				@Override
				public void startRDF() throws RDFHandlerException {
					System.out.println("start rdf");
					
				}
				
				
			});
			
			InputStream is = new ByteArrayInputStream(rdf.getBytes("UTF-8"));
			parser.parse(is, "");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statementList;
	}
	
	private String fetchRDF(String conceptURI){
		
		if(conceptURI.startsWith("http://profile.soa4all.org")){
			conceptURI = "http://coconut.tie.nl:8080/storage/repositories/profile/statements";
		}
		
		GetMethod getMtd = new GetMethod(conceptURI);
		
		
		if(conceptURI!="http://coconut.tie.nl:8080/storage/repositories/profile/statements"){
			getMtd.setRequestHeader("Accept",
	        "application/rdf+xml");
		}
		
		
		
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
			
		} catch (HttpException e) {
		
			e.printStackTrace();
		} catch (IOException e) {		
		
		} finally {
			getMtd.releaseConnection();
		}
		
		String response = "";
		try {
			response = getMtd.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
