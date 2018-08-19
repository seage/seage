package org.seage.problem.tsp.particles;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class TspParticleSwarmFactoryTest extends ProblemProviderTestBase
{
    public TspParticleSwarmFactoryTest()
    {
        super(new TspProblemProvider(), "ParticleSwarm");
    }

}
