package org.seage.problem.sat.sannealing;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{

    public SatSimulatedAnnealingFactoryTest()
    {
        super(new SatProblemProvider(), "SimulatedAnnealing");
    }

}
