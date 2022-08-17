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
 * Contributors: 
 * Richard Malek - Initial implementation 
 * David Omrai - Jsp implementation
 */

package org.seage.problem.fsp.antcolony;

import java.io.InputStream;
import java.util.Random;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.fsp.FspJobsDefinition;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.antcolony.JspAnt;
import org.seage.problem.jsp.antcolony.JspGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Richard Malek 
 * @author David Omrai
 */
public class FspAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  private static final Logger log = LoggerFactory.getLogger(FspAntColonyTest.class.getName());

  private int _edges;
  private Random generator = new Random();

  /**
   * .
   * @param args .
   */
  public static void main(String[] args) {
    try {
      String instanceID = "tai100_20_01";
      // String instanceID = "tai100_20_02";

      String path = String.format("/org/seage/problem/fsp/instances/%s.txt", instanceID);

      ProblemInstanceInfo jobInfo =
          new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      FspJobsDefinition jobs = null;

      try (InputStream stream = FspAntColonyTest.class.getResourceAsStream(path)) {
        jobs = new FspJobsDefinition(jobInfo, stream);
      }

      new FspAntColonyTest().runAlgorithm(jobs);
      new FspAntColonyTest().runAlgorithmAdapter(jobs);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  /**
   * .
   * @param graph .
   */
  public void testing(Graph graph) {
    double sum = 0;
    double edges = 0;
    for (Edge edge : graph.getEdges()) {
      sum += edge.getEdgePrice();
      edges++;
    }
    System.out.println(sum);
    System.out.println(edges);
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("AntColony");
    // for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
    // result.putValue(param.getValueStr("name"), param.getValue("init"));
    // }
    result.putValue("numAnts", 100);
    result.putValue("iterationCount", 1000);
    result.putValue("quantumOfPheromone", 1.0);
    result.putValue("localEvaporation", 0.95);
    result.putValue("defaultPheromone", 0.2);
    result.putValue("alpha", 1.1);
    result.putValue("beta", 1.9);

    return result;
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */ 
  public void runAlgorithmAdapter(FspJobsDefinition jobs) throws Exception {
    FspProblemProvider problemProvider = new FspProblemProvider();
    JspPhenotype[] schedules =
        problemProvider.generateInitialSolutions(jobs, 100, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();

    FspAntColonyFactory factory = new FspAntColonyFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    try {
      IAlgorithmAdapter<JspPhenotype, Ant> adapter = factory.createAlgorithm(jobs, eval);
      adapter.solutionsFromPhenotype(schedules);
      adapter.startSearching(params);
      var solutions = adapter.solutionsToPhenotype();
      System.out.println(adapter.getReport().toString());
      // System.out.println(solutions[0].getScore());

    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */
  public void runAlgorithm(FspJobsDefinition jobs) throws Exception {
    int opersNum = 0;
    for (int i = 0; i < jobs.getJobsCount(); i++) {
      opersNum += jobs.getJobInfos()[i].getOperationInfos().length;
    }
    _edges = opersNum * (opersNum - 1) / 2;

    int iterations = 100;

    // int numAnts = 1;
    // double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone = 100;
    // double alpha = 1, beta = 3;

    
    double defaultPheromone = 1;
    double localEvaporation = 0.9;
    double quantumPheromone = 10.0;
    double alpha = 1.1;
    double beta = 1.6;

    FspProblemProvider problemProvider = new FspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    FspGraph graph = new FspGraph(jobs, eval);
    System.out.println("Loaded ...");
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone,
        localEvaporation);

    int numAnts = 10;
    Ant ants[] = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++) {
      ants[i] = new FspAnt(graph, null, jobs, eval);
    }
    // brain.setParameters(graph.getNodeList().size(), alpha, beta);

    long t1 = System.currentTimeMillis();
    // Start from the 0 starting node
    colony.startExploring(graph.getNodes().get(0), ants);
    long t2 = System.currentTimeMillis();
    // graph.printPheromone();
    System.out.println("Global best: " + colony.getGlobalBest());
    System.out.println("Edges: " + colony.getBestPath().size());
    System.out.println("Nodes: " + graph.getNodes().size());
    System.out.println("Time [ms]: " + (t2 - t1));

    // visualization
    Integer[] jobArray = new Integer[colony.getBestPath().size()];
    jobArray[0] = colony.getBestPath().get(0).getNode2(graph.getNodes().get(0)).getID();
    int prev = jobArray[0];
    for (int i = 1; i < jobArray.length; i++) {
      jobArray[i] =
          colony.getBestPath().get(i).getNode2(graph.getNodes().get(jobArray[i - 1])).getID();
      prev = jobArray[i];
    }
    for (int i = 0; i < jobArray.length; i++) {
      System.out.print(jobArray[i] + " ");
      jobArray[i] = jobArray[i] / graph.getFactor();
    }
    System.out.println();
    System.out.println("Best: " + eval.evaluateSchedule(jobArray));

    // for(int i=0;i<graph.getEdges().size();i++) {
    // System.out.println(graph.getEdges().get(i));
    // }
  }

  @Override
  public void algorithmStarted(AntColonyEvent e) {
    System.out.println("algorithmStarted");

  }

  @Override
  public void algorithmStopped(AntColonyEvent e) {
    System.out.println("algorithmStopped");

  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e) {
    System.out.println(String.format("%f - %d - %d/%d", e.getAntColony().getGlobalBest(),
        e.getAntColony().getCurrentIteration(), e.getAntColony().getGraph().getEdges().size(),
        _edges));

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {

  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    if (e.getAntColony().getCurrentIteration() % 100 != 0) {
      return;
    }
    System.out.println("### iterationPerformed: " + e.getAntColony().getCurrentIteration());
    System.out.println("   - best : " + e.getAntColony().getGlobalBest());
    // System.out.println(" - path : " + e.getAntColony().getBestPath());
    System.out
        .println("   - edges: " + e.getAntColony().getGraph().getEdges().size() + " / " + _edges);
  }
}
