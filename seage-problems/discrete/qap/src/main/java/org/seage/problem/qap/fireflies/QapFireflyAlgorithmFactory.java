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

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.fireflies.FireflyAlgorithmAdapter;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.problem.qap.QapProblemInstance;

 
/** 
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("FireflyAlgorithm")
@Annotations.AlgorithmName("FireflyAlgorithm")
public class QapFireflyAlgorithmFactory implements IAlgorithmFactory
{
    private static final long serialVersionUID = -864910230419679081L;

    public void setProblemProvider(IProblemProvider provider) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getAlgorithmClass() {
        return FireflyAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig config) throws Exception
    {
        IAlgorithmAdapter algorithm;
        Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        algorithm = new FireflyAlgorithmAdapter(new QapFireflyOperator(), new QapObjectiveFunction(facilityLocation), false, "") {

            public void solutionsFromPhenotype(Object[][] source) throws Exception {
                int height = source.length;
                int width = source[0].length;
                Solution[] sols = new Solution[height];
                for(int i=0;i<height;i++){
                    Integer[] result = new Integer[width];
                    for(int j=0;j<width;j++){
                        result[j]=(Integer)source[i][j];
                    }
                    sols[i]=new QapSolution(result);
                }
                this._solutions=sols; 
            }

            public Object[][] solutionsToPhenotype() throws Exception {
                int height = _solutions.length;
                int width=((QapSolution)this._solutions[0])._assign.length;
                Object[][] r = new Object[height][width];
                for(int i=0;i<height;i++){
                    for(int j=0;j<width;j++){
                        r[i][j]=((QapSolution)this._solutions[i])._assign[j];
                    }
                }
                return r;
            }
        };

//        Object[][] solutions = _provider.generateInitialSolutions(_algParams.getValueInt("numSolution"));
//        algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    }

//    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, DataNode config) throws Exception {
//
//        Double[][][] facilityLocation = instance
//
//    }

}
