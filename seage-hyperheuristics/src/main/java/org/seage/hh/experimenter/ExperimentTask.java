package org.seage.hh.experimenter;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs experiment task and provides following experiment log:
 * <p/>
 * ExperimentTask # version 0.1 |_ ...
 * <p/>
 * ExperimentTaskReport # version 0.2 |_ version (0.4) |_ experimentID |_ startTimeMS |_ timeoutS |_
 * durationS |_ machineName |_ nrOfCores |_ totalRAM |_ availRAM |_ Config | |_ configID | |_ runID
 * | |_ Problem | | |_ problemID | | |_ Instance | | |_ name | |_ Algorithm | |_ algorithmID | |_
 * Parameters |_ AlgorithmReport |_ Parameters |_ Statistics |_ Minutes
 * 
 * @author Richard Malek
 */
public class ExperimentTask {

  protected static Logger _logger = LoggerFactory.getLogger(ExperimentTask.class.getName());

  protected UUID experimentTaskID;
  protected UUID experimentID;
  protected int jobID;
  protected int stageID;
  protected String experimentType;
  protected String problemID;
  protected String instanceID;
  protected String algorithmID;
  protected String configID;
  protected Date startDate;
  protected Date endDate;
  protected Double score;

  protected AlgorithmParams algorithmParams;
  protected long timeoutS;

  protected DataNode experimentTaskReport;

  /**
   * Constructor for DB mapper.
   */
  ExperimentTask() {}

  /**
   * ExperimentTask for running algorithm.
   * @param taskInfo .
   * @throws Exception .
   */
  public ExperimentTask(ExperimentTaskRequest taskInfo) throws Exception {
    this(
        taskInfo.getExperimentTaskID(),
        taskInfo.getExperimentID(),
        taskInfo.getJobID(),
        taskInfo.getStageID(),
        taskInfo.getProblemID(),
        taskInfo.getInstanceID(),
        taskInfo.getAlgorithmID(),
        taskInfo.getAlgorithmParams(),
        taskInfo.getTimeoutS()
    );
  }

  /**
   * ExperimentTask for running algorithm.
   */
  private ExperimentTask(UUID experimentTaskID, 
      UUID experimentID, int jobID, int stageID, String problemID,
      String instanceID, String algorithmID, AlgorithmParams algorithmParams, long timeoutS)
      throws Exception {
    this.experimentTaskID = experimentTaskID;
    this.experimentID = experimentID;
    this.jobID = jobID;
    this.stageID = stageID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.configID = algorithmParams.hash();
    this.startDate = new Date();
    this.endDate = this.startDate;
    this.score = Double.MAX_VALUE;

    this.algorithmParams = algorithmParams;
    this.timeoutS = timeoutS;

    // _reportName = reportName;
    // _reportOutputStream = reportOutputStream;

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
   */
  public void run() {
    _logger.debug("ExperimentTask started ({})", this.configID);
    this.startDate = new Date();
    try {
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

      Phenotype<?>[] solutions = provider.generateInitialSolutions(instance,
          this.algorithmParams.getValueInt("numSolutions"), this.experimentID.hashCode());
      writeSolutions(evaluator,
          this.experimentTaskReport.getDataNode("Solutions").getDataNode("Input"), solutions);

      algorithm.solutionsFromPhenotype(solutions);
      algorithm.startSearching(this.algorithmParams, true);
      _logger.debug("Algorithm started");
      waitForTimeout(algorithm);
      algorithm.stopSearching();
      _logger.debug("Algorithm stopped");

      solutions = algorithm.solutionsToPhenotype();
      writeSolutions(evaluator,
          this.experimentTaskReport.getDataNode("Solutions").getDataNode("Output"), solutions);

      this.endDate = new Date();
      long durationS = (this.endDate.getTime() - this.startDate.getTime()) / 1000;

      this.experimentTaskReport.putDataNode(algorithm.getReport());
      this.experimentTaskReport.putValue("durationS", durationS);
      _logger.debug("Algorithm run duration: {}", durationS);

    } catch (Exception ex) {
      _logger.error(ex.getMessage(), ex);
      _logger.error(this.algorithmParams.toString());

    }
    _logger.debug("ExperimentTask finished ({})", this.configID);
  }

  private void waitForTimeout(IAlgorithmAdapter<?, ?> alg) throws Exception {
    long time = System.currentTimeMillis();
    while (alg.isRunning() && ((System.currentTimeMillis() - time) < this.timeoutS * 1000)) {
      Thread.sleep(100);
    }
  }

  private void writeSolutions(IPhenotypeEvaluator<Phenotype<?>> evaluator, DataNode dataNode,
      Phenotype<?>[] solutions) {
    for (Phenotype<?> p : solutions) {
      try {
        DataNode solutionNode = new DataNode("Solution");
        solutionNode.putValue("objVal", p.getObjValue());
        solutionNode.putValue("score", p.getScore());
        solutionNode.putValue("solution", p.toText());
        solutionNode.putValue("hash", p.computeHash());
        dataNode.putDataNode(solutionNode);

        if (this.score > p.getObjValue()) {
          this.score = p.getObjValue();
        }
      } catch (Exception ex) {
        _logger.error("Cannot write solution", ex);
      }
    }
  }

  ////////////////////
  public String getConfig() {
    return this.algorithmParams.toString();
  }

  public void setConfig(String config) {
    // Must be here because of the db reading - Not defined for now
  }

  /**
   * Method returns experiment task report statistics.
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
