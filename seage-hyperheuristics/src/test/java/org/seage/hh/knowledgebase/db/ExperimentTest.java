package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;

public class ExperimentTest {
  private Experiment experiment1;
  private Experiment experiment2;

  @BeforeEach
  void setUp() throws Exception {
    DbManager.initTest();

    this.experiment1 = new Experiment(
      UUID.fromString("447ee273-3a5d-493a-a84c-0403690dfe0f"), 
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
    
    this.experiment2 = new Experiment(
      UUID.fromString("3d03b29f-39d7-4993-b76d-607282be52d8"),
      "experimentType2", 
      "problemID2", 
      "instanceID2", 
      "algorithmID2", 
      "config2", 
      new Date(), 
      new Date(), 
      "hostname2", 
      2.0
    );
  }

  @Test
  void testEmptyDatabase() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      assertNotNull(mapper);

      int count = mapper.getExperimentCount();      
      assertEquals(0, count);
    }
  }

  @Test
  void testInsertAndGetExperiment() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      assertNotNull(mapper);

      mapper.insertExperiment(this.experiment1);
      session.commit();
      Experiment experiment = mapper.getExperiment(this.experiment1.getExperimentID());      
      assertNotNull(experiment);
      assertEquals(this.experiment1.getExperimentID(), experiment.getExperimentID());
    }
  }

  @Test
  void testSelectExperiments() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      assertNotNull(mapper);

      mapper.insertExperiment(this.experiment1);
      mapper.insertExperiment(this.experiment2);
      session.commit();
      // Experiment got id
      assertNotEquals(0, this.experiment1.getExperimentID());
      // Exactly two experiments inserted
      int count = mapper.getExperimentCount();      
      assertEquals(2, count);
      // Expected experiments inserted
      List<Experiment> experiments = mapper.getExperiments();
      assertEquals(2, experiments.size());
      assertEquals(this.experiment1.getExperimentID(), experiments.get(0).getExperimentID());
      assertEquals(this.experiment1.getExperimentType(), experiments.get(0).getExperimentType());
      assertEquals(this.experiment1.getExperimentID(), experiments.get(0).getExperimentID());
      assertEquals(this.experiment1.getProblemID(), experiments.get(0).getProblemID());
      assertEquals(this.experiment2.getInstanceID(), experiments.get(1).getInstanceID());
      assertEquals(this.experiment2.getAlgorithmID(), experiments.get(1).getAlgorithmID());      
      assertEquals(this.experiment2.getConfig(), experiments.get(1).getConfig());
      assertEquals(this.experiment2.getStartDate(), experiments.get(1).getStartDate());
    }
  }
}
