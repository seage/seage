package org.seage.hh.knowledgebase.db;

import java.util.Date;

public class Experiment {
  private int id;
  private Date startDate;  
  private String experimentID;
  private String experimentName;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private String hostname;
  private String config;
  private Date duration;
  
  public Experiment() {
    this.id = 0;
    this.startDate = new Date();
    this.duration = new Date();

    this.experimentName = "SomeExperimentType";
    this.hostname = "SomeHostname";
    this.config = "{a: 1}";
  }

  public Experiment(
      String experimentID, String experimentName, 
      String problemID, String instanceID,
      String algorithmID, String config, 
      Date startDate, Date duration, String hostname) {
    this.experimentID = experimentID;
    this.experimentName = experimentName;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.startDate = startDate;
    this.duration = duration;
    this.hostname = hostname;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
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

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public Date getDuration() {
    return duration;
  }

  public void setDuration(Date duration) {
    this.duration = duration;
  }
}
