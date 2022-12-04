package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.ExtendedDefaultConfigurator;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @throws Exception .
   */
  public SingleAlgorithmFeedbackExperiment(
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
        algorithmID,
        instanceID,
        numRuns,
        timeoutS,
        experimentReporter
    );

    experimentName = "SingleAlgorithmFeedback";
    configurator = new FeedbackConfigurator();
  }
  // public SingleAlgorithmFeedbackExperiment(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
