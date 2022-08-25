package org.seage.hh.experimenter.singlealgorithm;

import java.util.Arrays;
import java.util.Map;
import org.seage.hh.experimenter.configurator.ExtendedDefaultConfigurator;

public class SingleAlgorithmFeedbackExperimenter extends SingleAlgorithmExperimenter {

  /**
   * .
   * @param problemID .
   * @param instanceIDs .
   * @param algorithmIDs .
   * @param numConfigs .
   * @param timeoutS .
   */
  public SingleAlgorithmFeedbackExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS) throws Exception {
    super(
        "SingleAlgorithmFeedback",
        Arrays.asList(algorithmIDs), 
        Map.ofEntries(
          Map.entry(problemID, Arrays.asList(instanceIDs))), 
        numConfigs, 
        1, 
        timeoutS
    );
    this.configurator = new ExtendedDefaultConfigurator();
}
  // public SingleAlgorithmFeedbackExperimenter(String problemID, String[]
  // instanceIDs, String[] algorithmIDs,
  // int numConfigs, int timeoutS) throws Exception {
  // super(problemID, instanceIDs, algorithmIDs, numConfigs, timeoutS);
  // this.experimentName = "SingleAlgorithmFeedback";
  // _configurator = new FeedbackConfigurator();
  // }
}
