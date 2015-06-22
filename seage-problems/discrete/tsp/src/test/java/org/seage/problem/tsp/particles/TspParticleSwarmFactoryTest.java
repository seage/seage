package org.seage.problem.tsp.particles;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspParticleSwarmFactoryTest extends AlgorithmFactoryTestBase
{

	public TspParticleSwarmFactoryTest() 
	{
		super(new TspProblemProvider(), "ParticleSwarm");
	}

}
