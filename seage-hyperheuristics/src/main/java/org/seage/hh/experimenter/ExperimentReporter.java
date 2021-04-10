package org.seage.hh.experimenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// import java.lang.reflect.Type;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Date;
// import java.util.HashMap;
import java.util.Map;
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
      String problems = String.join(",", problemIDs);
      
      Experiment experiment = new Experiment(
          experimentID, 
          experimentName,
          problems,
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
   * Method updates the end date.
   * @param experimentID Experiment id.
   * @param endDate New end date.
   */
  public synchronized void updateEndDate(UUID experimentID, Date endDate) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateEndDate(experimentID, endDate);
      session.commit();
    }    
  }

  /**
   * Method updates the score.
   * @param experimentTaskID ExperimentTask uuid.
   * @param score New score.
   */
  public synchronized void updateInstanceScore(UUID experimentTaskID, double score) 
      throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {      
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      mapper.updateScore(experimentTaskID, score);
      session.commit();
    }  
  }

  private String scoreCardMapToString(Double score, Map<String, Map<String, Double>> scoreCardMap) {
    Gson gson = new Gson();
    String result = "{score: " + score + ", problems:[";
    for(String problemID: scoreCardMap.keySet()) {
      result += "{";
      for (String instanceID: scoreCardMap.get(problemID).keySet()) {
        result += "{name: " 
          + instanceID + ", score: " + scoreCardMap.get(problemID).get(instanceID);
      }
    }
    return result + "]}";
    // String scoreCard = gson.toJson(scoreCardMap);
    // return "{score: " 
    //   + score + ", problems:[" + scoreCard.substring(1, scoreCard.length() - 1) + "]}";
  }

  /**
   * Method updates the score and the score card.
   * @param experimentID Experiment id.
   * @param score Score.
   * @param scoreCard Score card.
   */
  public synchronized void updateExperimentScore(
      UUID experimentID, double score, Map<String, Map<String, Double>> scoreCard) 
      throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {      
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      mapper.updateScore(experimentID, score, scoreCardMapToString(score, scoreCard));
      session.commit();
    }
  }

  /**
   * This is a critical function. When non-sychronized, 
   * multiple threads open db sessions that timeouted.
   * @param experimentTask Experiment task.
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
            dn.getValueDouble("score"),
            iterNumber,
            Date.from(Instant.now())
        );
        mapper.insertSolution(s);
        session.commit();
      }
    }
  }
  
}
