package org.seage.hh.knowledgebase.db.dao;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
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
    };

    private static Logger logger = LoggerFactory.getLogger(DbManager.class.getName());

    private static SqlSessionFactory sqlSessionFactory;
    
    public static void init(DbType dbType) throws Exception {
        init(dbType, null);
    }
    public static void init(DbType dbType, Properties props) throws Exception {
        String commonPrefix = "org/seage/knowledgebase/importing/db/dao/";
        String configResourcePath = commonPrefix + "mybatis-config.xml";
        String environment = "development";
        String initSqlResourcePath = commonPrefix +  "create.sql";

        if(dbType == DbType.HSQLDB) 
            environment = "test";
        if(props == null || !props.containsKey("url")) {
            props = new Properties();
            props.put("url", "jdbc:postgresql://localhost:5432/seage");
        }
        try (InputStream inputStream = Resources.getResourceAsStream(configResourcePath);
                Reader reader = Resources.getResourceAsReader(initSqlResourcePath);) {
            
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment, props);
            logger.info(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getURL());
            
            try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
                ScriptRunner runner = new ScriptRunner(session.getConnection());
                runner.setLogWriter(new PrintWriter(System.out));
                runner.setErrorLogWriter(new PrintWriter(System.err));
                runner.runScript(reader);
            }
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() throws Exception {
        if (sqlSessionFactory == null)
            throw new Exception("DbManager not initialized");
        return sqlSessionFactory;
    }
}
