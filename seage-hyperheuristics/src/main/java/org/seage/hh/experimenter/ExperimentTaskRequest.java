package org.seage.hh.experimenter;

import java.util.UUID;
import org.seage.aal.algorithm.AlgorithmParams;

public class ExperimentTaskRequest {
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

  /**
   * Class constructor.
   * @param experimentTaskID Task experiment id.
   * @param experimentID Experiment id.
   * @param jobID Job id.
   * @param stageID Stage id.
   * @param problemID Problem id.
   * @param instanceID Instance id.
   * @param algorithmID Algorithm id. 
   * @param algorithmParams Algorithm params.
   * @param timeoutS Timeout.
   */
  public ExperimentTaskRequest(UUID experimentTaskID, UUID experimentID, int jobID, int stageID,
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
