package org.seage.problem.jsp.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jsp.JspProblemProvider;

@Disabled
public class JspSimulatedAnnealingFactoryTest extends ProblemProviderTestBase
{
    public JspSimulatedAnnealingFactoryTest()
    {
        super(new JspProblemProvider());
    }

}
