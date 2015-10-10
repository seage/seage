package org.seage.problem.jssp.sannealing;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

public class JsspSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{
    public JsspSimulatedAnnealingFactoryTest()
    {
        super(new JsspProblemProvider(), "SimulatedAnnealing");
    }

}
