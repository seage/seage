package org.seage.hh.knowledgebase.importing.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import org.seage.hh.knowledgebase.db.DbManager;

class DbManagerTest {

  @Test
  void test() throws Exception {
    DbManager.init();
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      assertNotNull(session);
      try (Connection c =
          session.getConfiguration().getEnvironment().getDataSource().getConnection()) {
        assertTrue(c.getMetaData().getURL().startsWith("jdbc:h2:file:"));
        assertTrue(c.getMetaData().getURL().endsWith("seage-test"));
      }
    }
  }
}
