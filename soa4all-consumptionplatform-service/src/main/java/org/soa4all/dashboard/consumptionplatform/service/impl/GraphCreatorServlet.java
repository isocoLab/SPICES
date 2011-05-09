package org.soa4all.dashboard.consumptionplatform.service.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GraphCreatorServlet extends HttpServlet {

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String concept = req.getParameter("concept");
		ConceptFetcher conceptFetcher = new ConceptFetcher();
		List<StatementHelperModel> model = conceptFetcher.getConceptDetails(concept);
		
		RDFGraphBuilder builder = new RDFGraphBuilder();
		byte[] imageBytes = builder.createGraph(concept,model);
		
		resp.setContentType("image/png");
		ServletOutputStream out =  resp.getOutputStream();
		out.write(imageBytes);


	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
		
	}
	
	
}
