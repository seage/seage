package org.seage.hh.knowledgebase.db.dbo;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs experiment task and provides following experiment log.
 * ---
 * ExperimentTask
 * ---
 * ExperimentTaskReport 
 * |_ experimentID 
 * |_ startTimeMS 
 * |_ timeoutS 
 * |_ durationS 
 * |_ machineName 
 * |_ nrOfCores 
 * |_ totalRAM 
 * |_ availRAM 
 * |_ Config 
 * | |_ configID | |_ runID
 * | |_ Problem 
 * | | |_ problemID 
 * | | |_ Instance 
 * | | |_ name 
 * | |_ Algorithm 
 * | | |_ algorithmID 
 * | |_ Parameters 
 * |_ AlgorithmReport 
 * |_ Parameters 
 * |_ Statistics 
 * |_ Minutes
 * 
 * @author Richard Malek
 */
public class ExperimentTaskRecord {

  private static Logger _logger = LoggerFactory.getLogger(ExperimentTaskRecord.class.getName());

  private Phenotype<?>[] solutions;
  private UUID experimentTaskID;
  private UUID experimentID;
  private int jobID;
  private int stageID;
  private String experimentType;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private String configID;
  private String config;
  private Date startDate;
  private Date endDate;
  private Double score;
  private Double scoreDelta;

  private AlgorithmParams algorithmParams;
  private long timeoutS;

  private DataNode experimentTaskReport;
  private boolean taskFinished;


  /**
   * Constructor for DB mapper.
   */
  ExperimentTaskRecord() {
    // Empty constructor
  }

  /**
   * ExperimentTask for running algorithm.
   * 
   * @param taskInfo .
   * @throws Exception .
   */
  public ExperimentTaskRecord(ExperimentTaskRequest taskInfo) throws Exception {
    this(
        taskInfo.getExperimentTaskID(),
        taskInfo.getExperimentID(),
        taskInfo.getJobID(),
        taskInfo.getStageID(),
        taskInfo.getAlgorithmParams(),
        taskInfo.getProblemID(),
        taskInfo.getInstanceID(),
        taskInfo.getAlgorithmID(),
        taskInfo.getSolutions(),
        taskInfo.getTimeoutS()
    );
  }

  /**
   * ExperimentTask for running algorithm.
   */
  private ExperimentTaskRecord(
      UUID experimentTaskID, 
      UUID experimentID, 
      int jobID, 
      int stageID, 
      AlgorithmParams algorithmParams,
      String problemID,
      String instanceID, 
      String algorithmID,
      Phenotype<?>[] solutions,
      long timeoutS)
      throws Exception {
    this.experimentTaskID = experimentTaskID;
    this.experimentID = experimentID;
    this.jobID = jobID;
    this.stageID = stageID;
    this.algorithmID = algorithmID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.solutions = solutions;

    this.configID = algorithmParams.hash();
    this.startDate = new Date();
    this.endDate = this.startDate;
    this.score = Double.MAX_VALUE;
    this.scoreDelta = 0.0;

    this.algorithmParams = algorithmParams;
    this.timeoutS = timeoutS;

    this.taskFinished = false;

    // TODO: Remove DataNode usage completely
    this.experimentTaskReport = new DataNode("ExperimentTaskReport");
    this.experimentTaskReport.putValue("version", "0.7");
    this.experimentTaskReport.putValue("experimentType", experimentType);
    this.experimentTaskReport.putValue("experimentID", experimentID);
    this.experimentTaskReport.putValue("timeoutS", timeoutS);

    DataNode configNode = new DataNode("Config");
    configNode.putValue("configID", this.configID);
    // configNode.putValue("runID", this.runID);

    DataNode problemNode = new DataNode("Problem");
    problemNode.putValue("problemID", this.problemID);

    DataNode instanceNode = new DataNode("Instance");
    instanceNode.putValue("name", this.instanceID);

    DataNode algorithmNode = new DataNode("Algorithm");
    algorithmNode.putValue("algorithmID", this.algorithmID);
    algorithmNode.putDataNode(this.algorithmParams);

    problemNode.putDataNode(instanceNode);
    configNode.putDataNode(problemNode);
    configNode.putDataNode(algorithmNode);

    this.experimentTaskReport.putDataNode(configNode);

    DataNode solutionsNode = new DataNode("Solutions");
    solutionsNode.putDataNode(new DataNode("Input"));
    solutionsNode.putDataNode(new DataNode("Output"));
    this.experimentTaskReport.putDataNode(solutionsNode);
  }

