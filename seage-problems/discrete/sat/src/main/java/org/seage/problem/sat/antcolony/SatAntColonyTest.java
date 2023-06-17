/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat.antcolony;

import java.io.FileInputStream;
import java.util.List;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author Zagy
 */
public class SatAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  private static final Logger log = LoggerFactory.getLogger(SatAntColonyTest.class.getName());

  public static void main(String[] args) throws Exception {
    try {
      // String filename = "uf20-01.cnf";
      // String filename = "uf75-01.cnf";
      String filename = "uf100-01.cnf";
      String path = String.format("seage-problems/discrete/sat/src/main/resources/org/seage/problem/sat/instances/%s", filename);

      long start = System.currentTimeMillis();
      new SatAntColonyTest().run(path);
      long end = System.currentTimeMillis();

      log.info("{} ms", (end - start));
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    Formula formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(new FileInputStream(path)));

    double quantumPheromone = 200;
    double evaporation = 0.08;
    double alpha = 1.1;
    double beta = 10.2;
    int numAnts = 50;

    int iterations = 50;
    Graph graph = new SatGraph(formula.getLiteralCount());

    FormulaEvaluator formulaEvaluator = new FormulaEvaluator(formula);
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, evaporation);

    Ant[] ants = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++) {
      ants[i] = new SatAnt(graph.nodesToNodePath(List.of(0)), formula, formulaEvaluator);
    }

    colony.startExploring(graph.getNodes().get(0), ants);

    List<Node> bestSolution = colony.getBestAnt().getNodePath();
    log.info("Global best: {}", formulaEvaluator.evaluate(formula, bestSolution));
  }

  @Override
  public void algorithmStarted(AntColonyEvent e) {
    // Nothing here
  }

  @Override
  public void algorithmStopped(AntColonyEvent e) {
    // Nothing here
  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e) {
    SatAnt satAnt = (SatAnt) e.getAntColony().getBestAnt();
    List<Node> bestSolution = satAnt.getNodePath();
    int objVal = FormulaEvaluator.evaluate(satAnt.getFormula(), bestSolution);

    log.info("{} - {} - {}/{} - {}", 
        e.getAntColony().getCurrentIteration(),
        e.getAntColony().getGlobalBest(), 
        e.getAntColony().getGraph().getEdges().size(),
        e.getAntColony().getGraph().getNodes().size()
            * e.getAntColony().getGraph().getNodes().size() / 2,
        objVal);

  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // log.info(e.getAntColony().getCurrentIteration());
  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {
    // Nothing here
  }
}
