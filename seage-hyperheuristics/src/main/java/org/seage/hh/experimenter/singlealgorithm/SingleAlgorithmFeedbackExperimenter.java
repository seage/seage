package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.ExtendedDefaultConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmExperimenter {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @throws Exception .
   */
  public SingleAlgorithmFeedbackExperimenter(
      UUID experimentID,
      String problemID, 
      String instanceID,
      String algorithmID, 
      int numRuns,
      int timeoutS,
      ExperimentReporter experimentReporter
  ) throws Exception {
    super(
        experimentID,
        problemID,
        instanceID,
        algorithmID,
        numRuns,
        timeoutS,
        experimentReporter
    );

    this.experimentName = "SingleAlgorithmFeedback";
    this.configurator = new ExtendedDefaultConfigurator();
  }
  // public SingleAlgorithmFeedbackExperimenter(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
