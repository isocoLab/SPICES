package org.soa4all.dashboard.consumptionplatform.servicepersonalisation;

import org.openrdf.rio.RDFHandlerException;

import junit.framework.TestCase;

public class RepositoryOperationsTest extends TestCase {
	
	RepositoryOperations repositoryOperations;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		repositoryOperations = new RepositoryOperations();
	}

	public void testPostStatement() throws Exception{
		
		String statement = repositoryOperations.createStatementToString("http://profile.soa4all.org#vian", "http://profile.soa4all.org#hasOpenID", "vian");
		
		
		//repositoryOperations.postStatements(statement);
		
		//String resource  = repositoryOperations.getRDFData("http://profile.soa4all.org#vian",null,null);
		//assertEquals(resource, "http://profile.soa4all.org#vian");
		
		//repositoryOperations.deleteStatements("http://profile.soa4all.org#vian");
		//repositoryOperations.listRepositoryStatements();
	}
	
	
}
