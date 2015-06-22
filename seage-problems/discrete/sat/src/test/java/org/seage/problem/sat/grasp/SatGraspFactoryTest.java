package org.seage.problem.sat.grasp;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatGraspFactoryTest extends AlgorithmFactoryTestBase
{

	public SatGraspFactoryTest() 
	{
		super(new SatProblemProvider(), "GRASP");
	}

}
