package org.seage.problem.jssp.sannealing;

import org.junit.Ignore;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

@Ignore
public class JsspSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{
    public JsspSimulatedAnnealingFactoryTest()
    {
        super(new JsspProblemProvider(), "SimulatedAnnealing");
    }

}
