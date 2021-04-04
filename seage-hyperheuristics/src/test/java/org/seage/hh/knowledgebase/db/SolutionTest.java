package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.Solution;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;
import org.seage.hh.knowledgebase.db.mapper.SolutionMapper;

public class SolutionTest {
  private Experiment experiment1;
  private ExperimentTask experimentTask1;
  private Solution solution1;
  private Solution solution2;

  @BeforeEach
  void setUp() throws Exception {
    DbManager.initTest();

    this.experiment1 = new Experiment(
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      "experimentType1", 
      "problemID1", 
      "instanceID1", 
      "algorithmID1", 
      "config1", 
      new Date(), 
      new Date(),
      1.0,
      "hostname1", 
      "1"
    );
    this.experimentTask1 = new ExperimentTask(new ExperimentTaskRequest(
      UUID.randomUUID(),
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"),
      1, 1,
      "problemID1", 
      "instanceID1", 
      "algorithmID1", 
      new AlgorithmParams(), 
      1L
    ));
    
    this.solution1 = new Solution(
      UUID.randomUUID(),
      experimentTask1.getExperimentTaskID(), 
      "123456789", 
      "[1, 2, 3, 4, 5, 6, 7, 8, 9]", 
      1000.0,
      2L,
      Date.from(Instant.now())
    );
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
      
      Solution solution = mapper.getSolution(this.solution1.getSolutionID());      
      assertNotNull(solution);
      assertEquals(this.solution1.getSolutionID(), solution.getSolutionID());
    }
  }
}
