package org.seage.problem.tsp.sannealing;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{

    public TspSimulatedAnnealingFactoryTest()
    {
        super(new TspProblemProvider(), "SimulatedAnnealing");
    }

}
