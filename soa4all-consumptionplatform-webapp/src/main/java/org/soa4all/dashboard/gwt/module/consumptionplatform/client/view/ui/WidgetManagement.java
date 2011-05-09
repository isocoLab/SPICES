package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Composite;

/**
 * Class : widget Management
 * 
 */
public class WidgetManagement extends Composite {

    /**
     * This widget can be used to display the actions that only admin people are allowed to  
     */
    public WidgetManagement()
    {
    	Listener<ComponentEvent> l = new Listener<ComponentEvent>() {
    		public void handleEvent(ComponentEvent ce) {
    			DataList l = (DataList) ce.component;
    			ServiceVisualizerManager.displayService(l.getSelectedItem().getId());
    		}
    	};
    	ContentPanel frame = new ContentPanel();
    	frame.setFrame(true);
    	frame.setCollapsible(true);
    	frame.setAnimCollapse(false);
    	frame.setHeading("Admin actions");

    	final DataList list = new DataList();
    	list.setFlatStyle(true);
    	list.addListener(Events.SelectionChange, l);

    	DataListItem item = new DataListItem();  
        item.setText("Recommendation System admin");  
        item.setId("RS");  
        list.add(item);  
        
		frame.setLayout(new FitLayout());
		frame.add(list);

		initWidget(frame);
   	}

}
