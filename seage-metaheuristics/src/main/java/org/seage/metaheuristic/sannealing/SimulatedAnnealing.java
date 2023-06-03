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
 * .
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.metaheuristic.sannealing;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;

/**
 * .
 *
 * @author Jan Zmatlik
 */

public class SimulatedAnnealing<S extends Solution> implements ISimulatedAnnealing<S> {

  /**
   * The current temperature.
   */
  private double currentTemperature;

  /**
   * The maximal temperature.
   */
  private double maximalTemperature;

  /**
   * The minimal temperature.
   */
  private double minimalTemperature;

  /**
   * The annealing coeficient.
   */
  private double annealCoefficient;

  /**
   * The number of current iteration.
   */
  private long currentIteration;

  /**
   * Provide firing Events, registering listeners.
   */
  // private SimulatedAnnealingListenerProvider _listenerProvider = new
  private AlgorithmEventProducer<IAlgorithmListener<SimulatedAnnealingEvent>, 
      SimulatedAnnealingEvent> eventProducer;
  /**
   * The Solution which is actual.
   */
  private S currentSolution;

  /**
   * The Solution which is best.
   */
  private S bestSolution;

  /**
   * Modifying current solution.
   */
  private IMoveManager moveManager;

  /**
   * Calculate and set objective value of solution.
   */
  private IObjectiveFunction objectiveFunction;

  /**
   * Maximal number of iterations.
   */
  private long maximalIterationCount;

  /**
   * Maximal count of success iterations.
   */
  @SuppressWarnings("unused")
  private long maximalSuccessIterationCount;

  private boolean stopSearching = false;

  private boolean isRunning = false;

  /**
   * Constructor.
   *
   * @param newObjectiveFunction is objective function.
   * @param newMoveManager       is performing modification solution.
   */
  public SimulatedAnnealing(IObjectiveFunction newObjectiveFunction, IMoveManager newMoveManager) {
    objectiveFunction = newObjectiveFunction;
    moveManager = newMoveManager;
    eventProducer = new AlgorithmEventProducer<>(new SimulatedAnnealingEvent(this));
  }

  /**
   * This method is called to perform the simulated annealing.
   *
   * @throws Exception .
   */
  @Override
  public void startSearching(S solution) throws Exception {
    eventProducer.fireAlgorithmStarted();
    // Searching is starting
    stopSearching = false;
    isRunning = true;

    // Initial probability
    double probability = 0;

    currentIteration = 0;

    // At first current temperature is same such as maximal temperature
    currentTemperature = Math.max(minimalTemperature, maximalTemperature);
    minimalTemperature = Math.min(minimalTemperature, maximalTemperature);
    maximalTemperature = currentTemperature;

    // annealCoefficient = Math.exp(
    //     Math.log(minimalTemperature / maximalTemperature) / maximalIterationCount);
    // The best solution is same as current solution
    solution.setObjectiveValue(objectiveFunction.getObjectiveValue(solution));
    bestSolution = currentSolution = solution;
    eventProducer.fireNewBestSolutionFound();

    // Fire event to listeners about that algorithm has started

    // Iterates until current temperature is equal or greather than minimal
    // temperature
    // and successful iteration count is less than zero
    while ((maximalIterationCount >= currentIteration) 
            && !stopSearching) {
      currentIteration++;

      // Move to new locations
      @SuppressWarnings("unchecked")
      S modifiedSolution = (S) moveManager.getModifiedSolution(currentSolution, currentTemperature);

      // Calculate objective function and set value to modified
      // solution
      modifiedSolution.setObjectiveValue(objectiveFunction.getObjectiveValue(modifiedSolution));

      if (modifiedSolution.compareTo(currentSolution) > 0) {
        probability = 1;
      } else {
        probability = Math.exp(
            -(modifiedSolution.getObjectiveValue() 
            - currentSolution.getObjectiveValue()) / currentTemperature);
      }

      if (Math.random() <= probability) {
        currentSolution = modifiedSolution;
        if (modifiedSolution.compareTo(bestSolution) > 0) {
          bestSolution = (S) modifiedSolution.clone();
          eventProducer.fireNewBestSolutionFound();
        }
      }
      // Anneal temperature
      if (currentTemperature > 0.0001) {
        currentTemperature = annealCoefficient * currentTemperature;
      }
      eventProducer.fireIterationPerformed();
    }
    isRunning = false;
    // Fire event to listeners about that algorithm was stopped
    eventProducer.fireAlgorithmStopped();
  }

  @Override
  public S getCurrentSolution() {
    return currentSolution;
  }

  public void setCurrentSolution(S currentSolution) {
    this.currentSolution = currentSolution;
  }

  public final void addSimulatedAnnealingListener(
      IAlgorithmListener<SimulatedAnnealingEvent> listener) {
    eventProducer.addAlgorithmListener(listener);
  }

  @Override
  public double getMaximalTemperature() {
    return maximalTemperature;
  }

  @Override
  public void setMaximalTemperature(double maximalTemperature) {
    this.maximalTemperature = maximalTemperature;
  }

  @Override
  public void setMinimalTemperature(double minimalTemperature) {
    this.minimalTemperature = minimalTemperature;
  }

  @Override
  public double getMinimalTemperature() {
    return minimalTemperature;
  }

  @Override
  public double getCurrentTemperature() {
    return currentTemperature;
  }

  public void setAnnealingCoefficient(double annealCoefficient) {
    this.annealCoefficient = annealCoefficient;
  }

  @Override
  public double getAnnealingCoefficient() {
    return this.annealCoefficient;
  }

  @Override
  public S getBestSolution() {
    return bestSolution;
  }

  @Override
  public void stopSearching() throws SecurityException {
    stopSearching = true;
  }

  @Override
  public long getCurrentIteration() {
    return currentIteration;
  }

  @Override
  public long getMaximalIterationCount() {
    return maximalIterationCount;
  }

  @Override
  public void setMaximalIterationCount(long newMaximalIterationCount) {
    maximalIterationCount = newMaximalIterationCount;
  }

  public boolean isRunning() {
    return isRunning;
  }
}
