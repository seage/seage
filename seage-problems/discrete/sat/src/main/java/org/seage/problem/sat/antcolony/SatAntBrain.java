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
package org.seage.problem.sat.antcolony;

import java.util.Vector;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatAntBrain extends AntBrain {

    private Formula _formula;

    public SatAntBrain(SatGraph graph, Formula formula) {
        super(graph);
        _formula = formula;
    }

//    @Override
//    protected List<Edge> getAvailableEdges(Node currentPosition, HashSet<Node> visited) {
//        if(currentPosition.getConnectionMap().size() == 0){
//            return null;
//        }
//        return currentPosition.getConnectionMap().;
//    }

//    @Override
//    protected Node selectNextNode(Node currentNode, List<Node> nodes, HashSet<Node> visited)
//    {
//        double[] probabilities = new double[edges.size()];
//        double sum = 0;
//        // for each Edges
//        for (int i = 0; i < probabilities.length; i++) {
//            Edge e = edges.get(i);
//            probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgePrice(), _beta);
//            sum += probabilities[i];
//        }
//        for (int i = 0; i < probabilities.length; i++) {
//            probabilities[i] /= sum;
//        }
//        return edges.get(next(probabilities));
//    }
 
    protected double pathCost(Vector<Edge> path) {
        boolean[] solution = new boolean[_formula.getLiteralCount()];
        Node node;
        for (int i = 0; i < _formula.getLiteralCount(); i++) {
            node = (Node) path.get(i).getNode2();
            if (node.getID() < 0) {
                solution[i] = false;
            } else {
                solution[i] = true;
            }
        }
        return (FormulaEvaluator.evaluate(_formula, solution) + 0.1);
    }

	@Override
	public double getNodesDistance(Node n1, Node n2)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
