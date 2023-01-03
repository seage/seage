package org.seage.hh.experimenter.singlealgorithm.feedback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;



public class NewFeedbackConfiguratorTest {
  private ExperimentRecord experiment1;
  private ExperimentTaskRecord experimentTask1;
  private ProblemInfo pi;

  @BeforeEach
  void setUp() throws Exception {
    DbManager.initTest();

    this.experiment1 = new ExperimentRecord(
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      "experimentType1", 
      "TestProblem", 
      "Instance01", 
      "Algorithm01", 
      "config1", 
      new Date(), 
      new Date(), 
      1.0,
      "hostname1",
      "1"
    );

    this.experimentTask1 = new ExperimentTaskRecord(new ExperimentTaskRequest(
      UUID.randomUUID(),
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"),
      1, 1,
      "TestProblem", 
      "Instance01", 
      "Algorithm01", 
      new AlgorithmParams(),
      null,
      1L
    ));

    this.pi = new ProblemInfo("TestProblem");
    pi.putValue("id", "TestProblem");
    pi.putDataNode(new DataNode("Instances"));
    pi.putDataNode(new DataNode("Algorithms"));

    DataNode ii = new DataNode("Instance");
    ii.putValue("id", "Instance01");
    ii.putValue("name", "Instance01");
    ii.putValue("type", "");
    ii.putValue("path", "");
    pi.getDataNode("Instances").putDataNode(ii);

    DataNode ai = new DataNode("Algorithm");
    ai.putValue("id", "Algorithm01");
    pi.getDataNode("Algorithms").putDataNode(ai);

  }

  @Test
  void testCreateConfigs() throws Exception {

    // Before putting the data to the database
    FeedbackConfigurator fc = new FeedbackConfigurator(0.0);

    ProblemConfig[] pcList = fc.prepareConfigs(pi, "Instance01", "Algorithm01", 10);

    assertEquals(10, pcList.length);

    // Inserting data to the database
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper0 = session.getMapper(ExperimentMapper.class);
      mapper0.insertExperiment(this.experiment1);

      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      assertNotNull(mapper);

      mapper.insertExperimentTask(this.experimentTask1);
      session.commit();

      
      ExperimentTaskRecord experimentTask = 
          mapper.getExperimentTask(this.experimentTask1.getExperimentTaskID());      
      assertNotNull(experimentTask);
      assertEquals(
          this.experimentTask1.getExperimentTaskID(), experimentTask.getExperimentTaskID());
    }

    // Test if it returns only one config from db when asked for 1
    pcList = fc.prepareConfigs(pi, "Instance01", "Algorithm01", 1);

    assertEquals(1, pcList.length);

    // Test if it returns 10 config from db when asked for 10
    pcList = fc.prepareConfigs(pi, "Instance01", "Algorithm01", 10);

    assertEquals(10, pcList.length);
  }

  @Test
  void testPrepareConfigs() throws Exception {
    FeedbackConfigurator fc = new FeedbackConfigurator(0.0);

    // Test if it returns all 10 asked configs no matter if there is something in db
    ProblemConfig[] pc = fc.prepareConfigs(pi, "Instance01", "Algorithm01", 10);

    assertEquals(10, pc.length);
  }
}