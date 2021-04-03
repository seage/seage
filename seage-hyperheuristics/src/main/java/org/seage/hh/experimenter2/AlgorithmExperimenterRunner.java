package org.seage.hh.experimenter2;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.experimenter.ExperimentTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmExperimenterRunner {
  protected static Logger logger =
      LoggerFactory.getLogger(AlgorithmExperimenterRunner.class.getName());

  private UUID experimentID;
  private String algorithmID;
  private HashMap<String, List<String>> problemInstanceIDs;
  private int numConfigs;
  private int timeoutS;

  /**
   * ApproachExperimenter.
   * 
   * @param algorithmID        .
   * @param problemInstanceIDs .
   * @throws Exception .
   */
  public AlgorithmExperimenterRunner(String algorithmID,
      HashMap<String, List<String>> problemInstanceIDs, int numConfigs, int timeoutS)
      throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.problemInstanceIDs = problemInstanceIDs;
    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;
  }

  /**
   * .
   * 
   * @throws Exception
   */
  public void runExperiment() throws Exception {
    logger.info("Algorithm '{}'", algorithmID);

    AlgorithmExperimenter ae = createAlgorithmExperimenter(algorithmID);

    for (Entry<String, List<String>> entry : problemInstanceIDs.entrySet()) {
      String problemID = entry.getKey();
      logger.info("  Problem '{}'", problemID);

      for (String instanceID : entry.getValue()) {

        // ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(
        // instanceID);

        // logger.info(" Instance '{}'", instanceInfo.getValue("name"));
        logger.info("    Instance '{}'", instanceID);
        ae.runExperiment();
      }

      // for (String instanceID : entry.getValue()) {
      // logger.info(" - Instance '{}'", instanceID);
      // new ExperimentTask(
      // experimentTaskID,
      // experimentID,
      // jobID, 1,
      // problemID, instanceID, algorithmID,
      // algorithmParams, timeoutS);
      // }
    }
  }

  private AlgorithmExperimenter createAlgorithmExperimenter(String algorithmID) throws Exception {
    boolean ordinaryAlg = ProblemProvider.getProblemProviders()
        .values()
        .stream()
        .anyMatch(pp -> {
          try {
            return pp.getProblemInfo().getDataNode("Algorithms").getDataNodeById(algorithmID) != null;
          } catch (Exception e) {
            return false;
          }
        });

    if (ordinaryAlg) {
      return new MetaheuristicExperimenter();
    }

    throw new Exception(String.format("Unknown algorithm id '%s'", algorithmID));
  }
}
