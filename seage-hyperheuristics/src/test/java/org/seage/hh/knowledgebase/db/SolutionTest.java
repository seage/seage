package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.knowledgebase.db.dbo.SolutionRecord;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;
import org.seage.hh.knowledgebase.db.mapper.SolutionMapper;

@Disabled
public class SolutionTest {
  private ExperimentRecord experiment1;
  private ExperimentTaskRecord experimentTask1;
  private SolutionRecord solution1;

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
      "1"
    );
    this.experimentTask1 = new ExperimentTaskRecord(new ExperimentTaskRequest(
      UUID.randomUUID(),
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"),
      1, 1,
      "TEST", 
      "instanceID1", 
      "algorithmID1", 
      new AlgorithmParams(),
      null,
      1L
    ));
    
    this.solution1 = new SolutionRecord(
      UUID.randomUUID(),
      experimentTask1.getExperimentTaskID(), 
      "123456789", 
      "[1, 2, 3, 4, 5, 6, 7, 8, 9]", 
      1000.0,
      0.6,
      2L,
      Date.from(Instant.now())
    );
  }
  
  @AfterEach
  void done() {
    DbManager.destroy();
  }

  @Test
  void testInsertAndGetSolution() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper0 = session.getMapper(ExperimentMapper.class);
      mapper0.insertExperiment(this.experiment1);

      ExperimentTaskMapper mapper1 = session.getMapper(ExperimentTaskMapper.class);
      mapper1.insertExperimentTask(this.experimentTask1);

      SolutionMapper mapper = session.getMapper(SolutionMapper.class);
      assertNotNull(mapper);

      mapper.insertSolution(this.solution1);
      session.commit();
      
      SolutionRecord solution = mapper.getSolution(this.solution1.getSolutionID());      
      assertNotNull(solution);
      assertEquals(this.solution1.getSolutionID(), solution.getSolutionID());
    }
  }
}
