package org.seage.problem.qap.sannealing;

import org.junit.Ignore;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class QapSimulatedAnnealingFactoryTest extends AlgorithmFactoryTestBase
{

    public QapSimulatedAnnealingFactoryTest()
    {
        super(new QapProblemProvider(), "SimulatedAnnealing");
    }

}
