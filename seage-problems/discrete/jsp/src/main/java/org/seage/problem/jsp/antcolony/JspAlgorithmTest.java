/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
import java.util.List;
import java.util.Random;

import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;

import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 * 
 * @author Richard Malek
 * Edited by David Omrai
 */
public class JspAlgorithmTest implements IAlgorithmListener<AntColonyEvent>
{
  private int _edges;
  private Random generator = new Random();

  public static void main(String[] args)
  {
    try
    {
      String instanceID = "yn_3x3_example";
      String path = String.format("/org/seage/problem/jsp/test-instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JobsDefinition jobs = null;

      try(InputStream stream = JspAlgorithmTest.class.getResourceAsStream(path)) {
        jobs = new JobsDefinition(jobInfo, stream);
      }

      new JspAlgorithmTest().runAlgorithm(jobs);
      // new JspAlgorithmTest().runAlgorithmAdapter(jobs);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void testing(Graph graph)
  {
    double sum = 0;
    double edges = 0;
    for (Edge edge : graph.getEdges())
    {
      sum += edge.getEdgePrice();
      edges++;
    }
    System.out.println(sum);
    System.out.println(edges);
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("AntColony");
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("quantumPheromone", 100);
    result.putValue("localEvaporation", 0.99);
    result.putValue("defaultPheromone", 0.491);
    result.putValue("alpha", 1.0);
    result.putValue("beta", 2.3);
    result.putValue("numAnts", 10);
    return result;
  }

  public void runAlgorithmAdapter(JobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, 1, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();

    JspAntColonyFactory factory = new JspAntColonyFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    try {
        IAlgorithmAdapter<JspPhenotype, Ant> adapter =  factory.createAlgorithm(jobs, eval);
        adapter.solutionsFromPhenotype(schedules);
        adapter.startSearching(params);
        var solutions = adapter.solutionsToPhenotype();
        System.out.println(solutions[0].getObjValue());
        System.out.println(solutions[0].getScore());

    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public void runAlgorithm(JobsDefinition jobs) throws Exception
  {
    int opersNum = 0;
    for (int i = 0; i < jobs.getJobsCount(); i++)
      opersNum += jobs.getJobInfos()[i].getOperationInfos().length;

    _edges = opersNum * (opersNum - 1) / 2;
    int iterations = 100;
    // int numAnts = 500;
    // double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone = 100;
    // double alpha = 1, beta = 3;
    int numAnts = 10;
    double defaultPheromone = 0.01, localEvaporation = 0.96, quantumPheromone = 100;
    double alpha = 1.0, beta = 2.3;

    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspGraph graph = new JspGraph(jobs, eval);
    System.out.println("Loaded ...");
    JspAntBrain brain = new JspAntBrain(graph, jobs, eval);
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

    Ant ants[] = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++)
      ants[i] = new Ant(brain, graph, null);
    //brain.setParameters(graph.getNodeList().size(), alpha, beta);

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
    jobArray[0] = colony.getBestPath().get(0).getNode2().getID();
    int prev = jobArray[0];
    for (int i = 1; i < jobArray.length; i++)
    {
      jobArray[i] = colony.getBestPath().get(i).getNode2().getID();
      if (prev == jobArray[i])
      {
        jobArray[i] = colony.getBestPath().get(i).getNode1().getID();
      }
      prev = jobArray[i];      
    }
    for (int i = 0; i < jobArray.length; i++) {
      System.out.print(jobArray[i] + " ");
      jobArray[i] = jobArray[i] / graph.getFactor();
    }
    System.out.println();
    System.out.println("Best: " + eval.evaluateSchedule(jobArray));
  }

  @Override
  public void algorithmStarted(AntColonyEvent e)
  {
    System.out.println("algorithmStarted");

  }

  @Override
  public void algorithmStopped(AntColonyEvent e)
  {
    System.out.println("algorithmStopped");

  }

  @Override
  public void newBestSolutionFound(AntColonyEvent e)
  {
    System.out.println(String.format("%f - %d - %d/%d",
        e.getAntColony().getGlobalBest(),
        e.getAntColony().getCurrentIteration(),
        e.getAntColony().getGraph().getEdges().size(),
        _edges));

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e)
  {

  }

  @Override
  public void iterationPerformed(AntColonyEvent e)
  {
    //System.out.println("iterationPerformed: " + e.getAntColony().getCurrentIteration());
    //System.out.println(" - edges: " + e.getAntColony().getGraph().getEdges().size() +" / "+_edges);

  }
}
