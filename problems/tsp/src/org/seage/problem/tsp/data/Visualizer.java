

package org.seage.problem.tsp.data;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
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
public class Visualizer extends JFrame
{
    // <editor-fold defaultstate="collapsed" desc="Singleton design pattern">
    private static Visualizer _instance;
    private Visualizer()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static Visualizer instance()
    {        
        if(_instance == null)
            _instance = new Visualizer();
        return _instance;
    }
    // </editor-fold>

    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );

    private JGraphModelAdapter m_jgAdapter;

    private JGraph _jgraph;
/*
    public void init()
    {
         this.setVisible(true);
    }*/

    public void createGraph(City[] cities, Integer[] tour, String path)
    {
        // TODO: A - add JGraphT logic here

        ListenableGraph listenableGraph = new ListenableDirectedGraph( DefaultEdge.class );

        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter( listenableGraph );

        _jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( _jgraph );

        // Add first city
        String city = String.format("City%s[%s,%s]", tour[0], cities[ tour[0] ].X, cities[ tour[0] ].Y);
        listenableGraph.addVertex( city ) ;

        String beforeCity = "";
        int count = tour.length;
        for(int i = 1; i < count; i++)
        {
            city = String.format("City%s[%s,%s]", tour[i], cities[ tour[i] ].X, cities[ tour[i] ].Y);
            beforeCity = String.format("City%s[%s,%s]", tour[i - 1], cities[ tour[i - 1] ].X, cities[ tour[i - 1] ].Y);

            listenableGraph.addVertex( city );
            listenableGraph.addEdge( city , beforeCity );

            int positionX = (int)cities[ tour[i - 1] ].X * 20;
            int positionY = (int)cities[ tour[i - 1] ].Y * 20;

            positionVertexAt( city, positionX,  positionY);
            //positionVertexAt( city, (int)cities[ tour[i - 1] ].X, (int)cities[ tour[i - 1] ].Y );
        }
        _jgraph.repaint();
        this.getContentPane().add( new JScrollPane( _jgraph ) );
        this.pack();
    }

    private void adjustDisplaySettings( JGraph jg ) {
        jg.setBackground( DEFAULT_BG_COLOR );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds =
            new Rectangle2D.Double( x , y, bounds.getWidth(), bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);

    }

}
