/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

import org.seage.metaheuristic.fireflies.*;

/**
 * This abstract class implements {@link FireflySearch} and defines the event-handling methods
 * including the add/remove methods and package-level fireXxx methods.
 * It is the base class for the {@link MultiThreadedFireflySearch} and {@link SingleThreadedFireflySearch}.
 *
 *
 * @author  Robert Harder
 * @version 1.0a
 * @since 1.0
 */

public abstract class FireflySearchBase implements IFireflySearch
{
    /**
     * Tracks the number of iterations comleted since the
     * inception of this firefly search.
     *
     * @since 1.0a
     */
    private int iterationsCompleted;   

    /**
     * Returns the total number iterations that have been
     * completed since the instantiation of this {@link FireflySearch}.
     *
     * @return Total number of completed iterations
     * @since 1.0a
     */
    public synchronized int getIterationsCompleted()
    {   return iterationsCompleted;
    }   // end getIterationsCompleted

    /**
     * Increments the number of iterations completed by one.
     * This method should be called by all implementations
     * of {@link FireflySearchBase} at the end of an iteration.
     *
     * @since 1.0a
     */
    protected void incrementIterationsCompleted()
    {   iterationsCompleted++;
    }   // end incrementIterationsCompleted


    /* ********  E V E N T   C O D E  ******** */

    /**
     * This is the "lightweight" event that is used for all listeners.
     * Having one event object that gets passed around is faster than
     * instantiating new event objects at every firing.
     * 
     * @see FireflySearchEvent
     * @since 1.0
     */
    private final FireflySearchEvent fireflyEvent = new FireflySearchEvent( this );


    /**
     * An array of {@link FireflySearchListener}s. An array is
     * used instead of an {@link javax.swing.event.EventListenerList} or a
     * {@link java.util.Vector}. This reduces the firefly search package's
     * dependence on java's objects. Also arrays are much
     * faster to access and iterate through than other objects.
     * 
     * @see FireflySearchListener
     * @since 1.0
     */
    private FireflySearchListener[] fireflySearchListenerList = {};
    

    /* ********  A D D   M E T H O D S  ******** */


    /**
     * Registers <tt>listener</tt> to receive firefly events when
     * a new best solution is found.
     * 
     * @param listener The {@link FireflySearchListener} to register.
     * @see FireflySearchListener
     * @since 1.0
     */
     public final synchronized void addEFASearchListener( FireflySearchListener listener )
     {  
        FireflySearchListener[] list = new FireflySearchListener[
            fireflySearchListenerList.length + 1 ];

        for( int i = 0; i < list.length - 1; i++ )
            list[i] = fireflySearchListenerList[i];
            
        list[ list.length-1 ] = listener;
        fireflySearchListenerList = list;
     }  // end addFireflySearchListener


    /* ********  R E M O V E   M E T H O D S  ******** */
     

     /**
      * Removes <tt>listener</tt> from list of objects to notify
      * when a new best solution is found.
      * 
      * @param listener {@link FireflySearchListener} to remove from notification list.
      * @see FireflySearchListener
      * @since 1.0
      */
     public final synchronized void removeEFASearchListener( FireflySearchListener listener )
     {  // Find location of listener to remove
        int index = -1;
        int j = 0;

        while( index < 0 && j < fireflySearchListenerList.length )
        {   if( fireflySearchListenerList[j] == listener )
                index = j;
            else j++;
        }   // end while: through list
    
        // If index is less than zero then it wasn't in the list
        if( index >= 0 )
        {   FireflySearchListener[] list = new FireflySearchListener[
                fireflySearchListenerList.length - 1 ];

            for( int i = 0; i < list.length; i++ )
                list[i] = fireflySearchListenerList[ i < index ? i : i + 1 ];
            fireflySearchListenerList = list;
        }   // end if: listener was in the list
     }  // end removeFireflySearchListener



    /* ********  F I R E   M E T H O D S  ******** */

    
    /**
     * This quick method is called when a new best solution is found.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0
     */
    protected synchronized final void fireNewBestSolution()
    {   int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].newBestSolutionFound( fireflyEvent );
    }   // end fireNewBestSolution


    /**
     * This quick method is called when a new current solution is found.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0
     */
    protected synchronized final void fireNewCurrentSolution()
    {   int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].newCurrentSolutionFound( fireflyEvent );
    }   // end fireNewCurrentSolution


    /**
     * This quick method is called when an unimproving move is made.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0
     */
    protected synchronized final void fireUnimprovingMoveMade()
    {
        int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].unimprovingMoveMade( fireflyEvent );
    }   // end fireUnimprovingMoveMade

    

    /**
     * This quick method is called when an improving move is made.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0-exp7
     */
    protected synchronized final void fireImprovingMoveMade()
    {   
        int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].improvingMoveMade( fireflyEvent );
    }   // end fireImprovingMoveMade


    /**
     * This quick method is called when a no change in value move is made.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0-exp7
     */
    protected synchronized final void fireNoChangeInValueMoveMade()
    {   
        int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].noChangeInValueMoveMade( fireflyEvent );
    }   // end fireNoChangeInValueMoveMade


    /**
     * This quick method is called when the fireflySearch finishes.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0
     */
    protected synchronized final void fireEFASearchStopped()
    {
        int len = fireflySearchListenerList.length;

        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].EFASearchStopped( fireflyEvent );

    }   // end fireFireflySearchStopped


    /**
     * This quick method is called when the fireflySearch starts.
     * The {@link FireflySearchEvent} sent to the listeners is a
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see FireflySearchEvent
     * @since 1.0
     */
    protected synchronized final void fireEFASearchStarted()
    {
        int len = fireflySearchListenerList.length;
        for( int i = 0; i < len; i++ )
            fireflySearchListenerList[i].EFASearchStarted( fireflyEvent );
    }   // end fireFireflySearchStarted

}   // end class FireflySearchBase