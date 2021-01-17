package org.seage.hh.experimenter;

import java.sql.Connection;
import java.util.Date;
import org.apache.ibatis.session.SqlSession;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.Experiment;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;

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
      String[] instanceIDs, String[] algorithmIDs, Date startDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      String instances = String.join(",", instanceIDs);
      String algorithms = String.join(",", algorithmIDs);
      Experiment experiment = new Experiment(
          experimentID, 
          experimentName,
          problemID,
          instances,
          algorithms,
          startDate
      );

      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.insertExperiment(experiment);
    }    
  }
  
}
