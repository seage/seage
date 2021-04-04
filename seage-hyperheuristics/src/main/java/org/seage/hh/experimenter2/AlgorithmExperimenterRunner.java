package org.seage.hh.experimenter2;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
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

    this.experimentReporter = new ExperimentReporter();
  }


  /**
   * Method runs experiment.
   */
  public void runExperiment() throws Exception {
    // Create experiment reporter
    this.experimentReporter.createExperimentReport(
        this.experimentID,
        this.experimentName,
        this.problemInstanceIDs.keySet().toString(),
        getProblemInstancesArray(),
        new String[] {this.algorithmID},
        getExperimentConfig(),
        Date.from(Instant.now())
    ); 

    
    logger.info("-------------------------------------");
    logger.info("Experimenter: {}", this.experimentName);
    logger.info("ExperimentID: {}", experimentID);
    logger.info("-------------------------------------");
    
    // Run experiments
    logger.info("Algorithm '{}'", algorithmID);

    long startDate;
    startDate = System.currentTimeMillis();

    for (Entry<String, List<String>> entry : problemInstanceIDs.entrySet()) {
      String problemID = entry.getKey();
      logger.info("  Problem '{}'", problemID);

      for (String instanceID : entry.getValue()) {
        logger.info("    Instance '{}'", instanceID);

        createAlgorithmExperimenter(problemID, instanceID).runExperiment();
      }
    }
   
    long endDate = System.currentTimeMillis();
    logger.info("-------------------------------------");
    logger.info("Experiment {} finished ...", experimentID);
    logger.info("Experiment duration: {} (DD:HH:mm:ss)", getDurationBreakdown(endDate - startDate));
    
    this.experimentReporter.updateEndDate(this.experimentID, new Date(endDate));
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

  protected static String getDurationBreakdown(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration must be greater than zero!");
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds); // (sb.toString());
  }
}
