package org.seage.hh.experimenter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.seage.data.DataNode;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;
import org.seage.hh.knowledgebase.db.mapper.SolutionMapper;

public class ExperimentReporter {
  private static final String FORMAT_VERSION = "1.0.0";

  public ExperimentReporter() throws Exception {
    DbManager.init();
  }

  /**
   * Puts experiment info into database.
   */
  public void createExperimentReport(
      UUID experimentID, String experimentName, String problemID,
      String[] instanceIDs, String[] algorithmIDs, String config, 
      Date startDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      String instances = String.join(",", instanceIDs);
      String algorithms = String.join(",", algorithmIDs);
      Experiment experiment = new Experiment(
          experimentID, 
          experimentName,
          problemID,
          instances,
          algorithms,
          config,
          startDate,
          null,
          null,
          getHostInfo(),
          FORMAT_VERSION
      );

      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.insertExperiment(experiment);
      session.commit();
    }    
  }

  private String getHostInfo() {
    DataNode info = new DataNode("HostInfo");
    try {
      info.putValue("machineName", InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e) {
      info.putValue("machineName", "UNKNOWN");
    }
    info.putValue("nrOfCores", Runtime.getRuntime().availableProcessors());
    info.putValue("totalRAM", Runtime.getRuntime().totalMemory());
    info.putValue("availRAM", Runtime.getRuntime().maxMemory());
    return info.toString();
  }

  public void updateEndDate(UUID experimentID, Date endDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateEndDate(experimentID, endDate);
      session.commit();
    }    
  }

  public void updateScore(UUID experimentID, double bestObjVal) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateScore(experimentID, bestObjVal);
      session.commit();
    }  
  }

  public void reportExperimentTask(ExperimentTask experimentTask) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      mapper.insertExperimentTask(experimentTask);
      session.commit();      
      
      UUID experimentTaskID = experimentTask.getExperimentTaskID();
      DataNode report = experimentTask.getExperimentTaskReport();
      
      DataNode inputSolutions = report.getDataNode("Solutions").getDataNode("Input");
      insertSolutions(session, experimentTaskID, inputSolutions, "Solution", "init");
      
      DataNode outputSolutions = report.getDataNode("Solutions").getDataNode("Output");
      insertSolutions(session, experimentTaskID, outputSolutions, "Solution", "final");

      DataNode newSolutions = report.getDataNode("AlgorithmReport").getDataNode("Log");
      insertSolutions(session, experimentTaskID, newSolutions, "NewSolution", "new");
      
    }
  }

  private void insertSolutions(
      SqlSession session, UUID experimentTaskID, DataNode solutions, String elemName, String type)
      throws Exception {
    SolutionMapper mapper = session.getMapper(SolutionMapper.class);
    int numSolutionsToWrite = 0;
    for (DataNode dn : solutions.getDataNodes(elemName)) {
      Long iterNumber = 0L;
      if (type.equals("final")) {
        iterNumber = -1L;
      } else if (type.equals("new")) {
        iterNumber = dn.getValueLong("iterNumber");
      }
      Solution s = new Solution(
          UUID.randomUUID(),
          experimentTaskID,
          dn.getValueStr("hash"),
          dn.getValueStr("solution"),
          dn.getValueDouble("objVal"),
          iterNumber,
          Date.from(Instant.now())
      );
      mapper.insertSolution(s);
      
      numSolutionsToWrite++;
      if(numSolutionsToWrite == 10) {
        session.commit();
        numSolutionsToWrite = 0;
      }
    }
    session.commit();
  }
  
}
