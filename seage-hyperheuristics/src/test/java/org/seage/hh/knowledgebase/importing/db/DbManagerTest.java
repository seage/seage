package org.seage.hh.knowledgebase.importing.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.DbManager.DbType;

class DbManagerTest {

  @Test
  void test() throws Exception {
    DbManager.init(DbType.POSTGRES);
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      assertNotNull(session);
    }
  }

}
