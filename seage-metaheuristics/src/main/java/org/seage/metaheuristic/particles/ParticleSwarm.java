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
package org.seage.metaheuristic.particles;

/**
 * @author Jan Zmatlik
 */

public class ParticleSwarm implements IParticleSwarm
{
	private double _maximalValue;

	private double _minimalValue;

	/**
	 * Provide firing Events, registering listeners
	 */
	private ParticleSwarmListenerProvider _listenerProvider = new ParticleSwarmListenerProvider(this);

	private IVelocityManager _velocityManager;

	private IObjectiveFunction _objectiveFunction;

	// Global acceleration constant
	private double _alpha;

	// Local acceleration constant
	private double _beta;

	private Particle _globalMinimum = null;

	private Particle _localMinimum = null;

	// Inertia constant
	private double _inertia;

	private long _maximalIterationCount;

	private boolean _stopSearching = false;

	private Particle[] _particles = null;

	private boolean _isRunning = false;

	private long _currentIteration;

	/**
	 * Constructor
	 * 
	 * @param objectiveFunction is objective function.
	 */
	public ParticleSwarm(IObjectiveFunction objectiveFunction)
	{
		_objectiveFunction = objectiveFunction;
		// _velocityManager = new RapidVelocityManager();
		// _velocityManager = new AcceleratedVelocityManager();
		_velocityManager = new VelocityManager();
	}

	public void startSearching(Particle[] particles)
	{
		// Searching is starting
		_stopSearching = false;
		_isRunning = true;

		// Initial number of iteration
		_currentIteration = 0;

		_globalMinimum = _localMinimum = findMinimum(particles);

		// Fire event to listeners about that algorithm has started
		_listenerProvider.fireParticleSwarmStarted();

		_particles = particles;

		//long globalFoundIteration = 0;
		while (_maximalIterationCount > _currentIteration && !_stopSearching)
		{
			_listenerProvider.fireNewIterationStarted();
			_currentIteration++;

			for (Particle particle : particles)
			{
				if (_stopSearching)
					return;

				// Calculate velocity for current particle
				// Calculate new locations for current particle ->
				_velocityManager.calculateNewVelocityAndPosition(particle, _localMinimum, _globalMinimum, _alpha, _beta, _inertia);

				// Check and set minimal and maximal velocity
				//checkVelocityBounds(particle);
				checkValueBounds(particle);

				// Evaluate particle by objective function
				_objectiveFunction.setObjectiveValue(particle);
			}
			// System.out.println(">" + iterationCount);
			// System.out.println("");

			// Find best current x and global best g
			_localMinimum = findMinimum(particles);

			if (_localMinimum.getEvaluation() < _globalMinimum.getEvaluation())
			{
				_globalMinimum = _localMinimum.clone();
				//globalFoundIteration = iterationCount;
				_listenerProvider.fireNewBestSolutionFound();
			}
		}

		_listenerProvider.fireParticleSwarmStopped();

		_isRunning = false;
		// System.out.println("Global MINIMUM: " +
		// _globalMinimum.getEvaluation());
		// System.out.print("Global Coords: ");
		// printArray(_globalMinimum.getCoords());
		// System.out.println("");
		// System.out.println("Found in " + globalFoundIteration +
		// " iteration.");
	}
	
	private void checkValueBounds(Particle particle)
	{
		for (int i = 0; i < particle.getCoords().length; i++)
		{
			if (particle.getCoords()[i] < _minimalValue)
				particle.getCoords()[i] = _minimalValue;
			else if (particle.getCoords()[i] > _maximalValue)
				particle.getCoords()[i] = _maximalValue;
		}
	}

	void printArray(double[] array)
	{
		for (int i = 0; i < array.length; i++)
			System.out.print(" " + array[i]);
	}

	private Particle findMinimum(Particle[] particles)
	{
		double minEvaluation = Double.MAX_VALUE;
		Particle minParticle = particles[0];

		for (Particle particle : particles)
		{
			if (particle.getEvaluation() < minEvaluation)
			{
				minEvaluation = particle.getEvaluation();
				minParticle = particle;
			}
		}

		return minParticle.clone();
	}

	public final void addParticleSwarmOptimizationListener(IParticleSwarmListener listener)
	{
		_listenerProvider.addParticleSwarmListener(listener);
	}

	public void stopSearching()
	{
		_stopSearching = true;
	}

	public long getMaximalIterationCount()
	{
		return _maximalIterationCount;
	}

	public void setMaximalIterationCount(long maximalIterationCount)
	{
		this._maximalIterationCount = maximalIterationCount;
	}

	public double getMaximalVelocity()
	{
		return _maximalValue;
	}

	public void setMaximalVectorValue(double maximalVelocity)
	{
		_maximalValue = maximalVelocity;
	}

	public double getInertia()
	{
		return _inertia;
	}

	public void setInertia(double inertia)
	{
		this._inertia = inertia;
	}

	public double getMinimalVelocity()
	{
		return _minimalValue;
	}

	public void setMinimalVectorValue(double minimalVelocity)
	{
		this._minimalValue = minimalVelocity;
	}

	public double getAlpha()
	{
		return _alpha;
	}

	public void setAlpha(double alpha)
	{
		this._alpha = alpha;
	}

	public double getBeta()
	{
		return _beta;
	}

	public void setBeta(double beta)
	{
		this._beta = beta;
	}

	public Particle getBestParticle()
	{
		return _globalMinimum;
	}

	public Particle[] getParticles()
	{
		return _particles;
	}

	public void setParticles(Particle[] particles)
	{
		this._particles = particles;
	}

	public boolean isRunning()
	{
		return _isRunning;
	}
	
	public long getCurrentIteration()
	{
		return _currentIteration;
	}

}
