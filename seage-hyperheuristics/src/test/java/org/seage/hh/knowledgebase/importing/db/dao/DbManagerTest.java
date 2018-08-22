package org.seage.hh.knowledgebase.importing.db.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.seage.hh.knowledgebase.db.dao.DbManager;
import org.seage.hh.knowledgebase.db.dao.DbManager.DbType;

class DbManagerTest {

    @Test
    void test() throws Exception {
        DbManager.init(DbType.POSTGRES);
        SqlSession session = DbManager.getSqlSessionFactory().openSession();
        assertNotNull(session);
    }

}
