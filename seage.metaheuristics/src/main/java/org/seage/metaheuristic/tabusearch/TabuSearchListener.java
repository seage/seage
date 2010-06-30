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
 * An object that will listen for the tabu search to start.
 * 
 * @author Robert Harder
 * @see TabuSearch
 * @see TabuSearchEvent
 * @version 1.0
 * @since 1.0
 */
public interface TabuSearchListener extends java.io.Serializable, java.util.EventListener
{

    /**
     * Called when the tabu search has started.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that started.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0
     */
    public void tabuSearchStarted( TabuSearchEvent e );
    
    
    /**
     * Called when the tabu search stops solving, either because all requested
     * iterations are completed or someone has called 
     * {@link TabuSearch#stopSolving myTabuSearch.stopSolving()}.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that stopped.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0
     */
    public void tabuSearchStopped( TabuSearchEvent e );
    
    
    
    
    
    /**
     * Called when a new best solution is found.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that found
     * the new best solution.
     * @see TabuSearchEvent
     * @since 1.0
     */
    public void newBestSolutionFound( TabuSearchEvent e );
    
    /**
     * Called when a new current solution is found, which, by defintion,
     * is at every iteration, even if your "move" doesn't perceptibly
     * alter the solution.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that found
     * a new current solution.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0
     */
    public void newCurrentSolutionFound( TabuSearchEvent e );
    
    

    /**
     * Called when the tabu search makes an unimproving move.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that made
     * the unimproving move.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0
     */
    public void unimprovingMoveMade( TabuSearchEvent e );
    
 

    /**
     * Called when the tabu search makes an improving move.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that made
     * the improving move.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0-exp7
     */
    public void improvingMoveMade( TabuSearchEvent e );



    /**
     * Called when the tabu search makes a no change in value move.
     * 
     * @param e The {@link TabuSearchEvent} referencing the {@link TabuSearch} that made
     * the no change in solution value move.
     * @see TabuSearchEvent
     * @see TabuSearch
     * @since 1.0-exp7
     */
    public void noChangeInValueMoveMade( TabuSearchEvent e );

    

}   // end interface TabuSearchListener



