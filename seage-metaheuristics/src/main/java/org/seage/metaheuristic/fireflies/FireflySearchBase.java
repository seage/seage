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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

/**
 * This abstract class implements {@link FireflySearch} and defines the
 * event-handling methods including the add/remove methods and package-level
 * fireXxx methods. It is the base class for the
 * {@link MultiThreadedFireflySearch} and {@link SingleThreadedFireflySearch}.
 *
 *
 * @author Robert Harder
 * @version 1.0a
 * @since 1.0
 */

public abstract class FireflySearchBase<S extends Solution> implements IFireflySearch<S> {
  /**
   * 
   */
  private static final long serialVersionUID = 4689450489917508880L;

  /**
   * Tracks the number of iterations comleted since the inception of this firefly
   * search.
   *
   * @since 1.0a
   */
  private int iterationsCompleted;

  /**
   * Returns the total number iterations that have been completed since the
   * instantiation of this {@link FireflySearch}.
   *
   * @return Total number of completed iterations
   * @since 1.0a
   */
  @Override
  public synchronized int getIterationsCompleted() {
    return iterationsCompleted;
  } // end getIterationsCompleted

  /**
   * Increments the number of iterations completed by one. This method should be
   * called by all implementations of {@link FireflySearchBase} at the end of an
   * iteration.
   *
   * @since 1.0a
   */
  protected void incrementIterationsCompleted() {
    iterationsCompleted++;
  } // end incrementIterationsCompleted

  /* ******** E V E N T C O D E ******** */

  /**
   * This is the "lightweight" event that is used for all listeners. Having one
   * event object that gets passed around is faster than instantiating new event
   * objects at every firing.
   * 
   * @see FireflySearchEvent
   * @since 1.0
   */
  private final FireflySearchEvent fireflyEvent = new FireflySearchEvent(this);

  /**
   * An array of {@link FireflySearchListener}s. An array is used instead of an
   * {@link javax.swing.event.EventListenerList} or a {@link java.util.Vector}.
   * This reduces the firefly search package's dependence on java's objects. Also
   * arrays are much faster to access and iterate through than other objects.
   * 
   * @see FireflySearchListener
   * @since 1.0
   */
  private FireflySearchListener[] fireflySearchListenerList = {};

  /* ******** A D D M E T H O D S ******** */

  /**
   * Registers <code>listener</code> to receive firefly events when a new best
   * solution is found.
   * 
   * @param listener The {@link FireflySearchListener} to register.
   * @see FireflySearchListener
   * @since 1.0
   */
  @Override
  public final synchronized void addFireflySearchListener(FireflySearchListener listener) {
    FireflySearchListener[] list = new FireflySearchListener[fireflySearchListenerList.length + 1];

    for (int i = 0; i < list.length - 1; i++)
      list[i] = fireflySearchListenerList[i];

    list[list.length - 1] = listener;
    fireflySearchListenerList = list;
  } // end addFireflySearchListener

  /* ******** R E M O V E M E T H O D S ******** */

  /**
   * Removes <code>listener</code> from list of objects to notify when a new best
   * solution is found.
   * 
   * @param listener {@link FireflySearchListener} to remove from notification
   *                 list.
   * @see FireflySearchListener
   * @since 1.0
   */
  @Override
  public final synchronized void removeFireflySearchListener(FireflySearchListener listener) { // Find location of
                                                                                               // listener to remove
    int index = -1;
    int j = 0;

    while (index < 0 && j < fireflySearchListenerList.length) {
      if (fireflySearchListenerList[j] == listener)
        index = j;
      else
        j++;
    } // end while: through list

    // If index is less than zero then it wasn't in the list
    if (index >= 0) {
      FireflySearchListener[] list = new FireflySearchListener[fireflySearchListenerList.length - 1];

      for (int i = 0; i < list.length; i++)
        list[i] = fireflySearchListenerList[i < index ? i : i + 1];
      fireflySearchListenerList = list;
    } // end if: listener was in the list
  } // end removeFireflySearchListener

  /* ******** F I R E M E T H O D S ******** */

  /**
   * This quick method is called when a new best solution is found. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0
   */
  protected synchronized final void fireNewBestSolution() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].newBestSolutionFound(fireflyEvent);
  } // end fireNewBestSolution

  /**
   * This quick method is called when a new current solution is found. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0
   */
  protected synchronized final void fireNewCurrentSolution() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].newCurrentSolutionFound(fireflyEvent);
  } // end fireNewCurrentSolution

  /**
   * This quick method is called when an unimproving move is made. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0
   */
  protected synchronized final void fireUnimprovingMoveMade() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].unimprovingMoveMade(fireflyEvent);
  } // end fireUnimprovingMoveMade

  /**
   * This quick method is called when an improving move is made. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0-exp7
   */
  protected synchronized final void fireImprovingMoveMade() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].improvingMoveMade(fireflyEvent);
  } // end fireImprovingMoveMade

  /**
   * This quick method is called when a no change in value move is made. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0-exp7
   */
  protected synchronized final void fireNoChangeInValueMoveMade() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].noChangeInValueMoveMade(fireflyEvent);
  } // end fireNoChangeInValueMoveMade

  /**
   * This quick method is called when the fireflySearch finishes. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0
   */
  protected synchronized final void fireFireflySearchStopped() {
    int len = fireflySearchListenerList.length;

    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].FireflySearchStopped(fireflyEvent);

  } // end fireFireflySearchStopped

  /**
   * This quick method is called when the fireflySearch starts. The
   * {@link FireflySearchEvent} sent to the listeners is a "lightweight" event,
   * specifying only the source object, <code>this</code>.
   *
   * @see FireflySearchEvent
   * @since 1.0
   */
  protected synchronized final void fireFireflySearchStarted() {
    int len = fireflySearchListenerList.length;
    for (int i = 0; i < len; i++)
      fireflySearchListenerList[i].FireflySearchStarted(fireflyEvent);
  } // end fireFireflySearchStarted

} // end class FireflySearchBase
