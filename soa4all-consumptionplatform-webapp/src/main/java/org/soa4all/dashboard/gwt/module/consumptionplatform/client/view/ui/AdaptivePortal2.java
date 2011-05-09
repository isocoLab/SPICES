package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;

import java.util.List;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

/**
 * @author GDiMatteo
 *
 */
public class AdaptivePortal2 extends Portal
{
	// number of EFFECTIVE columns in the portal
	private int columnNumber;
	
	/**
	 * @return the columnNumber
	 */
	public int getColumnNumber() {
		return columnNumber;
	}
	/**
	 * @param columnNumber the columnNumber to set
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
    
    // Temporary variables where to store the (i,j) original position of the maximized portlet
    // i = column number; j = position in the column
    private static int maximisedPortletI, maximisedPortletJ;
    
	/**
	 * @return the maximisedPortletI
	 */
	public static int getMaximisedPortletI() {
		return maximisedPortletI;
	}
	/**
	 * @param i the maximisedPortletI to set
	 */
	public static void setMaximisedPortletI(int i) {
		maximisedPortletI = i;
	}
	/**
	 * @return the maximisedPortletJ
	 */
	public static int getMaximisedPortletJ() {
		return maximisedPortletJ;
	}
	/**
	 * @param j the maximisedPortletJ to set
	 */
	public static void setMaximisedPortletJ(int j) {
		maximisedPortletJ = j;
	}

	/**
	 * @param numColumns
	 */
	public AdaptivePortal2(int numColumns)
	{
		// we invoke the parent constructor adding one more column for maximization hack
		super(numColumns+1);
		setColumnNumber(numColumns);
		
		// Restoring main n columns size
		// Cycling on every EFFECTIVE column (from 1 to N)
		for (int i = 1; i <= getColumnNumber(); i++)
		{
			// ...setting its size to 1/n
			setColumnWidth(i, 1.0/getColumnNumber());
			getItem(i).setVisible(true);
		}
				
		// first column size is zero... it will be expanded when a portlet is maximised
		setColumnWidth(0, 0.0);
		
		/*getItem(0).setStyleAttribute("backgroundColor", "yellow");
		getItem(1).setStyleAttribute("backgroundColor", "red");
		getItem(2).setStyleAttribute("backgroundColor", "blue");
		getItem(3).setStyleAttribute("backgroundColor", "green");*/
		// setting the HACK column to invisible
		getItem(0).setVisible(false);
	}
	
	/**
	 * Maximizes the portlet passed as 1st parameter
	 * @param p the portlet to be maximized
	 * @param butt2add the new button to add to the maximized portlet header when maximized (can be null)
	 */
	public void maximizePortlet(Portlet p, ToolButton butt2add)
	{
		System.out.println(">>>>>Maximizing...");
		int numTools = p.getHeader().getToolCount();
		// cycle over all item tools (header buttons)
		for (int i = 0; i < numTools; i++)
		{
			// if current tool id is "maximizeButton", i.e. I get the maximizeButton tool
			if (0 == "maximizeButton".compareTo(p.getHeader().getTool(i).getId()))
			{
				// ...I remove it from the header
				p.getHeader().removeTool(p.getHeader().getTool(i));
				// and stop looking for it
				break;
			}
		}
		// if the user has specified a button to be added to the portlet header...
		if (null != butt2add)
			// ...add the new button that will substitute the "maximize" one (usually it's the "restore button", to allow bringing the portlet to its normal size)
			p.getHeader().addTool(butt2add);

		// Setting all the EFFECTIVE columns size to zero
		// Cycling on every EFFECTIVE column
		for (int i = 1; i <= getColumnNumber(); i++)
		{
			// ...setting its size to 1/n
			setColumnWidth(i, 0.0);	
		}
		// Set HACK column to all the screen
		setColumnWidth(0, 1.0);
		// Setting portlet width and height to 95% of the portal desktop dimentions
		p.setWidth(new Double(getWidth()*0.95).intValue());
		p.setHeight(new Double(getHeight()*0.95).intValue());

	    LayoutContainer currContainer = null;
	    List<Portlet>[] openPortlets = new List[getColumnNumber()];
	    // current portlet position (i = column, j = row)
	    int currPortletI = -1, currPortletJ = -1;
	    // cycling on EFFECTIVE columns
	    for (int i = 1; i <= getColumnNumber(); i++)
	    {
	    	// getting current column
	    	currContainer = getItem(i);
	    	// and setting it invisible
	        currContainer.setVisible(false);
	        // cycle over all portlets attached to this column
	        for (int j = 0; j < currContainer.getItemCount(); j++)
	        {
	        	try
	        	{
	        		// check if the current portlet is the one I am maximizing
	        		if (((Portlet)currContainer.getItem(j)).equals(p))
	        		{
	        			//System.out.println("found pos.: "+i+","+j);
	        			currPortletI = i;
	        			currPortletJ = j;
	        		}
	        	}
	        	catch (Exception e)
	        	{
	        		e.printStackTrace();
	        		System.out.println("PORTLET NOT FOUND!!!");;
				}
	        }
	    }
	    
	    // Add the portlet to the HACK hidden column
		super.add(p, 0);
		// and make it visible
		getItem(0).setVisible(true);
		
		// SETTING OF PREVIOUS POSITION OF THE JUST MAXIMISED PORTLET
		setMaximisedPortletI(currPortletI);
		setMaximisedPortletJ(currPortletJ);
	}
	
