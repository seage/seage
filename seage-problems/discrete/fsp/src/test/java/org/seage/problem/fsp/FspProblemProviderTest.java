package org.seage.problem.fsp;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jsp.JspPhenotype;

// @Ignore
public class FspProblemProviderTest extends ProblemProviderTestBase<JspPhenotype> {
    public FspProblemProviderTest() {
        super(new FspProblemProvider());
    }
}
