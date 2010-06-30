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

package org.seage.problem.rosenbrock.sannealing;

import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockSolution extends Solution {

    private double[] _coords;

    public RosenbrockSolution(int dimension)
    {
        this._coords = new double[dimension];
    }

    public void setCoords(double[] coords)
    {
        _coords = coords;
    }

    public double[] getCoords()
    {
        return _coords;
    }

    @Override
    public RosenbrockSolution clone()
    {
        RosenbrockSolution rosSolution = null;
        try
        {
            rosSolution = (RosenbrockSolution)super.clone();
            rosSolution.setCoords( _coords.clone() );
            rosSolution.setObjectiveValue(getObjectiveValue());
        } catch (Exception ex)
        {
            //Logger.getLogger(TspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rosSolution;
    }
}
