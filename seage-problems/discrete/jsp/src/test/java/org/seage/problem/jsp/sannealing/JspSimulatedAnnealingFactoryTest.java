package org.seage.problem.jsp.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspProblemProvider;

@Disabled
public class JspSimulatedAnnealingFactoryTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype>
{
    public JspSimulatedAnnealingFactoryTest()
    {
        super(new JspProblemProvider(), "SimulatedAnnealing");
    }

}
