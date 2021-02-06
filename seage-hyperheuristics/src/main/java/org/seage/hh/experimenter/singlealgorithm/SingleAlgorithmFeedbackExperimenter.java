package org.seage.hh.experimenter.singlealgorithm;

import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends Experimenter {

  public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS) throws Exception {
    super("", problemID, instanceIDs, algorithmIDs);
    FeedbackConfigurator configurator = new FeedbackConfigurator();
}

  @Override
  protected void runExperimentTasks(ProblemInstanceInfo instanceInfo) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  protected String getExperimentConfig() {
    // TODO Auto-generated method stub
    return null;
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

  // public SingleAlgorithmFeedbackExperimenter(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
