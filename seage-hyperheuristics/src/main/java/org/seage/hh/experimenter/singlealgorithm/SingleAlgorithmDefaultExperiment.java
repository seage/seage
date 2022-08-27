package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;

public class SingleAlgorithmDefaultExperiment extends SingleAlgorithmExperiment {

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
  public SingleAlgorithmDefaultExperiment(
      UUID experimentID,
      String problemID, 
      String instanceID,
      String algorithmID, 
      int numRuns,
      int timeoutS,
      ExperimentReporter experimentReporter,
      double spread
  )
      throws Exception {
    super(
        experimentID,
        problemID,
        algorithmID,
        instanceID,
        numRuns,
        timeoutS,
        experimentReporter
    );

    this.experimentName = "SingleAlgorithmDefault";
    this.configurator = new DefaultConfigurator(spread);
  }
  
}
