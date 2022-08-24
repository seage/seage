package org.seage.hh.experimenter.multialgorithm;

import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.hh.experimenter.OldExperimenter;

public class MultiAlgorithmRandomExperimenter extends OldExperimenter {

  public MultiAlgorithmRandomExperimenter(String experimentName) throws Exception {
    super(experimentName, "", null, null);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  protected void experimentMain() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected String getExperimentConfig() {
    // TODO Auto-generated method stub
    return null;
  }

}
