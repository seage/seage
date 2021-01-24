package org.seage.hh.experimenter;

import java.io.Serializable;
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
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs experiment task and provides following experiment log:
 * 
 * ExperimentTask # version 0.1 |_ ...
 * 
 * ExperimentTaskReport # version 0.2 |_ version (0.4) |_ experimentID |_ startTimeMS |_ timeoutS |_
 * durationS |_ machineName |_ nrOfCores |_ totalRAM |_ availRAM |_ Config | |_ configID | |_ runID
 * | |_ Problem | | |_ problemID | | |_ Instance | | |_ name | |_ Algorithm | |_ algorithmID | |_
 * Parameters |_ AlgorithmReport |_ Parameters |_ Statistics |_ Minutes
 * 
 * @author Richard Malek
 */
public class ExperimentTask implements Runnable, Serializable {
  private static final long serialVersionUID = -1342525824503090535L;

  protected static Logger _logger = LoggerFactory.getLogger(ExperimentTask.class.getName());

  protected int id;
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

  ExperimentTask() {
  }

  /**
   * ExperimentTask for running algorithm.
   */
  public ExperimentTask(UUID experimentID, int jobID, int stageID, String problemID,
      String instanceID, String algorithmID, AlgorithmParams algorithmParams, long timeoutS)
      throws Exception {
    this.experimentTaskID = UUID.randomUUID();
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

  // public String getReportName() throws Exception {
  // return this.configID + "-" + this.runID + ".xml";
  // }

  public String getConfigID() {
    return this.configID;
  }

  public DataNode getExperimentTaskReport() {
    return this.experimentTaskReport;
  }

  @Override
  public void run() {
    _logger.info("ExperimentTask started ({})", this.configID);
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
      ProblemInfo pi = provider.getProblemInfo();
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

      long startTime = System.currentTimeMillis();
      algorithm.solutionsFromPhenotype(solutions);
      algorithm.startSearching(this.algorithmParams, true);
      _logger.info("Algorithm started");
      waitForTimeout(algorithm);
      algorithm.stopSearching();
      _logger.info("Algorithm started");
      long endTime = System.currentTimeMillis();

      solutions = algorithm.solutionsToPhenotype();
      writeSolutions(evaluator,
          this.experimentTaskReport.getDataNode("Solutions").getDataNode("Output"), solutions);

      this.experimentTaskReport.putDataNode(algorithm.getReport());
      this.experimentTaskReport.putValue("durationS", (endTime - startTime) / 1000);
      _logger.info("Algorithm run duration: {}", this.experimentTaskReport.getValue("durationS"));
      // XmlHelper.writeXml(this.experimentTaskReport, _reportOutputStream,
      // getReportName());

    } catch (Exception ex) {
      _logger.error(ex.getMessage(), ex);
      _logger.error(this.algorithmParams.toString());

    }
    _logger.info("ExperimentTask finished ({})", this.configID);
  }

  private void waitForTimeout(IAlgorithmAdapter<?, ?> alg) throws Exception {
    long time = System.currentTimeMillis();
    while (alg.isRunning() && ((System.currentTimeMillis() - time) < this.timeoutS * 1000))
      Thread.sleep(100);
  }

  private void writeSolutions(IPhenotypeEvaluator<Phenotype<?>> evaluator, DataNode dataNode,
      Phenotype<?>[] solutions) {
    for (Phenotype<?> p : solutions) {
      try {
        DataNode solutionNode = new DataNode("Solution");
        double objValue = evaluator.evaluate(p)[0];        
        solutionNode.putValue("objValue", objValue);
        solutionNode.putValue("solution", p.toText());
        solutionNode.putValue("hash", p.computeHash());
        dataNode.putDataNode(solutionNode);

        if (this.score > objValue) {
          this.score = objValue;
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
  ////////////////////

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

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
