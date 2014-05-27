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
package org.seage.problem.qap.sannealing;

import java.util.Random;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public class QapMoveManager implements IMoveManager
{

    public Solution getModifiedSolution(Solution solution)
    {
        QapSolution qapSolution = ((QapSolution)solution).clone();

        Random rnd = new Random();
        int qapSolutionLength = qapSolution.getAssign().length;
        int a = rnd.nextInt( qapSolutionLength );
        int b = rnd.nextInt( qapSolutionLength );

        // Swap values if indices are different
        if(a != b)
        {

            qapSolution.getAssign()[a] = qapSolution.getAssign()[a] + qapSolution.getAssign()[b];
            qapSolution.getAssign()[b] = qapSolution.getAssign()[a] - qapSolution.getAssign()[b];
            qapSolution.getAssign()[a] = qapSolution.getAssign()[a] - qapSolution.getAssign()[b];
        }

        return (Solution)qapSolution;
    }

	@Override
	public Solution getModifiedSolution(Solution solution,
			double currentTemperature) throws Exception 
	{
		return null;
	}

}
