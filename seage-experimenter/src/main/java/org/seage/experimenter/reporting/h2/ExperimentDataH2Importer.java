package org.seage.experimenter.reporting.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class ExperimentDataH2Importer
{
	public void processLogs() throws Exception
	{
		String queryCreate =
				"DROP TABLE IF EXISTS test;"+
				"CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY, name VARCHAR(50))"; 
		
		String queryCreate2 =
				//"DROP TABLE IF EXISTS test2;"+
				"CREATE TEMP TABLE test2 (id INTEGER PRIMARY KEY)";
		
		String queryInsert =				
				"INSERT INTO test VALUES (?, ?)"; 
		
		String queryInsert2 =				
				"INSERT INTO test2 VALUES (?)"; 
		
		String queryMinus =				
				"SELECT id FROM test2 MINUS SELECT id FROM test"; 
		
		Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/h2db/seage", "sa", "sa");
        PreparedStatement createStmt = null;
        PreparedStatement insertStmt = null;        
        PreparedStatement selectStmt = null;
        try
        {
        	createStmt = conn.prepareStatement(queryCreate);
        	createStmt.execute();
        	createStmt = conn.prepareStatement(queryCreate2);
        	createStmt.execute();
        	
        	insertStmt = conn.prepareStatement(queryInsert);
        	
        	for(int i=0;i<10000;i++)
        	{
        		insertStmt.setInt(1, i+1);
        		insertStmt.setString(2, new Integer(((i+1)*10)).toString());
        		insertStmt.execute();
        	}
        	long t1 = System.currentTimeMillis();
        	insertStmt = conn.prepareStatement(queryInsert2);
        	
        	for(int i=0;i<100000;i++)
        	{
        		insertStmt.setInt(1, i);        		
        		insertStmt.execute();
        	}
        	
        	selectStmt = conn.prepareStatement(queryMinus);
        	
        	ResultSet rs = selectStmt.executeQuery();
        	long t2 = System.currentTimeMillis();
        	System.out.println(t2-t1);
        	
        }
        finally
        {
        	if(conn !=null)
        		conn.close();
        }
	}
}
