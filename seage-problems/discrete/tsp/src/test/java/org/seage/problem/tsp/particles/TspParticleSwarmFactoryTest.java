package org.seage.problem.tsp.particles;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class TspParticleSwarmFactoryTest extends AlgorithmFactoryTestBase
{
    public TspParticleSwarmFactoryTest()
    {
        super(new TspProblemProvider(), "ParticleSwarm");
    }

}
