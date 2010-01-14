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
package org.seage.metaheuristic.particles;

/**
 * @author Jan Zmatlik
 */

public class ParticleSwarmOptimization implements IParticleSwarmOptimization
{

  private double _maximalVelocity;

  /**
   * Provide firing Events, registering listeners
   */
  private ParticleSwarmOptimizationListenerProvider _listenerProvider = new ParticleSwarmOptimizationListenerProvider( this );

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
  private IVelocityManager _velocityManager;

  /**
   * Calculate and set objective value of solution
   */
  private IObjectiveFunction _objectiveFunction;

  /**
   * Maximal count of iterations
   */
  private long _maximalIterationCount;

  private boolean _stopSearching = false;

  /**
   * Constructor
   *
   * @param objectiveFunction is objective function.
   * @param moveManager is performing modification solution.
   */
  public ParticleSwarmOptimization(IObjectiveFunction objectiveFunction, IVelocityManager velocityManager)
  {
    _objectiveFunction = objectiveFunction;
    _velocityManager = velocityManager;
  }  

  /**
   * This method is called to
   * perform the simulated annealing.
   */
  public void startSearching(Solution[] solutions)
  {
    // Searching is starting
    _stopSearching = false;

    // Initial number of iteration
    long iterationCount = 0;

    // Fire event to listeners about that algorithm has started
    _listenerProvider.fireParticleSwarmOptimizationStarted();


    while ( _maximalVelocity > iterationCount )
    {
      iterationCount++;

      for(Solution solution : solutions)
      {
        
      }

    }

  }

//  public Solution getCurrentSolution() {
//    return _currentSolution;
//  }
//
//  public void setCurrentSolution(Solution _currentSolution) {
//    this._currentSolution = _currentSolution;
//  }

  public final void addParticleSwarmOptimizationListener( IParticleSwarmOptimizationListener listener )
  {
    _listenerProvider.addParticleSwarmOptimizationListener( listener );
  } 

  public Solution getBestSolution()
  {
    return _bestSolution;
  }

  public void stopSearching() {
    _stopSearching = true;
  }

  public long getMaximalIterationCount() {
      return _maximalIterationCount;
  }

  public void setMaximalIterationCount(long _maximalIterationCount) {
      this._maximalIterationCount = _maximalIterationCount;
  }

  public double getMaximalVelocity() {
    return _maximalVelocity;
  }

  public void setMaximalVelocity(double maximalVelocity) {
    _maximalVelocity = maximalVelocity;
  }

}