  public String getConfigID() {
    return this.configID;
  }

  public DataNode getExperimentTaskReport() {
    return this.experimentTaskReport;
  }

  /**
   * Method runs an experiment task.
   * 
   * @throws Exception .
   */
  public void run() throws Exception {
    if (taskFinished) {
      throw new IllegalStateException("Experiment task has been already finished");
    }

    _logger.debug("ExperimentTask started ({})", this.configID);
    this.startDate = new Date();

    try {
      this.experimentTaskReport.putValue("machineName",
          java.net.InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e) {
      _logger.warn(e.getMessage());
    }
    this.experimentTaskReport.putValue("nrOfCores", Runtime.getRuntime().availableProcessors());
    this.experimentTaskReport.putValue("totalRAM", Runtime.getRuntime().totalMemory());
    this.experimentTaskReport.putValue("availRAM", Runtime.getRuntime().maxMemory());

    // provider and factory
    IProblemProvider<Phenotype<?>> provider =
        ProblemProvider.getProblemProviders().get(this.problemID);
    IAlgorithmFactory<Phenotype<?>, ?> factory = provider.getAlgorithmFactory(this.algorithmID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(this.instanceID));

    IPhenotypeEvaluator<Phenotype<?>> evaluator = provider.initPhenotypeEvaluator(instance);

    // algorithm
    IAlgorithmAdapter<Phenotype<?>, ?> algorithm = factory.createAlgorithm(instance, evaluator);

    if (solutions == null) {
      int numSolutions = this.algorithmParams.getValueInt("numSolutions");
      long randomSeed = this.experimentID.hashCode();
      solutions = generateInitialSolutions(provider, instance, numSolutions, randomSeed);
    }

    writeSolutions(evaluator,
        this.experimentTaskReport.getDataNode("Solutions").getDataNode("Input"), solutions);

    _logger.debug("Solutions from phenotype");
    algorithm.solutionsFromPhenotype(solutions);
    _logger.debug("Starting the algorithm");
    algorithm.startSearching(this.algorithmParams, true);
    _logger.debug("Algorithm started");
    waitForTimeout(algorithm);
    _logger.debug("Stopping the algorithm");
    algorithm.stopSearching();
    _logger.debug("Algorithm stopped");

    _logger.debug("Solutions to phenotype");
    solutions = algorithm.solutionsToPhenotype();

    writeSolutions(evaluator,
        this.experimentTaskReport.getDataNode("Solutions").getDataNode("Output"), solutions);

    calculateExperimentScore();

    this.endDate = new Date();
    long durationS = (this.endDate.getTime() - this.startDate.getTime()) / 1000;

    this.experimentTaskReport.putDataNode(algorithm.getReport());
    this.experimentTaskReport.putValue("durationS", durationS);

    this.taskFinished = true;

    _logger.debug("Algorithm run duration: {}", durationS);
    _logger.debug("ExperimentTask finished ({})", this.configID);
  }

  private void waitForTimeout(IAlgorithmAdapter<?, ?> alg) throws Exception {
    _logger.debug("Waiting for timeout");
    long time = System.currentTimeMillis();
    while (alg.isRunning() && ((System.currentTimeMillis() - time) < this.timeoutS * 1000)) {
      Thread.sleep(250);
    }
    double timeS = (System.currentTimeMillis() - time) / 1000.0;
    _logger.debug("Timeout occurred after {}s", timeS);
  }

  private static Phenotype<?>[] generateInitialSolutions(
      IProblemProvider<Phenotype<?>> provider, ProblemInstance instance,
      int numSolutions, long randomSeed) throws Exception {
    _logger.debug("Generating initial solutions: {}", numSolutions);
    return provider.generateInitialSolutions(instance, numSolutions, randomSeed);
  }

  private void writeSolutions(IPhenotypeEvaluator<Phenotype<?>> evaluator, DataNode dataNode,
      Phenotype<?>[] solutions) {
    _logger.debug("Writing solutions into data node: {}", solutions.length);
    double bestScore = 0;
    Phenotype<?> bestSol = null;
    try {
      for (Phenotype<?> p : solutions) {
        double curScore = p.getScore();
        if (curScore > bestScore) {
          bestScore = curScore;
          bestSol = p;
        }
      }
      if (bestSol != null && bestScore > 0) {
        DataNode solutionNode = new DataNode("Solution");
        solutionNode.putValue("objVal", bestSol.getObjValue());
        solutionNode.putValue("score", bestSol.getScore());
        solutionNode.putValue("solution", bestSol.toText());
        solutionNode.putValue("hash", bestSol.computeHash());
        dataNode.putDataNode(solutionNode);
      }
    } catch (Exception ex) {
      _logger.error("Cannot write solution", ex);
    }
  }

  private void calculateExperimentScore() throws Exception {
    _logger.debug("Calculating experiment score");
    ProblemInfo problemInfo = ProblemProvider
        .getProblemProviders()
        .get(problemID)
        .getProblemInfo();
    ProblemScoreCalculator scoreCalculator = new ProblemScoreCalculator(problemInfo); 

    DataNode inputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Input");
    DataNode outputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Output");

    double bestObjValue = getBestObjectiveValue(outputs);
    double taskLatestScore = 
        scoreCalculator.calculateInstanceScore(instanceID, bestObjValue);

    double initObjValue = getBestObjectiveValue(inputs);
    double taskInitScore = 
        scoreCalculator.calculateInstanceScore(instanceID, initObjValue);

    this.score = taskLatestScore;
    // Delta score represents how better or worse the new score is (compared to initial one)
    this.scoreDelta = scoreCalculator.calculateScoreDelta(taskInitScore, taskLatestScore);
  }

  private double getBestObjectiveValue(DataNode solutions) throws Exception {
    double bestObjVal = Double.MAX_VALUE;
    for (DataNode s : solutions.getDataNodes("Solution")) {
      double objVal = s.getValueDouble("objVal");

      if (objVal < bestObjVal) {
        bestObjVal = objVal;
      }
    }
    return bestObjVal;
  }

  ////////////////////
  public String getConfig() {
    return this.algorithmParams.toString();
  }

  // public void setConfig(String config) {
  // // Must be here because of the db reading - Not defined for now
  // }

  /**
   * Method returns experiment task report statistics.
   * 
   * @return String with statistics info.
   */
  public String getStatistics() {
    try {
      return this.getExperimentTaskReport()
          .getDataNode("AlgorithmReport")
          .getDataNode("Statistics")
          .toString();
    } catch (Exception e) {
      return null;
    }
  }

  public void setStatistics(String statistics) {
    // Must be here because of the db reading - Not defined for now
  }
  ////////////////////

  public UUID getExperimentTaskID() {
    return experimentTaskID;
  }

  public void setExperimentTaskID(UUID experimentTaskID) {
    this.experimentTaskID = experimentTaskID;
  }

  public UUID getExperimentID() {
    return experimentID;
  }

  public void setExperimentID(UUID experimentID) {
    this.experimentID = experimentID;
  }

  public int getJobID() {
    return jobID;
  }

  public void setJobID(int jobID) {
    this.jobID = jobID;
  }

  public int getStageID() {
    return stageID;
  }

  public void setStageID(int stageID) {
    this.stageID = stageID;
  }

  public String getExperimentType() {
    return experimentType;
  }

  public void setExperimentType(String experimentType) {
    this.experimentType = experimentType;
  }

  public String getProblemID() {
    return problemID;
  }

  public void setProblemID(String problemID) {
    this.problemID = problemID;
  }

  public String getInstanceID() {
    return instanceID;
  }

  public void setInstanceID(String instanceID) {
    this.instanceID = instanceID;
  }

  public void setXmlConfig(String config) {
    this.config = config;
  }

  public String getXmlConfig() {
    return this.config;
  }

  public String getAlgorithmID() {
    return algorithmID;
  }

  public void setAlgorithmID(String algorithmID) {
    this.algorithmID = algorithmID;
  }

  public void setConfigID(String configID) {
    this.configID = configID;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Double getScoreDelta() {
    return scoreDelta;
  }

  public void setScoreDelta(Double scoreDelta) {
    this.scoreDelta = scoreDelta;
  }

  public long getTimeoutS() {
    return timeoutS;
  }

  public void setTimeoutS(long timeoutS) {
    this.timeoutS = timeoutS;
  }

  public void setExperimentTaskReport(DataNode experimentTaskReport) {
    this.experimentTaskReport = experimentTaskReport;
  }

}
