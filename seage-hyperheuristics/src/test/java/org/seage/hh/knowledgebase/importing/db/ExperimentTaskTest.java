package org.seage.hh.knowledgebase.importing.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;

import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.DbManager.DbType;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

public class ExperimentTaskTest {
  @Test
  void test() throws Exception {
    DbManager.init(DbType.HSQLDB);
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      assertNotNull(mapper);

    }
  }
}
