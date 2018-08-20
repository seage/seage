package org.seage.problem.qap.particles;

import org.junit.Ignore;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.qap.QapProblemProvider;

@Ignore("Adapter class not fully implemented yet")
public class QapParticleSwarmFactoryTest extends ProblemProviderTestBase
{
    public QapParticleSwarmFactoryTest()
    {
        super(new QapProblemProvider(), "ParticleSwarm");
    }

}
