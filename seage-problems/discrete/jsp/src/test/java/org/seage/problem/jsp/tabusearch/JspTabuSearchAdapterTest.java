package org.seage.problem.jsp.tabusearch;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspProblemProvider;

@Testable
public class JspTabuSearchAdapterTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {
    public JspTabuSearchAdapterTest() {
        super(new JspProblemProvider(), "TabuSearch");
    }
}
