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
 */
package org.seage.metaheuristic.tabusearch;

/**
 * This abstract class implements {@link TabuSearch} and defines the event-handling methods
 * including the add/remove methods and package-level fireXxx methods.
 * It is the base class for the {@link MultiThreadedTabuSearch} and {@link SingleThreadedTabuSearch}.
 *
 *
 * @author  Robert Harder
 * @version 1.0a
 * @since 1.0
 */

public abstract class TabuSearchBase implements ITabuSearch
{
    /**
     * Tracks the number of iterations comleted since the
     * inception of this tabu search.
     *
     * @since 1.0a
     */
    private int iterationsCompleted;   

    /**
     * Returns the total number iterations that have been
     * completed since the instantiation of this {@link TabuSearch}.
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
     * of {@link TabuSearchBase} at the end of an iteration.
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
     * @see TabuSearchEvent
     * @since 1.0
     */
    private final TabuSearchEvent tabuEvent = new TabuSearchEvent( this );


    /**
     * An array of {@link TabuSearchListener}s. An array is
     * used instead of an {@link javax.swing.event.EventListenerList} or a
     * {@link java.util.Vector}. This reduces the tabu search package's
     * dependence on java's objects. Also arrays are much
     * faster to access and iterate through than other objects.
     * 
     * @see TabuSearchListener
     * @since 1.0
     */
    private TabuSearchListener[] tabuSearchListenerList = {};
    

    /* ********  A D D   M E T H O D S  ******** */


    /**
     * Registers <tt>listener</tt> to receive tabu events when
     * a new best solution is found.
     * 
     * @param listener The {@link TabuSearchListener} to register.
     * @see TabuSearchListener
     * @since 1.0
     */
     public final synchronized void addTabuSearchListener( TabuSearchListener listener )
     {  
        TabuSearchListener[] list = new TabuSearchListener[
            tabuSearchListenerList.length + 1 ];

        for( int i = 0; i < list.length - 1; i++ )
            list[i] = tabuSearchListenerList[i];
            
        list[ list.length-1 ] = listener;
        tabuSearchListenerList = list;
     }  // end addTabuSearchListener


    /* ********  R E M O V E   M E T H O D S  ******** */
     

     /**
      * Removes <tt>listener</tt> from list of objects to notify
      * when a new best solution is found.
      * 
      * @param listener {@link TabuSearchListener} to remove from notification list.
      * @see TabuSearchListener
      * @since 1.0
      */
     public final synchronized void removeTabuSearchListener( TabuSearchListener listener )
     {  // Find location of listener to remove
        int index = -1;
        int j = 0;

        while( index < 0 && j < tabuSearchListenerList.length )
        {   if( tabuSearchListenerList[j] == listener )
                index = j;
            else j++;
        }   // end while: through list
    
        // If index is less than zero then it wasn't in the list
        if( index >= 0 )
        {   TabuSearchListener[] list = new TabuSearchListener[
                tabuSearchListenerList.length - 1 ];

            for( int i = 0; i < list.length; i++ )
                list[i] = tabuSearchListenerList[ i < index ? i : i + 1 ];
            tabuSearchListenerList = list;
        }   // end if: listener was in the list
     }  // end removeTabuSearchListener



    /* ********  F I R E   M E T H O D S  ******** */

    
    /**
     * This quick method is called when a new best solution is found.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0
     */
    protected synchronized final void fireNewBestSolution()
    {   int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].newBestSolutionFound( tabuEvent );
    }   // end fireNewBestSolution


    /**
     * This quick method is called when a new current solution is found.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0
     */
    protected synchronized final void fireNewCurrentSolution()
    {   int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].newCurrentSolutionFound( tabuEvent );
    }   // end fireNewCurrentSolution


    /**
     * This quick method is called when an unimproving move is made.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0
     */
    protected synchronized final void fireUnimprovingMoveMade()
    {
        int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].unimprovingMoveMade( tabuEvent );
    }   // end fireUnimprovingMoveMade

    

    /**
     * This quick method is called when an improving move is made.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0-exp7
     */
    protected synchronized final void fireImprovingMoveMade()
    {   
        int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].improvingMoveMade( tabuEvent );
    }   // end fireImprovingMoveMade


    /**
     * This quick method is called when a no change in value move is made.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0-exp7
     */
    protected synchronized final void fireNoChangeInValueMoveMade()
    {   
        int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].noChangeInValueMoveMade( tabuEvent );
    }   // end fireNoChangeInValueMoveMade


    /**
     * This quick method is called when the tabuSearch finishes.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0
     */
    protected synchronized final void fireTabuSearchStopped()
    {
        int len = tabuSearchListenerList.length;

        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].tabuSearchStopped( tabuEvent );

    }   // end fireTabuSearchStopped


    /**
     * This quick method is called when the tabuSearch starts.
     * The {@link TabuSearchEvent} sent to the listeners is a 
     * "lightweight" event, specifying only the source object,
     * <tt>this</tt>.
     *
     * @see TabuSearchEvent
     * @since 1.0
     */
    protected synchronized final void fireTabuSearchStarted()
    {
        int len = tabuSearchListenerList.length;
        for( int i = 0; i < len; i++ )
            tabuSearchListenerList[i].tabuSearchStarted( tabuEvent );
    }   // end fireTabuSearchStarted

}   // end class TabuSearchBase