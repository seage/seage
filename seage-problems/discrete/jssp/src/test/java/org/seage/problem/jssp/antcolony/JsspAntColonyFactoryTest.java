package org.seage.problem.jssp.antcolony;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

@Disabled
public class JsspAntColonyFactoryTest extends ProblemProviderTestBase
{
    public JsspAntColonyFactoryTest()
    {
        super(new JsspProblemProvider());
    }

}
