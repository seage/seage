/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 */

/**
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

/**
 * These events are fired off by the {@link TabuSearch} and are useful
 * for extending the tabu search to include such things as long term memory
 * and other tabu extras. These are lightweight events that only contain
 * a reference to their source object.
 *
 * @author Robert Harder, rharder@usa.net
 * @see TabuSearch
 * @see TabuSearchListener
 * @see java.util.EventObject
 * @version 1.0
 * @since 1.0
 */
public class TabuSearchEvent extends java.util.EventObject
{

    /**
     * Constructor that accepts the source of the event.
     * 
     * @param source The source of the event.
     * @since 1.0
     */
    public TabuSearchEvent(Object source)
    {   super( source );
    }   // end constructor

    
    
    /**
     * Casts the <tt>source</tt> as a {@link TabuSearch}
     * prior to returning. It is equivalent to
     * <code>(TabuSearch) getSource()</code>.
     *
     * @return source as {@link TabuSearch}
     * @since 1.0
     */
    public final ITabuSearch getTabuSearch()
    {   return (ITabuSearch) source;
    }   // end getTabuSearch

    
}   // end class TabuSearchEvent

