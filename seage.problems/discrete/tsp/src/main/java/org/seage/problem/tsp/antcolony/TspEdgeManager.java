/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.antcolony;

import java.util.List;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.EdgeManager;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author rick
 */
public class TspEdgeManager extends EdgeManager{
    public TspEdgeManager(Graph g)
    {

    }

    @Override
    public List<Edge> getAvailableEdges(List<Edge> visited, Node currentNode) {
        return super.getAvailableEdges(visited, currentNode);
    }

    @Override
    public double getEdgeLength() {
        return super.getEdgeLength();
    }


}
