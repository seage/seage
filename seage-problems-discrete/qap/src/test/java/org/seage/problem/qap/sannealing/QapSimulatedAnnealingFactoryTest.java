package org.seage.problem.qap.sannealing;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.qap.QapProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class QapSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{

    public QapSimulatedAnnealingFactoryTest()
    {
        super(new QapProblemProvider(), "SimulatedAnnealing");
    }

}
