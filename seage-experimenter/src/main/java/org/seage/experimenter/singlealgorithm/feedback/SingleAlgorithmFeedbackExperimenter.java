package org.seage.experimenter.singlealgorithm.feedback;

import org.seage.experimenter.singlealgorithm.random.SingleAlgorithmRandomExperimenter;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmRandomExperimenter
{

	public SingleAlgorithmFeedbackExperimenter() throws Exception
	{
		super("SingleAlgorithmFeedback");
		_configurator = new FeedbackConfigurator();
	}
}
