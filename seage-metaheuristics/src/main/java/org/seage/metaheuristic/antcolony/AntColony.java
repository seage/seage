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
 *     - Event raising extension
 *     Martin Zaloga
 *     - Reimplementation
 */

package org.seage.metaheuristic.antcolony;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Martin Zaloga
 */
public class AntColony {
  private static final Logger logger = LoggerFactory.getLogger(AntColony.class.getName());
  private AlgorithmEventProducer<IAlgorithmListener<AntColonyEvent>, AntColonyEvent> eventProducer;
  private double roundBest;
  private double globalBest;
  private List<Edge> bestPath;
  private Graph graph;
  private Ant[] ants;
  private Ant bestAnt;

  private int numIterations;
  private boolean started;
  private boolean stopped;
  private boolean keepRunning;
  private long currentIteration;
  private double alpha;
  private double beta;
  private double quantumPheromone;

  /**
   * .
   * @param graph .
   */
  public AntColony(Graph graph) {
    eventProducer = new AlgorithmEventProducer<>(new AntColonyEvent(this));
    this.graph = graph;
    roundBest = Double.MAX_VALUE;
    globalBest = Double.MAX_VALUE;
    started = false;
    stopped = false;
  }

  public void addAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    eventProducer.addAlgorithmListener(listener);
  }

  public void removeAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    eventProducer.removeGeneticSearchListener(listener);
  }

  /**
   * .
   * @param numIterations .
   * @param alpha .
   * @param beta .
   * @param quantumPheromone .
   * @param defaultPheromone .
   * @param evaporCoeff .
   * @throws Exception Throws exception if the values cannot be set.
   */
  public void setParameters(
      int numIterations, double alpha, double beta, double quantumPheromone,
      double defaultPheromone, double evaporCoeff) throws Exception {
    this.numIterations = numIterations;
    this.alpha = alpha;
    this.beta = beta;
    this.quantumPheromone = quantumPheromone;
    graph.setEvaporCoeff(evaporCoeff);
    graph.setDefaultPheromone(defaultPheromone);
  }

  /**
   * Main part of ant-colony algorithm.
   */
  public void startExploring(Node startingNode, Ant[] ants) throws Exception {
    started = keepRunning = true;
    stopped = false;
    currentIteration = 0;
    globalBest = Double.MAX_VALUE;
    this.ants = ants;

    eventProducer.fireAlgorithmStarted();

    prepareGraph();

    while (currentIteration < numIterations && keepRunning) {
      currentIteration++;
      var antReports = new ArrayList<List<Edge>>();

      for (int j = 0; j < this.ants.length && keepRunning; j++) {
        /// EXPLORE
        var antReport = this.ants[j].explore(startingNode);
        antReports.add(antReport);
      }
      resolveRound(antReports);
      graph.evaporate();
      graph.prune(currentIteration);
      eventProducer.fireIterationPerformed();
    }
    eventProducer.fireAlgorithmStopped();
    stopped = true;
  }

  public void stopExploring() {
    keepRunning = false;
  }

  private void prepareGraph() {
    for (Ant a : ants) {
      if (!keepRunning) {
        break;
      }
      try {
        a.setParameters(alpha, beta, quantumPheromone);

        if (a.getNodeIDsAlongPath().isEmpty()) {
          continue;
        }      
        List<Edge> path = a.doFirstExploration();
        if (a.getDistanceTravelled() < globalBest) {
          globalBest = a.getDistanceTravelled();          
          bestPath = new ArrayList<>(path);
          bestAnt = a;
          eventProducer.fireNewBestSolutionFound();
        }
      } catch (Exception e) {
        logger.warn("Unable to do a first exploration", e);
      }
    }
  }

  /**
   * Evaluation for each iteration.
   */
  private void resolveRound(ArrayList<List<Edge>> antReports) {
    roundBest = Double.MAX_VALUE;
    boolean newBest = false;
    double pathLength = 0;
    int counter = 0;
    for (List<Edge> path : antReports) {
      if (bestPath == null) {
        bestPath = new ArrayList<>(path);
      }

      pathLength = ants[counter]._distanceTravelled;

      if (pathLength < roundBest) {
        roundBest = pathLength;
      }

      if (roundBest < globalBest) {
        globalBest = roundBest;
        bestPath = new ArrayList<Edge>(path);
        newBest = true;
        bestAnt = ants[counter];
      }
      counter++;
    }
    if (newBest) {
      eventProducer.fireNewBestSolutionFound();
    }
  }

  /**
   * The best solution.
   * 
   * @return The best path
   */
  public List<Edge> getBestPath() {
    return bestPath;
  }

  public Ant getBestAnt() {
    return bestAnt;
  }

  /**
   * Value finding of the best solution.
   * 
   * @return - Value of the best solution
   */
  public double getGlobalBest() {
    return globalBest;
  }

  public long getCurrentIteration() {
    return currentIteration;
  }

  public boolean isRunning() {
    return started && !stopped;
  }

  public Graph getGraph() {
    return graph;
  }
}
