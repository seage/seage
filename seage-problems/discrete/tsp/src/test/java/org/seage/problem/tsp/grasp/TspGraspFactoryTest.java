package org.seage.problem.tsp.grasp;

import org.junit.Ignore;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class TspGraspFactoryTest extends AlgorithmFactoryTestBase
{
    public TspGraspFactoryTest()
    {
        super(new TspProblemProvider(), "GRASP");
    }

}
