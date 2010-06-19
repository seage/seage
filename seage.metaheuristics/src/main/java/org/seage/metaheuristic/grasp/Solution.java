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
public class Solution implements java.lang.Cloneable {

    /**
     * The _value is value of Solution
     */
    double _value = Double.MAX_VALUE;

    /**
     * Returns the value of Solution
     * @return - The rating of Solution
     */
    public double getObjectiveValue() {
        return _value;
    }

    /**
     * Sets the value of Solution
     * @param objValue - Evaluation of the solution
     */
    public void setObjectiveValue(double objValue) {
        _value = objValue;
    }
}
