package org.seage.problem.sat.tabusearch;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatTabuSearchFactoryTest extends ProblemProviderTestBase
{

    public SatTabuSearchFactoryTest()
    {
        super(new SatProblemProvider(), "TabuSearch");
    }

}
