package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import org.soa4all.dashboard.gwt.module.consumptionplatform.client.controller.ServiceVisualizerManager;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.user.client.ui.Composite;

/**
 * Class : widgetCategories
 * 
 */
public class WidgetCategories extends Composite {

    /**
     * The widget Categories displays a taxonomy of categories
     */
    public WidgetCategories() {
    	ContentPanel frame = new ContentPanel();
    	frame.setFrame(true);
    	frame.setCollapsible(true);
    	frame.setAnimCollapse(false);
    	frame.setHeading("Categories Tree");

    	final Tree tree = new Tree(); 

    	ServiceVisualizerManager.getServiceChilds(tree.getRootItem(),"null");
    	tree.addListener(Events.OnDoubleClick, new Listener<TreeEvent>() {
    		public void handleEvent(TreeEvent te) {
    			ServiceVisualizerManager.getServiceChilds(te.item,te.item.getId());
    		}
    	});
    	frame.add(tree);

    	initWidget(frame);
    }

    private void configPanel(final ContentPanel panel, final int height) {
    	panel.setCollapsible(true);
    	panel.setAnimCollapse(false);
    	panel.getHeader().addTool(new ToolButton("x-tool-maximize", new SelectionListener<ComponentEvent>() {
    	    @Override
    	    public void componentSelected(ComponentEvent ce) {

    		panel.setSize(panel.getSize().width, panel.getSize().height + 200);
    	    }
    	}));
    	panel.getHeader().addTool(new ToolButton("x-tool-minimize", new SelectionListener<ComponentEvent>() {
    	    @Override
    	    public void componentSelected(ComponentEvent ce) {
    		if (panel.getSize().height != height) {
    		    panel.setSize(panel.getSize().width, panel.getSize().height - 200);
    		}
    	    }
    	}));
    	panel.getHeader().addTool(new ToolButton("x-tool-close", new SelectionListener<ComponentEvent>() {
    	    @Override
    	    public void componentSelected(ComponentEvent ce) {
    		panel.removeFromParent();
    	    }
    	}));
    }
}
