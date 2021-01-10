package org.seage.problem.tsp.particles;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class TspParticleSwarmAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {
  public TspParticleSwarmAdapterTest() {
    super(new TspProblemProvider(), "ParticleSwarm");
  }

}
