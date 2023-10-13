package org.seage.hh.experimenter.singlealgorithm;

import java.util.List;
import org.seage.hh.experimenter.configurator.RandomConfigurator;

public class SingleAlgorithmRandomExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs. 
   * @param numRuns .
   * @param timeoutS .
   * @throws Exception .
   */
  public SingleAlgorithmRandomExperiment(
      String algorithmID,
      String problemID, 
      List<String> instanceIDs,
      int numRuns,
      int timeoutS,
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
    
    experimentName = "SingleAlgorithmRandom";
    configurator = new RandomConfigurator();
  }
  
}
 