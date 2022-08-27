package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.GridConfigurator;

public class SingleAlgorithmGridExperimenter extends SingleAlgorithmExperimenter {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @param granularity .
   * @throws Exception .
   */
  public SingleAlgorithmGridExperimenter(
      UUID experimentID,
      String problemID, 
      String instanceID,
      String algorithmID, 
      int numRuns,
      int timeoutS,
      ExperimentReporter experimentReporter,
      int granularity)
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
    
    this.experimentName = "SingleAlgorithmGrid";
    this.configurator = new GridConfigurator(granularity);
  }
  
}
