package org.soa4all.dashboard.consumptionplatform.servicepersonalisation;

import java.util.Iterator;
import java.util.Map;

import org.soa4all.dashboard.consumptionplatform.service.impl.AnnotationsExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.soa4all.dashboard.consumptionplatform.service.PersonalisationService;
import junit.framework.TestCase;

public class PersonalisationServiceTest extends TestCase {
	private String serviceID = "http://iserve-dev.kmi.open.ac.uk:8080/iserve/resource/services/f8b9adc8-899e-4b5e-8496-3921e575c13f#LastFmFriends";

	private ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"classpath:soa4all_dashboard_consumptionplatform_service_applicationContext.xml");

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testExecuteServiceInputRecommendation() {
		PersonalisationService persService = (PersonalisationService) getApplicationContext()
				.getBean("personalisationService");

		// TODO: Adapt test for new "RDFToModelManager.extractInformationWithQueries"
//		Map<String, String> map = AnnotationsExtractor
//				.extractInformationWithQueries(serviceID);
//
//		// adding an input parameter that would be matching the profile property
//		// for test purpose
//		map.put("op0.input.model4", "http://profile.soa4all.org#hasSNTwitter");
//
//		Map<String, String> recommendedMap = persService
//				.getRecommendedServiceInput(map, "user.getFriends",
//						"http://aviandri.myopenid.com/");
//
//		if (recommendedMap != null) {
//			Iterator<String> iter = recommendedMap.keySet().iterator();
//			while (iter.hasNext()) {
//				String key = iter.next();
//				String value = recommendedMap.get(key);
//				assertEquals(value, "aviandri");
//			}
//		}
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
