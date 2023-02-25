package org.seage.hh.knowledgebase.db.dbo;

import java.util.Date;
import java.util.UUID;

public class ExperimentRecord {
  private UUID experimentID;
  private String experimentType;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private Date startDate; 
  private Date endDate;
  private String config;  
  private Double score;
  private String scoreCard;
  private String hostInfo;
  private String formatVersion;
  private String tag;

  /**
   * Experiment class mainly for storing to database.
   * This must be here because of mybatis
   */
  ExperimentRecord() {}

  /**
   * Experiment class mainly for storing to database.
   */
  public ExperimentRecord(
      UUID experimentID, String experimentType, 
      String problemID, String instanceID,
      String algorithmID, String config, 
      Date startDate, Date endDate, 
      Double score, String hostInfo, 
      String formatVersion, String tag) {
    this.experimentID = experimentID;
    this.experimentType = experimentType;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.config = config;
    this.startDate = startDate;
    this.endDate = endDate;    
    this.score = score;
    this.hostInfo = hostInfo;
    this.formatVersion = formatVersion;
    this.tag = tag;
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

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public String getScoreCard() {
    return scoreCard;
  }

  public void setScoreCard(String scoreCard) {
    this.scoreCard = scoreCard;
  }

  public String getHostInfo() {
    return hostInfo;
  }

  public void setHostInfo(String hostInfo) {
    this.hostInfo = hostInfo;
  }

  public String getFormatVersion() {
    return formatVersion;
  }

  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}
