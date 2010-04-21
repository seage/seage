/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antColony;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;

/**
 *
 * @author Zagy
 */
public class SatGraph extends Graph implements java.lang.Cloneable {

    boolean[] _preparedSolution;

    public SatGraph(Formula formula, double evaporation, double defaultPheromone) {
        super(evaporation);
        addNode(0, false);
        for (int i = 1; i <= formula.getLiteralCount(); i++) {
            addNode(i, true);
            addNode(-i, false);
        }
        addNode(1 + formula.getLiteralCount(), false);
        _nuberNodes = _nodeList.size();
        _preparedSolution = new boolean[formula.getLiteralCount()];
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
        SatEdge satEdge1, satEdge2, satEdge3, satEdge4;
        SatNode satNode1, satNode2, satNode3, satNode4;

        satNode1 = (SatNode) _nodeList.get(0);
        satNode2 = (SatNode) _nodeList.get(1);
        satNode3 = (SatNode) _nodeList.get(2);
        satEdge1 = new SatEdge(satNode1, satNode2, _localEvaporation, formula, _preparedSolution);
        satEdge2 = new SatEdge(satNode1, satNode3, _localEvaporation, formula, _preparedSolution);
        _edgeList.add(satEdge1);
        _edgeList.add(satEdge2);
        satNode1.addConnection(satEdge1);
        satNode1.addConnection(satEdge2);

        for (int i = 1; i < formula.getLiteralCount() * 2 - 2; i += 2) {
            satNode1 = (SatNode) _nodeList.get(i);
            satNode2 = (SatNode) _nodeList.get(i + 1);
            satNode3 = (SatNode) _nodeList.get(i + 2);
            satNode4 = (SatNode) _nodeList.get(i + 3);
            satEdge1 = new SatEdge(satNode1, satNode3, _localEvaporation, formula, _preparedSolution);
            satEdge2 = new SatEdge(satNode1, satNode4, _localEvaporation, formula, _preparedSolution);
            satEdge3 = new SatEdge(satNode2, satNode3, _localEvaporation, formula, _preparedSolution);
            satEdge4 = new SatEdge(satNode2, satNode4, _localEvaporation, formula, _preparedSolution);
            _edgeList.add(satEdge1);
            _edgeList.add(satEdge2);
            _edgeList.add(satEdge3);
            _edgeList.add(satEdge4);
            satNode1.addConnection(satEdge1);
            satNode1.addConnection(satEdge2);
            satNode2.addConnection(satEdge3);
            satNode2.addConnection(satEdge4);
        }
        satNode1 = (SatNode) _nodeList.get(_nodeList.size()-3);
        satNode2 = (SatNode) _nodeList.get(_nodeList.size()-2);
        satNode3 = (SatNode) _nodeList.get(_nodeList.size()-1);
        satEdge1 = new SatEdge(satNode1, satNode3, _localEvaporation, formula, _preparedSolution);
        satEdge2 = new SatEdge(satNode2, satNode3, _localEvaporation, formula, _preparedSolution);
        _edgeList.add(satEdge1);
        _edgeList.add(satEdge2);
        satNode1.addConnection(satEdge1);
        satNode2.addConnection(satEdge2);
        _nuberEdges = _edgeList.size();
    }
}
