package org.seage.problem.qap.tabusearch;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

public class QapTabuSearchFactoryTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{
    public QapTabuSearchFactoryTest()
    {
        super(new QapProblemProvider(), "TabuSearch");
    }

}
