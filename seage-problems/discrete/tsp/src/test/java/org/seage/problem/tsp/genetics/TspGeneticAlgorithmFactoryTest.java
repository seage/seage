package org.seage.problem.tsp.genetics;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspGeneticAlgorithmFactoryTest extends AlgorithmFactoryTestBase
{

	public TspGeneticAlgorithmFactoryTest() 
	{
		super(new TspGeneticAlgorithmFactory(), new TspProblemProvider());
		// TODO Auto-generated constructor stub
	}

}
