package org.seage.hh.knowledgebase;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HsqldbTest {
  private static Logger _logger = LoggerFactory.getLogger(HsqldbTest.class.getName());

  public static void main(String[] args) {
    try {
      Class.forName("org.hsqldb.jdbc.JDBCDriver");
      try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:/tmp/testdb", "SA", "")) {

      }
    } catch (Exception e) {
      _logger.error("HsqldbTest failed", e);
    }
  }
}
