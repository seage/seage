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
 * This extends the {@link Move} interface and requires you to implement
 * a <code>public int[] attributes()</code> method that returns attributes
 * of that move. The length of the array is the number of attributes
 * you are tracking in your {@link ComplexTabuList}.
 *
 * @author Robert Harder
 * @see ObjectiveFunction
 * @see Solution
 * @version 1.0
 * @since 1.0-exp9
 */

public interface ComplexMove extends Move
{

    /**
     * Returns an array of attributes to track in the
     * {@link ComplexTabuList}.
     *
     * @return array of attributes
     * @since 1.0-exp9
     */
    public abstract int[] attributes();


}   // end class ComplexMove