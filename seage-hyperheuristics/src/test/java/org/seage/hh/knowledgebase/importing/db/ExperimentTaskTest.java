package org.seage.hh.knowledgebase.importing.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.mapper.ExperimentMapper;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

public class ExperimentTaskTest {
  // @Test
  // void testInsertAndGetExperimentTask() throws Exception {    
  //   try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
  //     ExperimentMapper mapper = session.getMapper(ExperimentMapper.class);
  //     assertNotNull(mapper);

  //     ExperimentTask experimentTask1 = new ExperimentTask();
  //     mapper.insertExperimentTask(experimentTask1);
  //     session.commit();
  //     ExperimentTask experiment2 = mapper.getExperimentTask(experimentTask1.getId());      
  //     assertNotNull(experiment2);
  //     assertEquals(experimentTask1.getId(), experiment2.getId());
  //   }
  // }
}
