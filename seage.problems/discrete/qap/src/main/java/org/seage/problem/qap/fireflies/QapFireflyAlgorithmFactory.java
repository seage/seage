/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.problem.qap.fireflies;

import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.problem.qap.fireflies.*;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.fireflies.FireflyAlgorithmAdapter;
import org.seage.metaheuristic.fireflies.*;
import org.seage.problem.qap.*;

 
/** 
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("FireflyAlgorithm")
@Annotations.AlgorithmName("FireflyAlgorithm")
public class QapFireflyAlgorithmFactory implements IAlgorithmFactory
{

    public void setProblemProvider(IProblemProvider provider) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getAlgorithmClass() {
        return FireflyAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, DataNode config) throws Exception
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
