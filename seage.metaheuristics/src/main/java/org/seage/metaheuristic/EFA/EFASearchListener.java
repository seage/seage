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
package org.seage.metaheuristic.EFA;

import org.seage.metaheuristic.EFA.*;

/**
 * An object that will listen for the firefly search to start.
 * 
 * @author Robert Harder
 * @see FireflySearch
 * @see FireflySearchEvent
 * @version 1.0
 * @since 1.0
 */
public interface EFASearchListener extends java.io.Serializable, java.util.EventListener
{

    /**
     * Called when the firefly search has started.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that started.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void EFASearchStarted( EFASearchEvent e );
    
    
    /**
     * Called when the firefly search stops solving, either because all requested
     * iterations are completed or someone has called 
     * {@link FireflySearch#stopSolving myFireflySearch.stopSolving()}.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that stopped.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void EFASearchStopped( EFASearchEvent e );
    
    
    
    
    
    /**
     * Called when a new best solution is found.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that found
     * the new best solution.
     * @see FireflySearchEvent
     * @since 1.0
     */
    public void newBestSolutionFound( EFASearchEvent e );
    
    /**
     * Called when a new current solution is found, which, by defintion,
     * is at every iteration, even if your "move" doesn't perceptibly
     * alter the solution.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that found
     * a new current solution.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void newCurrentSolutionFound( EFASearchEvent e );
    
    

    /**
     * Called when the firefly search makes an unimproving move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the unimproving move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void unimprovingMoveMade( EFASearchEvent e );
    
 

    /**
     * Called when the firefly search makes an improving move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the improving move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0-exp7
     */
    public void improvingMoveMade( EFASearchEvent e );



    /**
     * Called when the firefly search makes a no change in value move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the no change in solution value move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0-exp7
     */
    public void noChangeInValueMoveMade( EFASearchEvent e );

    

}   // end interface FireflySearchListener



