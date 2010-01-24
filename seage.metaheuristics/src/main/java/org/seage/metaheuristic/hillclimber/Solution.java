/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author Martin Zaloga
 */
public class Solution implements java.lang.Cloneable {

    /**
     * The value is value of Solution
     * The switcher is for switching between types of initial solution
     */
    double value = Double.MAX_VALUE;
    //public String switcher;

    /**
     * Returns the value of Solution
     * @return - The rating of Solution
     */
    public double getObjectiveValue() {
        return value;
    }

    /**
     * Sets the value of Solution
     * @param objValue - Evaluation of the solution
     */
    public void setObjectiveValue(double objValue) {
        value = objValue;
    }
}
