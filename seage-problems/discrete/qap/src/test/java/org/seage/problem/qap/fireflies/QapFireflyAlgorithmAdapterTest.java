package org.seage.problem.qap.fireflies;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

@Disabled("Adapter class not fully implemented yet")

public class QapFireflyAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{
    public QapFireflyAlgorithmAdapterTest()
    {
        super(new QapProblemProvider(), "FireflyAlgorithm");
    }

}
