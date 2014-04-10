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
        v04.put("experimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v04.put("problemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v04.put("instanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v04.put("algorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));        
        v04.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v04.put("runID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v04.put("initSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v04.put("bestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v04.put("nrOfNewSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v04.put("lastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v04.put("nrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v04.put("durationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v04.put("timeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        
        Hashtable<String, XmlHelper.XPath> v05 = new Hashtable<String, XmlHelper.XPath>();
        v05.put("experimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v05.put("problemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v05.put("instanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v05.put("algorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));        
        v05.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v05.put("runID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v05.put("initSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v05.put("bestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v05.put("nrOfNewSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v05.put("lastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v05.put("nrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v05.put("durationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v05.put("timeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        
        _versionedXPaths.put("0.4", v04);
        _versionedXPaths.put("0.5", v05);
        _versionedXPaths.put("0.6", v05);
		
		String queryCreate = 
					"CREATE TABLE IF NOT EXISTS ExperimentTasks"+
					"("+
					"experimentID VARCHAR,"+
					"problemID VARCHAR,"+
					"instanceID VARCHAR,"+
					"algorithmID VARCHAR,"+					
					"configID VARCHAR,"+
					"runID VARCHAR,"+
					"initSolutionValue DOUBLE,"+
					"bestSolutionValue DOUBLE,"+
					"nrOfNewSolutions DOUBLE,"+
					"nrOfIterations DOUBLE,"+
					"lastIterNumberNewSol DOUBLE,"+
					"durationInSeconds DOUBLE,"+
					"timeoutInSeconds DOUBLE,"+
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
		_stmt.setString(1, getVersionedValue(doc, "experimentID", version));
		_stmt.setString(2, getVersionedValue(doc, "problemID", version));
		_stmt.setString(3, getVersionedValue(doc, "instanceID", version));
		_stmt.setString(4, getVersionedValue(doc, "algorithmID", version));
		_stmt.setString(5, getVersionedValue(doc, "configID", version));
		_stmt.setString(6, getVersionedValue(doc, "runID", version));
		_stmt.setDouble(7, Double.parseDouble(getVersionedValue(doc, "initSolutionValue", version)));
		_stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "bestSolutionValue", version)));
		_stmt.setDouble(9, Double.parseDouble(getVersionedValue(doc, "nrOfNewSolutions", version)));
		_stmt.setDouble(10, Double.parseDouble(getVersionedValue(doc, "nrOfIterations", version)));
		_stmt.setDouble(11, Double.parseDouble(getVersionedValue(doc, "lastIterNumberNewSol", version)));
		_stmt.setDouble(12, Double.parseDouble(getVersionedValue(doc, "durationInSeconds", version)));
		_stmt.setDouble(13, Double.parseDouble(getVersionedValue(doc, "timeoutInSeconds", version)));
		
		_stmt.executeUpdate();
		
	}
	
	protected String postProcessValue(String attName, String val, String version)
	{
		if(/*version.equals("0.4") &&*/ attName.equals("instanceID") && val.contains("."))
			return val.split("\\.")[0];
		return val;
	}
}
