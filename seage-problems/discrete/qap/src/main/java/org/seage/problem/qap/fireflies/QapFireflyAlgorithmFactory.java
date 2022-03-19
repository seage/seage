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
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.problem.qap.fireflies;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.fireflies.FireflyAlgorithmAdapter;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemInstance;

/** 
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("FireflyAlgorithm")
@Annotations.AlgorithmName("FireflyAlgorithm")
public class QapFireflyAlgorithmFactory implements IAlgorithmFactory<QapPhenotype, QapSolution>
{
    public void setProblemProvider(IProblemProvider<QapPhenotype> provider) throws Exception
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<?> getAlgorithmClass()
    {
        return FireflyAlgorithmAdapter.class;
    }

    // @Override
    // public IAlgorithmAdapter<QapSolution> createAlgorithm(ProblemInstance instance) throws Exception
    // {
    //     Double[][][] facilityLocation = ((QapProblemInstance) instance).getFacilityLocation();
    //     IAlgorithmAdapter<QapSolution> algorithm = new FireflyAlgorithmAdapter<>(new QapFireflyOperator(), new QapObjectiveFunction(facilityLocation),
    //             false, "")
    //     {

    //         @Override
    //         public void solutionsFromPhenotype(Object[][] source) throws Exception
    //         {
    //             int height = source.length;
    //             int width = source[0].length;
    //             ArrayList<QapSolution> sols = new ArrayList<>(height);
    //             for (int i = 0; i < height; i++)
    //             {
    //                 Integer[] result = new Integer[width];
    //                 for (int j = 0; j < width; j++)
    //                 {
    //                     result[j] = (Integer) source[i][j];
    //                 }
    //                 sols.add(new QapSolution(result));
    //             }
    //             this.solutions = sols;
    //         }

    //         @Override
    //         public Object[][] solutionsToPhenotype() throws Exception
    //         {
    //             int height = this.solutions.size();
    //             int width = ((QapSolution) this.solutions.get(0))._assign.length;
    //             Object[][] r = new Object[height][width];
    //             for (int i = 0; i < height; i++)
    //             {
    //                 for (int j = 0; j < width; j++)
    //                 {
    //                     r[i][j] = ((QapSolution) this.solutions.get(i))._assign[j];
    //                 }
    //             }
    //             return r;
    //         }

	// 		@Override
	// 		public Object[] solutionToPhenotype(QapSolution solution) throws Exception {
	// 			// TODO Auto-generated method stub
	// 			return null;
	// 		}
    //     };

    //     //        Object[][] solutions = _provider.generateInitialSolutions(_algParams.getValueInt("numSolution"));
    //     //        algorithm.solutionsFromPhenotype(solutions);
    //     return algorithm;
    // }

    @Override
    public IAlgorithmAdapter<QapPhenotype, QapSolution> createAlgorithm(ProblemInstance instance,
            IPhenotypeEvaluator<QapPhenotype> phenotypeEvaluator) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
