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
public class ParticleSwarmOptimizationListenerProvider  {

  private IParticleSwarmOptimizationListener[] _particleSwarmOptimizationListenerList = {};

  private final ParticleSwarmOptimizationEvent _particleSwarmOptimizationEvent;

  public ParticleSwarmOptimizationListenerProvider(IParticleSwarmOptimization particleSwarmOptimization)
  {
        _particleSwarmOptimizationEvent = new ParticleSwarmOptimizationEvent( particleSwarmOptimization );
  }

  public final void addParticleSwarmOptimizationListener( IParticleSwarmOptimizationListener listener )
  {
        IParticleSwarmOptimizationListener[] list = new IParticleSwarmOptimizationListener[
            _particleSwarmOptimizationListenerList.length + 1 ];

        int length = list.length;
        for( int i = 0; i < length - 1; i++ )
            list[i] = _particleSwarmOptimizationListenerList[i];

        list[ list.length - 1 ] = listener;
        _particleSwarmOptimizationListenerList = list;
  }

  protected final void fireParticleSwarmOptimizationStarted()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].particleSwarmOptimizationStarted( _particleSwarmOptimizationEvent );
  }

  protected final void fireParticleSwarmOptimizationStopped()
  {
      int length = _particleSwarmOptimizationListenerList.length;
        for( int i = 0; i < length; i++ )
            _particleSwarmOptimizationListenerList[i].particleSwarmOptimizationStopped( _particleSwarmOptimizationEvent );
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
