/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import java.util.Vector;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspAntBrain extends AntBrain {

    @Override
    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        double alpha = 2, beta = 1;
        double sum = 0;
        double[] probabilities = new double[currentPosition.getConnectionMap().size()];

        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = currentPosition.getConnectionMap().get(i);
            for (Node n : e.getConnections()) {
                if (visited.contains(n)) {
                    continue;
                } else {
                    probabilities[i] = Math.pow(e.getLocalPheromone(), alpha) * Math.pow(1 / e.getEdgeLength(), beta);
                    sum += probabilities[i];
                }
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return currentPosition.getConnectionMap().get(next(probabilities));
    }
}
