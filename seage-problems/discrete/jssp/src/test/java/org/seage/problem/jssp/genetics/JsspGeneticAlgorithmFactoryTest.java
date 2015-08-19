package org.seage.problem.jssp.genetics;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

public class JsspGeneticAlgorithmFactoryTest extends AlgorithmFactoryTestBase
{
    public JsspGeneticAlgorithmFactoryTest()
    {
        super(new JsspProblemProvider(), "GeneticAlgorithm");
    }

}
