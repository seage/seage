package org.seage.hh.experimenter.singlealgorithm;

import java.util.Arrays;
import java.util.Map;
import org.seage.hh.experimenter.configurator.RandomConfigurator;

public class SingleAlgorithmRandomExperimenter extends SingleAlgorithmExperimenter {

  public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS)
      throws Exception {
    super(
        "SingleAlgorithmRandom",
        Arrays.asList(algorithmIDs), 
        Map.ofEntries(
          Map.entry(problemID, Arrays.asList(instanceIDs))), 
        numConfigs, 
        1, 
        timeoutS
    );
    
    this.configurator = new RandomConfigurator();
  }
  
}
