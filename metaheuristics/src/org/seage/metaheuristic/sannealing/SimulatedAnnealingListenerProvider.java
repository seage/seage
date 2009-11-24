
package org.seage.metaheuristic.sannealing;

/**
 *
 * @author Jan Zmátlík
 */
public class SimulatedAnnealingListenerProvider  {

  private ISimulatedAnnealingListener[] _simulatedAnnealingListenerList = {};

  private final SimulatedAnnealingEvent _simulatedAnnealingEvent = new SimulatedAnnealingEvent( this );

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
