package org.seage.problem.sat.grasp;

import org.junit.Ignore;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class SatGraspFactoryTest extends AlgorithmFactoryTestBase
{
    public SatGraspFactoryTest()
    {
        super(new SatProblemProvider(), "GRASP");
    }

}
