package org.seage.experimenter.singlealgorithm.random;

import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;

public class RandomSingleAlgorithmExperimenter extends SingleAlgorithmExperimenter
{

	public RandomSingleAlgorithmExperimenter(String experimentName)
	{
		super(experimentName, new RandomConfigurator());
		// TODO Auto-generated constructor stub
	}

}
