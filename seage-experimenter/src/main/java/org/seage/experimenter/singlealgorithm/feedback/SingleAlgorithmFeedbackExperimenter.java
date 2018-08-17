package org.seage.experimenter.singlealgorithm.feedback;

import org.seage.experimenter.singlealgorithm.random.SingleAlgorithmRandomExperimenter;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmRandomExperimenter
{

    public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs,
			int numConfigs, int timeoutS) throws Exception {
		super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
		_experimentName = "SingleAlgorithmFeedback";
		_configurator = new FeedbackConfigurator();
	}
}
