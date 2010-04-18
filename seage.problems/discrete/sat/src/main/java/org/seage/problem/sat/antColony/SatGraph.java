/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antColony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;

/**
 *
 * @author Zagy
 */
public class SatGraph extends Graph implements java.lang.Cloneable {

    boolean[] _preparedSolution;

    public SatGraph(Formula formula, double locEvaporCoeff, double defaultPheromone) {
        super(locEvaporCoeff);
        for (int i = 0; i < formula.getLiteralCount(); i++) {
            addNode(i, true);
            addNode(formula.getLiteralCount() + i, true);
        }
        _nuberNodes = _nodeList.size();
        for (int i = 0; i < formula.getLiteralCount(); i++) {
            _preparedSolution[i] = true;
        }
        fillEdgeMap(formula);
        setDefaultPheromone(defaultPheromone);
    }

    public void addNode(int id, boolean value) {
        _nodeList.add(new SatNode(id, value));
    }

    public void fillEdgeMap(Formula formula) {
        SatEdge tspEdg;
        boolean same = false;
        for (Node i : _nodeList) {
            for (Node j : _nodeList) {
                if (!i.equals(j)) {
                    SatEdge theEdge = new SatEdge((SatNode) i, (SatNode) j, _localEvaporation, formula, _preparedSolution);
                    for (Edge k : _edgeList) {
                        tspEdg = (SatEdge) k;
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
}
