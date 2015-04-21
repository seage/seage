package org.seage.problem.tsp.grasp;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspGraspFactoryTest extends AlgorithmFactoryTestBase
{

	public TspGraspFactoryTest() 
	{
		super(new TspGraspAlgorithmFactory(), new TspProblemProvider());
		// TODO Auto-generated constructor stub
	}

}