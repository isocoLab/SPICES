package org.soa4all.dashboard.consumptionplatform.servicepersonalisation.impl;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.soa4all.dashboard.consumptionplatform.service.impl.AnnotationsExtractor;
import org.soa4all.dashboard.consumptionplatform.servicepersonalisation.PersonalisationService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author aviandri
 * 
 */
public class PersonalisationServiceImpl implements PersonalisationService {

	public static final String PROFILE_REPOSITORY = "profile";

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.soa4all.dashboard.consumptionplatform.servicepersonalisation.
	 * PersonalisationService#getRecommendedServiceInput(java.util.Map,
	 * java.lang.String, java.lang.String)
	 */
	public Map<String, String> getRecommendedServiceInput(
			Map<String, String> service, String opLabel, String userID) {
		Map<String, String> recommendationMap = new HashMap<String, String>();

		Iterator<String> iter = service.keySet().iterator();
		String op = null;
		while (iter.hasNext()) {
			String key = iter.next();
			String value = service.get(key);

			if (value.equals(opLabel)) {
				op = key;
			}
		}

		if (op == null || "".equals(op)) {
			return null;
		}

		int inputNo = 0;
		String opKey = op + ".input.model" + inputNo;
		String inputModel = service.get(opKey);
		while (inputModel != null) {

			String matchedValue = getMatchingConceptValue(inputModel, userID);

			if (matchedValue != null) {
				recommendationMap.put(inputModel, matchedValue);
			}

			inputNo++;
			opKey = op + ".input.model" + inputNo;
			inputModel = service.get(op + ".input.model" + inputNo);
		}

		return recommendationMap;

	}

	/**
	 * @param concept
	 * @param userID
	 * @return String
	 * 
	 *         This method try to find if there is a matching concept between
	 *         the passed concept with the user profile details. If matching
	 *         concept is found, the value of the user profile that mathced the
	 *         concept will be returned
	 */
	private String getMatchingConceptValue(String concept, String userID) {
		boolean isConceptMatch = false;

		AnnotationsExtractor extractor = new AnnotationsExtractor();
		String matchedValue = null;

		// try to find matching concept #1
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
				+ " PREFIX profile: <http://profile.soa4all.org#> "
				+ " SELECT  ?concept ?resource"
				+ " WHERE "
				+ " { "
				+ " <"+userID+"> ?concept ?resource . "
				+ " }";

		String response = AnnotationsExtractor.evalSPARQLQuery(
				PROFILE_REPOSITORY, query, null);
		Map<String, String> conceptMap = new HashMap<String, String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Reader reader = new CharArrayReader(response.toCharArray());
			Document doc = factory.newDocumentBuilder().parse(
					new InputSource(reader));

			Element root = doc.getDocumentElement();
			Element resultsNode = (Element) root
					.getElementsByTagName("results").item(0);
			NodeList resultNodeList = resultsNode
					.getElementsByTagName("result");

			for (int i = 0; i < resultNodeList.getLength(); ++i) {
				Node bNode = resultNodeList.item(i);

				if (bNode.getNodeType() == Node.ELEMENT_NODE) {
					Element bElement = (Element) bNode;

					String value = null;
					String cncpt = null;

					NodeList conceptNode = bElement
							.getElementsByTagName("binding");
					for (int k = 0; k < conceptNode.getLength(); k++) {
						Node cNode = conceptNode.item(k);
						if (cNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) cNode;
							String nameAtt = element.getAttribute("name");

							if (nameAtt.compareTo("concept") == 0) {
								Element uri = (Element) bElement
										.getElementsByTagName("uri").item(0);
								if (uri != null) {
									cncpt = uri.getChildNodes().item(0)
											.getNodeValue();
									// System.out.println("compatre concepts:"+s+" - "+concept);
									if (cncpt.equals(concept)) {
										isConceptMatch = true;

									}
								}
							} else if (nameAtt.compareTo("resource") == 0) {
								Element literal = (Element) bElement
										.getElementsByTagName("literal")
										.item(0);
								if (literal != null) {
									String s = literal.getChildNodes().item(0)
											.getNodeValue();
									value = s;
								}
							}

							if (isConceptMatch == true && value != null) {
								matchedValue = value;
								isConceptMatch = false;
							}
						}
					}

					if (value != null & cncpt != null) {
						conceptMap.put(cncpt, value);
					}

				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error in processing service personalisation");
		}

		if (matchedValue == null || "".equals(matchedValue)) {
			// try to find matching concept #2 (using owl:equivalentProperty)
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
					+ " PREFIX wsl: <http://cms-wg.sti2.org/ns/minimal-service-model#> "
					+ " PREFIX profile: <http://profile.soa4all.org#> "
					+ " PREFIX owl: <http://www.w3.org/2002/07/owl#> "
					+ " SELECT ?resource " + " WHERE " + " { "
					+ " ?resource owl:equivalentProperty <" + concept + "> . "
					+ " }";

			response = AnnotationsExtractor.evalSPARQLQuery(PROFILE_REPOSITORY,
					query, null);
			System.out.println(response);
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				Reader reader = new CharArrayReader(response.toCharArray());
				Document doc = factory.newDocumentBuilder().parse(
						new InputSource(reader));

				Element root = doc.getDocumentElement();
				Element resultsNode = (Element) root.getElementsByTagName(
						"results").item(0);
				NodeList resultNodeList = resultsNode
						.getElementsByTagName("result");
				// NodeList childNodes = resultsNode.getChildNodes();

				for (int i = 0; i < resultNodeList.getLength(); ++i) {
					Node bNode = resultNodeList.item(i);

					if (bNode.getNodeType() == Node.ELEMENT_NODE) {
						Element bElement = (Element) bNode;

						String value = null;

						NodeList conceptNode = bElement
								.getElementsByTagName("binding");
						for (int k = 0; k < conceptNode.getLength(); k++) {
							Node cNode = conceptNode.item(k);
							if (cNode.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) cNode;
								String nameAtt = element.getAttribute("name");

								if (nameAtt.compareTo("resource") == 0) {
									Element uri = (Element) bElement
											.getElementsByTagName("uri")
											.item(0);
									if (uri != null) {
										String cncpt = uri.getChildNodes()
												.item(0).getNodeValue();
										// System.out.println("compatre concepts:"+s+" - "+concept);
										value = conceptMap.get(cncpt);
										if (value != null) {
											isConceptMatch = true;
										}
									}
								}
							}
						}

						if (isConceptMatch == true) {
							matchedValue = value;
						}

					}
				}

			} catch (Exception e) {
				throw new RuntimeException("Error in processing service personalisation");
			}
		}

		return matchedValue;

	}
}
