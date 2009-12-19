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

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.genetics.GeneticAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.problem.tsp.TspProblemSolver;
import org.seage.problem.tsp.data.City;


/**
 *
 * @author Richard Malek
 */
public class TspGeneticAlgorithmFactory implements IAlgorithmFactory
{
    private DataNode _algParams;
    private City[] _cities;

    public TspGeneticAlgorithmFactory(DataNode algParams, City[] cities) throws Exception
    {
        _algParams = algParams;
        _cities = cities;
    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;

        algorithm = new GeneticAlgorithmAdapter(new TspGeneticOperator(), new TspEvaluator(_cities), false, "");

        Object[][] solutions = TspProblemSolver.generateInitialSolutions(_cities.length, _algParams.getValueInt("numSolution"));
        algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    }

}
