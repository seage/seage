package org.seage.problem.tsp.tabusearch;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspTabuSearchFactoryTest extends ProblemProviderTestBase
{

    public TspTabuSearchFactoryTest()
    {
        super(new TspProblemProvider(), "TabuSearch");
    }

}
