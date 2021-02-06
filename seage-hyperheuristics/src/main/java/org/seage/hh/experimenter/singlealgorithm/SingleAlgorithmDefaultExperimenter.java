package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.configurator.DefaultConfigurator;

public class SingleAlgorithmDefaultExperimenter extends SingleAlgorithmExperimenter {

  public SingleAlgorithmDefaultExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS, double spread)
      throws Exception {
    super("SingleAlgorithmDefault", problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
    
    this.configurator = new DefaultConfigurator(spread);
  }
  
}
