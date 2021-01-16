package org.seage.hh.knowledgebase.db;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbManager {

  public enum DbType {
    HSQLDB, POSTGRES
  }

  private static Logger logger = LoggerFactory.getLogger(DbManager.class.getName());

  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Initializes DbManager.
   */
  public static void init() throws Exception {
    String commonPrefix = "org/seage/hh/knowledgebase/db/";
    String configResourcePath = commonPrefix + "mybatis-config.xml";
    String initSqlResourcePath = commonPrefix + "create.sql";

    String dbUrl = null;
    Properties props = null;
    
    if (dbUrl == null || dbUrl.isEmpty()) {
      String tmpDirPath = System.getProperty("java.io.tmpdir");
      props = new Properties();
      props.put("url", String.format("jdbc:h2:file:%s/seage-test", tmpDirPath));
    }
    try (InputStream inputStream = Resources.getResourceAsStream(configResourcePath);
        Reader reader = Resources.getResourceAsReader(initSqlResourcePath);) {

      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);

      // try (Connection c =
      //     sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection()) {
      //   logger.debug(c.getMetaData().getURL());
      // }

      try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
        ScriptRunner runner = new ScriptRunner(session.getConnection());
        runner.setLogWriter(new PrintWriter(System.out));
        runner.setErrorLogWriter(new PrintWriter(System.err));
        runner.runScript(reader);
      }
    }
  }

  /**
   * Gets valied SqlSessionFactory instance or fails.
   */
  public static SqlSessionFactory getSqlSessionFactory() throws Exception {
    if (sqlSessionFactory == null) {
      throw new Exception("DbManager not initialized");
    }
    return sqlSessionFactory;
  }
}
