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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SatAntBrain class.
 *
 * @author Zagy
 */
public class SatAnt extends Ant {
  private static final Logger log = LoggerFactory.getLogger(SatAnt.class.getName());

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

  @Override
  public double getDistanceTravelled(Graph graph, List<Edge> path) throws Exception {
    Boolean[] solution = new Boolean[formula.getLiteralCount()];
    List<Node> nodeList = Graph.edgeListToNodeList(path);

    double subRes = 0.0;
    log.debug("Edges cost");
    for (Edge e : path) {

      subRes += e.getEdgeCost();
      log.debug("f: {}, t: {}, cost: {}", e.getNodes()[0].getID(), e.getNodes()[1].getID(), e.getEdgeCost());
    }
    
    for (Node n : nodeList) {
      if (n.getID() == 0) {
        continue;
      }
      solution[Math.abs(n.getID()) - 1] = n.getID() > 0;
    }

    double result = FormulaEvaluator.evaluate(formula, solution);

    // One is to prevent the dividing by zero
    // return Math.max((formula.getClauses().size() - result), 0.001);
    return Math.max(subRes, 0.001);
  }

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

  // @Override // poor performance 
  // public double getNodeDistance(Graph graph, List<Node> nodePath, Node n2) {
  //   Boolean[] solution = new Boolean[this.formula.getLiteralCount()];

  //   for (Node n : nodePath) {
  //     if (n.getID() == 0) {
  //       continue;
  //     }
  //     solution[Math.abs(n.getID()) - 1] = n.getID() > 0;
  //   }

  //   double prevCost = FormulaEvaluator.evaluate(formula, solution);

  //   solution[Math.abs(n2.getID()) - 1] = n2.getID() > 0;

  //   double newCost = FormulaEvaluator.evaluate(formula, solution);


  //   return prevCost - newCost + 0.001;
  // }

  // @Override // good performance
  // public double getNodeDistance(Graph graph, List<Node> nodePath, Node n2) {
  //   int n = formula.getClauses().size();
  //   // Boolean[] solution = new Boolean[formula.getLiteralCount()];

  //   Node n1 = nodePath.get(nodePath.size() - 1);
  //   // if (n1.getID() == 0) {
  //   //   return 1;
  //   // }

  //   // solution[Math.abs(n1.getID()) - 1] = n1.getID() > 0;
  //   // solution[Math.abs(n2.getID()) - 1] = n2.getID() > 0;
  //   double b = Math.max(formulaEvaluator.getSingleImpact(n2.getID()), 0.1);
    
  //   double newCost = formulaEvaluator.evaluate(Math.abs(n2.getID()), n2.getID() > 0);
  //   newCost = (newCost + 1.0) / (n * b);

  //   log.debug("from: " + n1.getID() + " to: " + n2.getID() + " cost: " + newCost);

  //   return newCost;
  // }

  @Override // not that good performance
  public double getNodeDistance(Graph graph, List<Node> nodePath, Node n2) {
    int n = formula.getClauses().size();
    Node  prevNode = !nodePath.isEmpty() ? nodePath.get(nodePath.size() - 1) : null;

    // TODO: 
    // Clauses affected by next node
    double b = formulaEvaluator.getLiteralImpact(n2.getID());
    if (prevNode == null) {
      return Math.max(b, 0.001);
    }
    // Clauses affected by previous node
    double a = formulaEvaluator.getLiteralImpact(prevNode.getID());
    // Clauses affected by combination of these literals
    double c = formulaEvaluator.getLiteralPairImpact(prevNode.getID(), n2.getID());

    // TODO: combine a, b, c values into the newCost value
    // Get the number of clauses affected by next node (excluding those affected by prevNode)
    double newCost = (((a + b) - 2 * c) - (a - c)); 

    return Math.max(newCost, 0.001);
  }

  // @Override // decent prefromance
  // public double getNodeDistance(Graph graph, List<Node> nodePath, Node n2) {
  //   int n = formula.getClauses().size();
  //   // Boolean[] solution = new Boolean[formula.getLiteralCount()];

  //   Node n1 = nodePath.get(nodePath.size() - 1);
  //   if (n1.getID() == 0) {
  //     return 1;
  //   }

  //   // solution[Math.abs(n1.getID()) - 1] = n1.getID() > 0;
  //   // solution[Math.abs(n2.getID()) - 1] = n2.getID() > 0;
    
  //   int n1i = formulaEvaluator.getSingleImpact(n1.getID());
  //   int n2i = formulaEvaluator.getSingleImpact(n2.getID());
  //   int n12i = formulaEvaluator.getPairImpact(n1.getID(), n2.getID());
  //   // double newCost = 1.0;//1 - () / formula.getClauses().size());
  //   double newCost = formula.getClauses().size() - FormulaEvaluator.evaluatePair(formula, n1.getID(), n2.getID());
  //   newCost = (newCost + 1.0) / (n * n2i);

  //   return newCost;
  // }

  @Override
  public List<Integer> getNodeIDsAlongPath() {
    List<Integer> result = super.getNodeIDsAlongPath();
    
    if (!result.isEmpty()) {
      result.remove(0);
    }

    return result;
  }

  public Formula getFormula() {
    return formula;
  }
}
