package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

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
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
