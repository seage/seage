package org.seage.problem.qap.fireflies;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.qap.QapProblemProvider;

public class QapFireflyAlgorithmFactoryTest extends AlgorithmFactoryTestBase
{

	public QapFireflyAlgorithmFactoryTest() 
	{
		super(new QapFireflyAlgorithmFactory(), new QapProblemProvider());
	}

}
