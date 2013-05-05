package org.seage.experimenter.singlealgorithm.random;

import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;

public class SingleAlgorithmRandomExperimenter extends SingleAlgorithmExperimenter
{

	public SingleAlgorithmRandomExperimenter()
	{
		super("SingleAlgorithmRandom", new RandomConfigurator());
	}

}
