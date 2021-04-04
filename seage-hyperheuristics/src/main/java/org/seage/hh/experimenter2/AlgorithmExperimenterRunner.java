package org.seage.hh.experimenter2;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.seage.aal.problem.ProblemProvider;
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
   * @param algorithmID Algorithm ID.
   * @param problemInstanceIDs Map of problem instances.
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
   * Method runs experiment.
   */
  public void runExperiment() throws Exception {
    logger.info("Algorithm '{}'", algorithmID);

    for (Entry<String, List<String>> entry : problemInstanceIDs.entrySet()) {
      String problemID = entry.getKey();
      logger.info("  Problem '{}'", problemID);

      for (String instanceID : entry.getValue()) {
        logger.info("    Instance '{}'", instanceID);

        createAlgorithmExperimenter(problemID, instanceID).runExperiment();
      }
    }
  }


  private AlgorithmExperimenter createAlgorithmExperimenter(
      String problemID, String instanceID) throws Exception {
    boolean ordinaryAlg = ProblemProvider
        .getProblemProviders()
        .get(problemID)
        .getProblemInfo()
        .getDataNode("Algorithms")
        .getDataNodeById(algorithmID) != null;
    
    if (ordinaryAlg) {
      return new MetaHeuristicExperimenter(
        experimentID, problemID, instanceID, algorithmID, numConfigs, timeoutS);
    }

    if (algorithmID.equals("HyperHeuristic1")) {
      return new HyperHeuristic1Experimenter();
    }

    throw new Exception(String.format("Unknown algorithm id '%s'", algorithmID));
  }
}
