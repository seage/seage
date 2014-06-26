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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.sannealing;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;

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
	// private SimulatedAnnealingListenerProvider _listenerProvider = new
	// SimulatedAnnealingListenerProvider( this );
	private AlgorithmEventProducer<IAlgorithmListener<SimulatedAnnealingEvent>, SimulatedAnnealingEvent> _eventProducer;
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
	 * Maximal number of iterations
	 */
	private long _maximalIterationCount;

	/**
	 * Maximal count of success iterations
	 */
	private long _maximalSuccessIterationCount;

	private boolean _stopSearching = false;

	private boolean _isRunning = false;

	/**
	 * Constructor
	 * 
	 * @param objectiveFunction
	 *            is objective function.
	 * @param moveManager
	 *            is performing modification solution.
	 */
	public SimulatedAnnealing(IObjectiveFunction objectiveFunction, IMoveManager moveManager)
	{
		_objectiveFunction = objectiveFunction;
		_moveManager = moveManager;
		_eventProducer = new AlgorithmEventProducer<IAlgorithmListener<SimulatedAnnealingEvent>, SimulatedAnnealingEvent>(new SimulatedAnnealingEvent(this));
	}

	/**
	 * This method is called to perform the simulated annealing.
	 * @throws Exception 
	 */
	public void startSearching(Solution solution) throws Exception
	{
		_eventProducer.fireAlgorithmStarted();
		// Searching is starting
		_stopSearching = false;
		_isRunning = true;

		// Initial probability
		double probability = 0;

		
		_currentIteration = 0;

		// At first current temperature is same such as maximal temperature		
		_currentTemperature = Math.max(_minimalTemperature, _maximalTemperature);
		_minimalTemperature = Math.min(_minimalTemperature, _maximalTemperature);		
		_maximalTemperature = _currentTemperature;

		_annealCoefficient = Math.exp(Math.log(_minimalTemperature/_maximalTemperature)/_maximalIterationCount);
		// The best solution is same as current solution
		solution.setObjectiveValue(_objectiveFunction.getObjectiveValue(solution));		
		_bestSolution = _currentSolution = solution;		
		_eventProducer.fireNewBestSolutionFound();

		// Fire event to listeners about that algorithm has started

		// Iterates until current temperature is equal or greather than minimal
		// temperature
		// and successful iteration count is less than zero
		while ((_maximalIterationCount >= _currentIteration) /*&& (successIterationCount > 0)*/ && !_stopSearching)
		{
			//while ((innerIterationCount < _maximalIterationCount) && (successIterationCount < _maximalSuccessIterationCount) && !_stopSearching)
			{
				_currentIteration++;				

				// Move to new locations
				Solution modifiedSolution = _moveManager.getModifiedSolution(_currentSolution, _currentTemperature);

				// Calculate objective function and set value to modified
				// solution
				modifiedSolution.setObjectiveValue(_objectiveFunction.getObjectiveValue(modifiedSolution));

				if (modifiedSolution.compareTo(_currentSolution) > 0)
					probability = 1;
				else
					probability = Math.exp(-(modifiedSolution.getObjectiveValue() - _currentSolution.getObjectiveValue()) / _currentTemperature);

				if (Math.random() <= probability)
				{					
					_currentSolution = modifiedSolution;
					if (modifiedSolution.compareTo(_bestSolution) > 0)
					{
						_bestSolution = modifiedSolution.clone();
						_eventProducer.fireNewBestSolutionFound();
					}
				}
			}
			// Anneal temperature
			_currentTemperature = _annealCoefficient * _currentTemperature;
			_eventProducer.fireIterationPerformed();
		}
		_isRunning = false;
		// Fire event to listeners about that algorithm was stopped
		_eventProducer.fireAlgorithmStopped();
	}

	public Solution getCurrentSolution()
	{
		return _currentSolution;
	}

	public void setCurrentSolution(Solution _currentSolution)
	{
		this._currentSolution = _currentSolution;
	}

	public final void addSimulatedAnnealingListener(IAlgorithmListener<SimulatedAnnealingEvent> listener)
	{
		_eventProducer.addAlgorithmListener(listener);
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
	
	public double getCurrentTemperature()
	{
		return _currentTemperature;
	}

//	public void setAnnealingCoefficient(double alpha)
//	{
//		this._annealCoefficient = alpha;
//	}

	public double getAnnealingCoefficient()
	{
		return this._annealCoefficient;
	}

	public Solution getBestSolution()
	{
		return _bestSolution;
	}

	public void stopSearching() throws SecurityException
	{
		_stopSearching = true;
	}

	public long getCurrentIteration()
	{
		return _currentIteration;
	}

	public long getMaximalIterationCount()
	{
		return _maximalIterationCount;
	}

	public void setMaximalIterationCount(long maximalIterationCount)
	{
		_maximalIterationCount = maximalIterationCount;
	}

//	public long getMaximalSuccessIterationCount()
//	{
//		return _maximalSuccessIterationCount;
//	}

//	public void setMaximalAcceptedSolutionsPerOneStepCount(long _maximalSuccessIterationCount)
//	{
//		this._maximalSuccessIterationCount = _maximalSuccessIterationCount;
//	}

	public boolean isRunning()
	{
		return _isRunning;
	}

}
