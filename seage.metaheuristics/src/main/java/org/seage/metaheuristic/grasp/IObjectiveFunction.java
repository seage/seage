/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.grasp;


/**
 *
 * @author Martin Zaloga
 */
public interface IObjectiveFunction {

    /**
     * Function that returns the rating for the next step
     * @param s - Solutions for which was generated the steps
     * @param move - The next step
     * @return - Rating for the step
     */
    double evaluateMove(Solution s, IMove move) throws Exception;

    /**
     * Function that sets this to indicate that this is the first step
     */
    void reset();
}
