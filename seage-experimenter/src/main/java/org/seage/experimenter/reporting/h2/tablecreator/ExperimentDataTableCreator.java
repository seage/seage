package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.Statement;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public class ExperimentDataTableCreator extends TableCreator implements IDocumentProcessor
{

	public ExperimentDataTableCreator(String dbPath, boolean clean) throws Exception
	{
		super(dbPath);
		
		String queryDrop = "DROP TABLE IF EXISTS ExperimentData";
		String queryCreate = 
						"CREATE TABLE IF NOT EXISTS ExperimentData"+
						"("+
						"ExperimentID VARCHAR PRIMARY KEY,"+
						"ExperimentType VARCHAR,"+
						"ProblemID VARCHAR,"+
						"AlgorithmID VARCHAR,"+
						"InstanceID VARCHAR,"+
						"ConfigID VARCHAR,"+
						"RunID VARCHAR,"+
						"InitSolutionValue DOUBLE,"+
						"BestSolutionValue DOUBLE,"+
						"NrOfNewSolutions DOUBLE,"+
						"NrOfIterations DOUBLE,"+
						"LastIterNumberNewSol DOUBLE,"+
						"DurationInSeconds DOUBLE,"+
						"TimeoutInSeconds DOUBLE,"+
						"ComputerName VARCHAR"+
						")";
		

			Statement stmt = _conn.createStatement();
			
			if(clean)
				stmt.execute(queryDrop);	
			stmt.execute(queryCreate);

	}

	@Override
	public synchronized boolean isInvolved(Document doc)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void processDocument(Document doc)
	{
		// TODO Auto-generated method stub
		
	}

}
