package org.seage.hh.knowledgebase;

import java.sql.Connection;
import java.sql.DriverManager;

public class HsqldbTest {
  public static void main(String[] args) {
    try {
      Class.forName("org.hsqldb.jdbc.JDBCDriver");
      @SuppressWarnings("unused")
      Connection c = DriverManager.getConnection("jdbc:hsqldb:file:/tmp/testdb", "SA", "");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
