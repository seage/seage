package org.seage.problem.jssp.antcolony;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

@Ignore
public class JsspAntColonyFactoryTest extends ProblemProviderTestBase
{
    public JsspAntColonyFactoryTest()
    {
        super(new JsspProblemProvider(), "AntColony");
    }

}
