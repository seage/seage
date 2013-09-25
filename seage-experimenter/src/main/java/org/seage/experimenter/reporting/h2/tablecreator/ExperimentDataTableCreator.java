package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.Connection;
import java.sql.Statement;

public class ExperimentDataTableCreator extends TableCreator
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
		
		Connection conn = createConnection();
		try
		{
			Statement stmt = conn.createStatement();
			
			if(clean)
				stmt.execute(queryDrop);	
			stmt.execute(queryCreate);
		}
		finally
		{
			if(conn != null)
				conn.close();
		}
	}

}
