package org.soa4all.dashboard.gwt.module.consumptionplatform.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.ConsumptionPlatformModule;
import org.soa4all.dashboard.gwt.module.consumptionplatform.client.model.ResultData;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public class OutputAdapter {

	/**
     * It adapts the response of the service in order to be displayed suitably.
     * 
     * @param service  the identifier of the service
     * @param info     the data returned by the service
     * @return
     */
    public static Grid<ResultData> buildView(String service, Map<String,String> result) {
		List<ResultData> results = new ArrayList<ResultData>();
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		Map<String, Object> rows = new HashMap<String,Object>();
		List<String> fields = new ArrayList<String>();

		if((result.get("response").compareTo("Error executing service.")==0) /*|| (result.get("field0")==null)*/ ){
		    columns.add(new ColumnConfig("param0", "Message Error", 200));
		    rows.put("param0", result.get("response"));
		    results.add(new ResultData(rows));
		} else if (result.get("field0")==null){ 
			columns.add(new ColumnConfig("param0", " Message", 200));
		    rows.put("param0", result.get("response"));
		    results.add(new ResultData(rows));
		} else {
				
			boolean insertRow = false;
			
			// First we check the parameters without numbering
			String field = result.get("field0");
	    	int i = 0;
	    	while(field!=null){
				if(result.get(field)!=null){
					rows.put(result.get(field), result.get(field));
					insertRow = true;
				}
				fields.add(field);
			    i = i + 1;
	    		field = result.get("field"+Integer.toString(i));
			}
			if(insertRow){
	    		results.add(new ResultData(rows));
			}

			// Then we check the parameters with numbering
			String item = result.get(fields.get(0)+".0");
	    	i = 0;
			while(item!=null){
				rows = new HashMap<String,Object>();
				for(String f : fields){
					rows.put(f, result.get(f+"."+Integer.toString(i)));					
				}
				results.add(new ResultData(rows));
			    i++;
	    		item = result.get(fields.get(0)+"."+Integer.toString(i));
		    	InfoConfig infoConfig = new InfoConfig("Item",item);
		    	infoConfig.display = ConsumptionPlatformModule.INFO_DISPLAY_TIME;
		    	Info.display(infoConfig);
			}
			
			// Then we create the columns
			field = result.get("field0");
	    	i = 0;
			while(field!=null){
			    columns.add(new ColumnConfig(field, field, 100));
			    i = i + 1;
	    		field = result.get("field"+Integer.toString(i));
			}
		}
		
		ColumnModel cm = new ColumnModel(columns);
		ListStore<ResultData> store = new ListStore<ResultData>();
		store.add(results);
		final Grid<ResultData> g = new Grid<ResultData>(store, cm);
		for (String field : fields) {
			g.setAutoExpandColumn(field);
		}	
		g.setAutoWidth(true);
		g.setBorders(true);
		return g;
    }

}
