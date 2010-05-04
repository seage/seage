/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatGraph extends Graph implements java.lang.Cloneable {

    private boolean[] _preparedSolution;

    public SatGraph(Formula formula, double evaporation, double defaultPheromone) {
        super(evaporation);
        _nodeList.add(new Node(0));
        for (int i = 1; i <= formula.getLiteralCount(); i++) {
            _nodeList.add(new Node(i));
            _nodeList.add(new Node(-i));
        }
        _nuberNodes = _nodeList.size();
        _preparedSolution = new boolean[formula.getLiteralCount()];
        for (int i = 0; i < formula.getLiteralCount(); i++) {
            _preparedSolution[i] = true;
        }
        fillEdgeMap(formula);
        setDefaultPheromone(defaultPheromone);
    }

    /**
     * Creating solution in form for determinig
     * @param node - Actual node
     * @param preparedSolution - pre-prepared solution
     * @return - Prepared array of values
     */
    private boolean[] createSol(Node node) {
        boolean[] solution = _preparedSolution.clone();
        int index = Math.abs(node.getId());
        if (node.getId() < 0) {
            solution[index - 1] = false;
        }
        return solution;
    }

    /**
     * Making edges
     * @param start - Start node
     * @param end - Destination node
     * @param formula - Readed formula
     */
    private void makeEdge(Node start, Node end, Formula formula) {
        Edge edge = new Edge(start, end, _localEvaporation);
        edge.setEdgeLength(FormulaEvaluator.evaluate(formula, createSol(end)));
        _edgeList.add(edge);
        start.addConnection(edge);
    }

    /**
     * List of edges filling
     * @param formula - Readed formula
     */
    private void fillEdgeMap(Formula formula) {
        makeEdge(_nodeList.get(0), _nodeList.get(1), formula);
        makeEdge(_nodeList.get(0), _nodeList.get(2), formula);
        for (int i = 1; i < formula.getLiteralCount() * 2 - 2; i += 2) {
            makeEdge(_nodeList.get(i), _nodeList.get(i + 2), formula);
            makeEdge(_nodeList.get(i), _nodeList.get(i + 3), formula);
            makeEdge(_nodeList.get(i + 1), _nodeList.get(i + 2), formula);
            makeEdge(_nodeList.get(i + 1), _nodeList.get(i + 3), formula);
        }
        _nuberEdges = _edgeList.size();
    }

    @Override
    public void printPheromone() {
        for (Node n : _nodeList) {
            System.out.println("n1:" + n.getId());
            for (Edge e : n.getConnectionMap()) {
                System.out.println(e.getEdgeLength() + "  n2:" + e.getNode2().getId() + "  ph:" + e.getLocalPheromone());
            }
        }
    }
}
