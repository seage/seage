package org.seage.hh.experimenter.singlealgorithm;

import java.util.UUID;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.RandomConfigurator;

public class SingleAlgorithmRandomExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @throws Exception .
   */
  public SingleAlgorithmRandomExperiment(
      UUID experimentID,
      String problemID, 
      String instanceID,
      String algorithmID, 
      int numRuns,
      int timeoutS,
      ExperimentReporter experimentReporter
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
    
    this.experimentName = "SingleAlgorithmRandom";
    this.configurator = new RandomConfigurator();
  }
  
}
 