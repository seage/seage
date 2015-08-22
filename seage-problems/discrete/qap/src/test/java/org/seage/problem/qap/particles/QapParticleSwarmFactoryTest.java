package org.seage.problem.qap.particles;

import org.junit.Ignore;
import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class QapParticleSwarmFactoryTest extends AlgorithmFactoryTestBase
{
    public QapParticleSwarmFactoryTest()
    {
        super(new QapProblemProvider(), "ParticleSwarm");
    }

}
