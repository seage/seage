package org.seage.problem.sat.genetics;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatGeneticAlgorithmFactoryTest extends AlgorithmFactoryTestBase
{

	public SatGeneticAlgorithmFactoryTest() 
	{
		super( new SatProblemProvider(), "GeneticAlgorithm");
	}

}
