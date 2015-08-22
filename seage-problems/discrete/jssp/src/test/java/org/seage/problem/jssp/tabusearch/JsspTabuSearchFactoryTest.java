package org.seage.problem.jssp.tabusearch;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

public class JsspTabuSearchFactoryTest extends AlgorithmFactoryTestBase
{
    public JsspTabuSearchFactoryTest()
    {
        super(new JsspProblemProvider(), "TabuSearch");
    }

}
