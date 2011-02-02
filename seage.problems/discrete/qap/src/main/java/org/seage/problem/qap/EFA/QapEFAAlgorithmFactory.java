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

package org.seage.problem.qap.EFA;

import org.seage.problem.qap.EFA.*;
import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.EFA.EFAAlgorithmAdapter;
import org.seage.metaheuristic.EFA.*;
import org.seage.problem.qap.*;


/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("EFAAlgorithm")
@Annotations.AlgorithmName("EFAAlgorithm")
public class QapEFAAlgorithmFactory implements IAlgorithmFactory
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
        return EFAAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;
        Double[][][] facilityLocation = QapProblemProvider.getFacilityLocation();
        algorithm = new EFAAlgorithmAdapter((EFAOperator)(new QapEFAOperator()), (ObjectiveFunction)(new QapObjectiveFunction(facilityLocation)), false, "");

//        Object[][] solutions = _provider.generateInitialSolutions(_algParams.getValueInt("numSolution"));
//        algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    } 

}
