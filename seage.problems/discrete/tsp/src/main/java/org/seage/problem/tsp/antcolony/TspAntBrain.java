/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import java.util.List;
import java.util.Vector;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspAntBrain extends AntBrain {

    public TspAntBrain(double alpha, double beta) {
        super(alpha, beta);
    }

    @Override
    protected double pathLength(Vector<Edge> path){
        double totalLength = 0;
        for(Edge e : path){
            totalLength += e.getEdgePrice();
        }
        return totalLength;
    }

    @Override
    protected List<Edge> getAvailableEdges(Node currentPosition, Vector<Node> visited) {
        if((currentPosition.getConnectionMap().size() + 1) == visited.size()){
            return null;
        }
        return currentPosition.getConnectionMap();
    }
}
