package org.seage.hh.experimenter;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.Date;
import org.apache.ibatis.session.SqlSession;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

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
      String experimentID, String experimentName, String problemID,
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

  public void updateEndDate(String experimentId, Date endDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateEndDate(experimentId, endDate);
      session.commit();
    }    
  }

  public void updateScore(String experimentId, double bestObjVal) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateScore(experimentId, bestObjVal);
      session.commit();
    }  
  }

  public void reportExperimentTask(ExperimentTask experimentTask) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      mapper.insertExperimentTask(experimentTask);
      session.commit();
    }
  }
  
}
