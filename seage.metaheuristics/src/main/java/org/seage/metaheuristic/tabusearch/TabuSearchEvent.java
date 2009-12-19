package org.seage.metaheuristic.tabusearch;

/**
 * These events are fired off by the {@link TabuSearch} and are useful
 * for extending the tabu search to include such things as long term memory
 * and other tabu extras. These are lightweight events that only contain
 * a reference to their source object.
 * 
 *
 * <p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
 * The Common Public License, developed by IBM and modeled after their industry-friendly IBM Public License,
 * differs from other common open source licenses in several important ways:
 * <ul>
 *  <li>You may include this software with other software that uses a different (even non-open source) license.</li>
 *  <li>You may use this software to make for-profit software.</li>
 *  <li>Your patent rights, should you generate patents, are protected.</li>
 * </ul>
 * </p>
 *
 *
 * @author Robert Harder, rharder@usa.net
 * @author Richard Malek
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

