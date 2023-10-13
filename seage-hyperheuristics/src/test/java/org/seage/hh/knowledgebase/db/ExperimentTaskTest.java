package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

public class ExperimentTaskTest {
  private ExperimentRecord experiment1;
  private ExperimentTaskRecord experimentTask1;
  private ExperimentTaskRecord experimentTask2;

  @BeforeEach
  void setUp() throws Exception {
    DbManager.initTest();

    this.experiment1 = new ExperimentRecord(
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      "experimentType1", 
      "TEST", 
      "instanceID1", 
      "algorithmID1", 
      "config1", 
      new Date(), 
      new Date(), 
      1.0,
      "hostname1",
      "1", "tag1"
    );

    AlgorithmParams config1 = new AlgorithmParams();
    this.experimentTask1 = new ExperimentTaskRecord(new ExperimentTaskRequest(
      UUID.randomUUID(),
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"),
      1, 1,
      "TEST", 
      "instanceID1", 
      "algorithmID1", 
      config1.hash(),
      config1,
      null,
      1L
    ));
    
    AlgorithmParams config2 = new AlgorithmParams();
    this.experimentTask2 = new ExperimentTaskRecord(new ExperimentTaskRequest(
      UUID.randomUUID(),
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      2, 2,
      "TEST", 
      "instanceID2", 
      "algorithmID2",
      config2.hash(),
      config2,
      null,
      2L
    ));
  }

  @Test
  void testInsertAndGetExperimentTask() throws Exception {    
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
  }
}
