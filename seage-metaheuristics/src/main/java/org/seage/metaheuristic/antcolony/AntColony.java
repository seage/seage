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
    this.eventProducer = new AlgorithmEventProducer<>(new AntColonyEvent(this));
    this.graph = graph;
    this.roundBest = Double.MAX_VALUE;
    this.globalBest = Double.MAX_VALUE;
    this.started = false;
    this.stopped = false;
  }

  public void addAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    this.eventProducer.addAlgorithmListener(listener);
  }

  public void removeAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    this.eventProducer.removeGeneticSearchListener(listener);
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
    this.graph.setEvaporCoeff(evaporCoeff);
    this.graph.setDefaultPheromone(defaultPheromone);
  }

  /**
   * Main part of ant-colony algorithm.
   */
  public void startExploring(Node startingNode, Ant[] ants) throws Exception {
    this.started = this.keepRunning = true;
    this.stopped = false;
    this.currentIteration = 0;
    this.globalBest = Double.MAX_VALUE;
    this.ants = ants;

    this.eventProducer.fireAlgorithmStarted();

    prepareGraph();

    while (this.currentIteration < this.numIterations && this.keepRunning) {
      this.currentIteration++;
      var antReports = new ArrayList<List<Edge>>();

      for (int j = 0; j < this.ants.length && this.keepRunning; j++) {
        /// EXPLORE
        var antReport = this.ants[j].explore(startingNode);
        antReports.add(antReport);
      }
      resolveRound(antReports);
      this.graph.evaporate();
      this.graph.prune(this.currentIteration);
      this.eventProducer.fireIterationPerformed();
    }
    this.eventProducer.fireAlgorithmStopped();
    this.stopped = true;
  }

  public void stopExploring() {
    this.keepRunning = false;
  }

  private void prepareGraph() {
    for (Ant a : this.ants) {
      if (!this.keepRunning) {
        break;
      }
      try {
        a.setParameters(this.alpha, this.beta, this.quantumPheromone);

        if (a.getNodeIDsAlongPath().isEmpty()) {
          continue;
        }      
        List<Edge> path = a.doFirstExploration();
        if (a.getDistanceTravelled() < this.globalBest) {
          this.globalBest = a.getDistanceTravelled();          
          this.bestPath = new ArrayList<>(path);
          this.bestAnt = a;
          this.eventProducer.fireNewBestSolutionFound();
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
    this.roundBest = Double.MAX_VALUE;
    boolean newBest = false;
    double pathLength = 0;
    int counter = 0;
    for (List<Edge> path : antReports) {
      if (this.bestPath == null) {
        this.bestPath = new ArrayList<>(path);;
      }

      pathLength = this.ants[counter]._distanceTravelled;

      if (pathLength < this.roundBest) {
        this.roundBest = pathLength;
      }

      if (this.roundBest < this.globalBest) {
        this.globalBest = this.roundBest;
        this.bestPath = new ArrayList<Edge>(path);
        newBest = true;
        bestAnt = this.ants[counter];
      }
      counter++;
    }
    if (newBest) {
      this.eventProducer.fireNewBestSolutionFound();
    }
  }

  /**
   * The best solution.
   * 
   * @return The best path
   */
  public List<Edge> getBestPath() {
    return this.bestPath;
  }

  public Ant getBestAnt() {
    return this.bestAnt;
  }

  /**
   * Value finding of the best solution.
   * 
   * @return - Value of the best solution
   */
  public double getGlobalBest() {
    return this.globalBest;
  }

  public long getCurrentIteration() {
    return this.currentIteration;
  }

  public boolean isRunning() {
    return this.started && !this.stopped;
  }

  public Graph getGraph() {
    return this.graph;
  }
}
