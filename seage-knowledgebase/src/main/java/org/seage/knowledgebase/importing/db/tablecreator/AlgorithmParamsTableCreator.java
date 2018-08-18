package org.seage.knowledgebase.importing.db.tablecreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.knowledgebase.importing.IDocumentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public abstract class AlgorithmParamsTableCreator extends DataTableCreator implements IDocumentProcessor
{
	private static Logger _logger = LoggerFactory.getLogger(AlgorithmParamsTableCreator.class.getName());
    protected final String VERSION = "0.6";
    private Hashtable<String, String> _algParamsConfigIDs;
    //protected PreparedStatement _stmt;
    protected String _algorithmID;

    public AlgorithmParamsTableCreator(String dbPath, String algorithmID) throws Exception
    {
        super(dbPath);
        _algorithmID = algorithmID;
        _algParamsConfigIDs = new Hashtable<String, String>();
    }

    @Override
    public synchronized boolean isInvolved(Document doc)
    {
        String algorithmID = XmlHelper.getValueFromDocument(doc.getDocumentElement(),
                new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
        if (!algorithmID.equals(_algorithmID))
            return false;

        String configID = XmlHelper.getValueFromDocument(doc.getDocumentElement(),
                new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));

        if (_algParamsConfigIDs.containsKey(configID))
            return false;
        else
            _algParamsConfigIDs.put(configID, configID);
        return true;
    }

    public static class GeneticAlgorithm extends AlgorithmParamsTableCreator
    {
        public GeneticAlgorithm(String dbPath) throws Exception
        {
            super(dbPath, "GeneticAlgorithm");

            String queryCreate = "CREATE TABLE IF NOT EXISTS AlgorithmParams_GeneticAlgorithm" +
                    "(" +
                    "configID VARCHAR," +
                    "crossLengthPct DOUBLE PRECISION," +
                    "eliteSubjectPct DOUBLE PRECISION," +
                    "iterationCount DOUBLE PRECISION," +
                    "mutateLengthPct DOUBLE PRECISION," +
                    "mutateSubjectPct DOUBLE PRECISION," +
                    "numSolutions DOUBLE PRECISION," +
                    "randomSubjectPct REAL" +
                    ")";
            
            try(Connection _conn = createConnection("")) {
	            Statement stmt = _conn.createStatement();
	            stmt.execute(queryCreate);
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

            Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algGAXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algGAXPaths.put("crossLengthPct",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
            algGAXPaths.put("eliteSubjectPct",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
            algGAXPaths.put("iterationCount",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algGAXPaths.put("mutateLengthPct",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
            algGAXPaths.put("mutateSubjectPct",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
            algGAXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algGAXPaths.put("randomSubjectPct",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));

            _versionedXPaths.put(VERSION, algGAXPaths);
        }

        @Override
        public synchronized void processDocument(Document doc, String containerFileName) throws Exception
        {
            String version = doc.getDocumentElement().getAttribute("version");
            if (version.compareToIgnoreCase("0.4") < 0)
                throw new Exception("Unsupported version: " + version);
            version = VERSION;
            String insertQuery = "INSERT INTO AlgorithmParams_GeneticAlgorithm VALUES(?,?,?,?,?,?,?,?)";

            try(Connection conn = createConnection("")) {           	 
	            PreparedStatement stmt = conn.prepareStatement(insertQuery);
	            stmt.clearParameters();
	            stmt.setString(1, getVersionedValue(doc, "configID", version));
	            stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "crossLengthPct", version)));
	            stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "eliteSubjectPct", version)));
	            stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
	            stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "mutateLengthPct", version)));
	            stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "mutateSubjectPct", version)));
	            stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
	            stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "randomSubjectPct", version)));
	
	            stmt.executeUpdate();
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

        }
    }

    public static class TabuSearch extends AlgorithmParamsTableCreator
    {
        public TabuSearch(String dbPath) throws Exception
        {
            super(dbPath, "TabuSearch");

            String queryCreate = "CREATE TABLE IF NOT EXISTS AlgorithmParams_TabuSearch" +
                    "(" +
                    "configID VARCHAR," +
                    "iterationCount DOUBLE PRECISION," +
                    "tabuListLength DOUBLE PRECISION," +
                    "numSolutions REAL" +
                    ")";
            
            try(Connection _conn = createConnection("")) {
	            Statement stmt = _conn.createStatement();
	            stmt.execute(queryCreate);
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

            Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("iterationCount",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
            algTSXPaths.put("tabuListLength",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));

            _versionedXPaths.put("0.4", algTSXPaths);
            _versionedXPaths.put("0.5", algTSXPaths);

            algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("iterationCount",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algTSXPaths.put("tabuListLength",
                    new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));

            _versionedXPaths.put(VERSION, algTSXPaths);
        }

        @Override
        public synchronized void processDocument(Document doc, String containerFileName) throws Exception
        {
            String version = doc.getDocumentElement().getAttribute("version");
            if (version.compareToIgnoreCase("0.4") < 0)
                throw new Exception("Unsupported version: " + version);
            //version = VERSION;
            String insertQuery = "INSERT INTO AlgorithmParams_TabuSearch VALUES(?,?,?,?)";

            try(Connection conn = createConnection("")) {           	 
	            PreparedStatement stmt = conn.prepareStatement(insertQuery);
	            stmt.clearParameters();
	            stmt.setString(1, getVersionedValue(doc, "configID", version));
	            stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
	            stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "tabuListLength", version)));
	            stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
	
	            stmt.executeUpdate();
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

        }
    }

    public static class AntColony extends AlgorithmParamsTableCreator
    {
        public AntColony(String dbPath) throws Exception
        {
            super(dbPath, "AntColony");

            String queryCreate = "CREATE TABLE IF NOT EXISTS AlgorithmParams_AntColony" +
                    "(" +
                    "configID VARCHAR," +
                    "alpha DOUBLE PRECISION," +
                    "beta DOUBLE PRECISION," +
                    "defaultPheromone DOUBLE PRECISION," +
                    "iterationCount DOUBLE PRECISION," +
                    "numSolutions DOUBLE PRECISION," +
                    "qantumOfPheromone DOUBLE PRECISION," +
                    "localEvaporation REAL" +
                    ")";            

            try(Connection _conn = createConnection("")) {
	            Statement stmt = _conn.createStatement();
	            stmt.execute(queryCreate);
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

            Hashtable<String, XmlHelper.XPath> algAntXPaths = new Hashtable<String, XmlHelper.XPath>();
            algAntXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algAntXPaths.put("alpha", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@alpha"));
            algAntXPaths.put("beta", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@beta"));
            algAntXPaths.put("defaultPheromone",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@defaultPheromone"));
            algAntXPaths.put("iterationCount",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@iterationCount"));
            algAntXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algAntXPaths.put("qantumOfPheromone",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@qantumOfPheromone"));
            algAntXPaths.put("localEvaporation",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@localEvaporation"));

            _versionedXPaths.put(VERSION, algAntXPaths);
        }

        @Override
        public synchronized void processDocument(Document doc, String containerFileName) throws Exception
        {
            String version = doc.getDocumentElement().getAttribute("version");
            if (version.compareToIgnoreCase("0.4") < 0)
                throw new Exception("Unsupported version: " + version);
            version = VERSION;
            
            String insertQuery = "INSERT INTO AlgorithmParams_AntColony VALUES(?,?,?,?,?,?,?,?)";
            
            try(Connection conn = createConnection("")) {           	 
	            PreparedStatement stmt = conn.prepareStatement(insertQuery);
	            stmt.clearParameters();
	            stmt.setString(1, getVersionedValue(doc, "configID", version));
	            stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "alpha", version)));
	            stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "beta", version)));
	            stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "defaultPheromone", version)));
	            stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
	            stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
	            stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "qantumOfPheromone", version)));
	            stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "localEvaporation", version)));
	
	            stmt.executeUpdate();
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

        }
    }

    public static class SimulatedAnnealing extends AlgorithmParamsTableCreator
    {
        public SimulatedAnnealing(String dbPath) throws Exception
        {
            super(dbPath, "SimulatedAnnealing");

            String queryCreate = "CREATE TABLE IF NOT EXISTS AlgorithmParams_SimulatedAnnealing" +
                    "(" +
                    "configID VARCHAR," +
                    //"annealCoeficient DOUBLE,"+
                    "numIterations DOUBLE PRECISION," +
                    "maxTemperature DOUBLE PRECISION," +
                    "minTemperature DOUBLE PRECISION," +
                    //"maxOneStepAcceptedSolutions DOUBLE,"+
                    "numSolutions REAL" +
                    ")";
            
            
            try(Connection _conn = createConnection("")) {
	            Statement stmt = _conn.createStatement();
	            stmt.execute(queryCreate);
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }	            
            

            Hashtable<String, XmlHelper.XPath> algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            //algSAXPaths.put("annealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("numIterations",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxInnerIterations"));
            algSAXPaths.put("maxTemperature",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("minTemperature",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            //algSAXPaths.put("maxOneStepAcceptedSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxOneStepAcceptedSolutions"));
            algSAXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            _versionedXPaths.put("0.5", algSAXPaths);

            algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            //algSAXPaths.put("annealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("numIterations",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numIterations"));
            algSAXPaths.put("maxTemperature",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("minTemperature",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            //algSAXPaths.put("maxOneStepAcceptedSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxOneStepAcceptedSolutions"));
            algSAXPaths.put("numSolutions",
                    new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            _versionedXPaths.put(VERSION, algSAXPaths);
        }

        @Override
        public synchronized void processDocument(Document doc, String containerFileName) throws Exception
        {
            String version = doc.getDocumentElement().getAttribute("version");
            if (version.compareToIgnoreCase("0.4") < 0)
                throw new Exception("Unsupported version: " + version);
            //version = VERSION;
            String insertQuery = "INSERT INTO AlgorithmParams_SimulatedAnnealing VALUES(?,?,?,?,?)";
            
            try(Connection conn = createConnection("")) {           	 
	            PreparedStatement stmt = conn.prepareStatement(insertQuery);
	
	            stmt.clearParameters();
	            stmt.setString(1, getVersionedValue(doc, "configID", version));
	            //_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "annealCoeficient", version)));
	            stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "numIterations", version)));
	            stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "maxTemperature", version)));
	            stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "minTemperature", version)));
	            //_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "maxOneStepAcceptedSolutions", version)));
	            stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
	
	            stmt.executeUpdate();
            } catch(SQLException ex) {
            	_logger.error(ex.getMessage(), ex);
            }

        }
    }
}
