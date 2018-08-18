package org.seage.problem.tsp.genetics;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspGeneticAlgorithmFactoryTest extends AlgorithmFactoryTestBase<TspPhenotype>
{

    public TspGeneticAlgorithmFactoryTest()
    {
        super(new TspProblemProvider(), "GeneticAlgorithm");
    }

}
