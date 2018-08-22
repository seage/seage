package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.config.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmRandomExperimenter
{

    public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs,
			int numConfigs, int timeoutS) throws Exception {
		super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
		_experimentName = "SingleAlgorithmFeedback";
		_configurator = new FeedbackConfigurator();
	}
}