	public void restorePortlet(Portlet p, ToolButton butt2add)
	{
		//System.out.println("Restoring...");
		int numTools = p.getHeader().getToolCount();
		// cycle over all item tools (portlet header buttons)
		for (int i=0; i<numTools; i++)
		{
			// if current tool id is "restoreButton", i.e. I get the maximizeButton tool
			if (0=="restoreButton".compareTo(p.getHeader().getTool(i).getId()))
			{
				// ...I remove it from the header
				p.getHeader().removeTool(p.getHeader().getTool(i));
				// and stop looking for it
				break;
			}
		}
		// Adding the "Maximize button" to allow maximizing the portlet
		p.getHeader().addTool(butt2add);
		// Remove portlet from full size column
		p.removeFromParent();

		/***************************************************
		 * HERE WE SHOULD MANAGE THE PORTLET HEIGHT ISSUE! *
		 ***************************************************/
		// Set the layout back to Flow
		p.setLayout(new FlowLayout());
		//System.out.println("inner: "+panel.getInnerHeight()+" frame: "+panel.getFrameHeight()+" actual: "+panel.getHeight()+" headerOffset:"+panel.getHeader().getOffsetHeight()+" "+((Portlet)panel).getHeight(false));
		p.setHeight(250);
		//p.setAutoHeight(true);
		
		// Restoring main n columns size
		// Cycling on every EFFECTIVE column
		for (int i = 1; i <= getColumnNumber(); i++)
		{
			// ...setting its size to 1/n
			setColumnWidth(i, 1.0/getColumnNumber());
			// setting it visible
			getItem(i).setVisible(true);
		}
		// Reduce last (hack) column width to 0
		setColumnWidth(0, 0.0);
		// and make it invisible
		getItem(0).setVisible(false);
		
	    /*super.*/insert(p, getMaximisedPortletJ(), getMaximisedPortletI());
	}
	
	/**
	 * Manages the closure of a portlet passed as input
	 * @param p the portlet to be closed
	 */
	public void closePortlet(Portlet p)
	{
		int numTools = p.getHeader().getToolCount();
		// cycle over all item tools (portlet header buttons)
		for (int i=0; i<numTools; i++)
		{
			// if current tool id is "restoreButton", i.e. the portlet is maximized
			if (0=="restoreButton".compareTo(p.getHeader().getTool(i).getId()))
			{
				// Restoring main n columns size
				// Cycling on every EFFECTIVE column
				for (int j = 1; j <= getColumnNumber(); j++)
				{
					// ...setting its size to 1/n
					setColumnWidth(j, 1.0/getColumnNumber());
					// setting it visible
					getItem(j).setVisible(true);			
				}
				// Reduce last (hack) column width to 0
				setColumnWidth(0, 0.0);
				getItem(0).setVisible(false);
				break;
			}
		}
		// and I delete it
		p.removeFromParent();
	}
	
	@Override
	public void add(Portlet p, int column)
	{
		//System.out.println("asked to insert in column: "+column);
		super.add(p, column+1);
	}
	
	/*@Override
	public void insert(Portlet p, int row, int column)
	{
		super.insert(p, row, column+1);
	}*/

}
