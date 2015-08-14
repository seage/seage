package org.seage.problem.sat.sannealing;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{

    public SatSimulatedAnnealingFactoryTest()
    {
        super(new SatProblemProvider(), "SimulatedAnnealing");
    }

}
