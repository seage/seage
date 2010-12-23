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
   * The number of current iteration
   */
  private long _currentIteration;

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
   * Maximal count of iterations
   */
  private long _maximalIterationCount;

  /**
   * Maximal count of success iterations
   */
  private long _maximalSuccessIterationCount;

  private boolean _stopSearching = false;

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
   * This method is called to
   * perform the simulated annealing.
   */
  public void startSearching(Solution solution)
  {
    // Searching is starting
    _stopSearching = false;

    // Initial number of iteration
    long innerIterationCount = 1;

    // Initial number of successful iteration
    long successIterationCount = 1;

    // Initial probability
    double probability = 0;

    _currentIteration = 0;

    // At first current temperature is same such as maximal temperature
    _currentTemperature = _maximalTemperature;

    // The best solution is same as current solution
    _bestSolution = _currentSolution = solution;
//    System.out.println("STARTING FITNESS: "+_bestSolution.getObjectiveValue());
    _objectiveFunction.setObjectiveValue( solution ); // <= ERROR


//    System.out.println("STARTING FITNESS: "+_bestSolution.getObjectiveValue());
    // Fire event to listeners about that algorithm has started
    _listenerProvider.fireSimulatedAnnealingStarted();

    // Iterates until current temperature is equal or greather than minimal temperature
    // and successful iteration count is less than zero
    while (( _currentTemperature >= _minimalTemperature ) && ( successIterationCount > 0 ))
    {
//        System.out.println("(CurrentTemperature = "+_currentTemperature+" >= "+_minimalTemperature+" = minimalTemperature");
        innerIterationCount = successIterationCount = 0;
        System.out.println(_bestSolution.getObjectiveValue());

        while(( innerIterationCount < _maximalIterationCount ) && ( successIterationCount < _maximalSuccessIterationCount ))
            {
//            System.out.println("(innderIteration = "+innerIterationCount+" >= "+_maximalIterationCount+" = _maximalIterationCount");
//            System.out.println("BEST SOLUTION="+_bestSolution.getObjectiveValue());;
            if( _stopSearching ) return;
            _listenerProvider.fireNewIterationStarted();

            _currentIteration++;
            innerIterationCount++;
                
            // Move to new locations
            Solution modifiedSolution = _moveManager.getModifiedSolution( _currentSolution );

            // Calculate objective function and set value to modified solution
            _objectiveFunction.setObjectiveValue( modifiedSolution );

            if(modifiedSolution.getObjectiveValue() < _currentSolution.getObjectiveValue())
                probability = 1;
            else
                probability = Math.exp( - ( modifiedSolution.getObjectiveValue() - _currentSolution.getObjectiveValue() ) / _currentTemperature );

            if( Math.random() <= probability )
            {
                successIterationCount++;
                _currentSolution = modifiedSolution;
                if(_currentSolution.getObjectiveValue() < _bestSolution.getObjectiveValue())
                {
                    _bestSolution = modifiedSolution.clone();
                    _listenerProvider.fireNewBestSolutionFound();
                }
            }
        }
        // Anneal temperature
        _currentTemperature = _annealCoefficient * _currentTemperature;
    }
    // Fire event to listeners about that algorithm was stopped
    _listenerProvider.fireSimulatedAnnealingStopped();
  }

  public Solution getCurrentSolution() {
    return _currentSolution;
  }

  public void setCurrentSolution(Solution _currentSolution) {
    this._currentSolution = _currentSolution;
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

  public void stopSearching() throws SecurityException {
    _stopSearching = true;
  }

  public long getCurrentIteration(){
        return _currentIteration;
  }

  public long getMaximalIterationCount() {
      return _maximalIterationCount;
  }

  public void setMaximalIterationCount(long _maximalIterationCount) {
      this._maximalIterationCount = _maximalIterationCount;
  }

  public long getMaximalSuccessIterationCount() {
      return _maximalSuccessIterationCount;
  }

  public void setMaximalSuccessIterationCount(long _maximalSuccessIterationCount) {
      this._maximalSuccessIterationCount = _maximalSuccessIterationCount;
  }

}