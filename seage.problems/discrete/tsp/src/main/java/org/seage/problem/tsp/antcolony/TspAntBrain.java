/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import java.util.ArrayList;
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

    @Override
    protected List<Edge> getAvailableEdges(Node currentPosition, Vector<Node> visited)
    {
        List<Edge> result = new ArrayList<Edge>();
        for(Edge e : currentPosition.getConnectionMap())
        {
            Node node2 = null;
            if(e.getNode1().equals(currentPosition))
                node2 = e.getNode2();
            else
                node2 = e.getNode1();

            if(visited.size() == _numNodes)
                if(node2 == _startingNode)
                {
                    result.add(e);
                    return result;
                }

            if(!visited.contains(node2))
                result.add(e);
        }

        return result;
    }
}
