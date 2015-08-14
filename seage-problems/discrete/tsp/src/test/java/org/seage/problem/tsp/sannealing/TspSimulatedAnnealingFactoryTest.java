package org.seage.problem.tsp.sannealing;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{

    public TspSimulatedAnnealingFactoryTest()
    {
        super(new TspProblemProvider(), "SimulatedAnnealing");
        // TODO Auto-generated constructor stub
    }

}
