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
  public synchronized void createExperimentReport(
      UUID experimentID, String experimentName, String[] problemIDs,
      String[] instanceIDs, String[] algorithmIDs, String config, 
      Date startDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      String instances = String.join(",", instanceIDs);
      String algorithms = String.join(",", algorithmIDs);
      Experiment experiment = new Experiment(
          experimentID, 
          experimentName,
          problemIDs.toString(),
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

  /**
   * .
   * @param experimentID .
   * @param endDate .
   * @throws Exception .
   */
  public synchronized void updateEndDate(UUID experimentID, Date endDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateEndDate(experimentID, endDate);
      session.commit();
    }    
  }

  /**
   * .
   * @param experimentID .
   * @param bestObjVal .
   * @throws Exception .
   */
  public synchronized void updateScore(UUID experimentID, double bestObjVal) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateScore(experimentID, bestObjVal);
      session.commit();
    }  
  }

  /**
   * This is a critical function. When non-sychronized, 
   * multiple threads open db sessions that timeouted.
   * @param experimentTask .
   * @throws Exception .
   */
  public synchronized void reportExperimentTask(ExperimentTask experimentTask) throws Exception {
     
    insertExperimentTask(experimentTask);

    UUID experimentTaskID = experimentTask.getExperimentTaskID();
    DataNode report = experimentTask.getExperimentTaskReport();
    
    DataNode inputSolutions = report.getDataNode("Solutions").getDataNode("Input");
    insertSolutions(experimentTaskID, inputSolutions, "Solution", "init");
    
    DataNode outputSolutions = report.getDataNode("Solutions").getDataNode("Output");
    insertSolutions(experimentTaskID, outputSolutions, "Solution", "final");

    // DataNode newSolutions = report.getDataNode("AlgorithmReport").getDataNode("Log");
    // insertSolutions(session, experimentTaskID, newSolutions, "NewSolution", "new");
      
  }

  private void insertExperimentTask(ExperimentTask experimentTask) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {      
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      mapper.insertExperimentTask(experimentTask);
      session.commit();     
    }
  }

  private void insertSolutions(
      UUID experimentTaskID, DataNode solutions, String elemName, String type)
      throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {      
      SolutionMapper mapper = session.getMapper(SolutionMapper.class);
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
        session.commit();
      }
    }
  }
  
}
