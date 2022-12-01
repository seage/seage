package org.seage.problem.fsp.tabusearch;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspPhenotype;

public class FspTabuSearchFactoryTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {
    public FspTabuSearchFactoryTest()
    {
        super(new FspProblemProvider(), "TabuSearch");
    }

}
