package org.seage.problem.jssp.tabusearch;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

public class JsspTabuSearchFactoryTest extends ProblemProviderTestBase
{
    public JsspTabuSearchFactoryTest()
    {
        super(new JsspProblemProvider(), "TabuSearch");
    }

}
