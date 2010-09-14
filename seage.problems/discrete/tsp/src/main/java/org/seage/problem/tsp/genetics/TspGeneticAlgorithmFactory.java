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

package org.seage.problem.tsp.genetics;

import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.genetics.GeneticAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemProvider;


/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class TspGeneticAlgorithmFactory implements IAlgorithmFactory
{
//    private DataNode _algParams;
//    private TspProblemProvider _provider;
//
//    public TspGeneticAlgorithmFactory(DataNode algParams, TspProblemProvider provider) throws Exception
//    {
//        _algParams = algParams;
//        _provider = provider;
//    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;
        City[] cities = TspProblemProvider.getCities();
        algorithm = new GeneticAlgorithmAdapter(new TspGeneticOperator(), new TspEvaluator(cities), false, "");

//        Object[][] solutions = _provider.generateInitialSolutions(_algParams.getValueInt("numSolution"));
//        algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    }

}
