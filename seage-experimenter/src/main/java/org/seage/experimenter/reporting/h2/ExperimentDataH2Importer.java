package org.seage.experimenter.reporting.h2;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.logging.Logger;

public class ExperimentDataH2Importer
{
	private static Logger _logger = Logger.getLogger(ExperimentDataH2Importer.class.getName());
	private String _logPath;
	private String _dbPath;
	private String[] _experimentIDs;
	
	public ExperimentDataH2Importer(String logPath, String dbPath) throws Exception
	{
		_logPath = logPath;
		_dbPath = dbPath;
		//_experimentIDs = getExperimentIDs();
	}

	public void processLogs() throws Exception
	{
		String[] ids = getExperimentIDs();
		for(String id : ids)
			_logger.info(id);
		_logger.info("Total: " + ids.length);
	}
	
	private String[] getExperimentIDs() throws Exception
	{
		HashSet<String> experimentIDs = new HashSet<String>();
		File f = new File(_logPath);
		File[] fl = f.listFiles(new FilenameFilter()
		{			
			@Override
			public boolean accept(File dir, String fileName)
			{				
				return fileName.endsWith(".zip");
			}
		});
		for(File z : fl)
		{
			String id = z.getName().split("-")[0];
			if(!experimentIDs.contains(id))
				experimentIDs.add(id);
		}
		
		String queryCreateExperiments =
				//"DROP TABLE IF EXISTS Experiments;"+
				"CREATE TABLE IF NOT EXISTS Experiments (id BIGINT PRIMARY KEY)"; 
		String queryCreateNewExperiments =
				//"DROP TABLE IF EXISTS test2;"+
				"CREATE TEMP TABLE NewExperiments (id BIGINT PRIMARY KEY)";
		String queryInsert =				
				"INSERT INTO NewExperiments VALUES (?)"; 
		String queryMinus =				
				"SELECT id FROM NewExperiments MINUS SELECT id FROM Experiments"; 
		String queryInsert2 =				
				"INSERT INTO Experiments VALUES (?)"; 
		
		Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:"+_dbPath, "sa", "sa");
        PreparedStatement stmt = null;
        try
        {
        	stmt = conn.prepareStatement(queryCreateExperiments);
        	stmt.execute();
        	stmt = conn.prepareStatement(queryCreateNewExperiments);
        	stmt.execute();
        	
        	stmt = conn.prepareStatement(queryInsert);	
        	for(String id : experimentIDs)
        	{        		
        		stmt.setLong(1, Long.parseLong(id));
        		stmt.execute();
        	}
        	
        	stmt = conn.prepareStatement(queryMinus);        	
        	ResultSet rs = stmt.executeQuery();
        	
        	experimentIDs.clear();
        	stmt = conn.prepareStatement(queryInsert2);        	
        	
        	while(rs.next())
        	{
        		experimentIDs.add(rs.getString(1));
        		stmt.setLong(1, rs.getLong(1));
        		stmt.execute();
        	}
        }
        finally
        {
        	if(conn !=null)
        		conn.close();
        }

		return experimentIDs.toArray(new String[]{});
	}
	
	public void processLogs0() throws Exception
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
