package org.seage.hh.experimenter.singlealgorithm;

import java.util.List;
import java.util.UUID;

import org.seage.hh.experimenter.configurator.GridConfigurator;

public class SingleAlgorithmGridExperiment extends SingleAlgorithmExperiment {

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
  public SingleAlgorithmGridExperiment(
      String algorithmID,
      String problemID, 
      List<String> instanceIDs,
      int numRuns,
      int timeoutS,
      int granularity, String tag)
      throws Exception {
    super(
        algorithmID,
        problemID,
        instanceIDs,
        numRuns,
        timeoutS,
        tag
    );
    
    experimentName = "SingleAlgorithmGrid";
    configurator = new GridConfigurator(granularity);
  }
  
}
