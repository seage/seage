package org.seage.problem.fsp.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.fsp.FspProblemProvider;

@Disabled
public class FspSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{
    public FspSimulatedAnnealingFactoryTest()
    {
        super(new FspProblemProvider());
    }

}
