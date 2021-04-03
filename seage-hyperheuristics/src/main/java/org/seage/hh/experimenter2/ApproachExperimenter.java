package org.seage.hh.experimenter2;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.experimenter.ExperimentTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApproachExperimenter {
  protected static Logger logger = LoggerFactory.getLogger(ApproachExperimenter.class.getName());
  
  private UUID experimentID;
  private String algorithmID;
  private HashMap<String, List<String>> problemInstanceIDs;
  private int numConfigs;
  private int timeoutS;

  /**
   * ApproachExperimenter.
   * @param algorithmID .
   * @param problemInstanceIDs .
   * @throws Exception .
   */
  public ApproachExperimenter(
      String algorithmID, HashMap<String, 
      List<String>> problemInstanceIDs,
      int numConfigs, int timeoutS) throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.problemInstanceIDs = problemInstanceIDs;
    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;
  }

  /**. */
  public void runExperiment() {
    logger.info("Approach '{}'", algorithmID);
    
    for (Entry<String, List<String>> entry: problemInstanceIDs.entrySet()) {
      String problemID = entry.getKey();
      logger.info("  Problem '{}'", problemID);

      ProblemInfo problemInfo = 
          ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
      if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
        logger.warn("Unimplemented problem '{}' for algorithm '{}'", problemID, algorithmID);
        continue;
      }

      for (String instanceID : entry.getValue()) {
        logger.info("   - Instance '{}'", instanceID);
        new ExperimentTask(
            experimentTaskID, 
            experimentID, 
            jobID, 1, 
            problemID, instanceID, algorithmID, 
            algorithmParams, timeoutS);
      }
    }
  }
}
