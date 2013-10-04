package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public abstract class AlgorithmParamsTableCreator extends H2DataTableCreator implements IDocumentProcessor
{
    protected final String VERSION="0.4";
    private Hashtable<String, String> _algParamsConfigIDs;
    protected PreparedStatement _stmt;
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
        String algorithmID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
        if(!algorithmID.equals(_algorithmID))
            return false;
        
        String configID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
      
        if(_algParamsConfigIDs.containsKey(configID))
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
            
            String queryCreate  = 
					"CREATE TABLE IF NOT EXISTS AlgorithmParams_GeneticAlgorithm"+
					"("+
					"ConfigID VARCHAR,"+
					"CrossLengthPct DOUBLE,"+
					"EliteSubjectPct DOUBLE,"+
					"IterationCount DOUBLE,"+
					"MutateLengthPct DOUBLE,"+
					"MutateSubjectPct DOUBLE,"+
					"NumSolutions DOUBLE,"+
					"RandomSubjectPct DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_GeneticAlgorithm VALUES(?,?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
            
            Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algGAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));            
            algGAXPaths.put("CrossLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
            algGAXPaths.put("EliteSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
            algGAXPaths.put("IterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algGAXPaths.put("MutateLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
            algGAXPaths.put("MutateSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
            algGAXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algGAXPaths.put("RandomSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));
            
            _versionedXPaths.put(VERSION, algGAXPaths);
        }
        
        @Override
        public synchronized void processDocument(Document doc) throws Exception
        {
        	String version = doc.getDocumentElement().getAttribute("version");
    		if(version.compareToIgnoreCase("0.4")<0)
    			throw new Exception("Unsupported version: "+version);
    		version = VERSION;
    				
    		_stmt.clearParameters();
    		_stmt.setString(1, getVersionedValue(doc, "ConfigID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "CrossLengthPct", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "EliteSubjectPct", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "IterationCount", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "MutateLengthPct", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "MutateSubjectPct", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "NumSolutions", version)));
    		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "RandomSubjectPct", version)));
    		
    		_stmt.executeUpdate();
                                   
        }
    }
    
    public static class TabuSearch extends AlgorithmParamsTableCreator
    {
        public TabuSearch(String dbPath) throws Exception
        {
            super(dbPath, "TabuSearch");
            
            String queryCreate  = 
					"CREATE TABLE IF NOT EXISTS AlgorithmParams_TabuSearch"+
					"("+
					"ConfigID VARCHAR,"+
					"NumIteration DOUBLE,"+
					"TabuListLength DOUBLE,"+
					"NumSolutions DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_TabuSearch VALUES(?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);

            Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("NumIteration", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
            algTSXPaths.put("TabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            
            _versionedXPaths.put(VERSION, algTSXPaths);
        }
        
        @Override
        public synchronized void processDocument(Document doc) throws Exception
        {
        	String version = doc.getDocumentElement().getAttribute("version");
    		if(version.compareToIgnoreCase("0.4")<0)
    			throw new Exception("Unsupported version: "+version);
    		version = VERSION;
    				
    		_stmt.clearParameters();
    		_stmt.setString(1, getVersionedValue(doc, "ConfigID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "NumIteration", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "TabuListLength", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "NumSolutions", version)));
    		
    		_stmt.executeUpdate();
                                   
        }
    }
    
    public static class AntColony extends AlgorithmParamsTableCreator
    {
        public AntColony(String dbPath) throws Exception
        {
            super(dbPath, "AntColony");
            
            String queryCreate  = 
					"CREATE TABLE IF NOT EXISTS AlgorithmParams_AntColony"+
					"("+
					"ConfigID VARCHAR,"+
					"Alpha DOUBLE,"+
					"Beta DOUBLE,"+
					"DefaultPheromone DOUBLE,"+
					"IterationCount DOUBLE,"+
					"NumSolutions DOUBLE,"+
					"QantumOfPheromone DOUBLE,"+
					"LocalEvaporation DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_AntColony VALUES(?,?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
            
            Hashtable<String, XmlHelper.XPath> algAntXPaths = new Hashtable<String, XmlHelper.XPath>();
            algAntXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algAntXPaths.put("Alpha", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@alpha"));
            algAntXPaths.put("Beta", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@beta"));
            algAntXPaths.put("DefaultPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@defaultPheromone"));
            algAntXPaths.put("IterationCount", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@iterationCount"));
            algAntXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algAntXPaths.put("QantumOfPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@qantumOfPheromone"));
            algAntXPaths.put("LocalEvaporation", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@localEvaporation"));

            _versionedXPaths.put(VERSION, algAntXPaths);
        }
        
        @Override
        public synchronized void processDocument(Document doc) throws Exception
        {
        	String version = doc.getDocumentElement().getAttribute("version");
    		if(version.compareToIgnoreCase("0.4")<0)
    			throw new Exception("Unsupported version: "+version);
    		version = VERSION;
    				
    		_stmt.clearParameters();
    		_stmt.setString(1, getVersionedValue(doc, "ConfigID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "Alpha", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "Beta", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "DefaultPheromone", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "IterationCount", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "NumSolutions", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "QantumOfPheromone", version)));
    		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "LocalEvaporation", version)));
    		
    		_stmt.executeUpdate();
                                   
        }
    }
    
    public static class SimulatedAnnealing extends AlgorithmParamsTableCreator
    {
        public SimulatedAnnealing(String dbPath) throws Exception
        {
            super(dbPath, "SimulatedAnnealing");
            
            String queryCreate  = 
					"CREATE TABLE IF NOT EXISTS AlgorithmParams_SimulatedAnnealing"+
					"("+
					"ConfigID VARCHAR,"+
					"AnnealCoeficient DOUBLE,"+
					"MaxInnerIterations DOUBLE,"+
					"MaxTemperature DOUBLE,"+
					"MinTemperature DOUBLE,"+
					"MaxOneStepAcceptedSolutions DOUBLE,"+
					"NumSolutions DOUBLE,"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_SimulatedAnnealing VALUES(?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
                       
            Hashtable<String, XmlHelper.XPath> algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algSAXPaths.put("AnnealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("MaxInnerIterations", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxInnerIterations"));
            algSAXPaths.put("MaxTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("MinTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            algSAXPaths.put("MaxOneStepAcceptedSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxOneStepAcceptedSolutions"));
            algSAXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            _versionedXPaths.put(VERSION, algSAXPaths);
        }
        
        @Override
        public synchronized void processDocument(Document doc) throws Exception
        {
        	String version = doc.getDocumentElement().getAttribute("version");
    		if(version.compareToIgnoreCase("0.4")<0)
    			throw new Exception("Unsupported version: "+version);
    		version = VERSION;
    				
    		_stmt.clearParameters();
    		_stmt.setString(1, getVersionedValue(doc, "ConfigID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "AnnealCoeficient", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "MaxInnerIterations", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "MaxTemperature", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "MinTemperature", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "MaxOneStepAcceptedSolutions", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "NumSolutions", version)));
    		
    		_stmt.executeUpdate();
                                   
        }
    }
}
