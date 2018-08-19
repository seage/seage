package org.seage.problem.tsp.genetics;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype>
{

    public TspGeneticAlgorithmAdapterTest()
    {
        super(new TspProblemProvider(), "GeneticAlgorithm");
    }

}
