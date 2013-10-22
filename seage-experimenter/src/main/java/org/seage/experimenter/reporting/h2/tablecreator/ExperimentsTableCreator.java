package org.seage.experimenter.reporting.h2.tablecreator;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.logging.Logger;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public class ExperimentsTableCreator extends H2DataTableCreator implements IDocumentProcessor
{
	private static Logger _logger = Logger.getLogger(ExperimentsTableCreator.class.getName());
	
	private String _expLogsPath;
	private HashSet<String> _experimentIDs;
	private PreparedStatement _stmt;

	public ExperimentsTableCreator(String expLogsPath, String dbPath, boolean clean) throws Exception
	{
		super(dbPath);
		_expLogsPath = expLogsPath;

		String queryDropExperiments =
				"DROP ALL OBJECTS";			
		String queryCreateExperiments =
				"CREATE TABLE IF NOT EXISTS Experiments (ExperimentID VARCHAR PRIMARY KEY, ExperimentType VARCHAR, ComputerName VARCHAR)"; 
		String queryInsert =				
				"INSERT INTO Experiments VALUES (?, ?, ?)"; 
		
		Statement stmt = _conn.createStatement();
		
		if(clean)
			stmt.execute(queryDropExperiments);	
		stmt.execute(queryCreateExperiments);	

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
		
		if(fl == null)
		{
			_logger.info("No zip file to process");
			return new HashSet<String>();
		}
		
		for(File z : fl)
		{
			String id = z.getName().split("-")[0];
			if(!_experimentIDs.contains(id))
				_experimentIDs.add(id);
		}		
		
		String queryCreateNewExperiments =
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
    		stmt.setString(1, id);
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
	public synchronized boolean isInvolved(Document doc)
	{
		String id = doc.getDocumentElement().getAttribute("experimentID");
		if(_experimentIDs.contains(id))
		{			
			_experimentIDs.remove(id);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void processDocument(Document doc) throws Exception
	{
		String id = doc.getDocumentElement().getAttribute("experimentID");
		String et = doc.getDocumentElement().getAttribute("experimentType"); et = et.length()>0?et:"SingleAlgorithmRandom";
		String cn = doc.getDocumentElement().getAttribute("machineName");
				
		_stmt.setLong(1, Long.parseLong(id));
		_stmt.setString(2, et);
		_stmt.setString(3, cn);
		_stmt.executeUpdate();		
	}
}
