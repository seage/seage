package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Date;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

public class ExperimentTaskTest {
  private Experiment experiment1;
  private ExperimentTask experimentTask1;
  private ExperimentTask experimentTask2;

  @BeforeEach
  void setUp() throws Exception {
    DbManager.initTest();

    this.experiment1 = new Experiment(
      "experimentID1", 
      "experimentType1", 
      "problemID1", 
      "instanceID1", 
      "algorithmID1", 
      "config1", 
      new Date(), 
      new Date(), 
      "hostname1", 
      1.0
    );

    this.experimentTask1 = new ExperimentTask(
      "experimentID1", 
      1, 1,
      "problemID1", 
      "instanceID1", 
      "algorithmID1", 
      new AlgorithmParams(), 
      1L
    );
    
    this.experimentTask2 = new ExperimentTask(
      "experimentID1", 
      2, 2,
      "problemID2", 
      "instanceID2", 
      "algorithmID2", 
      new AlgorithmParams(), 
      2L
    );
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
      ExperimentTask experimentTask = mapper.getExperimentTask(this.experimentTask1.getId());      
      assertNotNull(experimentTask);
      assertEquals(this.experimentTask1.getId(), experimentTask.getId());
    }
  }
}
