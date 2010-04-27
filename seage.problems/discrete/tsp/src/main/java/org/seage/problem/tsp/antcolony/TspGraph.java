/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    public TspGraph(City[] cities, double locEvaporCoeff, double defaultPheromone) {
        super(locEvaporCoeff);
        for (City c : cities) {
            addNode(c.X, c.Y);
        }
        _nuberNodes = _nodeList.size();
        fillEdgeMap();
        setDefaultPheromone(defaultPheromone);
    }

    public void addNode(double x, double y) {
        int id = _nodeList.size();
        _nodeList.add(new TspNode(id, x, y));
    }

    public void fillEdgeMap() {
        TspEdge tspEdg;
        boolean same = false;
        for (Node i : _nodeList) {
            for (Node j : _nodeList) {
                if (!i.equals(j)) {
                    TspEdge theEdge = new TspEdge((TspNode)i, (TspNode)j, _localEvaporation);
                    for (Edge k : _edgeList) {
                        tspEdg = (TspEdge)k;
                        if (tspEdg.getNode1().equals(j) && tspEdg.getNode2().equals(i)) {
                            same = true;
                        }
                    }
                    if (!same) {
                        _edgeList.add(theEdge);
                    }
                }
                same = false;
            }
        }
        for (Node i : _nodeList) {
            for (Edge j : _edgeList) {
                if (j.getNode1().equals(i) || j.getNode2().equals(i)) {
                    i.addConnection(j);
                }
            }
        }
        _nuberEdges = _edgeList.size();
    }

    @Override
    public void printPheromone(){
        for(Node n : _nodeList)
        {
            System.out.println(n.getId());
            for(Edge e : n.getConnectionMap()){
                System.out.printf("\t%3.3f\t%3.5f\n",e.getEdgeLength(),e.getLocalPheromone());
            }
        }
    }
}
