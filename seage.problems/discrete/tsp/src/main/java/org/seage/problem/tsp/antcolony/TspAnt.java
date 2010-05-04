/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import java.util.Random;
import java.util.Vector;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspAnt extends Ant {

    private Random _rand;

    public TspAnt(Graph graph, double qantumPheromone, AntBrain brain) {
        super(graph, qantumPheromone, brain);
        _rand = new Random(hashCode());
        Node start = _graph.getNodeList().get(_rand.nextInt(_graph.getNodeList().size()));
        _startPosition = start;
        _currentPosition = start;
        _visited.add(start);
    }

//    @Override
//    public Vector<Edge> explore() {
//        for (int i = 0; i < _graph.getNodeList().size() - 1; i++) {
//            updatePosition(_brain.getNextEdge(_visited, _currentPosition));
//        }
//        for (Edge last : _graph.getEdgeList()) {
//            Node node1 = last.getNode1();
//            Node node2 = last.getNode2();
//            if (node1.equals(_currentPosition) || node2.equals(_currentPosition)) {
//                if (node1.equals(_startPosition) || node2.equals(_startPosition)) {
//                    _path.add(last);
//                    break;
//                }
//            }
//        }
//        _distanceTravelled = _brain.pathLength(_path);
//        return _path;
//    }
}
