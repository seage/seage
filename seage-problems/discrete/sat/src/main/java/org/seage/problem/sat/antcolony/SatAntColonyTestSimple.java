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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Zagy
 */
public class SatAntColonyTestSimple implements IAlgorithmListener<AntColonyEvent> {
  private static final Logger log = LoggerFactory.getLogger(SatAntColonyTestSimple.class.getName());
  
  public static void main(String[] args) throws Exception {
    try {
      new SatAntColonyTestSimple().run();
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run() throws Exception {
    ArrayList<Clause> clauses = new ArrayList<Clause>();
    clauses.add(new Clause(new Literal[] { new Literal(0, false), new Literal(1, true), new Literal(2, true) }));
    clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, false), new Literal(2, true) }));
    clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, true), new Literal(2, false) }));

    Formula formula = new Formula(null, clauses);

    double quantumPheromone = 1000;
    double evaporation = 0.92;
    double defaultPheromone = 0.1;
    double alpha = 2;
    double beta = 1;
    int numAnts = 10;

    int iterations = 10;
    Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
    FormulaEvaluator evaluator = new FormulaEvaluator(formula);
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, evaporation);

    Ant[] ants = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++) {
      ants[i] = new SatAnt(graph, null, formula, evaluator);
    }

    colony.startExploring(graph.getNodes().get(0), ants);

    log.info("Global best: {}", colony.getGlobalBest());

    Boolean[] s = new Boolean[colony.getBestPath().size()];
    String line = "";
    for (int i = 0; i < s.length; i++) {
      s[i] = colony.getBestPath().get(i).getNodes()[0].getID() > 0;
      line += s[i] ? '1' : '0';
    }
    log.info("{}", line);
    log.info("Global best: {}", FormulaEvaluator.evaluate(formula, s));
    // graph.printPheromone();
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
    log.info("{} - {} - {}/{}", 
        e.getAntColony().getGlobalBest(), e.getAntColony().getCurrentIteration(),
        e.getAntColony().getGraph().getEdges().size(), 
        e.getAntColony().getGraph().getNodes().size() * 2);
  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // log.info("{}", e.getAntColony().getCurrentIteration());
  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {
    // Nothing here
  }
}