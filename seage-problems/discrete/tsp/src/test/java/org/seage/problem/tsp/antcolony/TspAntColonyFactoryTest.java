package org.seage.problem.tsp.antcolony;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspAntColonyFactoryTest extends AlgorithmFactoryTestBase
{

	public TspAntColonyFactoryTest() 
	{
		super(new TspAntColonyFactory(), new TspProblemProvider());
		// TODO Auto-generated constructor stub
	}

}
