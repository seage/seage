package org.seage.hh.knowledgebase.db;

import java.util.Date;
import java.util.UUID;

public class Experiment {
  private int id;
 
  private String experimentID;
  private String experimentName;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private Date startDate; 
  private Date endDate;
  private String config;
  private String hostname;
  private Double score;
  
  public Experiment() {
    this.id = 0;
    this.startDate = new Date();
    this.endDate = new Date();

    this.experimentID = UUID.randomUUID().toString();
    this.experimentName = "SomeExperimentType";
    this.problemID = "SomeProblem";
    this.instanceID = "SomeInstance";
    this.algorithmID = "SomeAlgorithm";
    this.hostname = "SomeHostname";
    this.config = "{a: 1}";
  }

  public Experiment(
      String experimentID, String experimentName, 
      String problemID, String instanceID,
      String algorithmID, String config, 
      Date startDate, Date duration, 
      String hostname, Double score) {
    this.experimentID = experimentID;
    this.experimentName = experimentName;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.config = config;
    this.startDate = startDate;
    this.endDate = duration;
    this.hostname = hostname;
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getExperimentID() {
    return experimentID;
  }

  public void setExperimentID(String experimentID) {
    this.experimentID = experimentID;
  }

  public String getExperimentName() {
    return experimentName;
  }

  public void setExperimentName(String experimentName) {
    this.experimentName = experimentName;
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
