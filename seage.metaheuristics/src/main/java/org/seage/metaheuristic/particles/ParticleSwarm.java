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

public class ParticleSwarm implements IParticleSwarm
{

  private double _maximalVelocity;

  /**
   * Provide firing Events, registering listeners
   */
  private ParticleSwarmListenerProvider _listenerProvider = new ParticleSwarmListenerProvider( this );

//  /**
//   * The Solution which is actual
//   */
//  private Solution _currentSolution;
//
//  /**
//   * The Solution which is best
//   */
//  private Solution _bestSolution;

  /**
   * Modifying current solution
   */
  private IVelocityManager _velocityManager;

  /**
   * Calculate and set objective value of solution
   */
  private IObjectiveFunction _objectiveFunction;

  // Learning parametr or acceleration constant
  private double _alpha = 2;

  // Learning parametr or acceleration constant
  private double _beta = 2;

  private Particle _globalMinimum = null;

  private Particle _localMinimum = null;

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
  public ParticleSwarm(IObjectiveFunction objectiveFunction, IVelocityManager velocityManager)
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


      //######################################
      // Rosenbrock

      int dimension = 2;

      Particle[] particles = 
      {
          new Particle( dimension ) ,
          new Particle( dimension ) ,
          new Particle( dimension ) ,
          new Particle( dimension )
      };

      //######################################
      // Initial
      for(Particle particle : particles)
      {
          // Initial coords
          for(int i = 0; i < dimension; i++)
              particle.getCoords()[i] = Math.random();

          // Initial velocity
          for(int i = 0; i < dimension; i++)
              particle.getVelocity()[i] = Math.random();

          // Evaluate
          rosenbrockObjectiveFunction( particle );
      }
      
      _globalMinimum = _localMinimum = findMinimum( particles );
      
      System.out.println("MINIMUM: " + _globalMinimum.getEvaluation());

      // TODO: A - create logic for algorithm
      while ( _maximalIterationCount > iterationCount && !_stopSearching )
      {
          iterationCount++;

          for(Particle particle : particles)
          {
              if( _stopSearching ) return;

              //###########################
              // Generate velocity for current solution v(iterationCount + 1)
              
              //System.out.println("OLD VELOCITY: " + particle.get );
              generateNewVelocity( particle );

              //###########################
              // Calculate new locations for current solution ->
              // -> x(iterationCount + 1) = x(iterationCount) + v(iterationCount + 1)

              additionVectorVectorToVector( particle.getCoords() , particle.getVelocity() );

              //###########################
              // Evaluate x(iterationCount + 1) by objective function

              rosenbrockObjectiveFunction( particle );

              //###########################
              // Find current minimum
              
              System.out.println("Local MINIMUM---: " + _localMinimum.getEvaluation());
              System.out.println();
          }

          System.out.println();
          //###########################
          // Find best current x and global best g

          _localMinimum = findMinimum(particles);

          if(_localMinimum.getEvaluation() < _globalMinimum.getEvaluation())
              _globalMinimum = _localMinimum;
      }

      System.out.println("Local MINIMUM: " + _localMinimum.getEvaluation());
      System.out.println("Global MINIMUM: " + _globalMinimum.getEvaluation());

  }

  private void generateNewVelocity(Particle particle)
  {
      double[] randomVector1 = new double[particle.getCoords().length];
      double[] randomVector2 = new double[particle.getCoords().length];

      setRandomVector(randomVector1);
      setRandomVector(randomVector2);

      particle.setVelocity
                (
                    additionVectorVector
                    (
                        particle.getVelocity()
                        ,
                        additionVectorVector
                        (
                            multiplicationVectorVector( multiplicationScalarVector(_alpha, randomVector1), subtractionVectorVector(_globalMinimum.getCoords(), particle.getCoords()))
                            ,
                            multiplicationVectorVector( multiplicationScalarVector(_beta, randomVector2), subtractionVectorVector(_localMinimum.getCoords(), particle.getCoords()))
                        )
                    )
               );

  }

  // w = u + (-1)v
  private double[] subtractionVectorVector(double[] a, double[] b)
  {
      double[] resultVector = new double[a.length];
      for(int i = 0; i < a.length; i++)
          resultVector[i] = a[i] - b[i];
      return resultVector;
  }

  private double[] multiplicationVectorVector(double[] a, double[] b)
  {
      double[] resultVector = new double[a.length];
      for(int i = 0; i < a.length; i++)
          resultVector[i] = a[i] * b[i];
      return resultVector;
  }

  private double[] multiplicationScalarVector(double scalar, double[] vector)
  {
      double[] resultVector = new double[vector.length];
      for(int i = 0; i < vector.length; i++)
          resultVector[i] = scalar * vector[i];
      return resultVector;
  }

  private double[] additionVectorVector(double[] a, double[] b)
  {
      double[] resultVector = new double[a.length];
      for(int i = 0; i < a.length; i++)
          resultVector[i] = a[i] + b[i];
      return resultVector;
  }

  private void additionVectorVectorToVector(double[] a, double[] b)
  {
      for(int i = 0; i < a.length; i++)
          a[i] = a[i] + b[i];
  }

  private void rosenbrockObjectiveFunction(Particle particle)
  {
      double[] coords = particle.getCoords();
      for(int i = 0; i < coords.length - 1; i++)
      {
          particle.setEvaluation( Math.pow(1 - coords[i], 2) + 100 * Math.pow( coords[i + 1] - Math.pow( coords[i], 2 ) , 2 ) );
      }
  }

  private Particle findMinimum(Particle[] particles)
  {
      double minEvaluation = Double.MAX_VALUE;
      Particle minParticle = null;

      for(Particle particle : particles)
      {
          if(particle.getEvaluation() < minEvaluation)
          {
              minEvaluation = particle.getEvaluation();
              minParticle = particle;
          }
      }
      
      return minParticle;
  }

  private void setRandomVector(double[] vector)
  {
      for(int i = 0; i < vector.length; i++)
          vector[i] = Math.random();
  }

//  public Solution getCurrentSolution() {
//    return _currentSolution;
//  }
//
//  public void setCurrentSolution(Solution _currentSolution) {
//    this._currentSolution = _currentSolution;
//  }

  public final void addParticleSwarmOptimizationListener( IParticleSwarmListener listener )
  {
    _listenerProvider.addParticleSwarmOptimizationListener( listener );
  } 

//  public Solution getBestSolution()
//  {
//    return _bestSolution;
//  }

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