/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony2;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatGraph2 extends Graph {

    private boolean[] _preparedSolution;

    public SatGraph2(Formula formula, double locEvaporCoeff, double defaultPheromone) {
        super(locEvaporCoeff);
        for (int id = 1; id < formula.getLiteralCount() + 1; id++) {
            _nodeList.add(new Node(id));
            _nodeList.add(new Node(-id));
        }

        _preparedSolution = new boolean[formula.getLiteralCount()];
        for (int i = 0; i < formula.getLiteralCount(); i++) {
            _preparedSolution[i] = true;
        }
        fillEdgeMap(formula);
        setDefaultPheromone(defaultPheromone);
    }

    private boolean[] createSol(Node node1, Node node2) {
        boolean[] solution = _preparedSolution.clone();
        int index = Math.abs(node1.getId());
        if (node1.getId() < 0) {
            solution[index - 1] = false;
        }
        index = Math.abs(node2.getId());
        if (node2.getId() < 0) {
            solution[index - 1] = false;
        }
        return solution;
    }

    /**
     * List of graph edges filling
     */
    private void fillEdgeMap(Formula formula) {
        boolean same = false;
        for (Node i : _nodeList) {
            for (Node j : _nodeList) {
                if (!i.equals(j) && !(Math.abs(i.getId()) == Math.abs(j.getId()))) {
                    Edge makedEdge = new Edge(i, j);
                    makedEdge.setEdgePrice(FormulaEvaluator.evaluate(formula, createSol(i, j)));
                    for (Edge k : _edgeList) {
                        if (k.getNode1().equals(j) && k.getNode2().equals(i)) {
                            same = true;
                        }
                    }
                    if (!same) {
                        _edgeList.add(makedEdge);
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
    }

    @Override
    public void printPheromone() {
        for (Node n : _nodeList) {
            System.out.println("n1:" + n.getId());
            for (Edge e : n.getConnectionMap()) {
                if (n.getId() != e.getNode1().getId()) {
                    System.out.println("length: " + e.getEdgePrice() + "  n2:" + e.getNode1().getId() + "  ph:" + e.getLocalPheromone());
                } else {
                    System.out.println("length: " + e.getEdgePrice() + "  n2:" + e.getNode2().getId() + "  ph:" + e.getLocalPheromone());
                }
            }
        }
    }
}
