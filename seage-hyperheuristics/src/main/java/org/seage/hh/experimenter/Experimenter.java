package org.seage.hh.experimenter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.logging.TimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * ExperimentApproachCommand --(calls)--> ExperimenterRunner
 *   ExperimenterRunner --(runs)--> Experimenter 
 *     Experimenter: MetaHeuristicExperimenter | HyperHeuristic1Experimenter
 */
public class Experimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(Experimenter.class.getName());
  
  protected ExperimentReporter experimentReporter;

  protected UUID experimentID;
  protected String algorithmID;
  protected Map<String, List<String>> instanceIDsPerProblems;
  protected int numRuns;
  protected int timeoutS;


  /**
   * ApproachExperimenter.
   * 
   * @param algorithmID Algorithm ID.
   * @param instanceIDsPerProblems Map of problem instances.
   */
  public Experimenter(String algorithmID,
      Map<String, List<String>> instanceIDsPerProblems, int numRuns, int timeoutS)
      throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.instanceIDsPerProblems = instanceIDsPerProblems;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;

    this.experimentReporter = new ExperimentReporter();
  }

  /**
   * Method runs experiment for possibly many problems with many problem instances.
   */
  public void runExperiment() throws Exception {
    // Create experiment reporter
    this.experimentReporter.createExperimentReport(
        this.experimentID,
        this.algorithmID,
        this.instanceIDsPerProblems.keySet().toArray(new String[0]),
        getProblemInstancesArray(),
        new String[] {this.algorithmID},
        getExperimentConfig(),
        Date.from(Instant.now())
    ); 
    
    logger.info("-------------------------------------");
    logger.info("Experimenter: {}", this.algorithmID);
    logger.info("ExperimentID: {}", experimentID);
    logger.info("-------------------------------------");
    
    // Run experiments
    logger.info("Algorithm '{}'", algorithmID);

    long startDate;
    startDate = System.currentTimeMillis();

    // Initialize array for problems scores
    List<Double> problemsScores = new ArrayList<>();

    ExperimentScoreCard scoreCard = new ExperimentScoreCard(
        this.algorithmID, this.instanceIDsPerProblems.keySet().toArray(new String[]{}));

    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();
      logger.info("  Problem '{}'", problemID);

      logger.debug("{}", ProblemProvider.getProblemProviders().values());

      ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(problemID)
          .getProblemInfo();
      
      ProblemScoreCalculator problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
      
      List<String> instanceIDs = new ArrayList<>();
      List<Double> instanceScores = new ArrayList<>();

      for (String instanceID : entry.getValue()) {
        logger.info("    Instance '{}'", instanceID);

        // RUN EXPERIMENT
        Experiment experiment = createExperimenter(problemID, instanceID);
        double score = experiment.run();
        // --- ----------

        scoreCard.putInstanceScore(problemID, instanceID, score);
  
        instanceIDs.add(instanceID);
        instanceScores.add(score);
      }
      
      double problemScore = problemScoreCalculator.calculateProblemScore(
          instanceIDs.toArray(new String[]{}), 
          instanceScores.stream().mapToDouble(a -> a).toArray());

      scoreCard.putProblemScore(problemID, problemScore);
      problemsScores.add(problemScore);
    }

    double experimentScore = ProblemScoreCalculator.calculateExperimentScore(problemsScores);
    scoreCard.setTotalScore(experimentScore);

    this.experimentReporter.updateExperimentScore(experimentID, scoreCard);
   
    long endDate = System.currentTimeMillis();
    logger.info("-------------------------------------");
    logger.info("Experiment {} finished ...", experimentID);    
    logger.info("Experiment duration: {} (DD:HH:mm:ss)", 
        TimeFormat.getTimeDurationBreakdown(endDate - startDate));
    logger.info("Experiment score: ### {} ###", scoreCard.getTotalScore());
        
    this.experimentReporter.updateEndDate(this.experimentID, new Date(endDate));
  }


  private Experiment createExperimenter(String problemID, String instanceID) 
      throws Exception {
    boolean ordinaryAlg = ProblemProvider
        .getProblemProviders()
        .get(problemID)
        .getProblemInfo()
        .getDataNode("Algorithms")
        .getDataNodeById(algorithmID) != null;
    
    if (ordinaryAlg) {
      return new MetaHeuristicExperiment(
        experimentID, problemID, instanceID, 
        algorithmID, numRuns, timeoutS, this.experimentReporter);
    }

    if (algorithmID.equals("HyperHeuristic1")) {
      return new HyperHeuristic1Experiment(
        experimentID, problemID, instanceID, 
        algorithmID, numRuns, timeoutS, this.experimentReporter
      );
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
    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();
      
      for (String instanceID : entry.getValue()) {
        results.add(String.format("%s:%s", problemID, instanceID));
      }
    }
    return results.toArray(new String[0]);
  }


}
