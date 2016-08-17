package org.seage.knowledgebase.importing.db.tablecreator;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import org.seage.knowledgebase.importing.IDocumentProcessor;
import org.w3c.dom.Document;

public class ExperimentsTableCreator extends H2DataTableCreator implements IDocumentProcessor
{
    //protected Connection _conn;
    private String _expLogsPath;
    private HashSet<String> _experimentIDs;
    private PreparedStatement _stmt;

    public ExperimentsTableCreator(String expLogsPath, String dbPath, boolean clean) throws Exception
    {
        super(dbPath);
        _expLogsPath = expLogsPath;

        String queryDropExperiments = "DROP ALL OBJECTS";
        String queryCreateExperiments = "CREATE TABLE Experiments (date TIMESTAMP, experimentID VARCHAR PRIMARY KEY, experimentType VARCHAR, computerName VARCHAR)";
        String queryInsert = "INSERT INTO Experiments VALUES (?, ?, ?, ?)";

        Statement stmt = _conn.createStatement();

        if (clean)
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

        for (File z : fl)
        {
            String id = z.getName().split("-")[0];
            if (!_experimentIDs.contains(id))
                _experimentIDs.add(id);
        }

        String queryCreateNewExperiments = "CREATE TEMP TABLE NewExperiments (ExperimentID BIGINT PRIMARY KEY)";
        String queryInsert = "INSERT INTO NewExperiments VALUES (?)";
        String queryMinus = "SELECT ExperimentID FROM NewExperiments MINUS SELECT ExperimentID FROM Experiments";

        PreparedStatement stmt = null;

        stmt = _conn.prepareStatement(queryCreateNewExperiments);
        stmt.execute();

        stmt = _conn.prepareStatement(queryInsert);
        for (String id : _experimentIDs)
        {
            stmt.setString(1, id);
            stmt.execute();
        }

        stmt = _conn.prepareStatement(queryMinus);
        ResultSet rs = stmt.executeQuery();

        _experimentIDs.clear();

        while (rs.next())
        {
            _experimentIDs.add(rs.getString(1));
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

        long timeStamp = Long.parseLong(id);
        _stmt.setTimestamp(1, new java.sql.Timestamp(timeStamp));
        _stmt.setLong(2, timeStamp);
        _stmt.setString(3, et);
        _stmt.setString(4, cn);        
        _stmt.executeUpdate();
    }
}