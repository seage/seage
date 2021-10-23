package org.seage.problem.fsp;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspPhenotype;

// @Ignore
public class FspProblemProviderTest extends ProblemProviderTestBase<JsspPhenotype> {
    public FspProblemProviderTest() {
        super(new FspProblemProvider());
    }
}
