package org.seage.hh.experimenter.dbo;

import java.util.Date;
import java.util.UUID;

public class SolutionRecord {
  private UUID solutionID;
 
  private UUID experimentTaskID;
  private String hash;
  private String solutionStr;
  private Double objectiveValue;
  private Double score;
  private Long iterationNumber;
  private Date date;

  /**
   * Solution class mainly for storing to database.
   * This must be here because of mybatis
   */
  SolutionRecord() {}

  /**
   * Solution class mainly for storing to database.
   */
  public SolutionRecord(
      UUID solutionID,
      UUID experimentTaskID, String hash, 
      String solution, Double objectiveValue, Double score,
      Long iterationNumber,
      Date date) {
    this.solutionID = solutionID;
    this.experimentTaskID = experimentTaskID;
    this.hash = hash;
    this.solutionStr = solution;
    this.objectiveValue = objectiveValue;
    this.score = score;
    this.iterationNumber = iterationNumber;
    this.date = date;
  }

  public UUID getSolutionID() {
    return solutionID;
  }

  public void setSolutionID(UUID solutionID) {
    this.solutionID = solutionID;
  }

  public UUID getExperimentTaskID() {
    return experimentTaskID;
  }

  public void setExperimentTaskID(UUID experimentTaskID) {
    this.experimentTaskID = experimentTaskID;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getSolution() {
    return solutionStr;
  }

  public void setSolution(String solution) {
    this.solutionStr = solution;
  }

  public Double getObjectiveValue() {
    return objectiveValue;
  }

  public void setObjectiveValue(Double objectiveValue) {
    this.objectiveValue = objectiveValue;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Long getIterationNumber() {
    return iterationNumber;
  }

  public void setIterationNumber(Long iterationNumber) {
    this.iterationNumber = iterationNumber;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
  
}
