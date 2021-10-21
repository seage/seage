package org.seage.problem.jssp.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

@Disabled
public class JsspSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{
    public JsspSimulatedAnnealingFactoryTest()
    {
        super(new JsspProblemProvider());
    }

}
