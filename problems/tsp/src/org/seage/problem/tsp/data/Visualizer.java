/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;


/**
 *
 * @author rick
 */
public class Visualizer
{
    // <editor-fold defaultstate="collapsed" desc="Singleton design pattern">
    private static Visualizer _instance;
    private Visualizer()
    {
    }

    public static Visualizer instance()
    {        
        if(_instance == null)
            _instance = new Visualizer();
        return _instance;
    }
    // </editor-fold>

    private JFrame frame;
    private GraphModel model;
    private JGraph graph;

    ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();

    public void createGraph(City[] cities, Integer[] tour, String path, int width, int height)
    {
         // Create the model to use.
        model = new DefaultGraphModel();
        graph = new JGraph( model );
        graph.setAntiAliased( true );
        graph.setEditable( false );
        // Show the graph
        frame = new JFrame("Travelling salesman problem visualiser");
        frame.setPreferredSize( new Dimension( width , height ) );
        frame.getContentPane().add( new JScrollPane( graph ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible(true);

        // The cell for the lemma to visualise
        // TODO: A - Normalize coordinates to default picture size
        DefaultGraphCell firstNode = createCell(tour[0].toString(), cities[tour[0]].X, cities[tour[0]].Y);
        cells.add(firstNode);

        DefaultGraphCell currentNode = firstNode;
        DefaultGraphCell lastNode = firstNode;
        for (int i = 1; i < tour.length; i++)
        {
            lastNode = createCell(tour[i].toString(), cities[tour[i]].X*10, cities[tour[i]].Y*10);
            DefaultGraphCell currentLink = createEdge(currentNode, lastNode);
            cells.add(lastNode);
            cells.add(currentLink);
            currentNode = lastNode;
        }

        DefaultGraphCell lastLink = createEdge(firstNode, lastNode);
        cells.add(lastLink);
        
        graph.getGraphLayoutCache().insert(cells.toArray());
        frame.pack();
        frame.setVisible(true);
        //graph.setScale(2.0);
        //System.out.println("SCALE> " + graph.getScale());
        try
        {
            BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( path ) );
            BufferedImage image = graph.getImage(graph.getBackground(), 0);
            //Image i = image.getScaledInstance(200, 200, hints);
            ImageIO.write(image, "png", out);
            out.close();
        } catch (Exception e) {
            System.out.println("Chyba");
        }


    }


    private  DefaultGraphCell createCell(String name, double x, double y) {
        DefaultGraphCell cell = new DefaultGraphCell(name);
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, 2, 2));
        GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createLineBorder(Color.BLACK));
        cell.addPort(new Point2D.Double(0, 0));
        return cell;
    }

    private DefaultGraphCell createEdge(DefaultGraphCell source, DefaultGraphCell target) {
        DefaultEdge edge = new DefaultEdge();
        source.addPort();
        edge.setSource(source.getChildAt(source.getChildCount() -1));
        target.addPort();
        edge.setTarget(target.getChildAt(target.getChildCount() -1));
        GraphConstants.setLabelAlongEdge(edge.getAttributes(), false);
        return edge;
    }
}
