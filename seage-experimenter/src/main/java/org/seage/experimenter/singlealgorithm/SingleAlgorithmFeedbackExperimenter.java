package org.seage.experimenter.singlealgorithm;

import org.seage.experimenter.config.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmRandomExperimenter {

  public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs,
      int numConfigs, int timeoutS) throws Exception {
    super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
    // TODO Auto-generated constructor stub
  }

  // public SingleAlgorithmFeedbackExperimenter(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // _experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
