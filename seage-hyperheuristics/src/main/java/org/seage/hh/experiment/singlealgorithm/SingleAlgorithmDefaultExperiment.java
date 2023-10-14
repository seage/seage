package org.seage.hh.experiment.singlealgorithm;

import java.util.List;
import org.seage.hh.configurator.DefaultConfigurator;

public class SingleAlgorithmDefaultExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs. 
   * @param numRuns .
   * @param timeoutS .
   * @param spread .
   * @param tag .
   * @throws Exception .
   */
  public SingleAlgorithmDefaultExperiment(String algorithmID, String problemID,
      List<String> instanceIDs, int numRuns, int timeoutS, double spread, String tag)
      throws Exception {
    super("SingleAlgorithmDefault", algorithmID, problemID, instanceIDs, numRuns, timeoutS, tag);

    configurator = new DefaultConfigurator(spread);
  }
  
}
