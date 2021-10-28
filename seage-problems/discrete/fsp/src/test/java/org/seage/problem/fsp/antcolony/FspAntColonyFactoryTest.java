package org.seage.problem.fsp.antcolony;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.fsp.FspProblemProvider;

@Disabled
public class FspAntColonyFactoryTest extends ProblemProviderTestBase
{
    public FspAntColonyFactoryTest()
    {
        super(new FspProblemProvider());
    }

}
