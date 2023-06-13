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
 *   Richard Malek
 *   - Initial implementation
 *   David Omrai
 *   - Jsp implementation
 */

package org.seage.problem.jsp.antcolony;

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
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Richard Malek
 * @author David Omrai
 */
public class JspAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  private static final Logger log = LoggerFactory.getLogger(JspAntColonyTest.class.getName());

  private int edges;
  private Random generator = new Random();

  /**
   * .
   * @param args .
   */
  public static void main(String[] args) {
    try {
      // String instanceID = "ft06";
      String instanceID = "ft10";
      // String instanceID = "ft20";
      // String instanceID = "la01";
      // String instanceID = "la02";
      // String instanceID = "la04";
      // String instanceID = "la35";
      // String instanceID = "swv20";
      String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
      // String instanceID = "yn_3x3_example";
      // String path = String.format("/org/seage/problem/jsp/test-instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo =
          new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JspJobsDefinition jobs = null;

      try (InputStream stream = JspAntColonyTest.class.getResourceAsStream(path)) {
        jobs = new JspJobsDefinition(jobInfo, stream);
      }

      new JspAntColonyTest().runAlgorithm(jobs);
      // new JspAntColonyTest().runAlgorithmAdapter(jobs);
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
      sum += edge.getEdgeCost();
      edges++;
    }
    log.info("{}", sum);
    log.info("{}", edges);
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
    result.putValue("evaporationCoef", 0.95);
    result.putValue("alpha", 1.1);
    result.putValue("beta", 1.9);

    return result;
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */
  public void runAlgorithmAdapter(JspJobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules =
        problemProvider.generateInitialSolutions(jobs, 100, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();

    JspAntColonyFactory factory = new JspAntColonyFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    try {
      IAlgorithmAdapter<JspPhenotype, Ant> adapter = factory.createAlgorithm(jobs, eval);
      adapter.solutionsFromPhenotype(schedules);
      adapter.startSearching(params);
      var solutions = adapter.solutionsToPhenotype();
      log.info("{}", adapter.getReport());
      // log.info(solutions[0].getScore());

    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */
  public void runAlgorithm(JspJobsDefinition jobs) throws Exception {
    int opersNum = 0;
    for (int i = 0; i < jobs.getJobsCount(); i++) {
      opersNum += jobs.getJobInfos()[i].getOperationInfos().length;
    }
    edges = opersNum * (opersNum - 1) / 2;

    int iterations = 1000;

    // int numAnts = 1;
    // double alpha = 1, beta = 3;

    double evaporationCoef = 0.995;
    double quantumPheromone = 100.0;
    double alpha = 1.0;

    double beta = 4.2;
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspGraph graph = new JspGraph(jobs, eval);
    log.info("Loaded ...");
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone,
        evaporationCoef);

    int numAnts = 100;
    Ant[] ants = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++) {
      ants[i] = new JspAnt(null, jobs, eval);
      // brain.setParameters(graph.getNodeList().size(), alpha, beta);
    }

    long t1 = System.currentTimeMillis();
    // Start from the 0 starting node
    colony.startExploring(graph.getNodes().get(0), ants);
    long t2 = System.currentTimeMillis();
    // graph.printPheromone();
    log.info("Global best: {}", colony.getGlobalBest());
    log.info("Edges: {}", colony.getBestPath().size());
    log.info("Nodes: {}", graph.getNodes().size());
    log.info("Time [ms]: {}", (t2 - t1));

    // visualization
    Integer[] jobArray = new Integer[colony.getBestPath().size()];
    jobArray[0] = colony.getBestPath().get(0).getNode2(graph.getNodes().get(0)).getID();
    int prev = jobArray[0];
    for (int i = 1; i < jobArray.length; i++) {
      jobArray[i] =
          colony.getBestPath().get(i).getNode2(graph.getNodes().get(jobArray[i - 1])).getID();
      prev = jobArray[i];
    }
    String line = "";
    for (int i = 0; i < jobArray.length; i++) {
      line += jobArray[i] + " ";
      jobArray[i] = jobArray[i] / graph.getFactor();
    }    
    log.info(line);
    log.info("");
    log.info("Best: {}", eval.evaluateSchedule(jobArray));

    // for(int i=0;i<graph.getEdges().size();i++) {
    // log.info(graph.getEdges().get(i));
    // }
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
    log.info("{} - {} -{}/{}", 
        e.getAntColony().getGlobalBest(), e.getAntColony().getCurrentIteration(), 
        e.getAntColony().getGraph().getEdges().size(),
        edges);
  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {
    // Do nothing
  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    if (e.getAntColony().getCurrentIteration() % 100 != 0) {
      return;
    }
    log.info("### iterationPerformed: {}", e.getAntColony().getCurrentIteration());
    log.info("   - best : {}", e.getAntColony().getGlobalBest());
    // log.info(" - path : {}", e.getAntColony().getBestPath());
    log.info("   - edges: {}", e.getAntColony().getGraph().getEdges().size() + " / " + edges);

    // var edges = e.getAntColony().getGraph().getEdges();
    // for(int i=0;i<edges.size();i++) {
    // Edge ed = edges.get(i);
    // log.info(String.format(" - %s (%f, %f)", ed, ed.getEdgeCost(),
    // ed.getLocalPheromone()));
    // }
  }

  // JspGraph g = (JspGraph)e.getAntColony().getGraph();
  // Ant a = e.getAntColony().getBestAnt();
  // ArrayList<Integer> jobArray = new ArrayList<>();
  // for(Integer id : a.getNodeIDsAlongPath()) {
  // if(id == 0) continue;
  // jobArray.add(id / g.getFactor());
  // }
  // try {
  // log.info(" - best ant : " + g.evaluator.evaluateSchedule(jobArray.toArray(new
  // Integer[0])));
  // } catch (Exception e1) {
  // // TODO Auto-generated catch block
  // log.error("{}", ex.getMessage(), ex);
  // }
}
