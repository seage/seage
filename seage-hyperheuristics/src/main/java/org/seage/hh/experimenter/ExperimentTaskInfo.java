package org.seage.hh.experimenter;

import java.util.UUID;
import org.seage.aal.algorithm.AlgorithmParams;

public class ExperimentTaskInfo {
  private UUID experimentTaskID;
  private UUID experimentID;
  private int jobID;
  private int stageID;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private String configID;
  private AlgorithmParams algorithmParams;
  private long timeoutS;

  public ExperimentTaskInfo(UUID experimentTaskID, UUID experimentID, int jobID, int stageID,
      String problemID, String instanceID, String algorithmID, AlgorithmParams algorithmParams,
      long timeoutS) throws Exception {
    this.experimentTaskID = experimentTaskID;
    this.experimentID = experimentID;
    this.jobID = jobID;
    this.stageID = stageID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.configID = algorithmParams.hash();
    this.algorithmParams = algorithmParams;
    this.timeoutS = timeoutS;

  }

  public UUID getExperimentTaskID() {
    return experimentTaskID;
  }

  public UUID getExperimentID() {
    return experimentID;
  }

  public int getJobID() {
    return jobID;
  }

  public int getStageID() {
    return stageID;
  }

  public String getProblemID() {
    return problemID;
  }

  public String getInstanceID() {
    return instanceID;
  }

  public String getAlgorithmID() {
    return algorithmID;
  }

  public String getConfigID() {
    return configID;
  }

  public AlgorithmParams getAlgorithmParams() {
    return algorithmParams;
  }

  public long getTimeoutS() {
    return timeoutS;
  }

}
