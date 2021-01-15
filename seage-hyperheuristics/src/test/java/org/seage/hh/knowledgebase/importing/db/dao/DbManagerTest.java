package org.seage.hh.knowledgebase.importing.db.dao;

import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.seage.hh.knowledgebase.db.dao.DbManager;
import org.seage.hh.knowledgebase.db.dao.DbManager.DbType;

@Disabled("Disabled for now")
class DbManagerTest {

  @Test
  void test() throws Exception {
    DbManager.init(DbType.POSTGRES);
    SqlSession session = DbManager.getSqlSessionFactory().openSession();
    // assertNotNull(session);
  }

}
