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

import java.io.FileInputStream;

import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    try {
      // String path = "data/sat/uf20-01.cnf";
      String path = "data/sat/uf100-01.cnf";

      long start = System.currentTimeMillis();
      new SatAntColonyTest().run(path);
      long end = System.currentTimeMillis();

      System.out.println((end - start) + " ms");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void run(String path) throws Exception {
    // args[0];
    Formula formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(new FileInputStream(path)));

    double quantumPheromone = 462, evaporation = 0.76, defaultPheromone = 0.33;
    double alpha = 1.15, beta = 1.18;
    int numAnts = 865, iterations = 3337;

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

  }

  @Override
  public void algorithmStarted(AntColonyEvent e) {

  }

  @Override
  public void algorithmStopped(AntColonyEvent e) {

  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e) {
    System.out.println(String.format("%d - %f - %d/%d", e.getAntColony().getCurrentIteration(),
        e.getAntColony().getGlobalBest(), e.getAntColony().getGraph().getEdges().size(),
        e.getAntColony().getGraph().getNodes().size() * e.getAntColony().getGraph().getNodes().size() / 2));

  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // System.out.println(e.getAntColony().getCurrentIteration());

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {

  }
}
