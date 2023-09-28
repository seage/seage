package org.seage.hh.experimenter.singlealgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;

public class SingleAlgorithmFeedbackExperiment extends SingleAlgorithmExperiment {

  /**
   * .
   * @param problemID .
   * @param instanceID .
   * @param algorithmID .
   * @param numRuns .
   * @param timeoutS .
   * @param spread .
   * @throws Exception .
   */
  public SingleAlgorithmFeedbackExperiment(
      String algorithmID, 
      String problemID, 
      List<String> instanceIDs,      
      int numRuns,
      int timeoutS,
      double spread,
      String tag
  ) throws Exception {
    super(
        algorithmID,
        problemID,
        instanceIDs,
        numRuns,
        timeoutS,
        tag
    );

    experimentName = "SingleAlgorithmFeedback";
    configurator = new FeedbackConfigurator(spread);
  }

  @Override
  protected Double runForInstance(String instanceID) throws Exception {
    ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);

    // The taskQueue size must be limited since the results are stored in the task's reports
    // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();

    // Prepare experiment task configs
    ProblemConfig[] configs = configurator.prepareConfigs(problemInfo,
        instanceInfo.getInstanceID(), algorithmID, numRuns);
      
    // Enqueue experiment tasks
    for (int runID = 1; runID <= numRuns; runID++) {
      taskQueue.add(new ExperimentTaskRequest(
          UUID.randomUUID(), experimentID, runID, 1, problemID, instanceID,
          algorithmID, configs[runID - 1].hash(), configs[runID - 1].getAlgorithmParams(), null, timeoutS));
    }
    
    // RUN EXPERIMENT TASKS
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

    return bestScore;
  }
}
