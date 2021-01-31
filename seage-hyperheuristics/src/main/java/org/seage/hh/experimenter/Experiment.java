package org.seage.hh.experimenter;

import java.util.Date;
import java.util.UUID;

public class Experiment {
  private UUID experimentID;
  private String experimentType;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private Date startDate; 
  private Date endDate;
  private String config;
  private String hostname;
  private Double score;

  /**
   * Experiment class mainly for storing to database.
   * This must be here because of mybatis
   */
  Experiment() {}

  /**
   * Experiment class mainly for storing to database.
   */
  public Experiment(
      UUID experimentID, String experimentType, 
      String problemID, String instanceID,
      String algorithmID, String config, 
      Date startDate, Date endDate, 
      String hostname, Double score) {
    this.experimentID = experimentID;
    this.experimentType = experimentType;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.config = config;
    this.startDate = startDate;
    this.endDate = endDate;
    this.hostname = hostname;
    this.score = score;
  }

  public UUID getExperimentID() {
    return experimentID;
  }

  public void setExperimentID(UUID experimentID) {
    this.experimentID = experimentID;
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

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }


}
