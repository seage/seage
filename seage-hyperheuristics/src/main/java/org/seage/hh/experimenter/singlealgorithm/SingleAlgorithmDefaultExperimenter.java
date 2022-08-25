package org.seage.hh.experimenter.singlealgorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import com.google.common.collect.Multiset.Entry;

public class SingleAlgorithmDefaultExperimenter extends SingleAlgorithmExperimenter {

  /**
   * .
   * @param problemID .
   * @param instanceIDs .
   * @param algorithmIDs .
   * @param numConfigs .
   * @param timeoutS .
   * @param spread .
   * @throws Exception .
   */
  public SingleAlgorithmDefaultExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS, double spread)
      throws Exception {
    super(
        "SingleAlgorithmDefault",
        Arrays.asList(algorithmIDs), 
        Map.ofEntries(
          Map.entry(problemID, Arrays.asList(instanceIDs))), 
        numConfigs, 
        1, 
        timeoutS
    );

    
    this.configurator = new DefaultConfigurator(spread);
  }
  
}
