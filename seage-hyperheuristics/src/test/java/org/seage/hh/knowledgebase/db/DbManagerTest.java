package org.seage.hh.knowledgebase.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

class DbManagerTest {

  @Test
  void test() throws Exception {
    DbManager.initTest();
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      assertNotNull(session);
      try (Connection c =
          session.getConfiguration().getEnvironment().getDataSource().getConnection()) {
        assertTrue(c.getMetaData().getURL().startsWith("jdbc:h2:file:"));
        assertTrue(c.getMetaData().getURL().contains("seage.test"));

        System.out.println(c.getMetaData().getURL());
      }
    }
  }
}
