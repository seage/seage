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
 * 
 * @author Martin Zaloga
 */
public class AntColony {
  private static final Logger _logger = LoggerFactory.getLogger(AntColony.class.getName());
  private AlgorithmEventProducer<IAlgorithmListener<AntColonyEvent>, AntColonyEvent> _eventProducer;
  private double _roundBest;
  private double _globalBest;
  private List<Edge> _bestPath;
  private Graph _graph;
  private Ant[] _ants;
  private Ant bestAnt;

  private int _numIterations;
  private boolean _started, _stopped;
  private boolean _keepRunning;
  private long _currentIteration;
  private double _alpha;
  private double _beta;
  private double _quantumPheromone;

  public AntColony(Graph graph) {
    _eventProducer = new AlgorithmEventProducer<IAlgorithmListener<AntColonyEvent>, AntColonyEvent>(
        new AntColonyEvent(this));
    _graph = graph;
    _roundBest = Double.MAX_VALUE;
    _globalBest = Double.MAX_VALUE;
    _started = false;
    _stopped = false;
  }

  public void addAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    _eventProducer.addAlgorithmListener(listener);
  }

  public void removeAntColonyListener(IAlgorithmListener<AntColonyEvent> listener) {
    _eventProducer.removeGeneticSearchListener(listener);
  }

  public void setParameters(
      int numIterations, double alpha, double beta, double quantumPheromone,
      double defaultPheromone, double evaporCoeff) throws Exception {
    _numIterations = numIterations;
    _alpha = alpha;
    _beta = beta;
    _quantumPheromone = quantumPheromone;
    _graph.setEvaporCoeff(evaporCoeff);
    _graph.setDefaultPheromone(defaultPheromone);
  }

  /**
   * Main part of ant-colony algorithm.
   */
  public void startExploring(Node startingNode, Ant[] ants) throws Exception {
    _started = _keepRunning = true;
    _stopped = false;
    _currentIteration = 0;
    _globalBest = Double.MAX_VALUE;
    _ants = ants;

    _eventProducer.fireAlgorithmStarted();

    prepareGraph();

    while (_currentIteration < _numIterations && _keepRunning) {
      _currentIteration++;
      var antReports = new ArrayList<List<Edge>>();

      for (int j = 0; j < _ants.length && _keepRunning; j++) {
        /// EXPLORE
        var antReport = _ants[j].explore(startingNode);
        antReports.add(antReport);
      }
      resolveRound(antReports);
      _graph.evaporate();
      _graph.prune(_currentIteration);
      _eventProducer.fireIterationPerformed();
    }
    _eventProducer.fireAlgorithmStopped();
    _stopped = true;
  }

  public void stopExploring() {
    _keepRunning = false;
  }

  private void prepareGraph() {
    for (Ant a : _ants) {
      if (!_keepRunning) {
        break;
      }
      try {
        a.setParameters(_alpha, _beta, _quantumPheromone);

        if (a.getNodeIDsAlongPath().size() == 0) {
          continue;
        }      
        List<Edge> path = a.doFirstExploration();
        if (a.getDistanceTravelled() < _globalBest) {
          _globalBest = a.getDistanceTravelled();          
          _bestPath = new ArrayList<>(path);
          this.bestAnt = a;
          _eventProducer.fireNewBestSolutionFound();
        }
      } catch (Exception e) {
        _logger.warn("Unable to do a first exploration", e);
      }
    }
  }

  /**
   * Evaluation for each iteration
   */
  private void resolveRound(ArrayList<List<Edge>> antReports) {
    _roundBest = Double.MAX_VALUE;
    boolean newBest = false;
    double pathLength = 0;
    int counter = 0;
    for (List<Edge> path : antReports) {
      if (_bestPath == null) {
        _bestPath = new ArrayList<>(path);;
      }

      pathLength = _ants[counter]._distanceTravelled;

      if (pathLength < _roundBest)
        _roundBest = pathLength;

      if (_roundBest < _globalBest) {
        _globalBest = _roundBest;
        _bestPath = new ArrayList<Edge>(path);
        newBest = true;
        bestAnt = _ants[counter];
      }
      counter++;
    }
    if (newBest)
      _eventProducer.fireNewBestSolutionFound();
  }

  /**
   * The best solution
   * 
   * @return The best path
   */
  public List<Edge> getBestPath() {
    return _bestPath;
  }

  public Ant getBestAnt() {
    return this.bestAnt;
  }

  /**
   * Value finding of the best solution
   * 
   * @return - Value of the best solution
   */
  public double getGlobalBest() {
    return _globalBest;
  }

  public long getCurrentIteration() {
    return _currentIteration;
  }

  public boolean isRunning() {
    return _started && !_stopped;
  }

  public Graph getGraph() {
    return _graph;
  }
}
