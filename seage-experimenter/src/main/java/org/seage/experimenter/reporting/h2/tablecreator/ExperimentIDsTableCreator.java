package org.seage.experimenter.reporting.h2.tablecreator;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

public class ExperimentIDsTableCreator extends TableCreator
{
	private String _expLogsPath;

	public ExperimentIDsTableCreator(String expLogsPath, String dbPath, boolean clean) throws Exception
	{
		super(dbPath);
		
		_expLogsPath = expLogsPath;
		
		String queryCreateExperiments =
				//"DROP TABLE IF EXISTS Experiments;"+
				"CREATE TABLE IF NOT EXISTS Experiments (ExperimentID BIGINT PRIMARY KEY)"; 
		String queryDropExperiments =
				"DROP TABLE IF EXISTS Experiments";
		
		Connection conn = createConnection();
		try
		{
			Statement stmt = conn.createStatement();
			
			if(clean)
				stmt.execute(queryDropExperiments);	
			stmt.execute(queryCreateExperiments);
		}
		finally
		{
			if(conn != null)
				conn.close();
		}
	}
	
	public HashSet<String> getExperimentIDs() throws Exception
	{
		
		HashSet<String> experimentIDs = new HashSet<String>();
		File f = new File(_expLogsPath);
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
		
		
		String queryCreateNewExperiments =
				//"DROP TABLE IF EXISTS test2;"+
				"CREATE TEMP TABLE NewExperiments (ExperimentID BIGINT PRIMARY KEY)";
		String queryInsert =				
				"INSERT INTO NewExperiments VALUES (?)"; 
		String queryMinus =				
				"SELECT ExperimentID FROM NewExperiments MINUS SELECT ExperimentID FROM Experiments"; 		
		
		Connection conn = createConnection();
		try
		{
	        PreparedStatement stmt = null;
	
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
	    	
	    	while(rs.next())
	    	{
	    		experimentIDs.add(rs.getString(1));    		
	    	}
		}
		finally
		{
			if(conn != null)
				conn.close();
		}


		return experimentIDs;
	}
	
	public void insertNewExperiments(HashSet<String> ids) throws Exception
	{
		String queryInsert =				
				"INSERT INTO Experiments VALUES (?)"; 
		
		Connection conn = createConnection();
		try
		{
			PreparedStatement stmt = conn.prepareStatement(queryInsert);
			
			for(String id : ids)
			{
				stmt.setLong(1, Long.parseLong(id));
				stmt.executeUpdate();
			}
		}
		finally
		{
			if(conn != null)
				conn.close();
		}
		
	}

}
