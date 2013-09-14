package org.seage.experimenter.singlealgorithm.feedback;

import org.seage.experimenter.singlealgorithm.random.SingleAlgorithmRandomExperimenter;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmRandomExperimenter
{

	public SingleAlgorithmFeedbackExperimenter(int numConfigs, int timeoutS) throws Exception
	{
		super("SingleAlgorithmFeedback", numConfigs, timeoutS);
		_configurator = new FeedbackConfigurator();
	}
}
