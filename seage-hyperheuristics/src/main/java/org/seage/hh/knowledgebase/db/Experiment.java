package org.seage.hh.knowledgebase.db;

import java.util.Date;

public class Experiment {
  private int id;
  private Date startDate;
  private Date duration;
  private String experimentType;
  private String hostname;
  private String config;
  
  public Experiment() {
    this.id = 0;
    this.startDate = new Date();
    this.duration = new Date();
    this.experimentType = "SomeExperimentType";
    this.hostname = "SomeHostname";
    this.config = "{a: 1}";
  }

  public Experiment(String experimentID, String experimentName, String problemID, String instances,
      String algorithms, Date startDate2) {
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setDuration(Date duration) {
    this.duration = duration;
  }

  public void setExperimentType(String experimentType) {
    this.experimentType = experimentType;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public int getId() {
    return id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getDuration() {
    return duration;
  }

  public String getExperimentType() {
    return experimentType;
  }

  public String getHostname() {
    return hostname;
  }

  public String getConfig() {
    return config;
  }
}
