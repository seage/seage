/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.antcolony;

import java.io.FileInputStream;
import java.util.List;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;

/**
 * 
 * @author Richard Malek
 */
public class TspAntColonyTest implements IAlgorithmListener<AntColonyEvent> {
  private int _edges;

  public static void main(String[] args) {
    try {
      String instanceID = "berlin52"; // 7542
      // String instanceID = "eil51"; // 426
      // String instanceID = "ch130"; // 6110
      // String instanceID = "lin318"; // 42029
      // String instanceID = "pcb442"; // 50778
      // String instanceID = "u574"; // 36905
      // String instanceID = "rat575-hyflex-2"; // 6773 (8255)

      String path = String.format("seage-problems/discrete/tsp/src/main/resources/org/seage/problem/tsp/instances/%s.tsp", instanceID);// args[0];
      new TspAntColonyTest().run(path);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

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

  public void run(String path) throws Exception {
    City[] cities = CityProvider.readCities(new FileInputStream(path));
    _edges = cities.length * (cities.length - 1) / 2;
    
    int iterations = 1000;
    
    // int numAnts = 500;
    // double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone =
    // 100;
    // double alpha = 1, beta = 3;
    
    // int numAnts = 200;
    // double defaultPheromone = 0.917556841901922, localEvaporation = 0.6269178017512955,
    //     quantumPheromone = 610.6257680691537;
    // double alpha = 1.0654234316716138, beta = 1.1515958770402412;
    
    // David
    // int numAnts = 733;
    // double defaultPheromone = 0.8689519218148817; 
    // double localEvaporation = 0.748594131091018;
    // double quantumPheromone = 288.9555351673542;
    // double alpha = 1.0162687039555678, beta = 6.35356118801852;
    
    // Richard
    int numAnts = 200;
    double defaultPheromone = 10.9, localEvaporation = 0.9, quantumPheromone = 100;
    double alpha = 1.1, beta = 2.1;

    // ----
    TspGraph graph = new TspGraph(cities);
    System.out.println("Loaded ...");
    AntColony colony = new AntColony(graph);
    colony.addAntColonyListener(this);
    colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

    Ant ants[] = new Ant[numAnts];
    for (int i = 0; i < numAnts; i++)
      ants[i] = new TspAnt(graph, null, cities);
    // brain.setParameters(graph.getNodeList().size(), alpha, beta);

    long t1 = System.currentTimeMillis();
    colony.startExploring(graph.getNodes().get(1), ants);
    long t2 = System.currentTimeMillis();
    // graph.printPheromone();
    System.out.println("Global best: " + colony.getGlobalBest());
    System.out.println("Global best hash: " + colony.getBestPath().hashCode());
    System.out.println("Edges: " + colony.getBestPath().size());
    System.out.println("Nodes: " + graph.getNodes().size());
    System.out.println("Time [ms]: " + (t2 - t1));

    List<Integer> tour = Graph.edgeListToNodeIds(colony.getBestPath());
    for (Integer t : tour)
      System.out.print(t + " ");
    System.out.println();
    // TODO: Fix TspPhenotypeEvaluator
    // System.out.println(new TspPhenotypeEvaluator(pro).evaluate(new TspPhenotype(tour))[0]);
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
    System.out.println(
        String.format("%f - %d - %d - %d/%d", e.getAntColony().getGlobalBest(), e.getAntColony().getCurrentIteration(),
            e.getAntColony().getBestPath().hashCode(), e.getAntColony().getGraph().getEdges().size(), _edges));

  }

  @Override
  public void noChangeInValueIterationMade(AntColonyEvent e) {

  }

  @Override
  public void iterationPerformed(AntColonyEvent e) {
    // System.out.println("iterationPerformed: " +
    // e.getAntColony().getCurrentIteration());
    // System.out.println(" - edges: " +
    // e.getAntColony().getGraph().getEdges().size() +" / "+_edges);

  }
}
