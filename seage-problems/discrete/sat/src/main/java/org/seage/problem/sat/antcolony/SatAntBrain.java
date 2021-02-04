/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat.antcolony;

import java.util.List;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatAntBrain extends AntBrain {

  private Formula _formula;

  public SatAntBrain(Graph graph, Formula formula) {
    super(graph);
    _formula = formula;
  }

  @Override
  protected void markSelected(Node nextNode) {
    super.markSelected(nextNode);

    if (nextNode != null) {
      Node n2 = _graph.getNodes().get(-nextNode.getID());
      _availableNodes.remove(n2);
    }
  }

  @Override
  public double getPathCost(List<Edge> path) {
    Boolean[] solution = new Boolean[_formula.getLiteralCount()];
    List<Node> nodeList = edgeListToNodeList(path);

    for (Node n : nodeList) {
      if (n.getID() == 0)
        continue;
      if (n.getID() < 0) {
        solution[-n.getID() - 1] = false;
      } else {
        solution[n.getID() - 1] = true;
      }
    }
    return (FormulaEvaluator.evaluate(_formula, solution) + 0.1);
  }
}
