package org.seage.problem.qap.particles;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

public class QapParticleSwarmFactoryTest extends AlgorithmFactoryTestBase
{

	public QapParticleSwarmFactoryTest() 
	{
		super(new QapProblemProvider(), "ParticleSwarm");
	}

}
