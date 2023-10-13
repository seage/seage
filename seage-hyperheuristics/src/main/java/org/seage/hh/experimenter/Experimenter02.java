package org.seage.hh.experimenter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.singlealgorithm.evolution.SingleAlgorithmConfigsEvolutionExperiment;
import org.seage.logging.TimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experimenter for experiment using multiple instances at once.
 * 
 * @author David Omrai
 */
public class Experimenter02 {
  protected static Logger logger = LoggerFactory.getLogger(Experimenter02.class.getName());

  protected ExperimentReporter experimentReporter;

  protected UUID experimentID;
  protected String algorithmID;
  protected Map<String, List<String>> instanceIDsPerProblems;
  protected int numRuns;
  protected int timeoutS;

  protected double spread;
  protected int granularity;
  protected int numOfIterations;

  protected String tag;

  /**
   * ApproachExperimenter.
   * 
   * @param algorithmID Algorithm ID.
   * @param instanceIDsPerProblems Map of problem instances.
   * @throws Exception
   */
  public Experimenter02(String algorithmID, Map<String, List<String>> instanceIDsPerProblems,
      int numRuns, int timeoutS) throws Exception {
    this(algorithmID, instanceIDsPerProblems, numRuns, timeoutS, null);
  }

  /**
   * ApproachExperimenter.
   * 
   * @param algorithmID Algorithm ID.
   * @param instanceIDsPerProblems Map of problem instances.
   */
  public Experimenter02(String algorithmID, Map<String, List<String>> instanceIDsPerProblems,
      int numRuns, int timeoutS, String tag) throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.instanceIDsPerProblems = instanceIDsPerProblems;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.tag = tag;
  }

  /**
   * Method sets the spread variable.
   * 
   * @param newSpread New spread value.
   * @return
   */
  public Experimenter02 setSpread(double newSpread) {
    spread = newSpread;
    return this;
  }


  /**
   * Method sets the granularity variable.
   * 
   * @param newGranularity New granularity value.
   * @return
   */
  public Experimenter02 setGranularity(int newGranularity) {
    granularity = newGranularity;
    return this;
  }

  public Experimenter02 setNumOfIterations(int newNumOfIterations) {
    numOfIterations = newNumOfIterations;
    return this;
  }

  /**
   * Default method without defined experiment.
   */
  public void runExperiment() throws Exception {
    runExperiment("Experimenter2");
  }


  /**
   * Method runs experiment for possibly many problems with many problem instances.
   */
  public void runExperiment(String experimentName) throws Exception {
    // Create experiment reporter
    experimentReporter.createExperimentReport(
        experimentID,
        experimentName,
        instanceIDsPerProblems.keySet().toArray(new String[0]),
        getProblemInstancesArray(),
        new String[] {algorithmID},
        getExperimentConfig(),
        Date.from(Instant.now()),
        tag
    ); 
    
    logger.info("---------------------------------------------------");
    logger.info("AlgorithmID:   ### {} ###", algorithmID);
    logger.info("---------------------------------------------------");
    logger.info("ExperimentTag: {}", this.tag);
    logger.info("ExperimentID:  {}", experimentID);
    logger.info("---------------------------------------------------");

    // Run experiments

    long startDate;
    startDate = System.currentTimeMillis();

    // Initialize array for problems scores
    List<Double> problemsScores = new ArrayList<>();

    // ExperimentScoreCard scoreCard = new ExperimentScoreCard(algorithmID,
    //     instanceIDsPerProblems.keySet().toArray(new String[] {}));

    // for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
    //   String problemID = entry.getKey();
    //   logger.info("Problem '{}'", problemID);

    //   logger.debug("{}", ProblemProvider.getProblemProviders().values());  
      
    //   logger.info("  Instance '{}'", entry.getValue().toString());
    //   Experiment experiment = createExperiment(experimentName, problemID, entry.getValue());
    //   double score = experiment.run();
    //   // --- ----------
    //   scoreCard.putProblemScore(problemID, score);
    //   problemsScores.add(score);
    // }
    

    // double experimentScore = ProblemScoreCalculator.calculateExperimentScore(problemsScores);
    // scoreCard.setAlgorithmScore(experimentScore);

    // experimentReporter.updateExperimentScore(experimentID, scoreCard);

    long endDate = System.currentTimeMillis();
    logger.info("---------------------------------------------------");
    logger.info("Experiment {} done", experimentID);
    logger.info("Experiment duration: {} (DD:HH:mm:ss)",
        TimeFormat.getTimeDurationBreakdown(endDate - startDate));
    // logger.info("Experiment score: ### {} ###", scoreCard.getAlgorithmScore());

    experimentReporter.updateEndDate(experimentID, new Date(endDate));
  }

  private Experiment createExperiment(
      String experimentName, String problemID, List<String> instanceIDs) throws Exception {
    // if (experimentName.equals("SingleAlgorithmEvolution")) {
    //   return new SingleAlgorithmConfigsEvolutionExperiment(experimentID, problemID, algorithmID,
    //       instanceIDs, numRuns, numOfIterations, timeoutS, tag);
    // }

    throw new Exception(String.format("Unknown algorithm id '%s'", algorithmID));
  }

  protected String getExperimentConfig() {
    DataNode config = new DataNode("Config");
    config.putValue("timeoutS", timeoutS);
    config.putValue("numRuns", numRuns);

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
