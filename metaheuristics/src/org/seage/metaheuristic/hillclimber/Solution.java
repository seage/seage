/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.hillclimber;

import org.seage.metaheuristic.sannealing.*;

/**
 *
 * @author Jan Zmatlik
 */
public class Solution implements java.lang.Cloneable {

    /**
     * The value is value of Solution
     */
    double value = Double.MAX_VALUE;

    /**
     * Returns the value of Solution
     * @return double value
     */
    public double getObjectiveValue()
    {
        return value;
    }

    /**
     * Sets the value of Solution
     * @param double objValue
     */
    public void setObjectiveValue(double objValue)
    {
        value = objValue;
    }

}
