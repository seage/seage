package org.seage.hh.knowledgebase.importing.db.tablecreator;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.seage.hh.knowledgebase.importing.IDocumentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class ExperimentsTableCreator extends DataTableCreator implements IDocumentProcessor
{
	private static Logger _logger = LoggerFactory.getLogger(ExperimentTasksTableCreator.class.getName());
    private String _expLogsPath;
    private HashSet<String> _experimentIDs;

    public ExperimentsTableCreator(String expLogsPath, String dbPath, boolean clean) throws Exception
    {
        super(dbPath);
        _expLogsPath = expLogsPath;

        //String queryDropExperiments = "DROP TABLE IF EXISTS Experiments CASCADE";
        String queryDropExperiments = "DROP SCHEMA if exists PUBLIC CASCADE;create schema public";
        String queryCreateExperiments = "CREATE TABLE IF NOT EXISTS Experiments (date TIMESTAMP, experimentID VARCHAR PRIMARY KEY, experimentType VARCHAR, computerName VARCHAR)";
        

        try(Connection _conn = createConnection("")) {
            Statement stmt = _conn.createStatement();            
            if (clean)
                stmt.execute(queryDropExperiments);            
            stmt.execute(queryCreateExperiments);
        } catch(SQLException ex) {
        	_logger.error(ex.getMessage(), ex);
        }
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
            return _experimentIDs;

        for (File z : fl)
        {
            String id = z.getName().split("-")[0];
            if (!_experimentIDs.contains(id))
                _experimentIDs.add(id);
        }

        String queryDropNewExperiments = "DROP TABLE IF EXISTS NewExperiments";
        String queryCreateNewExperiments = "CREATE TABLE IF NOT EXISTS NewExperiments (ExperimentID VARCHAR PRIMARY KEY)";
        String queryInsert = "INSERT INTO NewExperiments VALUES (?)";
        String queryMinus = "SELECT ExperimentID FROM NewExperiments EXCEPT SELECT ExperimentID FROM Experiments";

        PreparedStatement stmt = null;
        try(Connection conn = createConnection("")) {            
	        stmt = conn.prepareStatement(queryDropNewExperiments);
	        stmt.execute();
	        stmt = conn.prepareStatement(queryCreateNewExperiments);
	        stmt.execute();
	
	        stmt = conn.prepareStatement(queryInsert);
	        for (String id : _experimentIDs)
	        {
	            stmt.setString(1, id);
	            stmt.execute();
	        }
	
	        stmt = conn.prepareStatement(queryMinus);
	        ResultSet rs = stmt.executeQuery();
	        _experimentIDs.clear();
	        while (rs.next())
	        {
	            _experimentIDs.add(rs.getString(1));
	        }
        } catch(SQLException ex) {
        	_logger.error(ex.getMessage(), ex);
        }
        
        return (HashSet<String>) _experimentIDs.clone();
    }

    @Override
    public synchronized boolean isInvolved(Document doc)
    {
        String id = doc.getDocumentElement().getAttribute("experimentID");
        if (_experimentIDs.contains(id))
        {
            _experimentIDs.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void processDocument(Document doc, String containerFileName) throws Exception
    {
        String id = doc.getDocumentElement().getAttribute("experimentID");
        String et = doc.getDocumentElement().getAttribute("experimentType");
        et = et.length() > 0 ? et : "SingleAlgorithmRandom";
        String cn = doc.getDocumentElement().getAttribute("machineName");
        
        String queryInsert = "INSERT INTO Experiments VALUES (?, ?, ?, ?)";
        
        try(Connection conn = createConnection("")) {           	 
            PreparedStatement stmt = conn.prepareStatement(queryInsert);
	        long timeStamp = Long.parseLong(id);
	        stmt.setTimestamp(1, new java.sql.Timestamp(timeStamp));
	        stmt.setLong(2, timeStamp);
	        stmt.setString(3, et);
	        stmt.setString(4, cn);        
	        stmt.executeUpdate();
        } catch(SQLException ex) {
        	_logger.error(ex.getMessage(), ex);
        }
    }
}
