/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.antcolony;

import java.util.ArrayList;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    public TspGraph(ArrayList<Node> nodes) {
        super(nodes);
        setEdgesLenght();
    }

    public static ArrayList<Node> citiesToNodes(City[] cities){
        ArrayList<Node> nodes = new ArrayList();
        for(City c : cities){
            nodes.add(new TspNode(c.ID, c.X, c.Y));
        }
        return nodes;
    }

    public void setEdgesLenght(){
        ArrayList<Edge> edgeList = getEdgeList();
        TspEdge tspEdge;
        for(Edge e : edgeList){
            tspEdge = (TspEdge)e;
            e.setEdgeLength((double)tspEdge.calculateEdgeLength());
        }
    }
}
