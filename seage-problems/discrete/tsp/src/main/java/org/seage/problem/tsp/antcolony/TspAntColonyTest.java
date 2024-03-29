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

package org.seage.problem.tsp.antcolony;

import java.io.FileInputStream;
import java.util.List;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Richard Malek
 */
public class TspAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  private static final Logger log = LoggerFactory.getLogger(TspAntColonyTest.class.getName());
  private int edges;
  // private static String instanceID = "burma14"; // ?
  // private static String instanceID = "ulysses16"; // ?
  private static String instanceID = "berlin52"; // 7542
  // private static String instanceID = "eil51"; // 426
  // private static String instanceID = "ch130"; // 6110
  // private static String instanceID = "lin318"; // 42029
  // private static String instanceID = "pcb442"; // 50778
  // private static String instanceID = "u574"; // 36905
  // private static String instanceID = "rat575-hyflex-2"; // 6773 (8255)
  // private static String instanceID = "usa13509-hyflex-8"; // 19982859
  // private static String instanceID = "../test-instances/rm4.tsp";
  // private static String instanceID = "../test-instances/do8.tsp";
  

  public static void main(String[] args) {
    try {
      String instanceFilename = instanceID.endsWith(".tsp") ? instanceID : instanceID + ".tsp";
      String path = String.format(
          "seage-problems/discrete/tsp/src/main/resources/org/seage/problem/tsp/instances/%s",
          instanceFilename);// args[0];
      new TspAntColonyTest().run(path);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    City[] cities = CityProvider.readCities(new FileInputStream(path));
    edges = cities.length * (cities.length - 1) / 2;

    // int numAnts = 500;
    // double alpha = 1, beta = 3;

    // int numAnts = 200;
    // quantumPheromone = 610.6257680691537;
    // double alpha = 1.0654234316716138, beta = 1.1515958770402412;

    // David
    // int numAnts = 733;
    // double evaporationCoef = 0.748594131091018;
    // double quantumPheromone = 288.9555351673542;
    // double alpha = 1.0162687039555678, beta = 6.35356118801852;

    int iterations = 10000;
    // Richard
    int numAnts = 50;
    double alpha = 1.0;
    double beta = 5.3;
    double evaporationCoef = 0.95;
    double quantumPheromone = numAnts;
    
    // ----
    TspGraph graph = new TspGraph(cities);
    log.info("Loaded ...");
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, evaporationCoef);

    Ant[] ants = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++) {
      ants[i] = new TspAnt(null, cities);
      // brain.setParameters(graph.getNodeList().size(), alpha, beta);
    }

    long t1 = System.currentTimeMillis();
    colony.startExploring(graph.getNodes().get(1), ants);
    long t2 = System.currentTimeMillis();

    List<Integer> tour = Graph.edgeListToNodeIds(colony.getBestPath());
    String line = "";
    for (Integer t : tour) {
      line += t + " ";
    }

    Integer[] p = (Integer[]) tour.stream().toArray(Integer[]::new);
    TspPhenotype result = new TspPhenotype(p);

    TspProblemProvider provider = new TspProblemProvider();
    ProblemInstance problemInstance = provider.initProblemInstance(
        provider.getProblemInfo().getProblemInstanceInfo(instanceID));
    IPhenotypeEvaluator<TspPhenotype> evaluator = provider.initPhenotypeEvaluator(
        problemInstance);
    
    double[] objVals = evaluator.evaluate(result);

    log.info("Global score: {}", objVals[1]);

    // graph.printPheromone();
    log.info("Global best: {}", colony.getGlobalBest());
    log.info("Global best hash: {}", colony.getBestPath().hashCode());
    log.info("Edges: {}", colony.getBestPath().size());
    log.info("Nodes: {}", graph.getNodes().size());
    log.info("Time: {} ms ", (t2 - t1));

    log.info("{}", line);
    // TODO: Fix TspPhenotypeEvaluator
    // log.info(new TspPhenotypeEvaluator(pro).evaluate(new TspPhenotype(tour))[0]);
  }

  @Override
  public void algorithmStarted(AntColonyEvent e) {
    log.info("algorithmStarted");

  }

  @Override
  public void algorithmStopped(AntColonyEvent e) {
    log.info("algorithmStopped");

  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e) {
    log.info("{} - {} - {} - {}/{}", e.getAntColony().getGlobalBest(),
        e.getAntColony().getCurrentIteration(), e.getAntColony().getBestPath().hashCode(),
        e.getAntColony().getGraph().getEdges().size(), edges);

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {
    // Nothing here
  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // log.info("iterationPerformed: {} - edges: {}", 
    //     e.getAntColony().getCurrentIteration(),
    //     e.getAntColony().getGraph().getEdges().size());

  }
}
