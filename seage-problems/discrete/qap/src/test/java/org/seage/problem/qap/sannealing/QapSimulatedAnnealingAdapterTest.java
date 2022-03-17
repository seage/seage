package org.seage.problem.qap.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class QapSimulatedAnnealingFactoryTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{

    public QapSimulatedAnnealingFactoryTest()
    {
        super(new QapProblemProvider(), "SimulatedAnnealing");
    }

}
