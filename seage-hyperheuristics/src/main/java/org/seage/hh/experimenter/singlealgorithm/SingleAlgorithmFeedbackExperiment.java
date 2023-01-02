package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @param spread .
   * @throws Exception .
   */
  public SingleAlgorithmFeedbackExperiment(
      UUID experimentID,
      String problemID, 
      String instanceID,
      String algorithmID, 
      int numRuns,
      int timeoutS,
      double spread,
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
    configurator = new FeedbackConfigurator(spread);
  }
}
