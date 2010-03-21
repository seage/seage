/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    public void addVertice(double x, double y) {
        String id = new Integer(_verticeList.size() + 1).toString();
        _verticeList.add(new TspNode(id, x, y));
    }

    public void fillEdgeMap() {
        TspEdge tspEdg;
        boolean same = false;
        for (Node i : _verticeList) {
            for (Node j : _verticeList) {
                if (!i.equals(j)) {
                    TspEdge theEdge = new TspEdge((TspNode)i, (TspNode)j);
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
        for (Node i : _verticeList) {
            for (Edge j : _edgeList) {
                if (j.getOriginator().equals(i) || j.getDestination().equals(i)) {
                    i.buildEdgeMap(j);
                }
            }
        }
    }

}
