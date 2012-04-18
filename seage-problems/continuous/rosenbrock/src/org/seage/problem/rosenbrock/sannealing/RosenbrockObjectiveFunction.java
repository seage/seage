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

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockObjectiveFunction implements IObjectiveFunction
{
    // f(X) = SUM(to N-1, i = 0) [(1 - Xi)^2 + 100 * (Xi+1 - Xi^2)^2]
    public void setObjectiveValue(Solution solution)
    {
        double[] coords = ((RosenbrockSolution)solution).getCoords();
        double value = 0.0;
        for(int i = 0; i < coords.length - 1; i++)
        {
            value += (Math.pow(1 - coords[i], 2) + 100 * Math.pow( coords[i + 1] - Math.pow( coords[i], 2 ) , 2 ) );
        }
        solution.setObjectiveValue( value );
    }
}
