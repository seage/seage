/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Karel Durkota - Initial implementation Richard Malek - Added
 * algorithm annotations
 */
package org.seage.problem.sat.sannealing;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class SatSimulatedAnnealingFactory implements IAlgorithmFactory
{

    @Override
    public Class<?> getAlgorithmClass()
    {
        return SimulatedAnnealingAdapter.class;
    }

    @Override
    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        return new SimulatedAnnealingAdapter(new SatObjectiveFunction(), new SatMoveManager(), false, "")
        {

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                return null;
            }

            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                // TODO Auto-generated method stub

            }
        };
    }

}
