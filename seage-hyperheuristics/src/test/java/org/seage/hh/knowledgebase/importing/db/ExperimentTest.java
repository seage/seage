package org.seage.hh.knowledgebase.importing.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.Experiment;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;

public class ExperimentTest {
  @BeforeAll
  static void setUp() throws Exception {
    DbManager.init();
  }

  @Test
  void testInsertAndGetExperiment() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      assertNotNull(mapper);
      
      Experiment experiment1 = new Experiment();
      mapper.insertExperiment(experiment1);
      session.commit();
      Experiment experiment2 = mapper.getExperiment(experiment1.getId());      
      assertNotNull(experiment2);
      assertEquals(experiment1.getId(), experiment2.getId());
    }
  }

  @Test
  void testInsertExperiment() throws Exception {    
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
      assertNotNull(mapper);

      Experiment experiment1 = new Experiment();
      Experiment experiment2 = new Experiment();
      mapper.insertExperiment(experiment1);
      mapper.insertExperiment(experiment2);
      session.commit();
      assertNotEquals(0, experiment1.getId());
      int count = mapper.getExperimentCount();
      assertTrue(count >= 2);
    }
  }
}
