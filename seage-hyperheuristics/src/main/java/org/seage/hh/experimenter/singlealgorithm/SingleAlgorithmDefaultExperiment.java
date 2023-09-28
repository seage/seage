package org.seage.hh.experimenter.singlealgorithm;

import java.util.List;
import java.util.UUID;
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
      String problemID, 
      String algorithmID,
      List<String> instanceIDs, 
      int numRuns,
      int timeoutS,
      double spread,
      String tag
  )
      throws Exception {
    super(        
        algorithmID,
        problemID,
        instanceIDs,
        numRuns,
        timeoutS,
        tag
    );

    experimentName = "SingleAlgorithmDefault";
    configurator = new DefaultConfigurator(spread);
  }
  
}
