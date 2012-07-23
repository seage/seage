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

  protected final void fireNewIterationStarted()
  {
      int length = _simulatedAnnealingListenerList.length;
        for( int i = 0; i < length; i++ )
            _simulatedAnnealingListenerList[i].newIterationStarted( _simulatedAnnealingEvent );
  }

}
