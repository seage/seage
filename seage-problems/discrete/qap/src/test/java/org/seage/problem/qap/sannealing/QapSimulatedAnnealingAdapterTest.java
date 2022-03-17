package org.seage.problem.qap.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class QapSimulatedAnnealingAdapterTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{

    public QapSimulatedAnnealingAdapterTest()
    {
        super(new QapProblemProvider(), "SimulatedAnnealing");
    }

}
