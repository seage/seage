package org.seage.hh.experimenter2;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.UUID;
import org.seage.aal.Annotations.ProblemId;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmExperimenterRunner {
  protected static Logger logger =
      LoggerFactory.getLogger(AlgorithmExperimenterRunner.class.getName());
  protected ExperimentReporter experimentReporter;

  protected String experimentName = "Algorithm";
  private UUID experimentID;
  private String algorithmID;
  private HashMap<String, List<String>> problemInstanceIDs;
  private int numRuns;
  private int timeoutS;


  /**
   * ApproachExperimenter.
   * 
   * @param algorithmID Algorithm ID.
   * @param problemInstanceIDs Map of problem instances.
   */
  public AlgorithmExperimenterRunner(String algorithmID,
      HashMap<String, List<String>> problemInstanceIDs, int numRuns, int timeoutS)
      throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.problemInstanceIDs = problemInstanceIDs;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
  }


  /**
   * Method runs experiment.
   */
  public void runExperiment() throws Exception {
    // Create experiment reporter
    experimentReporter = new ExperimentReporter();
    this.experimentReporter.createExperimentReport(
        this.experimentID,
        this.experimentName,
        this.problemInstanceIDs.keySet().toString(),
        getProblemInstancesArray(),
        new String[] {this.algorithmID},
        getExperimentConfig(),
        Date.from(Instant.now())
    ); 
    
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
      String problemID, String instanceID)
       throws Exception {
    boolean ordinaryAlg = ProblemProvider
        .getProblemProviders()
        .get(problemID)
        .getProblemInfo()
        .getDataNode("Algorithms")
        .getDataNodeById(algorithmID) != null;
    
    if (ordinaryAlg) {
      return new MetaHeuristicExperimenter(
        experimentID, problemID, instanceID, 
        algorithmID, numRuns, timeoutS, this.experimentReporter);
    }

    if (algorithmID.equals("HyperHeuristic1")) {
      return new HyperHeuristic1Experimenter();
    }

    throw new Exception(String.format("Unknown algorithm id '%s'", algorithmID));
  }

  protected String getExperimentConfig() {
    DataNode config = new DataNode("Config");
    config.putValue("timeoutS", this.timeoutS);
    config.putValue("numRuns", this.numRuns);
    
    return config.toString();
  }

  protected String[] getProblemInstancesArray() {
    List<String> results = new ArrayList<>();
    for (Entry<String, List<String>> entry : problemInstanceIDs.entrySet()) {
      String problemID = entry.getKey();
      
      for (String instanceID : entry.getValue()) {
        results.add(String.format("%s:%s", problemID, instanceID));
      }
    }
    return results.toArray(new String[0]);
  }
}
