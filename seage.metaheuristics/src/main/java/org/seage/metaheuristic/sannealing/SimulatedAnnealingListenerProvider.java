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
 *
 * @author Jan Zmatlik
 */
public class SimulatedAnnealingListenerProvider  {

  private ISimulatedAnnealingListener[] _simulatedAnnealingListenerList = {};

  private final SimulatedAnnealingEvent _simulatedAnnealingEvent;

  public SimulatedAnnealingListenerProvider(ISimulatedAnnealing simulatedAnnealing)
  {
        _simulatedAnnealingEvent = new SimulatedAnnealingEvent( simulatedAnnealing );
  }

  public final void addSimulatedAnnealingListener( ISimulatedAnnealingListener listener )
  {
        ISimulatedAnnealingListener[] list = new ISimulatedAnnealingListener[
            _simulatedAnnealingListenerList.length + 1 ];

        int length = list.length;
        for( int i = 0; i < length - 1; i++ )
            list[i] = _simulatedAnnealingListenerList[i];

        list[ list.length - 1 ] = listener;
        _simulatedAnnealingListenerList = list;
  }

  protected final void fireSimulatedAnnealingStarted()
  {
      int length = _simulatedAnnealingListenerList.length;
        for( int i = 0; i < length; i++ )
            _simulatedAnnealingListenerList[i].simulatedAnnealingStarted( _simulatedAnnealingEvent );
  }

  protected final void fireSimulatedAnnealingStopped()
  {
      int length = _simulatedAnnealingListenerList.length;
        for( int i = 0; i < length; i++ )
            _simulatedAnnealingListenerList[i].simulatedAnnealingStopped( _simulatedAnnealingEvent );
  }

  protected final void fireNewBestSolutionFound()
  {
      int length = _simulatedAnnealingListenerList.length;
        for( int i = 0; i < length; i++ )
            _simulatedAnnealingListenerList[i].newBestSolutionFound( _simulatedAnnealingEvent );
  }

  protected final void fireNewCurrentSolutionFound()
  {
      int length = _simulatedAnnealingListenerList.length;
        for( int i = 0; i < length; i++ )
            _simulatedAnnealingListenerList[i].newCurrentSolutionFound( _simulatedAnnealingEvent );
  }

}
