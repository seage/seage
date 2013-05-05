package org.seage.experimenter.singlealgorithm.feedback;

import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmExperimenter
{

	public SingleAlgorithmFeedbackExperimenter() throws Exception
	{
		super("SingleAlgorithmFeedback", new FeedbackConfigurator());
	}

}
