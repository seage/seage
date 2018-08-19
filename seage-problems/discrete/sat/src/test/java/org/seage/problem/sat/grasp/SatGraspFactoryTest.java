package org.seage.problem.sat.grasp;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.sat.SatProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class SatGraspFactoryTest extends ProblemProviderTestBase
{
    public SatGraspFactoryTest()
    {
        super(new SatProblemProvider(), "GRASP");
    }

}
