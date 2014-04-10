package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public abstract class AlgorithmParamsTableCreator extends H2DataTableCreator implements IDocumentProcessor
{
    protected final String VERSION="0.6";
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
					"configID VARCHAR,"+
					"crossLengthPct DOUBLE,"+
					"eliteSubjectPct DOUBLE,"+
					"iterationCount DOUBLE,"+
					"mutateLengthPct DOUBLE,"+
					"mutateSubjectPct DOUBLE,"+
					"numSolutions DOUBLE,"+
					"randomSubjectPct DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_GeneticAlgorithm VALUES(?,?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
            
            Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algGAXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));            
            algGAXPaths.put("crossLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
            algGAXPaths.put("eliteSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
            algGAXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algGAXPaths.put("mutateLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
            algGAXPaths.put("mutateSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
            algGAXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algGAXPaths.put("randomSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));
            
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
    		_stmt.setString(1, getVersionedValue(doc, "configID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "crossLengthPct", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "eliteSubjectPct", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "mutateLengthPct", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "mutateSubjectPct", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
    		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "randomSubjectPct", version)));
    		
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
					"configID VARCHAR,"+
					"iterationCount DOUBLE,"+
					"tabuListLength DOUBLE,"+
					"numSolutions DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_TabuSearch VALUES(?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);

    		Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
            algTSXPaths.put("tabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            
            _versionedXPaths.put("0.4", algTSXPaths);
            _versionedXPaths.put("0.5", algTSXPaths);
            
            algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algTSXPaths.put("tabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            
            _versionedXPaths.put(VERSION, algTSXPaths);
        }
        
        @Override
        public synchronized void processDocument(Document doc) throws Exception
        {
        	String version = doc.getDocumentElement().getAttribute("version");
    		if(version.compareToIgnoreCase("0.4")<0)
    			throw new Exception("Unsupported version: "+version);
    		//version = VERSION;
    				
    		_stmt.clearParameters();
    		_stmt.setString(1, getVersionedValue(doc, "configID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "tabuListLength", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
    		
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
					"configID VARCHAR,"+
					"alpha DOUBLE,"+
					"beta DOUBLE,"+
					"defaultPheromone DOUBLE,"+
					"iterationCount DOUBLE,"+
					"numSolutions DOUBLE,"+
					"qantumOfPheromone DOUBLE,"+
					"localEvaporation DOUBLE"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_AntColony VALUES(?,?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
            
            Hashtable<String, XmlHelper.XPath> algAntXPaths = new Hashtable<String, XmlHelper.XPath>();
            algAntXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algAntXPaths.put("alpha", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@alpha"));
            algAntXPaths.put("beta", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@beta"));
            algAntXPaths.put("defaultPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@defaultPheromone"));
            algAntXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@iterationCount"));
            algAntXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algAntXPaths.put("qantumOfPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@qantumOfPheromone"));
            algAntXPaths.put("localEvaporation", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@localEvaporation"));

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
    		_stmt.setString(1, getVersionedValue(doc, "configID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "alpha", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "beta", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "defaultPheromone", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "iterationCount", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "qantumOfPheromone", version)));
    		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "localEvaporation", version)));
    		
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
					"configID VARCHAR,"+
					"annealCoeficient DOUBLE,"+
					"maxInnerIterations DOUBLE,"+
					"maxTemperature DOUBLE,"+
					"minTemperature DOUBLE,"+
					"maxOneStepAcceptedSolutions DOUBLE,"+
					"numSolutions DOUBLE,"+
					")";
            String insertQuery = 
					"INSERT INTO AlgorithmParams_SimulatedAnnealing VALUES(?,?,?,?,?,?,?)";
            
            Statement stmt = _conn.createStatement();	
    		stmt.execute(queryCreate);
    		_stmt = _conn.prepareStatement(insertQuery);
                       
            Hashtable<String, XmlHelper.XPath> algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algSAXPaths.put("annealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("maxInnerIterations", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxInnerIterations"));
            algSAXPaths.put("maxTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("minTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            algSAXPaths.put("maxOneStepAcceptedSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxOneStepAcceptedSolutions"));
            algSAXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
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
    		_stmt.setString(1, getVersionedValue(doc, "configID", version));    
    		_stmt.setDouble(2, Double.parseDouble(getVersionedValue(doc, "annealCoeficient", version)));
    		_stmt.setDouble(3, Double.parseDouble(getVersionedValue(doc, "maxInnerIterations", version)));
    		_stmt.setDouble(4, Double.parseDouble(getVersionedValue(doc, "maxTemperature", version)));
    		_stmt.setDouble(5, Double.parseDouble(getVersionedValue(doc, "minTemperature", version)));
    		_stmt.setDouble(6, Double.parseDouble(getVersionedValue(doc, "maxOneStepAcceptedSolutions", version)));
    		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "numSolutions", version)));
    		
    		_stmt.executeUpdate();
                                   
        }
    }
}
