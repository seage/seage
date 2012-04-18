/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
