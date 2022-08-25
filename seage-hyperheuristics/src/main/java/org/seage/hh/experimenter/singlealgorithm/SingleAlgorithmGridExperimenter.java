package org.seage.hh.experimenter.singlealgorithm;

import java.util.Arrays;
import java.util.Map;
import org.seage.hh.experimenter.configurator.GridConfigurator;

public class SingleAlgorithmGridExperimenter extends SingleAlgorithmExperimenter {

  /**
   * .
   * @param problemID .
   * @param instanceIDs .
   * @param algorithmIDs .
   * @param numConfigs .
   * @param timeoutS .
   * @param granularity .
   * @throws Exception .
   */
  public SingleAlgorithmGridExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS, int granularity)
      throws Exception {
    super(
        "SingleAlgorithmGrid",
        Arrays.asList(algorithmIDs), 
        Map.ofEntries(
          Map.entry(problemID, Arrays.asList(instanceIDs))), 
        numConfigs, 
        1, 
        timeoutS
    );
    
    this.configurator = new GridConfigurator(granularity);
  }
  
}
