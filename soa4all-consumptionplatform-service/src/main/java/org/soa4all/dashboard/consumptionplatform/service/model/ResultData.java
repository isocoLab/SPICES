package org.soa4all.dashboard.consumptionplatform.service.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ResultData extends BaseModel {
	  
	  public ResultData() {
	  }
	  // TODO: For a generic number
	  public ResultData(String param0, String param1, String param2, String param3, String param4) {
	    set("param0", param0);
	    set("param1", param1);
	    set("param2", param2);
	    set("param3", param3);
	    set("param4", param4);
	  }
	  public String getParam0() {
	    return (String) get("param0");
	  }
	  public String getParam1() {
	    return (String) get("param1");
	  }
	  public String getParam2() {
		return (String) get("param2");
	  }
	  public String getParam3() {
		return (String) get("param3");
	  }
	  public String getParam4() {
		return (String) get("param4");
	  }
	  public String toString() {
	    return getParam1();
	  }
	}
