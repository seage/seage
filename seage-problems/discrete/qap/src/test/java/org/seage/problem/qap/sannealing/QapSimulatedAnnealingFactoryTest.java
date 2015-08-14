package org.seage.problem.qap.sannealing;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

public class QapSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{

    public QapSimulatedAnnealingFactoryTest()
    {
        super(new QapProblemProvider(), "SimulatedAnnealing");
    }

}
