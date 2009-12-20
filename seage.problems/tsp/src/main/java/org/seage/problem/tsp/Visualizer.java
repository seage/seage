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
 *     Richard Malek
 *     - Redesign
 */
package org.seage.problem.tsp;

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
 * @author Richard Malek
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
        frame.setPreferredSize( new Dimension( (int)(width*1.5) , (int)(height*1.5) ) );
        frame.getContentPane().add( new JScrollPane( graph ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        

        // getting current width ang height
        double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE;
        double maxX=Double.MIN_VALUE, maxY=Double.MIN_VALUE;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[tour[i]].X < minX) minX = cities[tour[i]].X;
            if(cities[tour[i]].X > maxX) maxX = cities[tour[i]].X;
            if(cities[tour[i]].Y < minY) minY = cities[tour[i]].Y;
            if(cities[tour[i]].Y > maxY) maxY = cities[tour[i]].Y;
        }
        double currWidth = maxX-minX;
        double currHeight = maxY-minY;

        DefaultGraphCell firstNode = createCell(tour[0].toString(), width*cities[tour[0]].X/currWidth, width*cities[tour[0]].Y/currHeight);
        cells.add(firstNode);

        DefaultGraphCell currentNode = firstNode;
        DefaultGraphCell lastNode = firstNode;
        for (int i = 1; i < tour.length; i++)
        {            
            lastNode = createCell(tour[i].toString(), width*cities[tour[i]].X/currWidth, height*cities[tour[i]].Y/currHeight);
            DefaultGraphCell currentLink = createEdge(currentNode, lastNode);
            cells.add(lastNode);
            cells.add(currentLink);
            currentNode = lastNode;
        }

        DefaultGraphCell lastLink = createEdge(firstNode, lastNode);
        cells.add(lastLink);
        
        frame.pack();
        frame.setVisible(true);
        graph.getGraphLayoutCache().insert(cells.toArray());
        try
        {
            BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( path ) );
            BufferedImage image = graph.getImage(graph.getBackground(), 0);
            ImageIO.write(image, "png", out);
            out.close();
        } catch (Exception e) {
            System.out.println("Chyba");
            e.printStackTrace();
        }

    }


    private  DefaultGraphCell createCell(String name, double x, double y) {
        DefaultGraphCell cell = new DefaultGraphCell(name);
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, 2, 2));
        GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createLineBorder(Color.BLACK));
        GraphConstants.setSelectable(cell.getAttributes(), false);
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
        GraphConstants.setSelectable(edge.getAttributes(), false);
        return edge;
    }
}
