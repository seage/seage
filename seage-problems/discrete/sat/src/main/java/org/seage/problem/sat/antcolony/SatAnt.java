/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.

 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.

 * Contributors: Richard Malek - Initial implementation
 *               David Omrai - Revisiting and fixing the code
 */

package org.seage.problem.sat.antcolony;

import java.util.HashSet;
import java.util.List;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 * SatAntBrain class.
 *
 * @author Zagy
 */
public class SatAnt extends Ant {
  FormulaEvaluator formulaEvaluator;

  private Formula formula;

  /**
   * .
   *
   * @param initNodePath .
   * @param formula .
   * @param formulaEvaluator .
   */
  public SatAnt(
      List<Node> initNodePath, Formula formula, FormulaEvaluator formulaEvaluator) {
    super(initNodePath);
    this.formula = formula;
    this.formulaEvaluator = formulaEvaluator;
  }

  // @Override
  // public double getDistanceTravelled(Graph graph, List<Edge> path) throws Exception {
  //   Boolean[] solution = new Boolean[formula.getLiteralCount()];
  //   List<Node> nodeList = Graph.edgeListToNodeList(path);

  //   for (Node n : nodeList) {
  //     if (n.getID() == 0) {
  //       continue;
  //     }
  //     solution[Math.abs(n.getID()) - 1] = n.getID() > 0;
  //   }

  //   return FormulaEvaluator.evaluate(formula, solution);
  // }

  @Override
  protected HashSet<Node> getAvailableNodes(Graph graph, List<Node> nodePath) {
    var result = super.getAvailableNodes(graph, nodePath);

    for (Node n : nodePath) {
      int id = -n.getID();
      Node n2 = graph.getNodes().get(id);
      result.remove(n2);
    }
    
    return result;
  }

  // @Override
  public double getNodeDistance0(Graph graph, List<Node> nodePath, Node n2) {
    Boolean[] solution = new Boolean[this.formula.getLiteralCount()];

    for (Node n : nodePath) {
      if (n.getID() == 0) {
        continue;
      }
      solution[Math.abs(n.getID()) - 1] = n.getID() > 0;
    }

    double prevCost = FormulaEvaluator.evaluate(formula, solution);

    solution[Math.abs(n2.getID()) - 1] = n2.getID() > 0;

    double newCost = FormulaEvaluator.evaluate(formula, solution);


    return prevCost - newCost + 0.001;
  }

  @Override
  public double getNodeDistance(Graph graph, List<Node> nodePath, Node n2) {
    Boolean[] solution = new Boolean[this.formula.getLiteralCount()];

    Node n1 = nodePath.get(nodePath.size() - 1);
    if (n1.getID() == 0) {
      return 1;
    }

    solution[Math.abs(n1.getID()) - 1] = n1.getID() > 0;
    solution[Math.abs(n2.getID()) - 1] = n2.getID() > 0;

    double newCost = FormulaEvaluator.evaluate(formula, solution);

    return newCost;
  }

  @Override
  public List<Integer> getNodeIDsAlongPath() {
    List<Integer> result = super.getNodeIDsAlongPath();
    
    if (result.size() > 0) {
      result.remove(0);
    }

    return result;
  }
}
