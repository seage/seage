package org.seage.hh.experimenter.singlealgorithm;

import java.util.List;
import org.seage.hh.experimenter.configurator.GridConfigurator;

public class SingleAlgorithmGridExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs. 
   * @param numRuns .
   * @param timeoutS .
   * @param granularity .
   * @throws Exception .
   */
  public SingleAlgorithmGridExperiment(String algorithmID, String problemID,
      List<String> instanceIDs, int numRuns, int timeoutS, int granularity, String tag)
      throws Exception {
    super("SingleAlgorithmGrid", algorithmID, problemID, instanceIDs, numRuns, timeoutS, tag);
    
    configurator = new GridConfigurator(granularity);
  }
  
}
