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
import java.util.Random;

import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
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
public class JspAntColonyTest implements IAlgorithmListener<AntColonyEvent>
{
  private int _edges;
  private Random generator = new Random();

  public static void main(String[] args)
  {
    try
    {
      String instanceID = "ft10";
      String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JobsDefinition jobs = null;

      try(InputStream stream = JspAntColonyTest.class.getResourceAsStream(path)) {
        jobs = new JobsDefinition(jobInfo, stream);
      }

      //new JspSimulatedAnnealingTest().runAlgorithm(jobs);
      //new JspSimulatedAnnealingTest().runAlgorithmAdapter(jobs);
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
    result.putValue("quantumPheromone", 43);
    result.putValue("localEvaporation", 0.95);
    result.putValue("defaultPheromone", 0.491);
    result.putValue("alpha", 1.0);
    result.putValue("beta", 2.3);
    result.putValue("numAnts", 150);
    return result;
  }

  public void runAlgorithmAdapter(JobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, 1, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
  }


  public void run(String path) throws Exception
  {
//    City[] cities = CityProvider.readCities(new FileInputStream(path));
//    _edges = cities.length * (cities.length - 1) / 2;
//    int iterations = 10000;
//    //		int numAnts = 500;
//    //		double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone = 100;
//    //		double alpha = 1, beta = 3;
//    int numAnts = 150;
//    double defaultPheromone = 0.491, localEvaporation = 0.95, quantumPheromone = 43;
//    double alpha = 1.0, beta = 2.3;
//    JspGraph graph = new JspGraph(cities);
//    System.out.println("Loaded ...");
//    AntBrain brain = new JspAntBrain(graph);
//    AntColony colony = new AntColony(graph, brain);
//    colony.addAntColonyListener(this);
//    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);
//
//    Ant ants[] = new Ant[numAnts];
//    for (int i = 0; i < numAnts; i++)
//      ants[i] = new Ant(null);
//    //brain.setParameters(graph.getNodeList().size(), alpha, beta);
//
//    long t1 = System.currentTimeMillis();
//    colony.startExploring(graph.getNodes().get(1), ants);
//    long t2 = System.currentTimeMillis();
//    // graph.printPheromone();
//    System.out.println("Global best: " + colony.getGlobalBest());
//    System.out.println("Edges: " + colony.getBestPath().size());
//    System.out.println("Nodes: " + graph.getNodes().size());
//    System.out.println("Time [ms]: " + (t2 - t1));
//    // visualization
//    Integer[] tour = new Integer[colony.getBestPath().size() + 1];
//    tour[0] = colony.getBestPath().get(0).getNode1().getID();
//    for (int i = 1; i < tour.length - 1; i++)
//    {
//      tour[i] = colony.getBestPath().get(i).getNode1().getID();
//      if (i > 0 && tour[i - 1] == tour[i])
//      {
//        tour[i] = colony.getBestPath().get(i).getNode2().getID();
//      }
//
//      System.out.print(tour[i] + " ");
//    }
//    tour[tour.length - 1] = colony.getBestPath().get(tour.length - 2).getNode2().getID();
//
//    System.out.println();
//    System.out.println(new TspPhenotypeEvaluator(cities).evaluate(tour)[0]);
//    //		Arrays.sort(tour);
//    //		for (int i = 1; i < tour.length; i++)
//    //			System.out.print(tour[i] + " ");
//    //		System.out.println();
//
//    //int best = (int)colony.getGlobalBest();
//    //String path2 = "vizualization/ants-"+instance+"-"+best+"-"+System.currentTimeMillis()+".png";
//    //Visualizer.instance().createGraph(cities, tour, path2, 1000, 800);
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
