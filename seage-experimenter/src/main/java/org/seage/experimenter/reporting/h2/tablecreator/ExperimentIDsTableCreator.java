package org.seage.experimenter.reporting.h2.tablecreator;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public class ExperimentIDsTableCreator extends TableCreator implements IDocumentProcessor
{
	//protected Connection _conn;
	private String _expLogsPath;
	private HashSet<String> _experimentIDs;
	private PreparedStatement _stmt;

	public ExperimentIDsTableCreator(String expLogsPath, String dbPath, boolean clean) throws Exception
	{
		super(dbPath);
		_expLogsPath = expLogsPath;
		
		String queryCreateExperiments =
				//"DROP TABLE IF EXISTS Experiments;"+
				"CREATE TABLE IF NOT EXISTS Experiments (ExperimentID BIGINT PRIMARY KEY)"; 
		String queryDropExperiments =
				"DROP TABLE IF EXISTS Experiments";		

		Statement stmt = _conn.createStatement();
		
		if(clean)
			stmt.execute(queryDropExperiments);	
		stmt.execute(queryCreateExperiments);	
		
		String queryInsert =				
				"INSERT INTO Experiments VALUES (?)"; 

		_stmt = _conn.prepareStatement(queryInsert);
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<String> getExperimentIDs() throws Exception
	{
		_experimentIDs = new HashSet<String>();
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
			if(!_experimentIDs.contains(id))
				_experimentIDs.add(id);
		}		
		
		String queryCreateNewExperiments =
				//"DROP TABLE IF EXISTS test2;"+
				"CREATE TEMP TABLE NewExperiments (ExperimentID BIGINT PRIMARY KEY)";
		String queryInsert =				
				"INSERT INTO NewExperiments VALUES (?)"; 
		String queryMinus =				
				"SELECT ExperimentID FROM NewExperiments MINUS SELECT ExperimentID FROM Experiments"; 	
	
        PreparedStatement stmt = null;

    	stmt = _conn.prepareStatement(queryCreateNewExperiments);
    	stmt.execute();
    	
    	stmt = _conn.prepareStatement(queryInsert);	
    	for(String id : _experimentIDs)
    	{        		
    		stmt.setLong(1, Long.parseLong(id));
    		stmt.execute();
    	}
    	
    	stmt = _conn.prepareStatement(queryMinus);        	
    	ResultSet rs = stmt.executeQuery();
    	
    	_experimentIDs.clear();      	
    	
    	while(rs.next())
    	{
    		_experimentIDs.add(rs.getString(1));    		
    	}
	
		return (HashSet<String>)_experimentIDs.clone();
	}

	@Override
	public boolean isInvolved(Document doc)
	{
		String id = doc.getDocumentElement().getAttribute("experimentID");
		return _experimentIDs.contains(id);
	}

	@Override
	public synchronized void processDocument(Document doc) throws Exception
	{
		String id = doc.getDocumentElement().getAttribute("experimentID");
				
		//for(String id : _experimentIDs)
		//{
		_stmt.setLong(1, Long.parseLong(id));
		_stmt.executeUpdate();
		
		_experimentIDs.remove(id);
		//}
		
	}
}
