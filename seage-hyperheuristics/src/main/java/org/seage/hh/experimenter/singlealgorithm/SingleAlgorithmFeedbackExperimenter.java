package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmExperimenter {

  public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS) throws Exception {
    super("SingleAlgorithmFeedback", problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
    this.configurator = new FeedbackConfigurator();
}
  // public SingleAlgorithmFeedbackExperimenter(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
