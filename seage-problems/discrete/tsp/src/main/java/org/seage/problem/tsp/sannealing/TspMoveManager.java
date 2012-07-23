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
package org.seage.problem.tsp.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class TspMoveManager implements IMoveManager
{

    public Solution getModifiedSolution(Solution solution)
    {
        TspSolution tspSolution = ((TspSolution)solution).clone();
        
        Random rnd = new Random();
        int tspSolutionLength = tspSolution.getTour().length;
        int a = rnd.nextInt( tspSolutionLength );
        int b = rnd.nextInt( tspSolutionLength );

        // Swap values if indices are different
        if(a != b)
        {            
            tspSolution.getTour()[a] = tspSolution.getTour()[a] + tspSolution.getTour()[b];
            tspSolution.getTour()[b] = tspSolution.getTour()[a] - tspSolution.getTour()[b];
            tspSolution.getTour()[a] = tspSolution.getTour()[a] - tspSolution.getTour()[b];
        }

        return (Solution)tspSolution;
    }

}
