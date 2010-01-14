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
package org.seage.metaheuristic.particles;

import java.io.Serializable;

/**
 *
 * @author Jan Zmatlik
 */
public class Solution implements Cloneable, Serializable {

    /**
     * The value is value of Solution
     */
    private double _value = Double.MAX_VALUE;

    /**
     * The velocity is velocity of Solution
     */
    private double _velocity = Double.MAX_VALUE;

    /**
     * Returns the value of Solution
     * @return double value
     */
    public double getObjectiveValue()
    {
        return _value;
    }

    /**
     * Sets the value of Solution
     * @param double objValue
     */
    public void setObjectiveValue(double objValue)
    {
        _value = objValue;
    }

    public double getVelocity()
    {
        return _velocity;
    }

    public void setVelocity(double _velocity)
    {
        this._velocity = _velocity;
    }

    @Override
    public  Solution clone()
    {
        try
        {
            Solution sol = (Solution)super.clone();
            sol._value = _value;
            sol._velocity = _velocity;
            return sol;
        }
        catch(CloneNotSupportedException e)
        {
            throw new InternalError( e.toString() );
        }
    }
}
