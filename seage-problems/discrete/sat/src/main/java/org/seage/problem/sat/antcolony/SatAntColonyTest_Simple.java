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
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat.antcolony;

import java.util.ArrayList;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Clause;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest_Simple implements IAlgorithmListener<AntColonyEvent> {
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    try {
      new SatAntColonyTest_Simple().run();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void run() throws Exception {
    ArrayList<Clause> clauses = new ArrayList<Clause>();
    clauses.add(new Clause(new Literal[] { new Literal(0, false), new Literal(1, true), new Literal(2, true) }));
    clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, false), new Literal(2, true) }));
    clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, true), new Literal(2, false) }));

    Formula formula = new Formula(null, clauses);

    double quantumPheromone = 1000, evaporation = 0.92, defaultPheromone = 0.1;
    double alpha = 2, beta = 1;
    int numAnts = 10, iterations = 10;

    Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
    FormulaEvaluator evaluator = new FormulaEvaluator(formula);
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, evaporation);

    Ant ants[] = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++)
      ants[i] = new SatAnt(graph, null, formula, evaluator);

    colony.startExploring(graph.getNodes().get(0), ants);

    System.out.println("Global best: " + colony.getGlobalBest());

    Boolean[] s = new Boolean[colony.getBestPath().size()];
    for (int i = 0; i < s.length; i++) {
      s[i] = colony.getBestPath().get(i).getNodes()[0].getID() > 0;
      int s2 = 0;
      if (s[i])
        s2 = 1;
      System.out.print(s2);
    }
    System.out.println();
    System.out.println("Global best: " + FormulaEvaluator.evaluate(formula, s));
    // graph.printPheromone();
  }

  @Override
  public void algorithmStarted(AntColonyEvent e) {

  }

  @Override
  public void algorithmStopped(AntColonyEvent e) {

  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e) {
    System.out.println(
        String.format("%f - %d - %d/%d", e.getAntColony().getGlobalBest(), e.getAntColony().getCurrentIteration(),
            e.getAntColony().getGraph().getEdges().size(), e.getAntColony().getGraph().getNodes().size() * 2));

  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // System.out.println(e.getAntColony().getCurrentIteration());

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {

  }
}
