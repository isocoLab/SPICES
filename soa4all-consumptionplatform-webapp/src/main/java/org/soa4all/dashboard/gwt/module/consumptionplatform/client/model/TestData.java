package org.soa4all.dashboard.gwt.module.consumptionplatform.client.model;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;

public class TestData {   
public static List<Stock> getStocks() {
//	set("name", name);
//    set("open", open);
//    set("change", change);
//    set("percentChange", pctChange);
//    set("date", format.parse(date));
//    set("industry", industry);
    List<Stock> stocks = new ArrayList<Stock>();
    stocks.add(new Stock("iSOCO", 0, 0, 100, "07/03 10:33PM", "Enterprise"));
    stocks.add(new Stock("KMI", 0, 0, 100, "07/03 10:33PM", "University"));
    return stocks;
}

public static ColumnModel getStocksColumnModel() {
    List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
    ColumnConfig column = new ColumnConfig();
    column.setId("id");
    column.setHeader("identifier");
    column.setWidth(75);
    configs.add(column);
    
    column = new ColumnConfig();
    column.setId("name");
    column.setHeader("Company");
    column.setWidth(200);
    configs.add(column);
    
    
    column = new ColumnConfig();
    column.setId("phone");
    column.setHeader("Phone");
    column.setWidth(200);
    configs.add(column);
    
    ColumnModel cm=new ColumnModel(configs);
    
    return cm;
}}
