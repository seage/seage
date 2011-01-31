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

import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.fireflies.FireflyAlgorithmAdapter;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.problem.qap.*;


/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("FireflyAlgorithm")
@Annotations.AlgorithmName("FireflyAlgorithm")
public class QapFireflyAlgorithmFactory implements IAlgorithmFactory
{
//    private DataNode _algParams;
//    private TspProblemProvider _provider;
//
//    public TspGeneticAlgorithmFactory(DataNode algParams, TspProblemProvider provider) throws Exception
//    {
//        _algParams = algParams;
//        _provider = provider;
//    }

    public void setProblemProvider(IProblemProvider provider) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getAlgorithmClass() {
        return FireflyAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;
        Double[][][] facilityLocation = QapProblemProvider.getFacilityLocation();
        algorithm = new FireflyAlgorithmAdapter((FireflyOperator)(new QapFireflyOperator()), (ObjectiveFunction)(new QapObjectiveFunction(facilityLocation)), false, "");

//        Object[][] solutions = _provider.generateInitialSolutions(_algParams.getValueInt("numSolution"));
//        algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    } 

}
