package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;

public class SingleAlgorithmDefaultExperimenter extends SingleAlgorithmExperimenter {

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
  public SingleAlgorithmDefaultExperimenter(
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
        instanceID,
        algorithmID,
        numRuns,
        timeoutS,
        experimentReporter
    );

    this.experimentName = "SingleAlgorithmDefault";
    this.configurator = new DefaultConfigurator(spread);
  }
  
}
