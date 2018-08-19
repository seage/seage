package org.seage.problem.tsp.genetics;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspGeneticAlgorithmFactoryTest extends ProblemProviderTestBase<TspPhenotype>
{

    public TspGeneticAlgorithmFactoryTest()
    {
        super(new TspProblemProvider(), "GeneticAlgorithm");
    }

}
