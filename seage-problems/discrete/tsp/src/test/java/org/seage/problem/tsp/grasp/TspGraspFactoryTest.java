package org.seage.problem.tsp.grasp;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class TspGraspFactoryTest extends ProblemProviderTestBase
{
    public TspGraspFactoryTest()
    {
        super(new TspProblemProvider(), "GRASP");
    }

}
