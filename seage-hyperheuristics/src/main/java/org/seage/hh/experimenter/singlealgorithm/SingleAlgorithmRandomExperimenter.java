package org.seage.hh.experimenter.singlealgorithm;

import org.seage.hh.experimenter.configurator.RandomConfigurator;

public class SingleAlgorithmRandomExperimenter extends SingleAlgorithmExperimenter {

  public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS)
      throws Exception {
    super("SingleAlgorithmDefault", problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
    
    this.configurator = new RandomConfigurator();
  }
  
}
