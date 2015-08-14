package org.seage.problem.qap.tabusearch;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

public class QapTabuSearchFactoryTest extends AlgorithmFactoryTestBase
{

    public QapTabuSearchFactoryTest()
    {
        super(new QapProblemProvider(), "TabuSearch");
    }

}
