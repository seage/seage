package org.seage.hh.experimenter;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.Date;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.seage.data.DataNode;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;
import org.seage.hh.knowledgebase.db.mapper.SolutionMapper;

public class ExperimentReporter {
  public ExperimentReporter() throws Exception {
    DbManager.init();
  }

  // public void createExperimentReport() throws Exception {
  //   try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
  //     ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
  //     mapper.insertExperiment(experiment)
  //   }
  // }

  /**
   * Puts experiment info into database.
   */
  public void createExperimentReport(
      UUID experimentID, String experimentName, String problemID,
      String[] instanceIDs, String[] algorithmIDs, String config, 
      Date startDate, Date duration) throws Exception {
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
          InetAddress.getLocalHost().getHostName(),
          null
      );

      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.insertExperiment(experiment);
      session.commit();
    }    
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
    for (DataNode dn : solutions.getDataNodes(elemName)) {
      Long iterNumber = 0L;
      if (type == "final") {
        iterNumber = -1L;
      } else if (type == "new") {
        iterNumber = dn.getValueLong("iterNumber");
      }
      Solution s = new Solution(
          experimentTaskID,
          dn.getValueStr("hash"),
          dn.getValueStr("solution"),
          dn.getValueDouble("objVal"),
          iterNumber
      );
      mapper.insertSolution(s);        
    }
    session.commit();
  }
  
}
