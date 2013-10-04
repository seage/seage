package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

import com.rapidminer.example.Attribute;

public class ExperimentTasksTableCreator extends H2DataTableCreator implements IDocumentProcessor
{
	private PreparedStatement _stmt;
	
	public ExperimentTasksTableCreator(String dbPath) throws Exception
	{
		super(dbPath);
		
		Hashtable<String, XmlHelper.XPath> v04 = new Hashtable<String, XmlHelper.XPath>();
        v04.put("ExperimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v04.put("ProblemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v04.put("InstanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v04.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));        
        v04.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v04.put("RunID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v04.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v04.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v04.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v04.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v04.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v04.put("DurationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v04.put("TimeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        
        Hashtable<String, XmlHelper.XPath> v05 = new Hashtable<String, XmlHelper.XPath>();
        v05.put("ExperimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v05.put("ProblemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v05.put("InstanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v05.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));        
        v05.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v05.put("RunID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v05.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v05.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v05.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v05.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v05.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v05.put("DurationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v05.put("TimeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        
        _versionedXPaths.put("0.4", v04);
        _versionedXPaths.put("0.5", v05);
		
		String queryCreate = 
					"CREATE TABLE IF NOT EXISTS ExperimentTasks"+
					"("+
					"ExperimentID VARCHAR,"+
					"ProblemID VARCHAR,"+
					"InstanceID VARCHAR,"+
					"AlgorithmID VARCHAR,"+					
					"ConfigID VARCHAR,"+
					"RunID VARCHAR,"+
					"InitSolutionValue DOUBLE,"+
					"BestSolutionValue DOUBLE,"+
					"NrOfNewSolutions DOUBLE,"+
					"NrOfIterations DOUBLE,"+
					"LastIterNumberNewSol DOUBLE,"+
					"DurationInSeconds DOUBLE,"+
					"TimeoutInSeconds DOUBLE,"+
					")";		
		String insertQuery = 
					"INSERT INTO ExperimentTasks VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Statement stmt = _conn.createStatement();	
		stmt.execute(queryCreate);
		
		_stmt = _conn.prepareStatement(insertQuery);		

	}

	@Override
	public boolean isInvolved(Document doc)
	{
		return true;
	}

	@Override
	public synchronized void processDocument(Document doc) throws Exception
	{
		String version = doc.getDocumentElement().getAttribute("version");
		if(version.compareToIgnoreCase("0.4")<0)
			throw new Exception("Unsupported version: "+version);
				
		_stmt.clearParameters();
		_stmt.setString(1, getVersionedValue(doc, "ExperimentID", version));
		_stmt.setString(2, getVersionedValue(doc, "ProblemID", version));
		_stmt.setString(3, getVersionedValue(doc, "InstanceID", version));
		_stmt.setString(4, getVersionedValue(doc, "AlgorithmID", version));
		_stmt.setString(4, getVersionedValue(doc, "AlgorithmID", version));
		_stmt.setString(5, getVersionedValue(doc, "ConfigID", version));
		_stmt.setString(6, getVersionedValue(doc, "RunID", version));
		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "InitSolutionValue", version)));
		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "BestSolutionValue", version)));
		_stmt.setDouble(9, Double.parseDouble(getVersionedValue(doc, "NrOfNewSolutions", version)));
		_stmt.setDouble(10, Double.parseDouble(getVersionedValue(doc, "NrOfIterations", version)));
		_stmt.setDouble(11, Double.parseDouble(getVersionedValue(doc, "LastIterNumberNewSol", version)));
		_stmt.setDouble(12, Double.parseDouble(getVersionedValue(doc, "DurationInSeconds", version)));
		_stmt.setDouble(13, Double.parseDouble(getVersionedValue(doc, "TimeoutInSeconds", version)));
		
		_stmt.executeUpdate();
		
	}
	
	protected String postProcessValue(String attName, String val, String version)
	{
		if(/*version.equals("0.4") &&*/ attName.equals("InstanceID") && val.contains("."))
			return val.split("\\.")[0];
		return val;
	}
}
