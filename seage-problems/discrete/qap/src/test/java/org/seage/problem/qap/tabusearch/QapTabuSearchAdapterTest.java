package org.seage.problem.qap.tabusearch;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

@Disabled
public class QapTabuSearchAdapterTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{
    public QapTabuSearchAdapterTest()
    {
        super(new QapProblemProvider(), "TabuSearch");
    }

}
