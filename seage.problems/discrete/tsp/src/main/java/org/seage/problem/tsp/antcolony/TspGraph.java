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

    public TspGraph(City[] cities, double globEvaporCoeff, double locEvaporCoeff) {
        super(globEvaporCoeff, locEvaporCoeff);
        for (City c : cities) {
            addVertice(c.X, c.Y);
        }
        fillEdgeMap();
    }

    public void addVertice(double x, double y) {
        String id = new Integer(_nodeList.size() + 1).toString();
        _nodeList.add(new TspNode(id, x, y));
    }

    public void fillEdgeMap() {
        TspEdge tspEdg;
        boolean same = false;
        for (Node i : _nodeList) {
            for (Node j : _nodeList) {
                if (!i.equals(j)) {
                    TspEdge theEdge = new TspEdge((TspNode)i, (TspNode)j, _globalEvaporation, _localEvaporation);
                    for (Edge k : _edgeList) {
                        tspEdg = (TspEdge)k;
                        if (tspEdg.getOriginator().equals(j) && tspEdg.getDestination().equals(i)) {
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
                if (j.getOriginator().equals(i) || j.getDestination().equals(i)) {
                    i.buildEdgeMap(j);
                }
            }
        }
    }

}
