package org.seage.problem.sat.grasp;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class SatGraspAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {
    
    public SatGraspAdapterTest() {
        super(new SatProblemProvider(), "GRASP");
    }
}
