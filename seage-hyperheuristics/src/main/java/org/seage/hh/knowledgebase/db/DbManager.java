package org.seage.hh.knowledgebase.db;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.DatabaseMetaData;
import java.util.Optional;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbManager {

  public enum DbType {
    HSQLDB, POSTGRES
  }

  private static Logger logger = LoggerFactory.getLogger(DbManager.class.getName());

  private static SqlSessionFactory sqlSessionFactory;


  public static void initTest() throws Exception {
    DbManager.init(true);
  }

  /**
   * Initializes DbManager.
   */
  public static void init() throws Exception {
    DbManager.init(false);
  }

  private static void init(boolean testMode) throws Exception {
    String commonPrefix = "org/seage/hh/knowledgebase/db/";
    String configResourcePath = commonPrefix + "mybatis-config.xml";

    String dbUrl = Optional.ofNullable(System.getenv("DB_URL")).orElse("");
    if (!dbUrl.isEmpty() && !dbUrl.startsWith("jdbc:")) {
      throw new Exception(String.format("Incorrect DB_URL value: %s", dbUrl));
    }
    String environment = dbUrl.startsWith("jdbc:postgresql") ? "postgres" : "local";

    logger.info("DB_URL: {}", dbUrl);

    String username = Optional.ofNullable(System.getenv("DB_USER")).orElse("seage");
    String password = Optional.ofNullable(System.getenv("DB_PASSWORD")).orElse("seage");

    Properties props = new Properties();
    props.setProperty("url", dbUrl);
    props.setProperty("username", username);
    props.setProperty("password", password);

    if (testMode) {
      environment = "test";
    }

    try (InputStream inputStream = Resources.getResourceAsStream(configResourcePath)) {

      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment, props);
      if (testMode) {
        // runner.runScript(new StringReader("CREATE TYPE 'JSONB' AS json;"));
        try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
          ScriptRunner runner = new ScriptRunner(session.getConnection());
          // runner.setLogWriter(new PrintWriter(System.out));
          // runner.setErrorLogWriter(new PrintWriter(System.err));
          runner.runScript(new StringReader("DROP SCHEMA seage CASCADE;"));
        }
        DatabaseMetaData dbMetadata = sqlSessionFactory.getConfiguration().getEnvironment()
            .getDataSource().getConnection().getMetaData();
        dbUrl = dbMetadata.getURL();
        username = dbMetadata.getUserName();
        password = "";
      }
      // Database migration
      Flyway flyway = Flyway.configure().baselineOnMigrate(true)
          .locations("classpath:/org/seage/hh/knowledgebase/db/migration")
          .dataSource(dbUrl, username, password).load();
      flyway.migrate();
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
