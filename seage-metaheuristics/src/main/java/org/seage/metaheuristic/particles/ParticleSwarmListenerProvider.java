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
 *
 * @author Jan Zmatlik
 */
public class ParticleSwarmListenerProvider  {

  private IParticleSwarmListener[] _particleSwarmOptimizationListenerList = {};

  private final ParticleSwarmEvent _particleSwarmOptimizationEvent;

  public ParticleSwarmListenerProvider(IParticleSwarm particleSwarmOptimization)
  {
        _particleSwarmOptimizationEvent = new ParticleSwarmEvent( particleSwarmOptimization );
  }

  public final void addParticleSwarmListener( IParticleSwarmListener listener )
  {
        IParticleSwarmListener[] list = new IParticleSwarmListener[
            _particleSwarmOptimizationListenerList.length + 1 ];

        int length = list.length;
        for( int i = 0; i < length - 1; i++ )
            list[i] = _particleSwarmOptimizationListenerList[i];

        list[ list.length - 1 ] = listener;
        _particleSwarmOptimizationListenerList = list;
  }

  protected final void fireParticleSwarmStarted()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].particleSwarmStarted( _particleSwarmOptimizationEvent );
  }

  protected final void fireParticleSwarmStopped()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].particleSwarmStopped( _particleSwarmOptimizationEvent );
  }

  protected final void fireNewBestSolutionFound()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].newBestSolutionFound( _particleSwarmOptimizationEvent );
  }

  protected final void fireNewIterationStarted()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].newIterationStarted( _particleSwarmOptimizationEvent );
  }
}
