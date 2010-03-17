/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspEdge extends Edge {

    private TspNode _node1, _node2;
    double _dX, _dY;

    public TspEdge(TspNode start, TspNode end) {
        super(start, end);
        setEdgeLength(calculateEdgeLength());
    }

    public double calculateEdgeLength(){
        _node1 = (TspNode)getOriginator();
        _node2 = (TspNode)getDestination();
        _dX = (_node1.getX() - _node2.getX());
        _dY = (_node1.getY() - _node2.getY());
        return Math.sqrt(_dX*_dX + _dY*_dY);
    }
}
