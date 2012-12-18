/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
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

    public SatGraph2(Formula formula) throws Exception {
        super();
        for (int id = 1; id < formula.getLiteralCount() + 1; id++) {
            _nodes.put(new Integer(id),new Node(id));
            _nodes.put(new Integer(-id),new Node(-id));
        }

        _preparedSolution = new boolean[formula.getLiteralCount()];
        for (int i = 0; i < formula.getLiteralCount(); i++) {
            _preparedSolution[i] = true;
        }
        fillEdgeMap(formula);
    }

    private boolean[] createSol(Node node1, Node node2) {
        boolean[] solution = _preparedSolution.clone();
        int index = Math.abs(node1.getID());
        if (node1.getID() < 0) {
            solution[index - 1] = false;
        }
        index = Math.abs(node2.getID());
        if (node2.getID() < 0) {
            solution[index - 1] = false;
        }
        return solution;
    }

    /**
     * List of graph edges filling
     * @throws Exception 
     */
    private void fillEdgeMap(Formula formula) throws Exception {
        boolean same = false;
        for (Node i : _nodes.values()) {
            for (Node j : _nodes.values()) {
                if (!i.equals(j) && !(Math.abs(i.getID()) == Math.abs(j.getID()))) {
                    Edge makedEdge = new Edge(i, j);
                    makedEdge.setEdgePrice(FormulaEvaluator.evaluate(formula, createSol(i, j)));
                    for (Edge k : _edges) {
                        if (k.getNode1().equals(j) && k.getNode2().equals(i)) {
                            same = true;
                        }
                    }
                    if (!same) {
                        _edges.add(makedEdge);
                    }
                }
                same = false;
            }
        }
        for (Node i : _nodes.values()) {
            for (Edge j : _edges) {
                if (j.getNode1().equals(i) || j.getNode2().equals(i)) {
                    i.addEdge(j);
                }
            }
        }
    }

	@Override
	public double getNodesDistance(Node n1, Node n2)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
