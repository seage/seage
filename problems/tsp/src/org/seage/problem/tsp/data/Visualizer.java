/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

/**
 *
 * @author rick
 */
public class Visualizer
{
    // <editor-fold defaultstate="collapsed" desc="Singleton design pattern">
    private static Visualizer _instance;
    private Visualizer(){}

    public static Visualizer instance()
    {
        if(_instance == null)
            _instance = new Visualizer();
        return _instance;
    }
    // </editor-fold>

    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 530, 320 );

    //
    private JGraphModelAdapter m_jgAdapter;


    public void createGraph(City[] cities, Integer[] tour, String path)
    {
        // TODO: A - add JGraphT logic here

        // create a JGraphT graph
        ListenableGraph g = new ListenableDirectedGraph( DefaultEdge.class );

        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter( g );

        JGraph jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( jgraph );
        //getContentPane(  ).add( jgraph );
        //resize( DEFAULT_SIZE );

        // add some sample data (graph manipulated via JGraphT)
        g.addVertex( "v1" );
        g.addVertex( "v2" );
        g.addVertex( "v3" );
        g.addVertex( "v4" );

        g.addEdge( "v1", "v2" );
        g.addEdge( "v2", "v3" );
        g.addEdge( "v3", "v1" );
        g.addEdge( "v4", "v3" );

        // position vertices nicely within JGraph component
        positionVertexAt( "v1", 130, 40 );
        positionVertexAt( "v2", 60, 200 );
        positionVertexAt( "v3", 310, 230 );
        positionVertexAt( "v4", 380, 70 );

        // that's all there is to it!...

    }

    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            //colorStr = getParameter( "bgcolor" );
        }
         catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle2D        b    = GraphConstants.getBounds( attr );


        GraphConstants.setBounds( attr, b);

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        //m_jgAdapter.edit( cellAttr );

    }

}
