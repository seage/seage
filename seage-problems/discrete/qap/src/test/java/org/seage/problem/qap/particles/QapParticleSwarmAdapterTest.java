package org.seage.problem.qap.particles;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class QapParticleSwarmAdapterTest extends ProblemAlgorithmAdapterTestBase<QapPhenotype>
{
    public QapParticleSwarmAdapterTest()
    {
        super(new QapProblemProvider(), "ParticleSwarm");
    }

}
