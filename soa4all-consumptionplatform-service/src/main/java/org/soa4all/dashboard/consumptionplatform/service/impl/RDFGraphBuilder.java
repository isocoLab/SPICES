package org.soa4all.dashboard.consumptionplatform.service.impl;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

public class RDFGraphBuilder {
	private static final double width = 300;
	private static final double height = 50;
	private static final double space = 50;

	private ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
	private JFrame frame;

	public byte[] createGraph(String conceptSource,
			List<StatementHelperModel> statementList) {
		// Construct Model and Graph
		
		GraphModel model = new DefaultGraphModel();
		JGraph graph = new JGraph(model);

		frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(graph));
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane);
		splitPane.setRightComponent(new JScrollPane(graph));
		double x = 0;
		double y = 500;
		// Control-drag should clone selection
		graph.setCloneable(true);

		// Enable edit without final RETURN keystroke
		graph.setInvokesStopCellEditing(true);

		// When over a cell, jump to its default port (we only have one, anyway)
		graph.setJumpToDefaultPort(true);

		// Insert all three cells in one call, so we need an array to store them
		DefaultGraphCell source = new DefaultGraphCell();

		// Create Hello Vertex
		source = createCell(conceptSource,
				(((width + space) / 2) * (statementList.size() / 2)), 250);
		GraphConstants.setGradientColor(source.getAttributes(), Color.RED);
		cells.add(source);

		for (int i = 0; i < statementList.size(); i++) {
			StatementHelperModel statement = statementList.get(i);
			if (i % 2 == 0) {
				y = 50;
			} else {
				y = 450;
			}
			DefaultGraphCell currentLinkedLemma = createCell(statement
					.getObject(), x, y);
			DefaultGraphCell currentLink = createEdge(statement.getPredicate(),
					source, currentLinkedLemma);
			cells.add(currentLinkedLemma);
			cells.add(currentLink);
			if (i % 2 != 0) {
				x = x + width + space;
			}
		}

		graph.getGraphLayoutCache().insert(cells.toArray());
		graph.setSelectionCell(source);
		frame.pack();
		
		return getGraphImageAsBytes(graph);

	}
 
	private DefaultGraphCell createCell(String name, double x, double y) {
		DefaultGraphCell cell = new DefaultGraphCell(name);
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
				x, y, width, height));
		GraphConstants.setBorder(cell.getAttributes(), BorderFactory
				.createRaisedBevelBorder());
		GraphConstants.setOpaque(cell.getAttributes(), true);
		GraphConstants.setGradientColor(cell.getAttributes(), Color.LIGHT_GRAY);
		cell.addPort(new Point2D.Double(0, 0));
		GraphConstants.setAutoSize(cell.getAttributes(), true);
		return cell;
	}

	private DefaultGraphCell createEdge(String name, DefaultGraphCell source,
			DefaultGraphCell target) {
		DefaultEdge edge = new DefaultEdge();
		source.addPort();
		edge.setSource(source.getChildAt(source.getChildCount() - 1));
		target.addPort();
		edge.setTarget(target.getChildAt(target.getChildCount() - 1));
		GraphConstants.setLabelAlongEdge(edge.getAttributes(), true);
		edge.setUserObject(name);
		return edge;
	}
	
	
	public void saveImage(JGraph graph){
	File file = new File("Image.png");
	 try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            
            
            BufferedImage image = graph.getImage(graph.getBackground(), 10);
            ImageIO.write(image, "png", out);
            System.out.println("Image is written");
            out.flush();
            out.close();
            frame.dispose();
        } catch (Exception e) {
            //logger.error("", e);
        }
	}
	
	public byte[] getGraphImageAsBytes(JGraph graph){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedImage image = graph.getImage(graph.getBackground(), 10);
        try {
			ImageIO.write(image, "png", os);
			os.flush();
            os.close();
            frame.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
		return os.toByteArray();
        
   
	}
	public static void main(String args[]) throws IOException{
		RDFGraphBuilder builder = new RDFGraphBuilder();
		ConceptFetcher fetcher = new ConceptFetcher();
		List<StatementHelperModel> statementList = fetcher.getConceptDetails("http://xmlns.com/foaf/0.1/Person");
		
		builder.createGraph("http://xmlns.com/foaf/0.1/Person", statementList);
	}

}
