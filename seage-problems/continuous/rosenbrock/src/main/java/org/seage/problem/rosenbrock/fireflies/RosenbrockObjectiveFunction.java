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
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.rosenbrock.fireflies;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockObjectiveFunction implements ObjectiveFunction
{
    
    public int counter=0;
    
    // f(X) = SUM(to N-1, i = 0) [(1 - Xi)^2 + 100 * (Xi+1 - Xi^2)^2]
    public void setObjectiveValue(Solution sol)
    {
        Double[] coords = ((ContinuousSolution) sol)._assign;
        double[] value = {0.0};
        for(int i = 0; i < coords.length - 1; i++)
        {
            value[0] += (Math.pow(1 - coords[i], 2) + 100 * Math.pow( coords[i + 1] - Math.pow( coords[i], 2 ) , 2 ) );
        }
        sol.setObjectiveValue(value);
    }

    public void ObjectiveFunction(Solution soln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] evaluate(Solution soln) throws Exception {
        Double[] coords = ((ContinuousSolution) soln)._assign;
        double[] value = {0.0};
        for(int i = 0; i < coords.length - 1; i++)
        {
            value[0] += (Math.pow(1 - coords[i], 2) + 100 * Math.pow( coords[i + 1] - Math.pow( coords[i], 2 ) , 2 ) );
        }
        return value;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }
}
