package org.seage.hh.experimenter.multialgorithm;

import java.util.HashMap;

import org.seage.hh.experimenter.Experimenter;


public class MultiAlgorithmRandomExperimenter extends Experimenter {

  public MultiAlgorithmRandomExperimenter(String algorithmID) throws Exception {
    super(algorithmID, new HashMap<>(), 0, 0);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected String getExperimentConfig() {
    // TODO Auto-generated method stub
    return null;
  }
}
