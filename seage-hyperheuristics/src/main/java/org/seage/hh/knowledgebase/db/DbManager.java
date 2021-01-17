package org.seage.hh.knowledgebase.db;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
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
    String initSqlResourcePath = commonPrefix + "create-schema.sql";

    String dbUrl = null;    
    String username = "SA";
    String password = ""; 
    String environment = "local";   
    boolean testMode = dbUrl == null || dbUrl.isEmpty();

    Properties props = new Properties();
    props.setProperty("username", username);
    props.setProperty("password", password);

    if (testMode) {
      environment = "test";
      // Path testDbPath = Path.of(System.getProperty("java.io.tmpdir"), "seage-test-hyper");
      // props.put("driver", "org.hsqldb.jdbcDriver");
      // props.put("url", String.format("jdbc:hsqldb:file:%s;sql.syntax_pgs=true;check_props=true", 
      //     testDbPath.toAbsolutePath()));
      // props.put("driver", "org.h2.Driver");
      // props.put("url", String.format("jdbc:h2:file:%s", testDbPath.toAbsolutePath()));
    }

    try (InputStream inputStream = Resources.getResourceAsStream(configResourcePath);
        Reader reader = Resources.getResourceAsReader(initSqlResourcePath);) {

      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment, props);

      try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {       
        ScriptRunner runner = new ScriptRunner(session.getConnection());
        runner.setLogWriter(new PrintWriter(System.out));
        runner.setErrorLogWriter(new PrintWriter(System.err));

        if (testMode) {
          runner.runScript(new StringReader("DROP SCHEMA seage CASCADE;"));
        }
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
