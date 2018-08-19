package org.seage.problem.sat.genetics;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatGeneticAlgorithmFactoryTest extends ProblemProviderTestBase
{

    public SatGeneticAlgorithmFactoryTest()
    {
        super(new SatProblemProvider(), "GeneticAlgorithm");
    }

}
