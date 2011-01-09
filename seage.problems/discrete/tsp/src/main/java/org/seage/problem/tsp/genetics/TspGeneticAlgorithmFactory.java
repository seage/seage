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
import org.seage.aal.ProblemInstance;
import org.seage.aal.genetics.GeneticAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;



/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class TspGeneticAlgorithmFactory implements IAlgorithmFactory
{

    public Class getAlgorithmClass() {
        return GeneticAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance, DataNode config) throws Exception
    {        
        IAlgorithmAdapter algorithm;
        City[] cities = ((TspProblemInstance)instance).getCities();
        algorithm = new GeneticAlgorithmAdapter(new TspGeneticOperator(), new TspEvaluator(cities), false, "");

        return algorithm;
    }

}
