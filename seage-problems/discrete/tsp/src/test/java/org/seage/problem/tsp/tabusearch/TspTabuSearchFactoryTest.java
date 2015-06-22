package org.seage.problem.tsp.tabusearch;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspTabuSearchFactoryTest extends AlgorithmFactoryTestBase
{

	public TspTabuSearchFactoryTest() 
	{
		super(new TspProblemProvider(), "TabuSearch");
	}

}
