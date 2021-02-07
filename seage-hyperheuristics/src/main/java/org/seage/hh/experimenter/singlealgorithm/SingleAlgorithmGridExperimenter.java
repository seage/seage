package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.configurator.GridConfigurator;

public class SingleAlgorithmGridExperimenter extends SingleAlgorithmExperimenter {

  public SingleAlgorithmGridExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS, int granularity)
      throws Exception {
    super("SingleAlgorithmGrid", problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
    
    this.configurator = new GridConfigurator(granularity);
  }
  
}
