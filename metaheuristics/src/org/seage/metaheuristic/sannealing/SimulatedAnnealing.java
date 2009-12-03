/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.sannealing;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jan Zmatlik
 */

public class SimulatedAnnealing implements ISimulatedAnnealing
{

  /**
   * The current temperature.
   */
  private double _currentTemperature;

  /**
   * The maximal temperature.
   */
  private double _maximalTemperature;

  /**
   * The minimal temperature.
   */
  private double _minimalTemperature;

  /**
   * The annealing coeficient
   */
  private double _annealCoefficient;

  /**
   * Provide firing Events, registering listeners
   */
  private SimulatedAnnealingListenerProvider _listenerProvider = new SimulatedAnnealingListenerProvider( this );

  /**
   * The Solution which is actual
   */
  private Solution _currentSolution;

  /**
   * The Solution which is best
   */
  private Solution _bestSolution;

  /**
   * Modifying current solution
   */
  private IMoveManager _moveManager;

  /**
   * Calculate and set objective value of solution
   */
  private IObjectiveFunction _objectiveFunction;

  /**
   * Constructor
   *
   * @param objectiveFunction is objective function.
   * @param moveManager is performing modification solution.
   */
  public SimulatedAnnealing(IObjectiveFunction objectiveFunction, IMoveManager moveManager)
  {
      _objectiveFunction = objectiveFunction;
      _moveManager = moveManager;
  }  

  /**
   * Called to determine if annealing should take place.
   *
   * @param Delta is value from objective function.
   * @return True if annealing should take place.
   */
  public boolean anneal(double delta)
  {   
    return ( Math.random() < Math.exp( delta / _currentTemperature ) ) ? true : false;
  }

  /**
   * This method is called to
   * perform the simulated annealing.
   */
  public void startSearching(Solution solution)
  {
    // Number of iteration
    int iterationCount = 1;

    // At first current temperature is same such as maximal temperature
    _currentTemperature = _maximalTemperature;

    // The best solution is same as current solution
    _bestSolution = _currentSolution = solution;
    Logger.getLogger("maximal-temperature").log(Level.OFF, String.valueOf(_maximalTemperature));
    Logger.getLogger("minimal-temperature").log(Level.OFF, String.valueOf(_minimalTemperature));

    // Fire event to listeners about that algorithm has started
    _listenerProvider.fireSimulatedAnnealingStarted();

    // Iterates until current temperature is equal or greather than minimal temperature
    while (_currentTemperature >= _minimalTemperature)
    {
//        Logger.getLogger("iterations").log(Level.OFF, String.valueOf(iterationCount));

        // Perform actions
        performOneIteration();        

        // Anneal temperature
        _currentTemperature = _annealCoefficient * _currentTemperature;

        iterationCount++;
    }
      // Fire event to listeners about that algorithm was stopped
      _listenerProvider.fireSimulatedAnnealingStopped();
//      Logger.getLogger("total-iterations").log(Level.OFF, String.valueOf(iterationCount));
//      Logger.getLogger("best-solution").log(Level.OFF, String.valueOf(_bestSolution.getObjectiveValue()));
  }

  private void performOneIteration()
  {
      // Move randomly to new locations
      Solution modifiedSolution = _moveManager.getModifiedSolution( _currentSolution );

      // Calculate objective function and set value to modified solution
      _objectiveFunction.setObjectiveValue( modifiedSolution );

      // Get modified objective value
      double modifiedObjectiveValue = modifiedSolution.getObjectiveValue();

      // Accept new solution if better
      if(modifiedObjectiveValue < _bestSolution.getObjectiveValue())
      {
          _bestSolution = modifiedSolution;
          _listenerProvider.fireNewBestSolutionFound();
      }

      while(!anneal( modifiedObjectiveValue - _currentSolution.getObjectiveValue() ) )
      {
        _currentSolution = modifiedSolution;
      }
  }

  public final void addSimulatedAnnealingListener( ISimulatedAnnealingListener listener )
  {
    _listenerProvider.addSimulatedAnnealingListener( listener );
  } 

  public double getMaximalTemperature()
  {
    return _maximalTemperature;
  }

  public void setMaximalTemperature(double maximalTemperature)
  {
    this._maximalTemperature = maximalTemperature;
  }

  public void setMinimalTemperature(double minimalTemperature)
  {
    this._minimalTemperature = minimalTemperature;
  }

  public double getMinimalTemperature()
  {
    return _minimalTemperature;
  }

  public void setAnnealingCoefficient(double alpha)
  {
    this._annealCoefficient = alpha;
  }
  
  public double getAnnealingCoefficient()
  {
    return this._annealCoefficient;
  }

  public Solution getBestSolution()
  {
    return _bestSolution;
  }

  public void stopSearching() {
    throw new UnsupportedOperationException("Not supported yet.");
  }


}