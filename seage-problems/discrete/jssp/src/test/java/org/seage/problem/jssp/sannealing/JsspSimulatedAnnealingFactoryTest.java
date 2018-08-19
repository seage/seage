package org.seage.problem.jssp.sannealing;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

@Ignore
public class JsspSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{
    public JsspSimulatedAnnealingFactoryTest()
    {
        super(new JsspProblemProvider(), "SimulatedAnnealing");
    }

}
