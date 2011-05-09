package org.soa4all.dashboard.consumptionplatform.service.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class Service extends BaseModel {
	  
	  public Service() {
	  }
	  
	  public Service(String name, String desc, String uri, double rating) {
	    set("name", name);
	    set("desc", desc);
	    set("uri", uri);
	    set("rating", rating);
	  }

	  public String getName() {
	    return (String) get("name");
	  }

	  public String getDesc() {
		    return (String) get("desc");
		  }

	  public String getUri() {
		    return (String) get("uri");
		  }

	  public double getRating() {
	    Double rating = (Double) get("rating");
	    return rating.doubleValue();
	  }

	  public String toString() {
	    return getName();
	  }

	}