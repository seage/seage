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
            totalLength += e.getEdgeLength();
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

    @Override
    protected Edge selectNextEdge(List<Edge> edges, Vector<Node> visited) {
        double sum = 0;
        double[] probabilities = new double[edges.size()];
        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = edges.get(i);
            for (Node n : e.getConnections()) {
                if (visited.contains(n)) {
                    continue;
                } else {
                    probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgeLength(), _beta);
                    sum += probabilities[i];
                }
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return edges.get(next(probabilities));
    }
}
