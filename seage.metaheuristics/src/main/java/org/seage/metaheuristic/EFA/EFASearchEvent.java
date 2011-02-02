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
 * These events are fired off by the {@link FireflySearch} and are useful
 * for extending the firefly search extras. These are lightweight events that only contain
 * a reference to their source object.
 *
 * @author Robert Harder, rharder@usa.net
 * @see FireflySearch
 * @see FireflySearchListener
 * @see java.util.EventObject
 * @version 1.0
 * @since 1.0
 */
public class EFASearchEvent extends java.util.EventObject
{

    /**
     * Constructor that accepts the source of the event.
     * 
     * @param source The source of the event.
     * @since 1.0
     */
    public EFASearchEvent(Object source)
    {   super( source );
    }   // end constructor

    
    
    /**
     * Casts the <tt>source</tt> as a {@link FireflySearch}
     * prior to returning. It is equivalent to
     * <code>(FireflySearch) getSource()</code>.
     *
     * @return source as {@link FireflySearch}
     * @since 1.0
     */
    public final IEFASearch getEFASearch()
    {   return (IEFASearch) source;
    }   // end getFireflySearch

    
}   // end class FireflySearchEvent

